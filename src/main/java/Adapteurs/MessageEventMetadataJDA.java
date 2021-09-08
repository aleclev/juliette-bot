package Adapteurs;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class MessageEventMetadataJDA {

    private final MessageChannel salon;
    private final User utilisateur;
    private final Member member;
    private final Guild guild;

    public MessageEventMetadataJDA(MessageReceivedEvent evt) {
        salon = evt.getChannel();
        utilisateur = evt.getAuthor();
        member = evt.getMember();
        if (evt.isFromGuild()) {
            guild = evt.getGuild();
        }
        else {
            guild = null;
        }
    }

    public MessageEventMetadataJDA(SlashCommandEvent evt) {
        salon = evt.getChannel();
        utilisateur = evt.getUser();
        member = evt.getMember();
        if (evt.isFromGuild()) {
            guild = evt.getGuild();
        }
        else {
            guild = null;
        }
    }

    public Guild reqGuild() {
        return guild;
    }

    public Member reqMember() {
        return member;
    }

    public User reqUtilisateur() {
        return utilisateur;
    }

    public MessageChannel reqSalon() {
        return salon;
    }
}
