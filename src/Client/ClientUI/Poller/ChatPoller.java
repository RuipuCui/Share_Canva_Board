package Client.ClientUI.Poller;

import RMI.RemoteWhiteBoards;

import javax.swing.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class ChatPoller implements Runnable {
    private final RemoteWhiteBoards remote;
    private final JTextArea chatArea;
    private final List<String> previousMessages = new ArrayList<>();

    public ChatPoller(RemoteWhiteBoards remote, JTextArea chatArea) {
        this.remote = remote;
        this.chatArea = chatArea;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
                List<String> current = remote.getChatMessages();
                if (!current.equals(previousMessages)) {
                    previousMessages.clear();
                    previousMessages.addAll(current);
                    SwingUtilities.invokeLater(() -> chatArea.setText(String.join("\n", current)));
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (RemoteException e) {
                System.err.println("Chat polling error: " + e.getMessage());
            }
        }
    }
}
