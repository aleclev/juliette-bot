package Discord;

import CommandPermission.CommandPermissionGate;
import CommandPermission.FreeAccessGate;
import net.dv8tion.jda.api.interactions.commands.Command;

import java.security.Signature;
import java.util.ArrayList;

public class CommandMetadata {
    private ArrayList<String> l_noms;
    private String description;
    private String utilisation;
    private CommandSignature signature;
    private String exemple;
    private CommandPermissionGate gate = new FreeAccessGate();

    public CommandMetadata(ArrayList<String> p_l_noms, String p_description, CommandSignature p_signature) {
        l_noms = p_l_noms;
        description = p_description;
        signature = p_signature;
        utilisation = "";
    }

    /**
     * Constructeur par défaut.
     */
    public CommandMetadata() {
        l_noms = new ArrayList<>();
        description = "No description";
        signature = new CommandSignature();
        utilisation = "";
    }

    public ArrayList<String> reqNoms() {
        return l_noms;
    }
    public String reqDescription() {
        return description;
    }
    public CommandSignature reqSignature() {
        return signature;
    }
    public String reqUtilisation() {
        return utilisation;
    }
    public String reqExemple() {
        return exemple;
    }
    public CommandPermissionGate reqGate() {
        return gate;
    }

    public void setGate(CommandPermissionGate p_gate) {
        gate = p_gate;
    }

    public void setNoms(ArrayList<String> p_l_noms) {
        l_noms = p_l_noms;
    }
    public void ajouterNom(String nom) {
        l_noms.add(nom);
    }
    public void setDescription(String p_desc) {
        description = p_desc;
    }
    public void setSignature(CommandSignature p_sig) {
        signature = p_sig;
    }
    public void setUtilisation(String p_util) {
        utilisation = p_util;
    }
    public void setExemple(String p_ex) {
        exemple = p_ex;
    }
}
