package Discord;

import java.util.ArrayList;
import java.util.List;

public abstract class Module {
    /**
     * Constructeur par défaut
     */
    public Module() {}

    /**
     *
     * @param p_bot
     */
    public Module(Bot p_bot) {
        bot = p_bot;
        //bot.ajouterModule(this);
        nom = "Unnamed module";

        l_commandes = new ArrayList<>();
    }

    /**
     *
     * @param commande
     */
    public void ajouterCommande(Commande commande) {
        l_commandes.add(commande);
    }

    public String reqNom () {
        return nom;
    }

    public void setNom(String nouv) {
        nom = nouv;
    }

    public List<Commande> reqCommandes() {
        return l_commandes;
    }

    List<Commande> l_commandes;
    Bot bot;
    String nom;
}
