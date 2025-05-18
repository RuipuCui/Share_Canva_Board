package Client.ClientUI;

import Client.ClientUI.Poller.ChatPoller;
import RMI.RemoteWhiteBoard;
import RMI.RemoteWhiteBoards;
import Server.WhiteBoards;

import javax.swing.*;
import java.awt.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import java.util.ArrayList;
import java.util.List;

public class ChatPanel extends JPanel {
    private final JTextArea chatArea;

    public ChatPanel(RemoteWhiteBoards remoteWhiteBoards, String username) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(250, 250, 255));
        setPreferredSize(new Dimension(220, 300));

        JLabel chatLabel = new JLabel("Chat");
        chatLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        chatLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        add(chatLabel);

        chatArea = new JTextArea(12, 20);
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        JScrollPane scroll = new JScrollPane(chatArea);
        scroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        scroll.setMaximumSize(new Dimension(200, 250));
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(scroll);

        // === Input Area ===
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.X_AXIS));
        inputPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        inputPanel.setMaximumSize(new Dimension(200, 30));
        inputPanel.setBackground(new Color(250, 250, 255));

        JTextField input = new JTextField();
        input.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        input.setPreferredSize(new Dimension(120, 30));

        JButton send = new JButton("Send");
        send.setPreferredSize(new Dimension(60, 30));
        send.setMaximumSize(new Dimension(60, 30));
        send.setMinimumSize(new Dimension(60, 30));

        inputPanel.add(input);
        inputPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        inputPanel.add(send);

        add(Box.createRigidArea(new Dimension(0, 10)));
        add(inputPanel);

        setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0)); // top, left, bottom, right


        // === Send Handler ===
        send.addActionListener(e -> {
            String msg = input.getText().trim();
            if (!msg.isEmpty()) {
                try {
                    remoteWhiteBoards.sendChatMessage(username, msg);
                    input.setText("");
                } catch (RemoteException ex) {
                    ex.printStackTrace();
                }
            }
        });

        input.addActionListener(e -> send.doClick()); // press Enter to send

        // === Polling Thread ===
        new Thread(new ChatPoller(remoteWhiteBoards, chatArea)).start();
    }
}

