package Discord;

import java.sql.Timestamp;

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
        Timestamp courant = new Timestamp(System.currentTimeMillis());

        //le temps s'est écoulé
        if (courant.getTime() > ts.getTime() + cooldownMinutes * 60000) {
            ts = courant;
            messagesEnvoyes = 1;
        } else {
            messagesEnvoyes++;
            if (!(messagesEnvoyes > 2)) {
                ts = courant;
            }
        }
    }

    public boolean peutPoster() {
        return !(messagesEnvoyes > 2);
    }

    public int tempsRestant() {
        if (!(messagesEnvoyes > 2)) {
            return 0;
        }

        else {
            Timestamp courant = new Timestamp(System.currentTimeMillis());
            return (int)(ts.getTime()-courant.getTime()+(cooldownMinutes*60000))/60000;
        }
    }
}
