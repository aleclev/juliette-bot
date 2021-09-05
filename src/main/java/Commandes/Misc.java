package Commandes;

import Adapteurs.MessageEventAdapter;
import Discord.*;
import Discord.Module;
import java.util.List;

public class Misc extends Module {
    public Misc(Bot bot) {
        super(bot);

        //liste des commandes,
        this.ajouterCommande(new Ping(bot));
        this.ajouterCommande(new wake_tf_samurai(bot));
        //this.ajouterCommande(new plusOne(bot));
        //this.ajouterCommande(new annoyUser(bot));

        this.setNom("Misc");
    }
}

class Ping extends Commande {
    public Ping(Bot bot) {
        super(bot);
        ajouterNom("ping");
        setDescription("Juliette responds with Pong!. Used to see if she is online.");
        setUtilisation("j/ping");
        setExemple("j/ping");
        makeSlashSherpaRun();
    }

    @Override
    public void traitement(MessageEventAdapter evt, CommandThread cmdThread) {
        evt.repondre("Pong !");
    }
}

class wake_tf_samurai extends Commande {
    public wake_tf_samurai(Bot bot) {
        super(bot);
        ajouterNom("wake the fuck up samurai");
    }

    @Override
    public void traitement(MessageEventAdapter evt, CommandThread cmdThread) {
        evt.repondre("https://thumbs.gfycat.com/CreativeRepulsiveCatbird-max-1mb.gif");
        try {
            Thread.sleep(1000*3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        evt.repondre("*We've got a city to burn...*");
        try {
            Thread.sleep(3*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        evt.repondre("exhale made me do it...");
    }
}

class plusOne extends Commande {
    public plusOne(Bot bot) {
        super(bot);
        ajouterNom("plus_one");
        ajouterArg("number", "Number to add 1 to", Argument.Type.INT, false);
        makeSlashSherpaRun();
    }

    @Override
    public void traitement(MessageEventAdapter evt, CommandThread cmdThread) {
        List<Argument> l_a = creerArguments(evt);

        Integer i = l_a.get(0).toInt();
        i++;

        evt.repondre(String.format("Result : %s", i));
    }
}

class annoyUser extends Commande {
    public annoyUser(Bot bot) {
        super(bot);
        ajouterNom("annoy");
        setDescription("Annoy a user");
        ajouterArg("user", "The user to annoy!", Argument.Type.USER, false);
        makeSlashSherpaRun();
    }

    @Override
    public void traitement(MessageEventAdapter evt, CommandThread cmdThread) {
        List<Argument> l_a = creerArguments(evt);

        Integer i = l_a.get(0).toInt();
        i++;

        evt.repondre(String.format("Result : %s", i));
    }
}