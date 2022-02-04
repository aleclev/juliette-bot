package Wrapper.DiscordAPI;

import Wrapper.DiscordAPI.RequestBody.Threads.CreateThreadBody;
import com.google.gson.Gson;
import okhttp3.Request;
import okhttp3.Response;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Threads {
    public static void createThread(long channelId, long messageId, CreateThreadBody body) throws IOException {
        //URL ep = new URL(DiscordWrapper.apiUrl + String.format("%s/messages/%s/threads", channelId, messageId));

        var stringBody = new Gson().toJson(body);

        String url = DiscordWrapper.apiUrl + String.format("/channels/%s/messages/%s/threads", channelId, messageId);
        Request rq = DiscordWrapper.getPostJsonRequest(url, stringBody);
        //HttpURLConnection con = (HttpURLConnection)DiscordWrapper.getConnectionPost(ep, stringBody);

        Response rs = DiscordWrapper.getClient().newCall(rq).execute();

        System.out.println(rs);
    }
}
