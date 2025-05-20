package Client.Poller;

import Client.LaunchUI;
import RMI.RemoteWhiteBoards;

import javax.swing.*;
import java.util.List;

public class UserKickPoller implements Runnable {
    private final RemoteWhiteBoards remote;
    private final String username;
    private final JFrame frame;

    public UserKickPoller(RemoteWhiteBoards remote, String username, JFrame frame) {
        this.remote = remote;
        this.username = username;
        this.frame = frame;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000); // Poll every second
                List<String> users = remote.getUsers();

                if (!users.contains(username)) {
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(
                                frame,
                                "You have been kicked by the manager.",
                                "Kicked Out",
                                JOptionPane.WARNING_MESSAGE
                        );
                        frame.dispose();
                        LaunchUI.createAndShow(config -> {
                            if (config.isManager) {
                                Client.CreateWhiteBoard.launchWhiteBoard(config.ip, config.port, config.username);
                            } else {
                                Client.CreateWhiteBoard.joinWhiteBoard(config.ip, config.port, config.username);
                            }
                        });
                    });
                    break;
                }

            } catch (Exception e) {
                e.printStackTrace();
                break; // Exit loop on exception
            }
        }
    }
}

