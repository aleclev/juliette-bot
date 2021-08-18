/**
 * This class is an event listener. Events are sent to Controleur
 */

package Interface;

import Adapteurs.MessageEventAdapterJDA;
import Adapteurs.MetaAdapterJDA;
import Controleurs.Controleur;
import de.cerus.jdasc.command.ApplicationCommandListener;
import net.dv8tion.jda.api.JDA;
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
        controleur = new Controleur(config, new MetaAdapterJDA(jda));
    }


    @Override
    public void onMessageReceived(MessageReceivedEvent evt) {
        controleur.gestionnaireMessage(new MessageEventAdapterJDA(evt));
        //System.out.print(evt.getMessage().getContentRaw()+"\n");
    }

    private Controleur controleur;
}
