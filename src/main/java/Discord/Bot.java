package Discord;

import Accesseur.AccessMysql;
import Adapteurs.*;
import Commandes.*;
import Fonctions.CommandParse;
import org.json.simple.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Bot {
    private final List<NotifTagCooldownDTO> l_notifCooldown;
    private final List<Module> l_modules;
    private final String prefix;
    private final String notifPrefix;

    private final int maxCmdThread;

    private final List<CommandThread> l_cmdThread;
    //private final CommandThread[] l_cmdThread;
    private final Lock cmdThreadListLock;

    //private final AccessMysql accessMysql;
    private final MetaAdapter metaAdapter;

    private final JSONObject config;

    public Bot(JSONObject p_config, MetaAdapter p_metaAdapter) {
        config = p_config;

        prefix = (String)config.get("prefix");
        notifPrefix = (String)config.get("notifTag_prefix");

        //Liste des threads de commandes en cours d'exécution.
        l_cmdThread = new ArrayList<>();
        l_notifCooldown = new ArrayList<>();
        maxCmdThread = ((Long)config.get("max_cmdThreads")).intValue();

        for (int i = 0; i < maxCmdThread; i++) {
            l_cmdThread.add(null);
        }
        assert(l_cmdThread.size() == maxCmdThread);

        //MetaAdapter
        metaAdapter = p_metaAdapter;
        cmdThreadListLock = new ReentrantLock();

        //Ajout des modules actifs
        l_modules = new ArrayList<>();
        this.ajouterModule(new Steam(this));
        //this.ajouterModule(new Dev(this));
        this.ajouterModule(new Misc(this));
        this.ajouterModule(new Tag(this));
        this.ajouterModule(new Notif(this));
        this.ajouterModule(new Meta(this));

        System.out.print("Tous les modules de commandes instanciés avec succès...\n");
    }

    public void ajouterModule(Module module) {
        l_modules.add(module);
    }

    /**
     *
     * @param evt
     */
    public void traitementMessage(MessageEventAdapter evt) {
        broadcastOnMessage(evt);

        String rawMsg = evt.reqContenueRaw();
        if (!CommandParse.estRequeteCommande(rawMsg, prefix)) {
            try {
                traitementNotifTag(evt);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                evt.repondre("ERROR! Database connection could not be established. Please report this error if it keeps occuring.");
            }
            return;
        }

        String msgSansPrefix = CommandParse.dePrefixer(rawMsg, prefix);

        for (Module module : l_modules) {
            for (Commande commande : module.l_commandes) {
                if (commande.estAppele(msgSansPrefix)) {
                    CommandThread cmdThread = commande.call(evt);
                    if (!ajouterCmdThread(cmdThread)) {
                        evt.repondre("ERROR! Trafic excess, command was cancelled.");
                    }
                    return;
                }
            }
        }

        evt.repondre("ERROR! Command does not exist.");
    }

    /**
     * envoi l,event vers tous les cmdThreads. (l'event est gèré dans cmdThread)
     * @param evt
     */
    private void broadcastOnMessage(MessageEventAdapter evt) {
        cmdThreadListLock.lock();
        //System.out.printf("Début des appels à onMessage(), l_cmdThread\n";
        for (CommandThread cmd_t : l_cmdThread) {
            if (cmd_t != null) {
                cmd_t.onMessage(evt);
            }
        }
        cmdThreadListLock.unlock();
    }

    /**
     *
     * @param cmdThread
     * @return false si cmdThread n'a pas pu être ajouté exécuté.
     */
    private boolean ajouterCmdThread(CommandThread cmdThread) {
        //cmdThreadListLock.lock();
        for (int i = 0; i < maxCmdThread; i++) {
            if (l_cmdThread.get(i) == null) {
              l_cmdThread.set(i, cmdThread);
              cmdThread.start();
              return true;
            } else if (l_cmdThread.get(i).getState() == Thread.State.TERMINATED) {
                l_cmdThread.set(i, cmdThread);
                cmdThread.start();
                return true;
            }
        }
        return false;
        //cmdThreadListLock.unlock();
    }

    private void traitementNotifTag(MessageEventAdapter evt) throws SQLException {
        ArrayList<String> l_tag = CommandParse.reqNotifTagList(evt.reqContenueRaw(), notifPrefix);

        if (l_tag.size() == 0) {
            return;
        }
        NotifTagCooldownDTO p_nf = null;

        for (NotifTagCooldownDTO nf : l_notifCooldown) {
            if (nf.discord_id == evt.reqIdAuteur()) {
                //System.out.printf("DTO existant trouvé\n");
                p_nf = nf;
            }
        }

        if (p_nf == null) {
            p_nf = new NotifTagCooldownDTO(evt.reqIdAuteur(), 30);
            l_notifCooldown.add(p_nf);
        }

        NotifTagThread n_t = new NotifTagThread(evt, l_tag, p_nf,this);
        n_t.start();
    }

    /**
     * peut être appelé par les commandes ayant besoin d'une connection mysql.
     * @return
     * @throws SQLException
     */
    public AccessMysql reqAccessMysql() throws SQLException {
        return new AccessMysql(config);
    }

    public List<Module> reqModules() {
        return l_modules;
    }

    public MetaAdapter reqMetaAdapter() {
        return metaAdapter;
    }

    public String reqPrefix() {
        return prefix;
    }
}
