package Adapteurs;

import Discord.Argument;
import Discord.ArgumentMetaData;
import Discord.CommandMetadata;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;

public class NormalMessageEventAdapterJDA extends MessageEventAdapterJDA {
    public NormalMessageEventAdapterJDA(MessageReceivedEvent evt) {
        super(new MessageEventMetadataJDA(evt));
        original = evt;
    }

    @Override
    public String reqContenueRaw() {
        return original.getMessage().getContentRaw();
    }

    @Override
    public String reqURL() {
        return original.getMessage().getJumpUrl();
    }

    @Override
    public Boolean estSlash() {
        return false;
    }


    @Override
    public ArrayList<Argument> genArgs(CommandMetadata metadata) {
        ArrayList<Argument> r = new ArrayList<>();

        int i = 0;

        return null;
    }

    private final MessageReceivedEvent original;
}
