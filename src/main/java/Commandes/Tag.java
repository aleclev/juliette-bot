package Commandes;

import Accesseur.AccessMysql;
import Adapteurs.MessageEventAdapter;
import Discord.*;
import Discord.Module;
import Exceptions.MasterException;
import Fonctions.CommandParse;
import Fonctions.VerificationMessage.VerificateurMemeContexte;

import java.sql.SQLException;
import java.util.List;

public class Tag extends Module {
    public Tag(Bot bot) {
        super(bot);

        //liste des commandes,
        this.ajouterCommande(new tagGet(bot));
        //this.ajouterCommande(new tagCheck(bot));
        this.ajouterCommande(new tagSet(bot));
        this.ajouterCommande(new tagDel(bot));
        this.ajouterCommande(new listAll(bot));

        this.setNom("Tag");
    }
}

class tagGet extends Commande {
    public tagGet(Bot bot) {
        super(bot);
        ajouterNom("tag get");
        setDescription("Returns the text for a tag.");
        setUtilisation("j/tag get <tagName>");
        setExemple("j/tag get sometag");

        ajouterArg(new ArgumentMetaData(
                ArgumentType.STRING,
                false,
                "keyword",
                "The keyword of the entry you want to access"
        ));

        makeSlashSherpaRun();
    }

    @Override
    public void traitement(MessageEventAdapter evt, CommandThread cmdThread) throws InterruptedException, MasterException {
        List<Argument> args = creerArguments(evt);
        String kw = args.get(0).toStr();

        try {
            AccessMysql accessMysql = reqBot().reqAccessMysql();
            String tag_text = accessMysql.reqTag(evt.reqIdAuteur(), kw);
            evt.repondre(String.format("**%s :**\n%s", kw, tag_text));
            accessMysql.eteindre();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            evt.repondre("ERROR ! The requested tag may not exist.");
        }
    }
}

class tagCheck extends Commande {
    public tagCheck(Bot bot) {
        super(bot);
        ajouterNom("tag check");
    }

    @Override
    public void traitement(MessageEventAdapter evt, CommandThread cmdThread) throws InterruptedException {
        String[] args = CommandParse.decouperCommande(evt.reqContenueRaw());
        if (args.length < 3) {
            evt.repondre(MessageEventAdapter.MSGGEN.MANQUE_ARGS);
            return;
        }

        AccessMysql accessMysql;
        try {
            accessMysql = reqBot().reqAccessMysql();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            evt.repondre(MessageEventAdapter.MSGGEN.DB_CONNECT_FAIL);
            return;
        }
        try {
            if (accessMysql.checkTag(evt.reqIdAuteur(), args[2])) {
                evt.repondre("Tag exists!");
            } else {
                evt.repondre("Tag does not exist!");
            }
            accessMysql.eteindre();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}

class tagSet extends Commande {
    public tagSet(Bot bot) {
        super(bot);
        ajouterNom("tag set");
        setDescription("Save the text under a tag name.");
        setUtilisation("j/tag set <tagName> <tagText>");
        setExemple("j/tag set poem roses are red, violets are blue, ur mom gay, so are you");
        ajouterArg(new ArgumentMetaData(
                ArgumentType.STRING,
                false,
                "keyword",
                "The keyword that is used to retrieve the information"));

        ajouterArg(new ArgumentMetaData(
                ArgumentType.STRING,
                false,
                "text",
                "The information you would like to save"));

        makeSlashSherpaRun();
    }

    @Override
    public void traitement(MessageEventAdapter evt, CommandThread cmdThread) throws InterruptedException, MasterException {

        List<Argument> args = creerArguments(evt);
        String kw = args.get(0).toStr();
        String tag_text = args.get(1).toStr();

        try {
            AccessMysql accessMysql;
            accessMysql = reqBot().reqAccessMysql();
            if (accessMysql.checkTag(evt.reqIdAuteur(), kw)) {
                evt.repondre("This tag already exists. Would you like to override the old value? (y/n)");
                if ((cmdThread.attendreReponseMessage(30, new VerificateurMemeContexte(evt))).reqContenueRaw().equals("y")) {
                    accessMysql.setTag(evt.reqIdAuteur(), kw, tag_text);
                    evt.repondre("Tag successfully saved!");
                } else {
                    evt.repondre("Command cancelled.");
                }
            } else {
                accessMysql.setTag(evt.reqIdAuteur(), kw, tag_text);
                evt.repondre("Tag successfully saved!");
            }

            accessMysql.eteindre();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}

class tagDel extends Commande {
    public tagDel(Bot bot) {
        super(bot);
        ajouterNom("tag del");
        ajouterNom("tag delete");
        setDescription("Delete one of your tags.");
        setUtilisation("j/tag del <tagName>");
        setExemple("j/tag del poem");

        ajouterArg(new ArgumentMetaData(
                ArgumentType.STRING,
                false,
                "keyword",
                "The keyword of the entry you want to delete"
        ));

        makeSlashSherpaRun();
    }

    @Override
    public void traitement(MessageEventAdapter evt, CommandThread cmdThread) throws InterruptedException, MasterException {
        List<Argument> args = creerArguments(evt);

        String kw = args.get(0).toStr();

        AccessMysql accessMysql;
        try {
            accessMysql = reqBot().reqAccessMysql();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            evt.repondre(MessageEventAdapter.MSGGEN.DB_CONNECT_FAIL);
            return;
        }

        try {
            if (!accessMysql.checkTag(evt.reqIdAuteur(), kw)) {
                evt.repondre("Tag does not exist.");
            } else {
                accessMysql.deleteTag(evt.reqIdAuteur(), kw);
                evt.repondre("Tag was successfully deleted!");
            }
            accessMysql.eteindre();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}

class listAll extends Commande {
    public listAll(Bot bot) {
        super(bot);
        ajouterNom("tag list");
        ajouterNom("tag list_all");

        setDescription("List all of your tags.");
        setUtilisation("j/tag list");
        setExemple("j/tag list");

        makeSlashSherpaRun();
    }

    @Override
    public void traitement(MessageEventAdapter evt, CommandThread cmdThread) throws InterruptedException {
        try {
            AccessMysql accessMysql = reqBot().reqAccessMysql();
            StringBuilder sb = new StringBuilder();
            sb.append("List of tags:\n```");

            for (String tag : accessMysql.reqTousTag(evt.reqIdAuteur())) {
                sb.append(String.format("%s\n", tag));
            }
            sb.append("```");
            evt.repondre(sb.toString());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}