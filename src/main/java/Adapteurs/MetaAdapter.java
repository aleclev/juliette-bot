package Adapteurs;

import net.dv8tion.jda.api.entities.User;

public abstract class MetaAdapter {
    public abstract UserAdapter trouverUtilisateur(long discord_id);
    //public abstract UserAdapter trouverUtilisateurUncached(long discord_id);
    public abstract void setStatusText(PRESENCE pres, String text);

    public abstract UserAdapter stringToUser(String s);

    public abstract GuildAdapter getSherpaRun();

    public enum PRESENCE {
        RIEN,
        JOUE,
        ECOUTE,
        REGARDE
    }
}
