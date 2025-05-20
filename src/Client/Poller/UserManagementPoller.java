package Client.Poller;

import RMI.RemoteWhiteBoards;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class UserManagementPoller implements Runnable {

    private final RemoteWhiteBoards remote;
    private final JPanel waitingRoomPanel;
    private final JPanel currentUsersPanel;
    private final AtomicBoolean running;
    private final Runnable repaintCallback;

    private List<String> lastWaiting = null;
    private List<String> lastActive = null;

    public UserManagementPoller(RemoteWhiteBoards remote,
                                JPanel waitingRoomPanel,
                                JPanel currentUsersPanel,
                                AtomicBoolean running,
                                Runnable repaintCallback) {
        this.remote = remote;
        this.waitingRoomPanel = waitingRoomPanel;
        this.currentUsersPanel = currentUsersPanel;
        this.running = running;
        this.repaintCallback = repaintCallback;
    }

    @Override
    public void run() {
        while (running.get()) {
            try {
                Thread.sleep(1000);

                List<String> waiting = remote.getWaitingUsers();
                List<String> active = remote.getUsers();

                if (!waiting.equals(lastWaiting) || !active.equals(lastActive)) {
                    lastWaiting = waiting;
                    lastActive = active;

                    SwingUtilities.invokeLater(() -> {
                        waitingRoomPanel.removeAll();
                        currentUsersPanel.removeAll();

                        for (String user : waiting) {
                            JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
                            row.add(new JLabel(user));
                            JButton approveBtn = new JButton("Approve");
                            approveBtn.addActionListener(e -> {
                                try {
                                    remote.approveUser(user);
                                    remote.sendGroupMessage(user + " is approved to join");
                                } catch (RemoteException ex) {
                                    ex.printStackTrace();
                                }
                            });
                            row.add(approveBtn);
                            waitingRoomPanel.add(row);
                        }

                        for (int i = 0; i < active.size(); i++) {
                            String user = active.get(i);
                            if (i == 0) continue; // Skip manager

                            JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
                            row.add(new JLabel(user));
                            JButton kickBtn = new JButton("Kick");
                            kickBtn.addActionListener(e -> {
                                try {
                                    remote.kickUser(user);
                                    remote.sendGroupMessage(user + " has been removed");
                                } catch (RemoteException ex) {
                                    ex.printStackTrace();
                                }
                            });
                            row.add(kickBtn);
                            currentUsersPanel.add(row);
                        }

                        repaintCallback.run();
                    });
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (RemoteException ex) {
                ex.printStackTrace();
            }
        }
    }
}

