package Adapteurs;

import Discord.Argument;
import Discord.CommandMetadata;
import Discord.CommandSignature;

import java.util.ArrayList;
import java.util.List;

public abstract class GuildAdapter {
    public abstract MemberAdapter reqMember(UserAdapter u);
    public abstract MemberAdapter reqMember(long discord_id);
    public abstract ArrayList<MemberAdapter> getMembersUncached();
    public abstract void creerSlashCommande(CommandMetadata metadata);
    public abstract void resetAllSlashCommands();
}
