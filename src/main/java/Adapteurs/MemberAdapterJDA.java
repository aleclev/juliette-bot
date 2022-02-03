package Adapteurs;

import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Member;

public class MemberAdapterJDA extends MemberAdapter {
    public MemberAdapterJDA(Member mem) {
        original = mem;
    }

    @Override
    public boolean estEnLigne() {
        if (original == null) {
            return false;
        }
        return original.getOnlineStatus() != OnlineStatus.OFFLINE;
    }

    @Override
    public UserAdapter reqUser() {
        return new UserAdapterJDA(original.getUser());
    }

    @Override
    public boolean notNull() {
        return original != null;
    }

    @Override
    public void ajouterRole(long role_id) {
        var role = original.getGuild().getRoleById(role_id);

        original.getGuild().addRoleToMember(original, role).queue();
    }

    @Override
    public String reqNick() {
        return original.getEffectiveName();
    }

    Member original;
}
