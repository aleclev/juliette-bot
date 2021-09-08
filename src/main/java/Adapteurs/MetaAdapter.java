package Adapteurs;

import Discord.Argument;
import Discord.CommandMetadata;
import Exceptions.APIException;
import Exceptions.CommandParseException;
import Exceptions.MessageEventException;
import Exceptions.UserNotFoundException;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;

public abstract class MetaAdapter {
    public abstract UserAdapter trouverUtilisateur(long discord_id) throws UserNotFoundException;
    //public abstract UserAdapter trouverUtilisateurUncached(long discord_id);
    public abstract void setStatusText(PRESENCE pres, String text);

    public abstract UserAdapter buildUser(Long id);

    public abstract ArrayList<Argument> genArgs(CommandMetadata cd, MessageEventAdapter evt) throws MessageEventException, APIException;

    public abstract GuildAdapter getSherpaRun();

    public abstract void resetSherpaRun();

    public abstract UserAdapter reqOwner();

    public enum PRESENCE {
        RIEN,
        JOUE,
        ECOUTE,
        REGARDE
    }
}
