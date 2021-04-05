/**
 * This class redirects events from the Interface layer to the Logic layer.
 */

package Controleurs;

import Adapteurs.MessageEventAdapter;
import Adapteurs.MetaAdapter;
import Discord.Bot;
import org.json.simple.JSONObject;

public class Controleur {
    public Controleur(JSONObject config, MetaAdapter p_metaAdapter) {
        bot = new Bot(config, p_metaAdapter);
    }


    public void gestionnaireMessage(MessageEventAdapter evt) {
        bot.traitementMessage(evt);
    }

    private Bot bot;
}
