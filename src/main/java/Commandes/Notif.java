package Commandes;

import Accesseur.AccessMysql;
import Adapteurs.MessageEventAdapter;
import Adapteurs.UserAdapter;
import Discord.*;
import Discord.Module;
import Exceptions.APIException;
import Exceptions.CommandParseException;
import Exceptions.MasterException;
import Fonctions.CommandParse;
import Fonctions.VerificationMessage.VerificateurMemeContexte;
import Misc.GenerateurGraphNotifTag;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class Notif  extends Module {
    public Notif(Bot bot) {
        super(bot);

        //liste des commandes,
        this.ajouterCommande(new updateDnd(bot));
        this.ajouterCommande(new bloquerUtilisateur(bot));
        this.ajouterCommande(new retirerTousBlocages(bot));
        this.ajouterCommande(new retirerBlocages(bot));
        this.ajouterCommande(new reqBlacklist(bot));
        this.ajouterCommande(new ajouterTag(bot));
        this.ajouterCommande(new enleverTag(bot));
        this.ajouterCommande(new reqTousNotifTags(bot));
        this.ajouterCommande(new retirerTousNotifTags(bot));
        this.ajouterCommande(new reqGraph(bot));

        this.setNom("NotifTag");
    }
}

class updateDnd extends Commande {
    public updateDnd(Bot bot) {
        super(bot);
        ajouterNom("notif dnd");
        setDescription("Set a number of hours for which you will not receive any notifications (do not disturb).");
        setUtilisation("j/notif dnd <hours>");
        setExemple("j/notif dnd <hours>");

        ajouterArg(new ArgumentMetaData(
                ArgumentType.INT,
                false,
                "hours",
                "Number of hours for which you will not receive notifications."
        ));

        makeSlashSherpaRun();
    }

