package Commandes;

import Accesseur.AccessMysql;
import Adapteurs.MessageEventAdapter;
import Discord.Bot;
import Discord.CommandThread;
import Discord.Commande;
import Discord.Module;

import java.sql.SQLException;

public class Steam extends Module {
    public Steam (Bot bot) {
        super(bot);
        this.ajouterCommande(new steam_joinme(bot));
        this.ajouterCommande(new steam_id(bot));
        this.setNom("Steam");
    }
}

/**
 * Retourne le steam id de l'auteur du message
 */
class steam_joinme extends Commande {
    public steam_joinme(Bot bot) {
        super(bot);
        ajouterNom("steam joinme");
        ajouterNom("st j");
        setDescription("Summons your Destiny 2 /join code.");
        setUtilisation("j/steam joinme");
        setExemple("j/steam joinme");
        makeSlashSherpaRun();
    }

    @Override
    public void traitement(MessageEventAdapter evt, CommandThread cmdThread) throws InterruptedException {
        long discord_id = evt.reqIdAuteur();
        Long steam_id = null;
        try {
            AccessMysql accessMysql = reqBot().reqAccessMysql();
            steam_id = accessMysql.reqIdSteam(discord_id);
            evt.repondre(String.format("Join code of %s\n```css\n/join %s```", evt.reqUtilisateur().reqMention(), steam_id));
            accessMysql.eteindre();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            evt.repondre(MessageEventAdapter.MSGGEN.STEAM_ID_DNE);
        }
    }
}

/**
 * Retourne l'id steam de l'auteur si enregistré.
 */
class steam_id extends Commande {
    public steam_id(Bot bot) {
        super(bot);
        ajouterNom("steam id");
        ajouterNom("st id");
        setDescription("Returns your steam id if registered.");
        setUtilisation("j/steam id");
        setExemple("j/steam id");
        makeSlashSherpaRun();
    }

    @Override
    public void traitement(MessageEventAdapter evt, CommandThread cmdThread) throws InterruptedException {
        long discord_id = evt.reqIdAuteur();
        Long steam_id = null;
        try {
            AccessMysql accessMysql = reqBot().reqAccessMysql();
            steam_id = accessMysql.reqIdSteam(discord_id);
            evt.repondre(String.format("Steam id of %s: **%s**", evt.reqUtilisateur().reqMention(), steam_id));
            accessMysql.eteindre();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            evt.repondre(MessageEventAdapter.MSGGEN.STEAM_ID_DNE);
        }
    }
}