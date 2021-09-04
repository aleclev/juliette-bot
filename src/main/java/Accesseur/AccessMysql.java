package Accesseur;

import org.json.simple.JSONObject;

import java.sql.*;
import java.util.ArrayList;
//TODO : ajouter enregSteam et enregDiscord
/**
 * classe pour envoyer des requêtes à mariannedata et à l'api de bungie.
 */
public class AccessMysql {
    //Connection connection;
    String hote;
    String nomUtil;
    String motPasse;
    String nomBD;
    Connection m_connection;
    AccessBungie accessBungie;

    public AccessMysql(JSONObject config) throws SQLException {
            hote = (String) config.get("bd_nom_hote");
            nomUtil = (String) config.get("bd_nom_utilisateur");
            motPasse = (String) config.get("bd_mot_passe");
            nomBD = (String) config.get("bd_nom");
            m_connection = DriverManager.getConnection(String.format("jdbc:mariadb://%s:3306/%s", hote, nomBD), nomUtil, motPasse);
            accessBungie = new AccessBungieMaison((String)config.get("cleDestiny"));
    }

    /**
     * retourne vrai si l'utilisateur a son discord_id enregistré.
     * @param discord_id
     * @return
     * @throws SQLException
     */
    public boolean estEnregDiscord(long discord_id) throws SQLException {
        Connection connection = reqConnection();
        String res = "SELECT EXISTS(SELECT * FROM utilisateur WHERE discord_id= ?);";
        PreparedStatement stmnt = connection.prepareStatement(res);
        stmnt.setLong(1, discord_id);
        ResultSet rs = stmnt.executeQuery();
        rs.next();

        return rs.getBoolean(1);
    }

    public boolean estEnregSteam(long discord_id) throws SQLException {
        Connection connection = reqConnection();
        String res = "SELECT EXISTS(SELECT * FROM utilisateur WHERE discord_id= ? AND steam_id IS NOT NULL);";
        PreparedStatement stmnt = connection.prepareStatement(res);
        stmnt.setLong(1, discord_id);
        ResultSet rs = stmnt.executeQuery();
        rs.next();

        return rs.getBoolean(1);
    }

    /**
     *
     * @param idDiscord
     * @return l'id steam de la personne si le profile existe.
     * @throws SQLException
     */
    public Long reqIdSteam(Long idDiscord) throws SQLException {
            Connection connection = reqConnection();
            String res = "SELECT steam_id FROM utilisateur WHERE discord_id = ? ;";
            PreparedStatement stmnt = connection.prepareStatement(res);
            stmnt.setLong(1, idDiscord);

            ResultSet rs = stmnt.executeQuery();
            //connection.close();
            rs.next();

            return rs.getLong("steam_id");
    }

    /**
     *
     * @param discord_id
     * @param tag_nom
     * @return le texte du tag identifié par discord_id/tag_nom s'il existe
     * @throws SQLException
     */
    public String reqTag(long discord_id, String tag_nom) throws SQLException {
        Connection connection = reqConnection();
        String res = "SELECT * FROM tag WHERE tag_nom = ? AND utilisateur_discord_id = ?;";
        PreparedStatement stmnt = connection.prepareStatement(res);
        stmnt.setString(1, tag_nom);
        stmnt.setLong(2, discord_id);

        ResultSet rs = stmnt.executeQuery();

        //connection.close();

        rs.next();
        return rs.getString("tag_text");
    }

    public ArrayList<String> reqTousTag(long discord_id) throws SQLException {
        Connection connection = reqConnection();
        String res = "SELECT tag_nom FROM tag WHERE utilisateur_discord_id = ?;";
        PreparedStatement stmnt = connection.prepareStatement(res);
        stmnt.setLong(1, discord_id);

        ResultSet rs = stmnt.executeQuery();

        ArrayList<String> r = new ArrayList<>();

        while (rs.next()) {
            r.add(rs.getString(1));
        }
        return r;
    }

    /**
     *
     * @param discord_id
     * @param tag_nom
     * @return true s'il existe un tag identifié par discord_id/tag_nom
     * @throws SQLException
     */
    public boolean checkTag(long discord_id, String tag_nom) throws SQLException {
        Connection connection = reqConnection();
        String res = "SELECT EXISTS(SELECT * FROM tag WHERE utilisateur_discord_id= ? AND tag_nom= ?);";
        PreparedStatement stmnt = connection.prepareStatement(res);
        stmnt.setLong(1, discord_id);
        stmnt.setString(2, tag_nom);

        ResultSet rs = stmnt.executeQuery();
        //connection.close();

        rs.next();
        return rs.getBoolean(1);
    }

    /**
     *
     * @param discord_id
     * @return true s'il existe un profil avec discord_id.
     * @throws SQLException
     */
    public boolean checkUtilisateurDiscord(long discord_id) throws SQLException {
        Connection connection = reqConnection();
        String res = "SELECT EXISTS(SELECT * FROM utilisateur WHERE discord_id= ?);";
        PreparedStatement stmnt = connection.prepareStatement(res);
        stmnt.setLong(1, discord_id);
        ResultSet rs = stmnt.executeQuery();
        //connection.close();

        rs.next();
        return rs.getBoolean(1);
    }

