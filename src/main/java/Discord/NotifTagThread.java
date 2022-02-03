package Discord;

import Accesseur.AccessMysql;
import Adapteurs.*;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

public class NotifTagThread extends Thread {
    public NotifTagThread(MessageEventAdapter p_evt, ArrayList<String> p_l_tag, NotifTagCooldownDTO p_notifTagCooldownDTO, Bot p_bot) throws SQLException {
        evt = p_evt;
        l_tag = p_l_tag;
        bot = p_bot;
        metaAdapter = bot.reqMetaAdapter();
        accessMysql = bot.reqAccessMysql();
        notifTagCooldownDTO = p_notifTagCooldownDTO;
    }

    //TODO : assurer qu'il n'y ait pas de condition de concurrence
    @Override
    public void run() {
        try {
            if (!accessMysql.estEnregDiscord(evt.reqIdAuteur())) {
                evt.repondre("ERROR! You must register your Discord profile to use the Notification function : `m/register discord` (Provided by Marianne)");
                accessMysql.eteindre();
                return;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            accessMysql.eteindre();
            return;
        }

        EmbedBuilderAdapter eb = evt.reqEmbedBuilder();

        if (evt.reqContenueRaw().length() > 200) {
            eb.setTitle(evt.reqContenueRaw().substring(0, 199) + "...");
        }
        else {
            eb.setTitle(evt.reqContenueRaw());
        }

        notifTagCooldownDTO.update();

        if (!notifTagCooldownDTO.peutPoster()) {
            evt.repondre(String.format("Notification demand rejected, you are still on cooldown for **%s minute(s)**...", notifTagCooldownDTO.tempsRestant()));
            accessMysql.eteindre();
            return;
        }

        //initialisation de l'embed qui sera envoyé à tous les utilisateurs
        Random rand = new Random(System.currentTimeMillis());
        float r = rand.nextFloat();
        float g = rand.nextFloat();
        float b = rand.nextFloat();
        Color randomColor = new Color(r, g, b);
        eb.addField("Sender", String.format("%s", evt.reqUtilisateur().reqMention()), false);

        try {
            eb.addField("Bungie Name", String.format("%s", bot.reqAccessMysql().reqBungieNameFromDiscordID(evt.reqIdAuteur())), false);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        eb.addField("Message URL", String.format("[Click to go to message](%s)", evt.reqURL()), false);
        eb.addField("Report Problems", "[Report bugs & get help here](https://discord.com/channels/439563888342859776/878759972962447371/)\n[Join Sherpa Run](https://discord.gg/YC4gXxN)", false);
        eb.setThumbnail("https://cdn.discordapp.com/attachments/740374907883618365/818240712633221140/searchpng.com-notification-icon-png-image-free-download-2.png");
        eb.setFooter(String.format("j/notif block %s to no longer receive notifications from this user", evt.reqIdAuteur()), null);
        eb.setColor(randomColor);

        GuildAdapter guild = evt.reqGuild();

        try {
            ResultSet rs = accessMysql.reqAbonnesNotifTags(l_tag);

            //Liste complets de tous les tags incluant les tags parents
            StringBuilder messageBuilder = new StringBuilder("Detected notification request for tag(s): ");
            for (String tag : l_tag) {
                messageBuilder.append(String.format(" `%s`", tag));
            }
            String message = messageBuilder.toString();
            message += "... Executing...";
            evt.repondre(message);

            rs.last();
            int totUtilisateurs = rs.getRow();
            rs.beforeFirst();

            if (totUtilisateurs == 0) {
                evt.repondre("No users found in the provided tag(s). Demand discarded.");
                return;
            }
            //evt.repondre(String.format("**%s** user(s) found, sending notifications...", rs.getRow()));

            int totEnLigne = 0;

            while (rs.next()) {
                try {
                    long discord_id = rs.getLong(1);
                    MemberAdapter mem = guild.reqMember(discord_id);

                    if (!mem.notNull()) {
                        continue;
                    }

                    UserAdapter u = mem.reqUser();

                    if (mem.estEnLigne()) {
                        totEnLigne++;

                        //Saute si l'utilisateur est bloqué
                        if (!accessMysql.notifBlocageExiste(discord_id, evt.reqIdAuteur())) {
                            u.messagePrive(eb);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            accessMysql.eteindre();
            //evt.repondre("All notifications sent...");

            evt.repondre(String.format("All messages sent!\nOnline users found: **%s**\nTotal users found: **%s**", totEnLigne, totUtilisateurs));

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            //prends en note la requête de notification courante.
            evt.repondre("An error occurred and prevented this command from being successfully executed.");
            notifTagCooldownDTO.logNotif();
            return;
        }



        //prends en note la requête de notification courante.
        notifTagCooldownDTO.logNotif();
        evt.ajouterReaction("\uD83D\uDE4B");
    }

    private boolean utilisateurEnCooldown() {
        return false;
    }

    MessageEventAdapter evt;
    ArrayList<String> l_tag;
    Bot bot;
    MetaAdapter metaAdapter;
    AccessMysql accessMysql;
    NotifTagCooldownDTO notifTagCooldownDTO;
}
