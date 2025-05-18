package Client;

import Client.ClientUI.MainClientUI;
import Client.ClientUI.RemoteHandler;
import RMI.RemoteWhiteBoards;
import Server.WhiteBoards;

import javax.swing.*;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class CreateWhiteBoard {
    public static void main(String[] args) {
        LaunchUI.createAndShow(config -> {
            if (config.isManager) {
                launchWhiteBoard(config.ip, config.port, config.username);
            } else {
                joinWhiteBoard(config.ip, config.port, config.username);
            }
        });
    }

    public static void launchWhiteBoard(String ip, int port, String username) {
        try {
            LocateRegistry.createRegistry(port);
            RemoteWhiteBoards whiteBoards = new WhiteBoards();
            Naming.rebind("rmi://" + ip + ":" + port + "/WhiteBoards", whiteBoards);

            System.out.println("WhiteBoard Manager '" + username + "' started at " + ip + ":" + port);
            Thread.sleep(500);
            RemoteWhiteBoards remote = RemoteHandler.getRemoteWhiteBoards(ip, port);
            remote.addUser(username + "(manager)");
            remote.sendGroupMessage("Welcome user " + username + " join!");
            MainClientUI.launchUI(ip, port, username, true, remote);  // isManager = true
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void joinWhiteBoard(String ip, int port, String username) {
        try {
            System.out.println("Joining whiteboard as '" + username + "'...");
            RemoteWhiteBoards remote = RemoteHandler.getRemoteWhiteBoards(ip, port);

            if (remote == null) {
                throw new Exception("Could not connect to whiteboard server.");
            }

            remote.addUser(username);
            remote.sendGroupMessage("Welcome user " + username + " join!");
            MainClientUI.launchUI(ip, port, username, false, remote);  // isManager = false
        } catch (Exception e) {
            e.printStackTrace();

            // Show an error dialog
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(
                        null,
                        "Failed to connect to whiteboard server at " + ip + ":" + port + ".\n" +
                                "Make sure the manager has started the server.",
                        "Connection Error",
                        JOptionPane.ERROR_MESSAGE
                );

                // Relaunch the LaunchUI so user can try again
                LaunchUI.createAndShow(config -> {
                    if (config.isManager) {
                        launchWhiteBoard(config.ip, config.port, config.username);
                    } else {
                        joinWhiteBoard(config.ip, config.port, config.username);
                    }
                });
            });
        }
    }

}



