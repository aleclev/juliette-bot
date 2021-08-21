import Interface.InterfaceJDA;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;

public class JulietteJDA {
    /**
     * @brief Fonction main. Définit le bot et les listeners.
     * @param args
     * @throws Exception
     */
    public static void main(String args[]) throws Exception {
        //java -cp "mariadb-java-client-2.7.2.jar;JulietteBot.jar;" Juliette
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader("./config.json"));
        JSONObject config = (JSONObject) obj;

        System.out.print((String)config.get("nom")+"\n");

        JDA jda = JDABuilder.createDefault((String) config.get("jetton"))
                .setChunkingFilter(ChunkingFilter.ALL) // enable member chunking for all guilds
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .enableIntents(GatewayIntent.GUILD_PRESENCES)
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .enableCache(CacheFlag.CLIENT_STATUS)
                .setRawEventsEnabled(true)
                .build();
        jda.awaitReady();

        System.out.print("JDA prêt!\n");

        jda.addEventListener(new InterfaceJDA(config, jda));
    }
}