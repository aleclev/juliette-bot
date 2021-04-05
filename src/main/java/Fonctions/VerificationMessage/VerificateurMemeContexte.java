package Fonctions.VerificationMessage;

import Adapteurs.MessageEventAdapter;

public class VerificateurMemeContexte extends VerificateurAbstrait {
    public VerificateurMemeContexte(MessageEventAdapter p_evt) {
        super(p_evt);
    }

    @Override
    public boolean verifie(MessageEventAdapter p_evt) {
        return p_evt.reqIdAuteur().equals(this.reqEvent().reqIdAuteur()) && p_evt.reqIdSalon().equals(this.reqEvent().reqIdSalon());
    }
}
