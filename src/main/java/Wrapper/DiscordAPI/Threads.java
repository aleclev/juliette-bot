package Wrapper.DiscordAPI;

import Wrapper.DiscordAPI.RequestBody.Threads.CreateThreadBody;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Threads {
    public static void createThread(long channelId, long messageId, CreateThreadBody body) throws IOException {
        URL ep = new URL(DiscordWrapper.apiUrl + String.format("%s/messages/%s/threads", channelId, messageId));

        HttpURLConnection con = (HttpURLConnection)DiscordWrapper.getConnection(ep);

        con.setRequestProperty("Content-Type", "application/json");

        //con.
    }
}
