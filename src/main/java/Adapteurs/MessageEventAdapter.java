package Adapteurs;

import java.io.InputStream;

public abstract class MessageEventAdapter {

    public abstract void repondre(String message);
    public abstract void repondre(MSGGEN msg);
    public abstract void repondre(EmbedBuilderAdapter embed);
    public abstract void repondre(InputStream is, String nom);

    //getters
    public abstract Long reqIdAuteur();
    public abstract Long reqIdSalon();
    public abstract String reqContenueRaw();
    public abstract EmbedBuilderAdapter reqEmbedBuilder();
    public abstract UserAdapter reqUtilisateur();
    public abstract MemberAdapter reqMember();
    public abstract GuildAdapter reqGuild();
    public abstract String reqURL();
    public abstract Boolean estSlash();

    //messages génériques
    //DNE : does not exist
    public enum MSGGEN {
        STEAM_ID_DNE,   //L'idenfiant steam n'est pas enregistré
        DISCORD_ID_DNE,
        ERREUR_THREAD,  //Erreur générique de thread
        MANQUE_ARGS,     //Arguments manquants
        DB_CONNECT_FAIL
    }
}
