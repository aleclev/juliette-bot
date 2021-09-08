package Commandes;

import Adapteurs.EmbedBuilderAdapter;
import Adapteurs.MessageEventAdapter;
import Adapteurs.MetaAdapter;
import CommandPermission.IsOwnerGate;
import Discord.Bot;
import Discord.CommandThread;
import Discord.Commande;
import Discord.Module;
import Exceptions.UserNotFoundException;
import Fonctions.CommandParse;
import Fonctions.VerificationMessage.VerificateurMemeContexte;

import java.sql.Timestamp;

//TODO: Permissions d'accès aux commandes classifiés.
/**
 * @class Dev classe pour faire des tests directement dans discord.
 */
public class Dev extends Module {
    public Dev(Bot bot) {
        super(bot);

        //liste des commandes,
        this.ajouterCommande(new messageTest(bot));
        //this.ajouterCommande(new threadTest(bot));
        //this.ajouterCommande(new reponseTest(bot));
        //this.ajouterCommande(new testAttenteLongue(bot));
        //this.ajouterCommande(new testMessagePrivee(bot));
        //this.ajouterCommande(new testEmbed(bot));
        //this.ajouterCommande(new setStatus(bot));
    }
}

class messageTest extends Commande {
    public messageTest(Bot bot) {
        super(bot);
        ajouterNom("blocage");
        this.reqMetadata().setGate(new IsOwnerGate());
    }

    @Override
    public void traitement(MessageEventAdapter evt, CommandThread cmdThread) throws InterruptedException {
        evt.repondre("Salut la gang!");
    }
}

class threadTest extends Commande {
    public threadTest(Bot bot) {
        super(bot);
        ajouterNom("thread_test");
    }

    @Override
    public void traitement(MessageEventAdapter evt, CommandThread cmdThread) throws InterruptedException {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        evt.repondre(String.format("Attente de 10 secondes... id=%s", timestamp.getNanos()));
        Thread.sleep(10000);
        evt.repondre(String.format("Temps écoulé... id=%s", timestamp.getNanos()));
    }
}

class reponseTest extends Commande {
    public reponseTest(Bot bot) {
        super(bot);
        ajouterNom("reponse");
    }

    @Override
    public void traitement(MessageEventAdapter evt, CommandThread cmdThread) throws InterruptedException {
        evt.repondre("Commande reçue, attente de réponse...");
        MessageEventAdapter res = cmdThread.attendreReponseMessage(10, new VerificateurMemeContexte(evt));
        if (res == null) {
            evt.repondre("Timeout...");
        } else {
            evt.repondre(res.reqContenueRaw());
        }

        evt.repondre("En attente d'une deuxième réponse.");

        res = cmdThread.attendreReponseMessage(10, new VerificateurMemeContexte(evt));

        if (res == null) {
            evt.repondre("Timeout 2...");
        } else {
            evt.repondre(res.reqContenueRaw());
        }
    }
}

class testAttenteLongue extends Commande {
    public testAttenteLongue(Bot bot) {
        super(bot);
        ajouterNom("test_attente_longue");
    }

    @Override
    public void traitement(MessageEventAdapter evt, CommandThread cmdThread) throws InterruptedException {
        evt.repondre("Sommeil de 30 secondes...");
        Thread.sleep(30*1000);
        evt.repondre("Sommeil terminé...");
    }
}

class testMessagePrivee extends Commande {
    public testMessagePrivee(Bot bot) {
        super(bot);
        ajouterNom("test_message_prive");
    }

    @Override
    public void traitement(MessageEventAdapter evt, CommandThread cmdThread) throws InterruptedException, UserNotFoundException {

        EmbedBuilderAdapter eb = evt.reqEmbedBuilder();
        eb.setTitle("title");
        reqBot().reqMetaAdapter().trouverUtilisateur(evt.reqIdAuteur()).messagePrive(eb);
    }
}

class testEmbed extends Commande {
    public testEmbed(Bot bot) {
        super(bot);
        ajouterNom("test_embed");
    }

    @Override
    public void traitement(MessageEventAdapter evt, CommandThread cmdThread) throws InterruptedException {
        EmbedBuilderAdapter eb = evt.reqEmbedBuilder();

        eb.setTitle("Ceci est un embed");
        eb.addField("Ceci est un champs", "ceci est une valeur", false);
        evt.repondre(eb);
    }
}

class setStatus extends Commande {
    public setStatus(Bot bot) {
        super(bot);
        ajouterNom("set_status");
    }

    @Override
    public void traitement(MessageEventAdapter evt, CommandThread cmdThread) throws InterruptedException {
        String[] args = CommandParse.decouperCommande(evt.reqContenueRaw());
        if (args.length < 3) {
            evt.repondre(MessageEventAdapter.MSGGEN.MANQUE_ARGS);
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 2; i < args.length; i++) {
            sb.append(args[i]).append(" ");
        }

        String text = sb.toString();

        if (args[1].equals("ecoute")) {
            reqBot().reqMetaAdapter().setStatusText(MetaAdapter.PRESENCE.ECOUTE, text);
        } else if (args[1].equals("regarde")) {
            reqBot().reqMetaAdapter().setStatusText(MetaAdapter.PRESENCE.REGARDE, text);
        } else if (args[1].equals("joue")) {
            reqBot().reqMetaAdapter().setStatusText(MetaAdapter.PRESENCE.JOUE, text);
        } else {
            evt.repondre("args[2] accepte seulement : ecoute, regarde, joue");
        }
        evt.repondre("Succès !");
    }
}