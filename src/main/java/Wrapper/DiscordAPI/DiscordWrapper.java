package Wrapper.DiscordAPI;

import org.json.simple.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DiscordWrapper {
    private static DiscordWrapper instance = null;
    public static URL apiUrl;
    private static String apiKey;

    private DiscordWrapper() {
        try {
            apiUrl = new URL("https://discord.com/api/");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public static HttpURLConnection getConnection(URL endpoint) throws IOException {
        HttpURLConnection con = (HttpURLConnection) endpoint.openConnection();

        con.setRequestProperty("Authorization", String.format("Bot %s", apiKey));

        return con;
    }

    public static void configure(JSONObject config) {
        apiKey = (String)config.get("jetton");
    }
}
