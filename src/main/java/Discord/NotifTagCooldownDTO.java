package Discord;

import java.awt.*;
import java.sql.Timestamp;
import java.util.ArrayList;

public class NotifTagCooldownDTO {
    public long discord_id;
    private long cooldownMinutes;
    private int messagesEnvoyes;
    private Timestamp ts;

    public NotifTagCooldownDTO(long p_discord_id, long p_cooldownMinutes) {
        discord_id = p_discord_id;
        cooldownMinutes = p_cooldownMinutes;
        messagesEnvoyes = 0;
        ts = new Timestamp(0);
    }

    public void logNotif() {
        update();

        messagesEnvoyes++;
    }

    public void update() {
        Timestamp courant = new Timestamp(System.currentTimeMillis());

        if (courant.getTime() < ts.getTime() + cooldownMinutes * 60000) {
            //messagesEnvoyes++;
        }
        else {
            ts = courant;
            messagesEnvoyes = 0;
        }
    }

    public boolean peutPoster() {
        return messagesEnvoyes < 2;
    }

    public int tempsRestant() {
        if (messagesEnvoyes < 2) {
            return 0;
        }

        else {
            Timestamp courant = new Timestamp(System.currentTimeMillis());
            return (int)(ts.getTime()-courant.getTime()+(cooldownMinutes*60000))/60000;
        }
    }
}
