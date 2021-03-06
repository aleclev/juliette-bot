/**
 * This class is an event listener. Events are sent to Controleur
 */

package Interface;

import Adapteurs.MessageEventAdapterJDA;
import Adapteurs.MetaAdapterJDA;
import Adapteurs.NormalMessageEventAdapterJDA;
import Adapteurs.SlashMessageEventAdapterJDA;
import Controleurs.Controleur;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.json.simple.JSONObject;

import java.sql.SQLException;

public class InterfaceJDA extends ListenerAdapter {

    /**
     * Propage la config vers la couche du bas
     * @param config
     */
    public InterfaceJDA(JSONObject config, JDA jda) throws SQLException {
        controleur = new Controleur(config, MetaAdapterJDA.construireMetaAdapter(jda));
    }


    @Override
    public void onMessageReceived(MessageReceivedEvent evt) {
        controleur.gestionnaireMessage(new NormalMessageEventAdapterJDA(evt));
    }

    @Override
    public void onSlashCommand(SlashCommandEvent evt) {
        System.out.print("Commande slash reçue !");
        controleur.gestionnaireMessage(new SlashMessageEventAdapterJDA(evt));
    }

    private Controleur controleur;
}