    /**
     * écrit le nouveau tuple dans la table tag. peut écraser un tuple avec le même identifiant.
     * @param discord_id
     * @param tag_nom
     * @param tag_text
     * @throws SQLException
     */
    public void setTag(long discord_id, String tag_nom, String tag_text) throws SQLException {
        Connection connection = reqConnection();
        String res = "REPLACE INTO tag VALUES (?, ?, ?);";
        PreparedStatement stmnt = connection.prepareStatement(res);
        stmnt.setLong(1, discord_id);
        stmnt.setString(2, tag_nom);
        stmnt.setString(3, tag_text);
        stmnt.executeQuery();
        //connection.close();
    }

    /**
     *
     * @param discord_id
     * @param tag_nom
     * @throws SQLException
     */
    public void deleteTag(long discord_id, String tag_nom) throws SQLException {
        Connection connection = reqConnection();
        String res = "DELETE FROM tag WHERE tag_nom= ? AND utilisateur_discord_id=?;";
        PreparedStatement stmnt = connection.prepareStatement(res);
        stmnt.setString(1, tag_nom);
        stmnt.setLong(2, discord_id);
        stmnt.executeQuery();
        //connection.close();
    }

    public ResultSet reqAbonnesNotifTags(ArrayList<String> l_tag) throws SQLException {
        Connection connection = reqConnection();
        String res = "SELECT DISTINCT abnt.utilisateur_discord_id FROM abonnement_notiftag abnt, notif_dnd dnd WHERE abnt.utilisateur_discord_id = dnd.utilisateur_discord_id AND dnd.temps < ? AND (";

        //Déclaré ainsi pour éviter qu'un tag soît recherché deux fois
        int max = l_tag.size();

        //Construit l'arbe de tous les
        for (int i = 0; i < max; i++) {
            String tag = l_tag.get(i);
            reqTagsParents(tag, l_tag);
        }

        //formate la requète
        for (int i = 0; i < l_tag.size()-1; i++) {
            res += " nomTag= ? OR";
        }
        res += " nomtag= ? );";

        PreparedStatement stmnt = connection.prepareStatement(res);

        stmnt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
        for (int i = 0; i < l_tag.size(); i++) {
            stmnt.setString(i+2, l_tag.get(i));
        }

        ResultSet rs = stmnt.executeQuery();
        //connection.close();
        return rs;
    }

    /**
     * Fonction récusive qui ajoute tous les tags parents de tag à la liste l_tags
     * Limitation courante : un tag_enfant ne peut avoir qu'un seul parent.
     * @param tag
     * @param l_tags
     * @throws SQLException
     */
    private void reqTagsParents(String tag, ArrayList<String> l_tags) throws SQLException {
        Connection connection = reqConnection();
        String res = "SELECT tag_parent FROM arbre_tag WHERE tag_enfant = ?;";
        PreparedStatement stmnt = connection.prepareStatement(res);
        stmnt.setString(1, tag);
        ResultSet rs = stmnt.executeQuery();

        if (rs.next()) {
            String newtag = rs.getString(1);
            if (!l_tags.contains(newtag)) {
                l_tags.add(newtag);
            }
            System.out.printf("%s\n", newtag);
            //appel récursif
            reqTagsParents(newtag, l_tags);
        } else {
            //condition d'arrêt
            return;
        }
    }

    /**
     * abonne discord_id à tag.
     * @param discord_id
     * @param tag
     * @throws SQLException
     */
    public void ajouterNotifTag(long discord_id, String tag) throws SQLException {
        Connection connection = reqConnection();
        String res = "INSERT INTO abonnement_notiftag VALUES (?, ?);";
        PreparedStatement stmnt = connection.prepareStatement(res);
        stmnt.setLong(1, discord_id);
        stmnt.setString(2, tag);
        stmnt.executeQuery();
    }

    /**
     * désabonne discord_id de tag.
     * @param discord_id
     * @param tag
     * @throws SQLException
     */
    public void enleverNotifTag(long discord_id, String tag) throws SQLException {
        Connection connection = reqConnection();
        String res = "DELETE FROM abonnement_notiftag WHERE utilisateur_discord_id = ? AND nomTag = ?;";
        PreparedStatement stmnt = connection.prepareStatement(res);
        stmnt.setLong(1, discord_id);
        stmnt.setString(2, tag);
        stmnt.executeQuery();
    }

    public ResultSet reqTousNotifTags(long discord_id) throws SQLException {
        Connection connection = reqConnection();
        String res = "SELECT nomTag FROM abonnement_notiftag WHERE utilisateur_discord_id = ?;";
        PreparedStatement stmnt = connection.prepareStatement(res);
        stmnt.setLong(1, discord_id);
        return stmnt.executeQuery();
    }

    public void enleverTousNotifTags(long discord_id) throws SQLException {
        Connection connection = reqConnection();
        String res = "DELETE FROM abonnement_notiftag WHERE utilisateur_discord_id = ?;";
        PreparedStatement stmnt = connection.prepareStatement(res);
        stmnt.setLong(1, discord_id);
        stmnt.executeQuery();
    }