    @Override
    public void traitement(MessageEventAdapter evt, CommandThread cmdThread) throws InterruptedException, MasterException {
        var args = creerArguments(evt);

        long hourOffset = args.get(0).toLong();

        try {
            AccessMysql accessMysql = reqBot().reqAccessMysql();
            Timestamp ts = new Timestamp(System.currentTimeMillis() + hourOffset * (1000*60*60));
            accessMysql.updateDND(evt.reqIdAuteur(), ts);
            evt.repondre(String.format("You will not recieve ping notifications for the next %s hours. You can cancel this action with `j/notif dnd 0`", hourOffset));
            accessMysql.eteindre();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}

class bloquerUtilisateur extends Commande {
    public bloquerUtilisateur(Bot bot) {
        super(bot);
        ajouterNom("notif block");

        ajouterArg(new ArgumentMetaData(
                ArgumentType.USER,
                false,
                "user",
                "The user you want to block."
                )
        );

        makeSlashSherpaRun();
    }
    @Override
    public void traitement(MessageEventAdapter evt, CommandThread cmdThread) throws InterruptedException, MasterException {
        var args = creerArguments(evt);

        long discord_id = args.get(0).toUser().reqId();

        try {
            AccessMysql accessMysql = reqBot().reqAccessMysql();
            accessMysql.ajouterBlocage(evt.reqIdAuteur(), discord_id);
            evt.repondre("User blocked!");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            evt.repondre("ERROR! User may already be blocked!");
        }
    }
}

class retirerTousBlocages extends Commande {
    public retirerTousBlocages(Bot bot) {
        super(bot);
        ajouterNom("notif unblock_all");
        setDescription("Unblock all previously blocked users.");
        setUtilisation("j/notif unblock_all");
        setExemple("j/notif unblock_all");

        makeSlashSherpaRun();
    }
    @Override
    public void traitement(MessageEventAdapter evt, CommandThread cmdThread) throws InterruptedException {
        evt.repondre("This command will unblock every user you previously blocked. Do you wish to proceed? (y/n)");
        MessageEventAdapter res = cmdThread.attendreReponseMessage(60, new VerificateurMemeContexte(evt));

        if (!res.reqContenueRaw().equals("y")) {
            evt.repondre("Command cancelled!");
            return;
        }

        try {
            AccessMysql accessMysql = reqBot().reqAccessMysql();
            accessMysql.enleverTousBlocage(evt.reqIdAuteur());
            evt.repondre("Operation successful!");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}

class retirerBlocages extends Commande {
    public retirerBlocages(Bot bot) {
        super(bot);
        ajouterNom("notif unblock");
        setDescription("Unblock a user");
        setUtilisation("j/notif unblock <discord_id>");
        setExemple("j/notif unblock 1234567890");

        ajouterArg(new ArgumentMetaData(
                ArgumentType.USER,
                false,
                "user",
                "The user to unblock"
        ));

        makeSlashSherpaRun();
    }

    @Override
    public void traitement(MessageEventAdapter evt, CommandThread cmdThread) throws InterruptedException, MasterException {
        var args = creerArguments(evt);

        long discord_id = args.get(0).toUser().reqId();

        try {
            AccessMysql accessMysql = reqBot().reqAccessMysql();
            accessMysql.enleverBlocage(evt.reqIdAuteur(), discord_id);
            evt.repondre("User unblocked!");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}

class reqBlacklist extends Commande {
    public reqBlacklist(Bot bot) {
        super(bot);
        ajouterNom("notif blacklist");
        setDescription("Returns the list of all blocked users.");
        setUtilisation("j/notif blacklist");
        setExemple("j/notif blacklist");
    }

    @Override
    public void traitement(MessageEventAdapter evt, CommandThread cmdThread) throws InterruptedException {
        try {
            AccessMysql accessMysql = reqBot().reqAccessMysql();
            ResultSet rs = accessMysql.reqBlacklist(evt.reqIdAuteur());

            StringBuilder sb = new StringBuilder();
            sb.append("You do not get notifications from the following users : \n");

            while(rs.next()) {
                sb.append(String.format("<@%s>\n", rs.getLong(1)));
            }
            evt.repondre(sb.toString());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}

class ajouterTag extends Commande {
    public ajouterTag(Bot bot) {
        super(bot);
        ajouterNom("notif add");
        setDescription("Adds a tag to your list.");
        setUtilisation("j/notif add <tagName>");
        setExemple("j/notif add raid");
        ajouterArg(new ArgumentMetaData(
                ArgumentType.STRING,
                false,
                "tag",
                "The tag you want to add to your list"
        ));

        makeSlashSherpaRun();
    }

    @Override
    public void traitement(MessageEventAdapter evt, CommandThread cmdThread) throws InterruptedException, MasterException {
        var args = creerArguments(evt);

        String tag = args.get(0).toStr();

        try {
            AccessMysql accessMysql = reqBot().reqAccessMysql();
            accessMysql.ajouterNotifTag(evt.reqIdAuteur(), tag);
            evt.repondre("Tag added!");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            evt.repondre("ERROR! You may already be subscribed to this tag.");
        }
    }
}

class enleverTag extends Commande {
    public enleverTag(Bot bot) {
        super(bot);
        ajouterNom("notif del");
        setDescription("Removes a tag from your list.");
        setUtilisation("j/notif del <tagName>");
        setExemple("j/notif del raid");

        ajouterArg(new ArgumentMetaData(
                ArgumentType.STRING,
                false,
                "tag",
                "The tag you want to add to your list."
        ));

        makeSlashSherpaRun();
    }

    @Override
    public void traitement(MessageEventAdapter evt, CommandThread cmdThread) throws InterruptedException, MasterException {

        var args = creerArguments(evt);
        String tag = args.get(0).toStr();

        try {
            AccessMysql accessMysql = reqBot().reqAccessMysql();
            accessMysql.enleverNotifTag(evt.reqIdAuteur(), tag);
            evt.repondre("Tag removed!");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}

class reqTousNotifTags extends Commande {
    public reqTousNotifTags(Bot bot) {
        super(bot);
        ajouterNom("notif list_all");
        ajouterNom("notif list");
        setDescription("Returns the list of all your tags.");
        setUtilisation("j/notif list_all");
        setExemple("j/notif_list");

        makeSlashSherpaRun();
    }

    @Override
    public void traitement(MessageEventAdapter evt, CommandThread cmdThread) throws InterruptedException {
        try {
            AccessMysql accessMysql = reqBot().reqAccessMysql();
            ResultSet rs = accessMysql.reqTousNotifTags(evt.reqIdAuteur());

            StringBuilder sb = new StringBuilder();
            sb.append("List of all your tags : ```\n");

            while(rs.next()) {
                sb.append(String.format("%s\n", rs.getString(1)));
            }
            sb.append("```");
            evt.repondre(sb.toString());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}

class retirerTousNotifTags extends Commande {
    public retirerTousNotifTags(Bot bot) {
        super(bot);
        ajouterNom("notif remove_all");
        setDescription("Removes all of your tags.");
        setUtilisation("j/notif remove_all");
        setExemple("j/notif remove_all");

        makeSlashSherpaRun();
    }
    @Override
    public void traitement(MessageEventAdapter evt, CommandThread cmdThread) throws InterruptedException {
        evt.repondre("This command will remove all your tags. Do you wish to proceed? (y/n)");
        MessageEventAdapter res = cmdThread.attendreReponseMessage(60, new VerificateurMemeContexte(evt));

        if (!res.reqContenueRaw().equals("y")) {
            evt.repondre("Command cancelled!");
            return;
        }

        try {
            AccessMysql accessMysql = reqBot().reqAccessMysql();
            accessMysql.enleverTousNotifTags(evt.reqIdAuteur());
            evt.repondre("Operation successful!");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}

class reqGraph extends Commande {
    public reqGraph(Bot bot) {
        super(bot);
        ajouterNom("notif graph");
        setDescription("Generate a graph of all defined relationships between notification tags.");
        setUtilisation("j/notif graph");
        setExemple("j/notif graph");

        makeSlashSherpaRun();
    }
    @Override
    public void traitement(MessageEventAdapter evt, CommandThread cmdThread) throws InterruptedException {
        try {
            GenerateurGraphNotifTag generateurGraphNotifTag = new GenerateurGraphNotifTag(reqBot().reqAccessMysql());
            evt.repondre(generateurGraphNotifTag.genererGraph(), "graph.png");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}