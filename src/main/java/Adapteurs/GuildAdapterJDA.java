package Adapteurs;

import Discord.Argument;
import Discord.ArgumentMetaData;
import Discord.CommandMetadata;
import Discord.CommandSignature;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.requests.restaction.CommandCreateAction;
import net.dv8tion.jda.api.utils.concurrent.Task;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;

public class GuildAdapterJDA extends GuildAdapter {
    public GuildAdapterJDA(Guild guild) {
        original = guild;
    }

    @Override
    public MemberAdapter reqMember(UserAdapter u) {
        if (u == null) {
            return null;
        }
        return new MemberAdapterJDA(original.getMember((User) u.reqOriginal()));
    }

    @Override
    public MemberAdapter reqMember(long discord_id) {
        return new MemberAdapterJDA(original.getMemberById(discord_id));
    }

    @Override
    public ArrayList<MemberAdapter> getMembersUncached() {
        var listTask = original.loadMembers();
        ArrayList<MemberAdapter> r = new ArrayList<>();
        for (Member mem : listTask.get()) {
            r.add(new MemberAdapterJDA(mem));
        }
        return r;
    }

    @Override
    public void creerSlashCommande(CommandMetadata metadata) {
        //TODO: Ajouter plus de paramètres à la création de commandes slash.
        //String[] nameList = metadata.reqNoms().get(0).split("_");
        String name = metadata.reqNoms().get(0).replace(" ", "-");

        CommandCreateAction ca = original.upsertCommand(name, metadata.reqDescription());

        for (ArgumentMetaData arg : metadata.reqSignature().reqMetadata()) {
            OptionType optionType;

            switch (arg.reqType()) {
                case INT:
                    optionType = OptionType.INTEGER;
                    break;
                case FLOAT:
                    optionType = OptionType.NUMBER;
                    break;
                case STRING:
                    optionType = OptionType.STRING;
                    break;
                case USER:
                    optionType = OptionType.USER;
                    break;
                default:
                    optionType = OptionType.STRING;
            }

            ca.addOption(optionType, arg.reqNom(), arg.reqDescription(), !arg.reqOptionel());
        }
        ca.queue();
    }

    @Override
    public void resetAllSlashCommands() {
        original.updateCommands().queue();
    }

    Guild original;
}
