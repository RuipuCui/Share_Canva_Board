package Client.Poller;

import Client.CreateWhiteBoard;
import Client.LaunchUI;
import RMI.RemoteWhiteBoards;

import javax.swing.*;
import java.util.List;

public class WaitingApprovalPoller implements Runnable {
    private final RemoteWhiteBoards remote;
    private final String ip;
    private final int port;
    private final String username;
    private final JDialog waitingDialog;

    public WaitingApprovalPoller(RemoteWhiteBoards remote, String ip, int port, String username, JDialog dialog) {
        this.remote = remote;
        this.ip = ip;
        this.port = port;
        this.username = username;
        this.waitingDialog = dialog;
    }

    @Override
    public void run() {
        while (true) {
            try {
                List<String> approvedUsers = remote.getUsers();
                List<String> waitingUsers = remote.getWaitingUsers();

                if (approvedUsers.contains(username)) {
                    waitingDialog.dispose();
                    SwingUtilities.invokeLater(() ->
                            Client.ClientUI.MainClientUI.launchUI(ip, port, username, false, remote)
                    );
                    break;
                }

                if (!waitingUsers.contains(username)) {
                    SwingUtilities.invokeLater(() -> {
                        waitingDialog.dispose();
                        JOptionPane.showMessageDialog(
                                null,
                                "You have been removed or rejected by the manager.",
                                "Access Denied",
                                JOptionPane.WARNING_MESSAGE
                        );
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

                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(
                            null,
                            "Connection lost or manager rejected your request.",
                            "Connection Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                    waitingDialog.dispose();
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

