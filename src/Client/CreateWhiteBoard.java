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
            // Try to start RMI registry
            try {
                LocateRegistry.createRegistry(port);
            } catch (java.rmi.server.ExportException e) {
                throw new Exception("A server is already running on port " + port + ".");
            }

            RemoteWhiteBoards whiteBoards = new WhiteBoards();

            try {
                Naming.rebind("rmi://" + ip + ":" + port + "/WhiteBoards", whiteBoards);
            } catch (Exception e) {
                throw new Exception("Failed to bind to the RMI registry. A server may already be running at this address.");
            }

            System.out.println("WhiteBoard Manager '" + username + "' started at " + ip + ":" + port);
            Thread.sleep(500);
            RemoteWhiteBoards remote = RemoteHandler.getRemoteWhiteBoards(ip, port);
            remote.addUser(username);
            remote.sendGroupMessage("Welcome user " + username + " join!");
            MainClientUI.launchUI(ip, port, username, true, remote);  // isManager = true

        } catch (Exception e) {
            e.printStackTrace();

            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(
                        null,
                        "Failed to start server at " + ip + ":" + port + ".\n" +
                                e.getMessage(),
                        "Server Start Error",
                        JOptionPane.ERROR_MESSAGE
                );

                // Relaunch launch UI
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


    public static void joinWhiteBoard(String ip, int port, String username) {
        try {
            System.out.println("Joining whiteboard as '" + username + "'...");
            RemoteWhiteBoards remote = RemoteHandler.getRemoteWhiteBoards(ip, port);

            if (remote == null) {
                throw new Exception("Could not connect to whiteboard server.");
            }

            boolean success = remote.addUser(username);
            if (!success) {
                JOptionPane.showMessageDialog(
                        null,
                        "The username '" + username + "' is already in use.\nPlease choose a different one.",
                        "Username Taken",
                        JOptionPane.WARNING_MESSAGE
                );

                LaunchUI.createAndShow(config -> {
                    if (config.isManager) {
                        CreateWhiteBoard.launchWhiteBoard(config.ip, config.port, config.username);
                    } else {
                        joinWhiteBoard(config.ip, config.port, config.username);
                    }
                });
                return;
            }

            remote.sendGroupMessage("Welcome user " + username + " join!");
            MainClientUI.launchUI(ip, port, username, false, remote);  // isManager = false

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                    null,
                    "Failed to connect to whiteboard server at " + ip + ":" + port + ".\nMake sure the manager has started the server.",
                    "Connection Error",
                    JOptionPane.ERROR_MESSAGE
            );

            LaunchUI.createAndShow(config -> {
                if (config.isManager) {
                    CreateWhiteBoard.launchWhiteBoard(config.ip, config.port, config.username);
                } else {
                    joinWhiteBoard(config.ip, config.port, config.username);
                }
            });
        }
    }


}



