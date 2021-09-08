package Commandes;

import Adapteurs.MessageEventAdapter;
import Adapteurs.UserAdapter;
import Discord.*;
import Discord.Module;
import Exceptions.APIException;
import Exceptions.CommandParseException;
import Exceptions.MasterException;
import Exceptions.MessageEventException;

import java.util.List;
import java.util.Random;

public class Misc extends Module {
    public Misc(Bot bot) {
        super(bot);

        //liste des commandes,
        this.ajouterCommande(new Ping(bot));
        this.ajouterCommande(new wake_tf_samurai(bot));
        this.ajouterCommande(new plusOne(bot));
        this.ajouterCommande(new rng(bot));
        this.ajouterCommande(new annoyUser(bot));

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
        ajouterArg(new ArgumentMetaData(ArgumentType.INT, false, "number", "Number to add 1 to"));
        makeSlashSherpaRun();
    }

    @Override
    public void traitement(MessageEventAdapter evt, CommandThread cmdThread) throws MasterException {
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
        ajouterArg(new ArgumentMetaData(ArgumentType.USER, false, "user", "The user to annoy!"));
        makeSlashSherpaRun();
    }

    @Override
    public void traitement(MessageEventAdapter evt, CommandThread cmdThread) throws MasterException {
        List<Argument> l_a = creerArguments(evt);

        UserAdapter u = l_a.get(0).toUser();

        evt.repondre(String.format("Name:%s\nID:%s\nMention:%s", u.reqNom(), u.reqId(), u.reqMention()));
    }
}

class rng extends Commande {
    public rng(Bot bot) {
        super(bot);
        ajouterNom("rng");
        setDescription("Generate a random number");
        ajouterArg(new ArgumentMetaData(ArgumentType.INT, false, "low_bound", "Lowest possible number (inclusive)"));
        ajouterArg(new ArgumentMetaData(ArgumentType.INT, true, "high_bound", "Highest possible number (inclusive)"));

        makeSlashSherpaRun();
    }

    @Override
    public void traitement(MessageEventAdapter evt, CommandThread cmdThread) throws MasterException {
        List<Argument> l_a = creerArguments(evt);

        Long borneInf;
        Long borneSup;

        if (l_a.size() == 1) {
            borneInf = 1L;
            borneSup = l_a.get(0).toLong();
        }
        else {
            borneInf = l_a.get(0).toLong();
            borneSup = l_a.get(1).toLong();
        }

        Random rand = new Random(System.currentTimeMillis());
        long i = rand.nextLong();

        if (i < 0) {
            i *= -1;
        }

        i = i % (borneSup-borneInf+1);
        i += borneInf;

        evt.repondre(String.format("I'm thinking of **%s**", i));
    }
}