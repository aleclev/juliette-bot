package Discord;

import Adapteurs.MessageEventAdapter;
import Fonctions.VerificationMessage.VerificateurAbstrait;

import java.util.concurrent.CountDownLatch;

public class CommandThread extends Thread {
    public CommandThread(Commande p_cmd, MessageEventAdapter evt) {
        cmd = p_cmd;
        msgEvt = evt;
        termine = false;
        //this.start();
    }

    /**
     * Pour chaque type d'événement, si l'événement est non-nul, exécute le traitement dans cmd.
     */
    @Override
    public void run() {
        try {
            if (msgEvt != null) {
                cmd.traitement(msgEvt, this);
                return;
            }
            try {
                System.out.printf("Aucun type d'événement valide pour la commande : %s\n", cmd.reqNoms().get(0)); //TODO : vérifier le fonctionnement de ce code
            } catch (Exception e) {
            }

        } catch (InterruptedException e) {
            msgEvt.repondre(MessageEventAdapter.MSGGEN.ERREUR_THREAD);
        }
        termine = true;
    }

    /**
     * Awaits a message input from a discord user.
     *
     * Permet d'attendre la réponse d'un utilisateur quelconque dans Discord.
     * Commence par déclarer un CountdownLatch. Le latch est ouvert soit lorsqu'un
     * Message est vérifié par le verificateur, soit après le timeout.
     * Le timeout est gèré à travers la class CommandThreadTimer.
     * @param timeout nombre de secondes après lequel le latch est automatiquement ouvert.
     * @param p_verif la stratégie de vérification de message shouaitée.
     * @return le premier EventAdapterMessage verifier par le verificateur, ou null après le timeout.
     */
    public MessageEventAdapter attendreReponseMessage(int timeout, VerificateurAbstrait p_verif) {
        System.out.print("En attente d'une réponse...\n");

        msgEvtSec = null;
        msgEvtLatch = new CountDownLatch(1);
        enAttenteMessage = true;
        verif = p_verif;

        CommandThreadTimer cmdThreadTimer = new CommandThreadTimer(timeout, msgEvtLatch);
        try {
            msgEvtLatch.await();
            cmdThreadTimer.interrupt(); //Le timer est interrompu.
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return msgEvtSec;
    }

    /**
     * Cette méthode est appelée par Bot à chaque événement.
     * @param evt
     */
    public void onMessage(MessageEventAdapter evt) {
        //System.out.printf("Appel à onMessage() avec : %s", evt.reqContenueRaw());
        if (enAttenteMessage && verif != null && verif.verifie(evt)) {
            msgEvtSec = evt;
            enAttenteMessage = false;
            msgEvtLatch.countDown();
        }
    }

    //getters
    public boolean estTermine() {
        return termine;
    }

    private boolean termine; //Inidque si la commande a terminé son exécution.

    private boolean enAttenteMessage;
    private VerificateurAbstrait verif;

    private final Commande cmd;
    private final MessageEventAdapter msgEvt;
    private MessageEventAdapter msgEvtSec; //Événement secondaire utilisé dans attendreReponse

    private CountDownLatch msgEvtLatch;
}
