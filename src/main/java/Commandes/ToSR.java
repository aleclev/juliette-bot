package Commandes;

import Accesseur.AccessMysql;
import Adapteurs.MemberAdapter;
import Adapteurs.MessageEventAdapter;
import Discord.*;
import Discord.Module;
import Exceptions.MasterException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ToSR extends Module {
    public ToSR(Bot bot) {
        super(bot);

        //liste des commandes,
        this.ajouterCommande(new regFreeAgent(bot));
        this.ajouterCommande(new regTeam(bot));

        this.setNom("Trials of Sherpa Run");
    }
}

class regFreeAgent extends Commande {
    public regFreeAgent(Bot bot) {
        super(bot);
        ajouterNom("reg fa");
        setDescription("Register as a free agent. This means you will get randomly paired with random teammates.");
        setUtilisation("j/reg fa");
        setExemple("j/reg fa");

        //makeSlashSherpaRun();
    }

    @Override
    public void traitement(MessageEventAdapter evt, CommandThread cmdThread) throws InterruptedException, MasterException {
        evt.reqMember().ajouterRole(889953187442880513L);
        evt.reqMember().ajouterRole(888914890352394280L);

        AccessMysql access = null;
        try {
            access = reqBot().reqAccessMysql();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            evt.repondre("ERROR!");
        }
            evt.repondre(String.format("The appropriate roles were added to your profile. Admins will handle the rest.\n" +
                            "<@&890054370689945640>\n" +
                            "**New Free Agent:**\n" +
                            "**Discord:** %s\n" +
                            "**Bungie:** %s",
                    evt.reqMember().reqNick(),
                    access.reqBungieNameFromDiscordID(evt.reqIdAuteur())));
    }
}

class regTeam extends Commande {
    public regTeam(Bot bot) {
        super(bot);
        ajouterNom("reg team");
        setDescription("Register as a team.");
        setUtilisation("j/reg team");
        setExemple("j/reg team @sophie @xplux The Glorious CCP");

        ajouterArg(new ArgumentMetaData(
                ArgumentType.USER,
                false,
                "teammate_1",
                "Your first teammate"
        ));

        ajouterArg(new ArgumentMetaData(
                ArgumentType.USER,
                false,
                "teammate_2",
                "Your second teammate"
        ));

        ajouterArg(new ArgumentMetaData(
                ArgumentType.STRING,
                false,
                "team_name",
                "Your team name."
        ));

        //makeSlashSherpaRun();
    }

    @Override
    public void traitement(MessageEventAdapter evt, CommandThread cmdThread) throws InterruptedException, MasterException {
        var args = creerArguments(evt);

        List<MemberAdapter> l_m = new ArrayList<>();

        l_m.add(evt.reqMember());
        l_m.add(evt.reqGuild().reqMember(args.get(0).toUser()));
        l_m.add(evt.reqGuild().reqMember(args.get(1).toUser()));

        String team_name = args.get(2).toStr();

        AccessMysql access;

        try {
            access = reqBot().reqAccessMysql();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            evt.repondre("Error!");
            return;
        }

        StringBuilder sb = new StringBuilder("The appropriate roles were added to you and your teammates. Admins will take care of the rest\n<@&890054370689945640>\n" +
                "__**New Team:**__ " + team_name + "\n"
                );
        for (MemberAdapter mem: l_m) {
            mem.ajouterRole(888914890352394280L);
            sb.append(format(mem, access));
        }

        evt.repondre(sb.toString());
    }

    public String format(MemberAdapter mem, AccessMysql accessMysql) {
        return String.format("**Discord:** %s\n" +
                "**Bungie:** %s\n" +
                "\n",
                mem.reqNick(),
                accessMysql.reqBungieNameFromDiscordID(mem.reqUser().reqId()));
    }
}