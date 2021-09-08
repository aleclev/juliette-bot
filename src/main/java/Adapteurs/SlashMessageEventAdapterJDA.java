package Adapteurs;

import Discord.Argument;
import Discord.ArgumentMetaData;
import Discord.CommandMetadata;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import java.util.List;

import java.util.ArrayList;

public class SlashMessageEventAdapterJDA extends MessageEventAdapterJDA {
    private final SlashCommandEvent original;

    public SlashMessageEventAdapterJDA(SlashCommandEvent evt) {
        super(new MessageEventMetadataJDA(evt));
        original = evt;
    }

    @Override
    public String reqContenueRaw() {
        return original.getName();
    }

    @Override
    public String reqURL() {
        return "No valid URL for slash interactions.";
    }

    @Override
    public Boolean estSlash() {
        return true;
    }

    @Override
    public ArrayList<Argument> genArgs(CommandMetadata metadata) {
        int i = 0;
        List<ArgumentMetaData> l_m = metadata.reqSignature().reqMetadata();
        ArrayList<Argument> r = new ArrayList<>();

        while (i < original.getOptions().size() && i < l_m.size()) {
            Object o = null;
            OptionMapping opt = original.getOptions().get(i);
            switch (l_m.get(i).reqType()) {
                case INT:
                    o = opt.getAsLong();
                    break;
                case STRING:
                    o = opt.getAsString();
                    break;
                case USER:
                    o = new UserAdapterJDA(opt.getAsUser());
            }
            r.add(new Argument(o, l_m.get(i)));
            i++;
        }
        return r;
    }

    @Override
    public void repondre(String m) {
        //Impossible de répondre plus d'une fois à une interaction slash.
        if (original.isAcknowledged()) {
            original.getChannel().sendMessage(m).queue();
        }
        else {
            original.reply(m).queue();
        }
    }
}
