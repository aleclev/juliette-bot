package Interface;

import de.cerus.jdasc.JDASlashCommands;
import de.cerus.jdasc.command.ApplicationCommandListener;
import de.cerus.jdasc.command.CommandBuilder;
import de.cerus.jdasc.interaction.Interaction;
import net.dv8tion.jda.api.JDA;
import org.json.simple.JSONObject;

public class SlashInterfaceJDA implements ApplicationCommandListener {
    public SlashInterfaceJDA(JSONObject config, JDA jda) {
        JDASlashCommands.initialize(jda, (String)config.get("jetton"), "123");
        JDASlashCommands.submitGlobalCommand(new CommandBuilder().name("hello").desc("something cool").build(), this);
        System.out.print("Hello!!!\n");
    }

    @Override
    public void onInteraction(Interaction interaction) {
        System.out.print("Interaction confirmé!\n");
    }
}
