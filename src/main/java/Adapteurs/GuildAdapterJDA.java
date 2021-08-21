package Adapteurs;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.restaction.CommandCreateAction;
import net.dv8tion.jda.api.utils.concurrent.Task;

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
    public void creerSlashCommande(String nom, String desc) {
        //TODO: Ajouter plus de paramètres à la création de commandes slash.
        CommandCreateAction ca = original.upsertCommand(nom, desc);
        ca.queue();
    }

    Guild original;
}
