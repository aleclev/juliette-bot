package Commandes;

import Adapteurs.MessageEventAdapter;
import Discord.Bot;
import Discord.CommandThread;
import Discord.Commande;
import Discord.Module;

public class Destiny extends Module {
    public Destiny(Bot bot) {
        super(bot);

        //liste des commandes,
        this.ajouterCommande(new roadmap(bot));
    }
}

/**
 * Retourne l'url de la roadmap de la saison courante.
 */
class roadmap extends Commande {
    public roadmap(Bot bot) {
        super(bot);
        ajouterNom("roadmap");
        ajouterNom("rm");
        setDescription("Fetches the current season's roadmap.");
        setUtilisation("j/roadmap");
    }

    @Override
    public void traitement(MessageEventAdapter evt, CommandThread cmdThread) throws InterruptedException {
        evt.repondre(reqBot().reqDestinyDTO().roadmap_url);
    }
}