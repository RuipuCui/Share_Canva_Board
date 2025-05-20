package Client.Poller;

import Client.LaunchUI;
import Client.CreateWhiteBoard;
import RMI.RemoteWhiteBoards;

import javax.swing.*;
import java.rmi.RemoteException;

public class ManagerDisconnectPoller implements Runnable {
    private final RemoteWhiteBoards remote;
    private final String username;
    private final JFrame frame;

    public ManagerDisconnectPoller(RemoteWhiteBoards remote, String username, JFrame frame) {
        this.remote = remote;
        this.username = username;
        this.frame = frame;
    }

    @Override
    public void run() {
        while (true) {
            try {
                remote.getUsers(); // any periodic call
                Thread.sleep(2000);
            } catch (RemoteException | InterruptedException e) {
                // manager likely quit
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(
                            null,
                            "The manager has exited. You have been disconnected.",
                            "Disconnected",
                            JOptionPane.WARNING_MESSAGE
                    );
                    frame.dispose();
                    LaunchUI.createAndShow(config -> {
                        if (config.isManager) {
                            CreateWhiteBoard.launchWhiteBoard(config.ip, config.port, config.username);
                        } else {
                            CreateWhiteBoard.joinWhiteBoard(config.ip, config.port, config.username);
                        }
                    });
                });
                break;
            }
        }
    }
}
