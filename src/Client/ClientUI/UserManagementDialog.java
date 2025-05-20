package Client.ClientUI;

import Client.Poller.UserManagementPoller;
import RMI.RemoteWhiteBoards;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class UserManagementDialog extends JDialog {

    private final JPanel waitingRoomPanel = new JPanel();
    private final JPanel currentUsersPanel = new JPanel();
    private final RemoteWhiteBoards remote;
    private final AtomicBoolean running = new AtomicBoolean(true);

    private List<String> lastWaiting = null;
    private List<String> lastActive = null;

    public UserManagementDialog(RemoteWhiteBoards remote, Component parent) {
        super(SwingUtilities.getWindowAncestor(parent), "User Management", ModalityType.APPLICATION_MODAL);
        this.remote = remote;

        setSize(400, 300);
        setLayout(new GridLayout(1, 2, 10, 0));
        setLocationRelativeTo(parent);

        waitingRoomPanel.setLayout(new BoxLayout(waitingRoomPanel, BoxLayout.Y_AXIS));
        waitingRoomPanel.setBorder(BorderFactory.createTitledBorder("Waiting Room"));

        currentUsersPanel.setLayout(new BoxLayout(currentUsersPanel, BoxLayout.Y_AXIS));
        currentUsersPanel.setBorder(BorderFactory.createTitledBorder("Current Users"));

        add(waitingRoomPanel);
        add(currentUsersPanel);

        // Start polling thread
        startPollingThread();

        // Stop thread when dialog is closed
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                running.set(false);
            }
        });

        setVisible(true);
    }

    private void startPollingThread() {
        Runnable repaintCallback = () -> {
            revalidate();
            repaint();
        };

        UserManagementPoller poller = new UserManagementPoller(
                remote,
                waitingRoomPanel,
                currentUsersPanel,
                running,
                repaintCallback
        );

        Thread pollingThread = new Thread(poller);
        pollingThread.setDaemon(true);
        pollingThread.start();
    }

}

