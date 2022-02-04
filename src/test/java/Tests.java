
import Wrapper.DiscordAPI.DiscordWrapper;
import Wrapper.DiscordAPI.RequestBody.Threads.CreateThreadBody;
import Wrapper.DiscordAPI.Threads;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import material.DestinyAPI;
import material.clan.Clan;
import material.user.BungieUser;
import okhttp3.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.*;

public class Tests {

    @Test
    public void CreateThreadTest() throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader("./config.json"));
        JSONObject config = (JSONObject) obj;
//
//        DiscordWrapper.configure(config);
//        CreateThreadBody body = new CreateThreadBody();
//        body.name = "some thread";
//        Threads.createThread(442450151949467649L, 938800473606082600L, body);


//        OkHttpClient client = new OkHttpClient().newBuilder()
//                .build();
//        MediaType mediaType = MediaType.parse("application/json");
//        RequestBody body = RequestBody.create(mediaType, "{\r\n    \"name\": \"some_thread\"\r\n}");
//        Request request = new Request.Builder()
//                .url("https://discord.com/api/channels/614575166487396552/messages/938975247930122321/threads")
//                .method("POST", body)
//                .addHeader("Authorization", "Bot NzUwNzc5NTA3OTYyNjc1MjQw.X0_f5w.e6zpRtGPKY8hQpbDpkC04pJXoGI")
//                .addHeader("Content-Type", "application/json")
//                //.addHeader("Cookie", "__dcfduid=cb1160cb4fd811ecb97a42010a0a0e05; __sdcfduid=cb1160cb4fd811ecb97a42010a0a0e055b46474273883dbd42ac97611e8c3ae3ba0c0c705dfcdd6a9a75a44b0f50ce1a")
//                .build();
//        Response response = client.newCall(request).execute();
//
//        System.out.println(response);

        DiscordWrapper.configure(config);
        CreateThreadBody body = new CreateThreadBody();
        body.name = "some thread";
        Threads.createThread(442450151949467649L, 939016750454960148L, body);
    }

