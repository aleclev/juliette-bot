package Fonctions.VerificationMessage;

import Adapteurs.MessageEventAdapter;

public abstract class VerificateurAbstrait {
    public VerificateurAbstrait() {}

    public VerificateurAbstrait(MessageEventAdapter p_evt) {
        evt = p_evt;
    }

    /**
     * verifie une condition quelconque entre p_evt et evt.
     * @param p_evt
     * @return
     */
    public abstract boolean verifie(MessageEventAdapter p_evt);
    public MessageEventAdapter reqEvent() {
        return evt;
    }

    private MessageEventAdapter evt;
}
