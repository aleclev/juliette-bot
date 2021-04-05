package Commandes;

import Accesseur.AccessMysql;
import Adapteurs.MessageEventAdapter;
import Discord.Bot;
import Discord.CommandThread;
import Discord.Commande;
import Discord.Module;
import Fonctions.CommandParse;
import Fonctions.VerificationMessage.VerificateurMemeContexte;

import java.sql.SQLException;

public class Tag extends Module {
    public Tag(Bot bot) {
        super(bot);

        //liste des commandes,
        this.ajouterCommande(new tagGet(bot));
        this.ajouterCommande(new tagCheck(bot));
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
    }

    @Override
    public void traitement(MessageEventAdapter evt, CommandThread cmdThread) throws InterruptedException {
        String[] args = CommandParse.decouperCommande(evt.reqContenueRaw());
        if (args.length < 3) {
            evt.repondre(MessageEventAdapter.MSGGEN.MANQUE_ARGS);
        }

        try {
            AccessMysql accessMysql = reqBot().reqAccessMysql();
            String tag_text = accessMysql.reqTag(evt.reqIdAuteur(), args[2]);
            evt.repondre(String.format("**%s :**\n%s", args[2], tag_text));
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
    }

    @Override
    public void traitement(MessageEventAdapter evt, CommandThread cmdThread) throws InterruptedException {
        String[] args = CommandParse.decouperCommande(evt.reqContenueRaw());
        if (args.length < 4) {
            evt.repondre(MessageEventAdapter.MSGGEN.MANQUE_ARGS);
        }

        String tag_text = "";

        for (int i = 3; i < args.length; i++) {
            tag_text += args[i];

            if (i != args.length-1) {
                tag_text += " ";
            }
        }

        try {
            AccessMysql accessMysql;
            accessMysql = reqBot().reqAccessMysql();
            if (accessMysql.checkTag(evt.reqIdAuteur(), args[2])) {
                evt.repondre("This tag already exists. Would you like to override the old value? (y/n)");
                if ((cmdThread.attendreReponseMessage(30, new VerificateurMemeContexte(evt))).reqContenueRaw().equals("y")) {
                    accessMysql.setTag(evt.reqIdAuteur(), args[2], tag_text);
                    evt.repondre("Tag successfully saved!");
                } else {
                    evt.repondre("Command cancelled.");
                }
            } else {
                accessMysql.setTag(evt.reqIdAuteur(), args[2], tag_text);
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
    }

    @Override
    public void traitement(MessageEventAdapter evt, CommandThread cmdThread) throws InterruptedException {
        AccessMysql accessMysql;
        try {
            accessMysql = reqBot().reqAccessMysql();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            evt.repondre(MessageEventAdapter.MSGGEN.DB_CONNECT_FAIL);
            return;
        }

        String[] args = CommandParse.decouperCommande(evt.reqContenueRaw());
        if (args.length < 3) {
            evt.repondre(MessageEventAdapter.MSGGEN.MANQUE_ARGS);
        }

        try {
            if (!accessMysql.checkTag(evt.reqIdAuteur(), args[2])) {
                evt.repondre("Tag does not exist.");
            } else {
                accessMysql.deleteTag(evt.reqIdAuteur(), args[2]);
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