//    @Test
//    public void BungieMembershipID() throws IOException {
//
//        String apiKey = "ddc2f6cc7bc842a1a2e55a207dc1e1cb";
//
//        // Endpoint for Gjallarhorn
//        String url = String.format("https://www.bungie.net/platform/User/GetMembershipFromHardLinkedCredential/%s/%s/","12", "76561198177596658");
//
//        URL obj = new URL(url);
//        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
//
//        con.setRequestMethod("GET");
//
//        // Set header
//        con.setRequestProperty("X-API-KEY", apiKey);
//
//        int responseCode = con.getResponseCode();
//        System.out.println("\nSending 'GET' request to Bungie.Net : " + url);
//        System.out.println("Response Code : " + responseCode);
//
//        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
//        String inputLine;
//        String response = "";
//
//        while ((inputLine = in.readLine()) != null) {
//            response += inputLine;
//        }
//
//        in.close();
//
//        // Uses Gson - https://github.com/google/gson
//        JsonParser parser = new JsonParser();
//        JsonObject json = (JsonObject) parser.parse(response);
//
//        System.out.println();
//        System.out.println(json.getAsJsonObject("Response").getAsJsonPrimitive("membershipId").getAsString());//.getAsJsonObject("membershipId").getAsString());
//    }
//
//    @Test
//    public void MemIDToBungieName() throws IOException {
//        String apiKey = "ddc2f6cc7bc842a1a2e55a207dc1e1cb";
//
//        // Endpoint for Gjallarhorn
//        String url = String.format("https://www.bungie.net/platform/User/GetBungieNetUserById/%s/", "5225201");
//
//        URL obj = new URL(url);
//        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
//
//        con.setRequestMethod("GET");
//
//        // Set header
//        con.setRequestProperty("X-API-KEY", apiKey);
//
//        int responseCode = con.getResponseCode();
//        System.out.println("\nSending 'GET' request to Bungie.Net : " + url);
//        System.out.println("Response Code : " + responseCode);
//
//        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
//        String inputLine;
//        String response = "";
//
//        while ((inputLine = in.readLine()) != null) {
//            response += inputLine;
//        }
//
//        in.close();
//
//        // Uses Gson - https://github.com/google/gson
//        JsonParser parser = new JsonParser();
//        JsonObject json = (JsonObject) parser.parse(response);
//
//        System.out.println();
//        System.out.println(json);
//    }
//
//    @Test
//    public void DestinyMemToBungieMem() throws IOException {
//        String apiKey = "ddc2f6cc7bc842a1a2e55a207dc1e1cb";
//
//        // Endpoint for Gjallarhorn
//        String url = String.format("https://www.bungie.net/platform/User/GetMembershipsById/%s/%s/", "4611686018471011052", "3");
//
//        URL obj = new URL(url);
//        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
//
//        con.setRequestMethod("GET");
//
//        // Set header
//        con.setRequestProperty("X-API-KEY", apiKey);
//
//        int responseCode = con.getResponseCode();
//        System.out.println("\nSending 'GET' request to Bungie.Net : " + url);
//        System.out.println("Response Code : " + responseCode);
//
//        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
//        String inputLine;
//        String response = "";
//
//        while ((inputLine = in.readLine()) != null) {
//            response += inputLine;
//        }
//
//        in.close();
//
//        // Uses Gson - https://github.com/google/gson
//        JsonParser parser = new JsonParser();
//        JsonObject json = (JsonObject) parser.parse(response);
//
//        System.out.println();
//        System.out.println(json.getAsJsonObject("Response").getAsJsonObject("bungieNetUser"));
//    }
//
//    @Test
//    public void SteamToBungieName() throws IOException {
//        String apiKey = "ddc2f6cc7bc842a1a2e55a207dc1e1cb";
//
//        // Endpoint for Gjallarhorn
//        String url = String.format("https://www.bungie.net/platform/User/GetMembershipFromHardLinkedCredential/%s/%s/","12", "76561198177596658");
//
//        URL obj = new URL(url);
//        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
//
//        con.setRequestMethod("GET");
//
//        // Set header
//        con.setRequestProperty("X-API-KEY", apiKey);
//
//        int responseCode = con.getResponseCode();
//        System.out.println("\nSending 'GET' request to Bungie.Net : " + url);
//        System.out.println("Response Code : " + responseCode);
//
//        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
//        String inputLine;
//        String response = "";
//
//        while ((inputLine = in.readLine()) != null) {
//            response += inputLine;
//        }
//
//        in.close();
//
//        // Uses Gson - https://github.com/google/gson
//        JsonParser parser = new JsonParser();
//        JsonObject json = (JsonObject) parser.parse(response);
//
//        //System.out.println();
//        //System.out.println(json.getAsJsonObject("Response").getAsJsonPrimitive("membershipId").getAsString());//.getAsJsonObject("membershipId").getAsString());
//
//        String memid = json.getAsJsonObject("Response").getAsJsonPrimitive("membershipId").getAsString();
//
//        ///////////////////
//        // Endpoint for Gjallarhorn
//        url = String.format("https://www.bungie.net/platform/User/GetMembershipsById/%s/%s/", "4611686018471011052", "3");
//
//        obj = new URL(url);
//        con = (HttpURLConnection) obj.openConnection();
//
//        con.setRequestMethod("GET");
//
//        // Set header
//        con.setRequestProperty("X-API-KEY", apiKey);
//
//        in = new BufferedReader(new InputStreamReader(con.getInputStream()));
//
//        response = "";
//
//        while ((inputLine = in.readLine()) != null) {
//            response += inputLine;
//        }
//
//        in.close();
//
//        json = (JsonObject) parser.parse(response);
//
//        //System.out.println();
//        //System.out.println(json.getAsJsonObject("Response").getAsJsonObject("bungieNetUser").getAsJsonPrimitive("displayName").getAsString());
//
//        String name = json.getAsJsonObject("Response").getAsJsonObject("bungieNetUser").getAsJsonPrimitive("displayName").getAsString();
//        String code = json.getAsJsonObject("Response").getAsJsonObject("bungieNetUser").getAsJsonPrimitive("cachedBungieGlobalDisplayNameCode").getAsString();
//
//        if (code.length() < 4) {
//            code = "0" + code;
//        }
//
//        String complet = String.format("%s#%s", name, code);
//
//        Assert.assertEquals(complet, "clevQC#0132");
//    }
}
