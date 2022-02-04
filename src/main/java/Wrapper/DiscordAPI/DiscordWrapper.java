package Wrapper.DiscordAPI;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class DiscordWrapper {
    public static URL apiUrl;
    private static String apiKey;

//    private DiscordWrapper() {
//        try {
//            apiUrl = new URL("https://discord.com/api/");
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//    }

//    public static HttpURLConnection getConnectionPost(URL endpoint, String body) throws IOException {
////        HttpURLConnection con = (HttpURLConnection) endpoint.openConnection();
////
////        con.setRequestMethod("POST");
////        con.setRequestProperty("Authorization", String.format("Bot %s", apiKey));
////        con.setRequestProperty("Content-Type", "application/json");
////        con.setRequestProperty("Accept", "application/json");
////        con.setDoOutput(true);
////
////        try(OutputStream os = con.getOutputStream()) {
////            byte[] input = body.getBytes(StandardCharsets.UTF_8);
////            os.write(input, 0, input.length);
////        }
////
////        return con;
//    }

    private static Request.Builder getRequestBuilder() {
        //OkHttpClient client = new OkHttpClient().newBuilder().build();
        return new Request.Builder()
                .addHeader("Authorization", "Bot " + apiKey);
    }

    public static Request getPostJsonRequest(String url, String stringBody) {
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, stringBody);

        return getRequestBuilder()
                .url(url)
                .addHeader("Content-Type", "application/json")
                .method("POST", body)
                .build();
    }

    public static OkHttpClient getClient() {
        return new OkHttpClient().newBuilder().build();
    }

    public static void configure(JSONObject config) throws MalformedURLException {
        apiKey = (String)config.get("jetton");
        apiUrl = new URL("https://discord.com/api/");
    }
}
