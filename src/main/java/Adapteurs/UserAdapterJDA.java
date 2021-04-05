package Adapteurs;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

public class UserAdapterJDA extends UserAdapter{
    public UserAdapterJDA(User utilisateur) {
        original = utilisateur;
    }

    public void messagePrive(String message) {
        original.openPrivateChannel()
                .flatMap(channel -> channel.sendMessage(message))
                .queue();
    }

    @Override
    public void messagePrive(EmbedBuilderAdapter eb) {
        original.openPrivateChannel()
                .flatMap(channel -> channel.sendMessage((MessageEmbed) eb.build()))
                .queue();
    }

    @Override
    public boolean estEnLigne() {
        return false;
    }

    @Override
    public String reqNom() {
        return original.getName();
    }

    @Override
    public Object reqOriginal() {
        return original;
    }

    @Override
    public String reqMention() {
        return original.getAsMention();
    }

    @Override
    public long reqId() {
        return Long.parseLong(original.getId());
    }

    User original;
}
