package Adapteurs;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.InputStream;

public class MessageEventAdapterJDA extends MessageEventAdapter {
    private MessageReceivedEvent original;

    public MessageEventAdapterJDA(MessageReceivedEvent evt) {
        original = evt;
    }

    /**
     * Envoi un message dans le salon d'origine de l'event.
     * @param message
     */
    @Override
    public void repondre(String message) {
        original.getChannel().sendMessage(message).queue();
    }

    @Override
    public void repondre(MSGGEN msg) {
        if (msg == MSGGEN.STEAM_ID_DNE) {
            repondre("ERROR! I could not find your steam id in my database.");
            return;
        }
        else if (msg == MSGGEN.ERREUR_THREAD) {
            repondre("ERROR! There was an uncaught exception in the command's execution. No other detail can be given.");
            return;
        } else if (msg == MSGGEN.MANQUE_ARGS) {
            repondre("ERROR! Missing arguments.");
        } else if (msg == MSGGEN.DB_CONNECT_FAIL) {
            repondre("ERROR! Database connection could not be established. Please report this error if it keeps happening.");
        }
        else {
            repondre("ERROR! This message template is not defined. Please report this error.");
        }
    }

    @Override
    public void repondre(EmbedBuilderAdapter embed) {
        embed.build();
        original.getChannel().sendMessage((MessageEmbed) embed.build()).queue();
    }

    @Override
    public void repondre(InputStream is, String nom) {
        original.getChannel().sendFile(is, nom).queue();
    }

    @Override
    public Long reqIdAuteur() {
        return Long.parseLong(original.getAuthor().getId());
    }

    @Override
    public Long reqIdSalon() {
        return Long.parseLong(original.getChannel().getId());
    }

    @Override
    public String reqContenueRaw() {
        return original.getMessage().getContentRaw();
    }

    @Override
    public EmbedBuilderAdapter reqEmbedBuilder() {
        return new EmbedBuilderAdapterJDA();
    }

    @Override
    public UserAdapter reqUtilisateur() {
        return new UserAdapterJDA(original.getAuthor());
    }

    @Override
    public MemberAdapter reqMember() {
        return new MemberAdapterJDA(original.getMember());
    }

    @Override
    public GuildAdapter reqGuild() {
        return new GuildAdapterJDA(original.getGuild());
    }

    @Override
    public String reqURL() {
        return String.format("https://discordapp.com/channels/%s/%s/%s", original.getGuild().getId(), original.getChannel().getId(), original.getMessageId());
    }
}
