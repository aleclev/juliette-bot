package Adapteurs;

import java.util.ArrayList;

public abstract class GuildAdapter {
    public abstract MemberAdapter reqMember(UserAdapter u);
    public abstract MemberAdapter reqMember(long discord_id);
    public abstract ArrayList<MemberAdapter> getMembersUncached();
    public abstract void creerSlashCommande(String nom, String desc);
}
