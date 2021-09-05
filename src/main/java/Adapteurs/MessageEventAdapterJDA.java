package Adapteurs;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.InputStream;

public class MessageEventAdapterJDA extends MessageEventAdapter {

    private final MessageChannel salon;
    private final User utilisateur;
    private final Member member;
    private final Guild guild;
    private final Message message;
    private final Boolean estSlash;
    private final String contenu;
    private final SlashCommandEvent slashCommandEvent;


    public MessageEventAdapterJDA(MessageReceivedEvent evt) {
        salon = evt.getChannel();
        utilisateur = evt.getAuthor();

        if (evt.isFromGuild()) {
            guild = evt.getGuild();
        }
        else {
            guild = null;
        }

        message = evt.getMessage();
        contenu = message.getContentRaw();
        member = evt.getMember();
        estSlash = false;
        slashCommandEvent = null;
    }

    public MessageEventAdapterJDA(SlashCommandEvent evt) {
        salon = evt.getChannel();
        utilisateur = evt.getUser();
        member = evt.getMember();
        guild = evt.getGuild();
        message = null;
        contenu = evt.getName();
        estSlash = true;
        slashCommandEvent = evt;
    }

    /**
     * Envoi un message dans le salon d'origine de l'event.
     * @param message
     */
    @Override
    public void repondre(String message) {
        if (!estSlash) {
            salon.sendMessage(message).queue();
        }
        else {
            slashCommandEvent.reply(message).queue();
        }
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
        } else if (msg == MSGGEN.DISCORD_ID_DNE) {
            repondre("ERROR! You are not registered on discord. Use `m/register discord` (Provided by Marianne)");
        }

        else {
            repondre("ERROR! This message template is not defined. Please report this error.");
        }
    }

    @Override
    public void repondre(EmbedBuilderAdapter embed) {
        embed.build();
        salon.sendMessage((MessageEmbed) embed.build()).queue();
    }

    @Override
    public void repondre(InputStream is, String nom) {
        salon.sendFile(is, nom).queue();
    }

    @Override
    public Long reqIdAuteur() {
        return Long.parseLong(utilisateur.getId());
    }

    @Override
    public Long reqIdSalon() {
        return Long.parseLong(salon.getId());
    }

    @Override
    public String reqContenueRaw() {
        if (estSlash) {
            return contenu;
        }

        return message.getContentRaw();
    }

    @Override
    public EmbedBuilderAdapter reqEmbedBuilder() {
        return new EmbedBuilderAdapterJDA();
    }

    @Override
    public UserAdapter reqUtilisateur() {
        return new UserAdapterJDA(utilisateur);
    }

    @Override
    public MemberAdapter reqMember() {
        return new MemberAdapterJDA(member);
    }

    @Override
    public GuildAdapter reqGuild() {
        return new GuildAdapterJDA(guild);
    }

    @Override
    public String reqURL() {
        if (estSlash) {
            return "No valid URL";
        }

        return String.format("https://discordapp.com/channels/%s/%s/%s", guild.getId(), salon.getId(), message.getId());
    }

    @Override
    public Boolean estSlash() {
        return estSlash;
    }
}
