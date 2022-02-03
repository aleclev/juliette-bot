
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import material.DestinyAPI;
import material.clan.Clan;
import material.user.BungieUser;
import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.*;

public class Tests {

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
