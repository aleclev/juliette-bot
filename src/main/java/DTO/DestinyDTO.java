/**
 * DTO to hold some Destiny related stuff.
 */

package DTO;

import org.json.simple.JSONObject;

public class DestinyDTO {
    public final String roadmap_url;

    /**
     * Constructor
     * @param config
     */
    public DestinyDTO(JSONObject config) {
        roadmap_url = (String) config.get("destiny_roadmap_url");
    }
}
