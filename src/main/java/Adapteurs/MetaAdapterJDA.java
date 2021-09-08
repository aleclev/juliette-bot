package Adapteurs;

import Discord.Argument;
import Discord.CommandMetadata;
import Exceptions.APIException;
import Exceptions.CommandParseException;
import Exceptions.MessageEventException;
import Exceptions.UserNotFoundException;
import Fonctions.CommandParse;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public class MetaAdapterJDA extends MetaAdapter {
    private static MetaAdapterJDA instance = null;

    private MetaAdapterJDA(JDA p_jda) {
        jda = p_jda;
        //instance = new MetaAdapterJDA(jda);
    }

    public static MetaAdapter construireMetaAdapter(JDA p_jda) {
        if (instance==null) {
            instance = new MetaAdapterJDA(p_jda);
        }
        return instance;
    }

    public static MetaAdapter reqMetaAdapter() {
        return instance;
    }

    @Override
    public UserAdapter trouverUtilisateur(long discord_id) throws UserNotFoundException {
        User u = jda.getUserById(discord_id);

        if (u == null) {
            throw new UserNotFoundException(discord_id);
        }
        return new UserAdapterJDA(u);
    }

    @Override
    public void setStatusText(PRESENCE pres, String text) {
        if (pres == PRESENCE.ECOUTE) {
            jda.getPresence().setActivity(Activity.listening(text));
        } else if (pres == PRESENCE.REGARDE) {
            jda.getPresence().setActivity(Activity.watching(text));
        } else if (pres == PRESENCE.JOUE) {
            jda.getPresence().setActivity(Activity.playing(text));
        }
    }

    @Override
    public UserAdapter buildUser(Long id) {
        User u = jda.getUserById(id);
        return null;
    }

    @Override
    public ArrayList<Argument> genArgs(CommandMetadata cd, MessageEventAdapter evt) throws MessageEventException, APIException {
        if (evt instanceof NormalMessageEventAdapterJDA) {
            return genArgs(cd, (NormalMessageEventAdapterJDA)evt);
        }
        else if (evt instanceof SlashMessageEventAdapterJDA) {
            return genArgs(cd, (SlashMessageEventAdapterJDA)evt);
        }
        else {
            return null;
        }
    }

    public ArrayList<Argument> genArgs(CommandMetadata cd, SlashMessageEventAdapterJDA evt) {
        return evt.genArgs(cd);
    }


    public ArrayList<Argument> genArgs(CommandMetadata cd, NormalMessageEventAdapterJDA evt) throws MessageEventException, APIException {
        return CommandParse.genererArgumentsText(cd, evt);
    }

    /**
     * Retourne un adapteur pour Sherpa Run.
     * @return
     */
    @Override
    @Nonnull
    public GuildAdapter getSherpaRun() {
        Guild sr_jda = jda.getGuildById("439563888342859776");
        return new GuildAdapterJDA(sr_jda);
    }

    /**
     * Fait un refresh des commandes sur Sherpa Run.
     */
    @Override
    public void resetSherpaRun() {
        var g = getSherpaRun();

        g.resetAllSlashCommands();
    }

    //TODO : Trouver un moyen d'obtenir le propriétaire de façon dynamique.
    @Override
    public UserAdapter reqOwner() {
        return new UserAdapterJDA(jda.getUserById(250785024767557632L));
    }

    private final JDA jda;
}
