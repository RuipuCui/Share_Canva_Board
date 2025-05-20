package Client.Poller;

import Client.ClientUI.WhiteBoardUI;
import Client.CreateWhiteBoard;
import Client.LaunchUI;

import javax.swing.*;
import java.rmi.RemoteException;

public class WhiteboardRepaintPoller implements Runnable {
    private final WhiteBoardUI whiteBoardUI;
    private final long intervalMillis;

    public WhiteboardRepaintPoller(WhiteBoardUI whiteBoardUI, long intervalMillis) {
        this.whiteBoardUI = whiteBoardUI;
        this.intervalMillis = intervalMillis;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(intervalMillis);
                whiteBoardUI.repaint();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
