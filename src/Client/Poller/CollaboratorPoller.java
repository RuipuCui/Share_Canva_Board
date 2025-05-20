package Client.Poller;

import RMI.RemoteWhiteBoards;

import javax.swing.*;
import java.rmi.RemoteException;
import java.util.List;

public class CollaboratorPoller implements Runnable {
    private final RemoteWhiteBoards remote;
    private final JTextArea collaboratorArea;

    public CollaboratorPoller(RemoteWhiteBoards remote, JTextArea collaboratorArea) {
        this.remote = remote;
        this.collaboratorArea = collaboratorArea;
    }

    @Override
    public void run() {
        while (true) {
            try {
                List<String> users = remote.getUsers();
                SwingUtilities.invokeLater(() -> {
                    collaboratorArea.setText(String.join("\n", users));
                });
                Thread.sleep(2000);
            } catch (RemoteException | InterruptedException e) {
                System.err.println("Collaborator polling error: " + e.getMessage());
                break;
            }
        }
    }
}

