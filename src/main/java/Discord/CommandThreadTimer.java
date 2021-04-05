package Discord;

import java.util.concurrent.CountDownLatch;

public class CommandThreadTimer extends Thread {
    public CommandThreadTimer(int p_timeout, CountDownLatch p_latch) {
        timeout = p_timeout;
        latch = p_latch;
        this.start();
    }

    /**
     * À la fin du sleep, le timer ouvre la latch. (Utilisé dans attendreReponse de CommandThread)
     *
     * NOTE : Le timer essaiera toujours d'ouvrir la latch, même si elle fut ouverte ailleur.
     * Donc, cette classe s'arrête seulement après lorsque le CommandThread se termine, ou lorsque
     * le Sleep se termine.
     *
     * NOTE2 : La latch ne devrait jamais être supprimée.
     */
    @Override
    public void run() {
        try {
            Thread.sleep(1000*timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        latch.countDown();
    }

    final private int timeout;
    final private CountDownLatch latch;
}
