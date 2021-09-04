package Commandes;

import Adapteurs.MessageEventAdapter;
import Discord.Bot;
import Discord.CommandThread;
import Discord.Commande;
import Discord.Module;

import java.sql.SQLException;

public class Destiny extends Module {
    public Destiny(Bot bot) {
        super(bot);

        this.setNom("Destiny");

        //liste des commandes,
        this.ajouterCommande(new roadmap(bot));
        this.ajouterCommande(new bungieJoinme(bot));
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
        makeSlashSherpaRun();
    }

    @Override
    public void traitement(MessageEventAdapter evt, CommandThread cmdThread) throws InterruptedException {
        evt.repondre(reqBot().reqDestinyDTO().roadmap_url);
    }
}

/**
 * Retourne le bungie name de l'utilisateur s'il est enregistré.
 */
class bungieJoinme extends Commande {
    public bungieJoinme(Bot bot) {
        super(bot);
        ajouterNom("bungie joinme");
        ajouterNom("b jm");
        setDescription("Copies your bungie name if your steam profile is registered.");
        setUtilisation("j/bungie joinme");
    }

    @Override
    public void traitement(MessageEventAdapter evt, CommandThread cmdThread) throws InterruptedException {
        try {
            if (!reqBot().reqAccessMysql().estEnregDiscord(evt.reqIdAuteur())) {
                evt.repondre(MessageEventAdapter.MSGGEN.DISCORD_ID_DNE);
                return;
            }
            else if (!reqBot().reqAccessMysql().estEnregSteam(evt.reqIdAuteur())) {
                evt.repondre(MessageEventAdapter.MSGGEN.STEAM_ID_DNE);
                return;
            }
            else {
                String bn = reqBot().reqAccessMysql().reqBungieNameFromDiscordID(evt.reqIdAuteur());
                evt.repondre(String.format("Join code of %s\n```\n/join %s```", evt.reqUtilisateur().reqMention(), bn));
                return;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            evt.repondre("ERROR!");
        }
    }
}