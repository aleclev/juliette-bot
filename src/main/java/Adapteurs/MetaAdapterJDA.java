package Adapteurs;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;

public class MetaAdapterJDA extends MetaAdapter {
    public MetaAdapterJDA(JDA p_jda) {
        jda = p_jda;
    }

    @Override
    public UserAdapter trouverUtilisateur(long discord_id) {
        return new UserAdapterJDA(jda.getUserById(discord_id));
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

    private JDA jda;
}
