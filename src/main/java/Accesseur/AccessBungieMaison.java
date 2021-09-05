package Accesseur;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AccessBungieMaison extends AccessBungie {
    public AccessBungieMaison(String p_cleApi) {
        cleApi = p_cleApi;
    }

    /**
     *
     * @param steam_id Le steam id du compte recherché.
     * @return BungieName
     */
    @Override
    public String reqBungieNameFromSteamID(long steam_id) {
        try {
            String apiKey = "ddc2f6cc7bc842a1a2e55a207dc1e1cb";

            String url = String.format("https://www.bungie.net/platform/User/GetMembershipFromHardLinkedCredential/%s/%s/", "12", steam_id);

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("GET");

            // Set header
            con.setRequestProperty("X-API-KEY", apiKey);

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            String response = "";

            while ((inputLine = in.readLine()) != null) {
                response += inputLine;
            }

            in.close();

            // Uses Gson - https://github.com/google/gson
            JsonParser parser = new JsonParser();
            JsonObject json = (JsonObject) parser.parse(response);

            String memid = json.getAsJsonObject("Response").getAsJsonPrimitive("membershipId").getAsString();

            ///////////////////
            // Endpoint for Gjallarhorn
            url = String.format("https://www.bungie.net/platform/User/GetMembershipsById/%s/%s/", memid, "3");

            obj = new URL(url);
            con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("GET");

            // Set header
            con.setRequestProperty("X-API-KEY", apiKey);

            in = new BufferedReader(new InputStreamReader(con.getInputStream()));

            response = "";

            while ((inputLine = in.readLine()) != null) {
                response += inputLine;
            }

            in.close();

            json = (JsonObject) parser.parse(response);

            String name = json.getAsJsonObject("Response").getAsJsonObject("bungieNetUser").getAsJsonPrimitive("displayName").getAsString();
            String code = json.getAsJsonObject("Response").getAsJsonObject("bungieNetUser").getAsJsonPrimitive("cachedBungieGlobalDisplayNameCode").getAsString();

            if (code.length() < 4) {
                code = "0" + code;
            }

            return String.format("%s#%s", name, code);
        }
        catch (IOException e) {
            return "COULD NOT FETCH NAME";
        }
    }

    private final String cleApi;
}