    /**
     * retourne vrai si l'utilisateur id_utilisateur bloque id_bloque
     * @param id_utilisateur
     * @param id_bloque
     * @return
     * @throws SQLException
     */
    public boolean notifBlocageExiste(long id_utilisateur, long id_bloque) throws SQLException {
        Connection connection = reqConnection();
        String res = "SELECT EXISTS(SELECT utilisateur_bloque_discord_id FROM blocage_notiftag WHERE utilisateur_discord_id= ? AND utilisateur_bloque_discord_id= ?);";
        PreparedStatement stmnt = connection.prepareStatement(res);
        stmnt.setLong(1, id_utilisateur);
        stmnt.setLong(2, id_bloque);
        ResultSet rs = stmnt.executeQuery();
        //connection.close();

        rs.next();
        return rs.getBoolean(1);
    }

    /**
     * ajoue un blocage. discord_id bloque id_bloque
     * @param discord_id
     * @param id_bloque
     * @throws SQLException
     */
    public void ajouterBlocage(long discord_id, long id_bloque) throws SQLException {
        Connection connection = reqConnection();
        String res = "INSERT INTO blocage_notiftag VALUES (?, ?);";
        PreparedStatement stmnt = connection.prepareStatement(res);
        stmnt.setLong(1, discord_id);
        stmnt.setLong(2, id_bloque);
        stmnt.executeQuery();
    }

    /**
     * enlève un blocage mis auparavant.
     * @param discord_id
     * @param id_bloque
     * @throws SQLException
     */
    public void enleverBlocage(long discord_id, long id_bloque) throws SQLException {
        Connection connection = reqConnection();
        String res = "DELETE FROM blocage_notiftag WHERE utilisateur_discord_id = ? AND utilisateur_bloque_discord_id = ?;";
        PreparedStatement stmnt = connection.prepareStatement(res);
        stmnt.setLong(1, discord_id);
        stmnt.setLong(2, id_bloque);
        stmnt.executeQuery();
    }

    public void enleverTousBlocage(long discord_id) throws SQLException {
        Connection connection = reqConnection();
        String res = "DELETE FROM blocage_notiftag WHERE utilisateur_discord_id = ?;";
        PreparedStatement stmnt = connection.prepareStatement(res);
        stmnt.setLong(1, discord_id);
        stmnt.executeQuery();
    }

    public ResultSet reqBlacklist(long discord_id) throws SQLException {
        Connection connection = reqConnection();
        String res = "SELECT utilisateur_bloque_discord_id FROM blocage_notiftag WHERE utilisateur_discord_id = ? ;";
        PreparedStatement stmnt = connection.prepareStatement(res);
        stmnt.setLong(1, discord_id);
        return stmnt.executeQuery();
    }

    /**
     * met à jour le temps limite de bloquage de notifications pour l'utilisateur
     * @param discord_id
     * @param ts
     * @throws SQLException
     */
    public void updateDND(long discord_id, Timestamp ts) throws SQLException {
        Connection connection = reqConnection();
        String res = "INSERT INTO notif_dnd values(?, ?) ON DUPLICATE KEY UPDATE temps = ? ;";
        PreparedStatement stmnt = connection.prepareStatement(res);
        stmnt.setLong(1, discord_id);
        stmnt.setTimestamp(2, ts);
        stmnt.setTimestamp(3, ts);
        stmnt.executeQuery();
    }

    /**
     * initialise un champ dans notif_dnd pour chaque utilisateur
     * @throws SQLException
     */
    public void initDnd() throws SQLException {
        Connection connection = reqConnection();
        String res = "SELECT discord_id FROM utilisateur;";
        PreparedStatement stmnt = connection.prepareStatement(res);
        ResultSet rs = stmnt.executeQuery();
        res = "INSERT IGNORE INTO notif_dnd VALUES (?, 0);";

        while (rs.next()) {
            stmnt = connection.prepareStatement(res);
            stmnt.setLong(1, rs.getLong(1));
            stmnt.executeQuery();
        }
    }

    /**
     * retourne tous les champs dans c
     * @return
     */
    public ResultSet reqTousArbeNotif() throws SQLException {
        Connection connection = reqConnection();
        String res = "SELECT * FROM arbre_tag;";
        PreparedStatement stmnt = connection.prepareStatement(res);
        return stmnt.executeQuery();
    }

    /**
     * Retourne le bungie name à partir du discord_id fourni.
     * @param discord_id
     * @return
     */
    public String reqBungieNameFromDiscordID(long discord_id) {
        try {
            long sid = reqIdSteam(discord_id);
            return accessBungie.reqBungieNameFromSteamID(sid);
        } catch (SQLException throwables) {
            return "Could not fetch Bungie Name";
        }
    }

    /**
     *
     * @return une connection mysql utilisable.
     * @throws SQLException
     */
    private Connection reqConnection() throws SQLException {
        //return DriverManager.getConnection(String.format("jdbc:mariadb://%s:3306/%s", hote, nomBD), nomUtil, motPasse);
        return m_connection;
    }

    public void eteindre() {
        try {
            m_connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


}