package Adapteurs;

import Discord.Argument;
import Discord.ArgumentMetaData;
import Discord.CommandMetadata;
import Wrapper.DiscordAPI.RequestBody.Threads.CreateThreadBody;
import Wrapper.DiscordAPI.Threads;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.IOException;
import java.util.ArrayList;

public class NormalMessageEventAdapterJDA extends MessageEventAdapterJDA {
    public NormalMessageEventAdapterJDA(MessageReceivedEvent evt) {
        super(new MessageEventMetadataJDA(evt));
        original = evt;
    }

    public void createThread() {
        //
    }

    @Override
    public void ajouterReaction(String r) {
        original.getMessage().addReaction(r).queue();
    }

    @Override
    public void createThread(String threadName) {
        CreateThreadBody body = new CreateThreadBody();
        body.name = threadName;
        try {
            Threads.createThread(original.getChannel().getIdLong(), original.getMessageIdLong(), body);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
