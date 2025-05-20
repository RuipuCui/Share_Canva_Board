package Client.ClientUI;

import Client.Poller.CollaboratorPoller;
import RMI.RemoteWhiteBoards;

import javax.swing.*;
import java.awt.*;

public class CollaboratorPanel extends JPanel {
    private final JTextArea collaboratorArea;

    public CollaboratorPanel(RemoteWhiteBoards remoteWhiteBoards) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(250, 250, 255));
        setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JLabel label = new JLabel("Collaborators");
        label.setFont(new Font("SansSerif", Font.BOLD, 16));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(label);
        add(Box.createRigidArea(new Dimension(0, 10)));

        collaboratorArea = new JTextArea(6, 20);
        collaboratorArea.setEditable(false);
        collaboratorArea.setLineWrap(true);
        collaboratorArea.setWrapStyleWord(true);
        collaboratorArea.setBackground(Color.WHITE);
        collaboratorArea.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        JScrollPane scroll = new JScrollPane(collaboratorArea);
        scroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        scroll.setMaximumSize(new Dimension(200, 100));
        add(scroll);

        setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0)); // top, left, bottom, right

        // Periodically update list of collaborators
        new Thread(new CollaboratorPoller(remoteWhiteBoards, collaboratorArea)).start();
    }
}
