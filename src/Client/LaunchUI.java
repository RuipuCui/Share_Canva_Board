package Client;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class LaunchUI {
    public static void createAndShow(Consumer<LaunchConfig> onLaunch) {
        JFrame frame = new JFrame("Whiteboard Launcher");
        frame.setSize(420, 250);
        frame.setLayout(new GridBagLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 5, 8);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel ipLabel = new JLabel("Server IP:");
        frame.add(ipLabel, gbc);

        gbc.gridx = 1;
        JTextField ipField = new JTextField("127.0.0.1", 15);
        frame.add(ipField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel portLabel = new JLabel("Port:");
        frame.add(portLabel, gbc);

        gbc.gridx = 1;
        JTextField portField = new JTextField("1010", 15);
        frame.add(portField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel nameLabel = new JLabel("Username:");
        frame.add(nameLabel, gbc);

        gbc.gridx = 1;
        JTextField nameField = new JTextField("Alice", 15);
        frame.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel roleLabel = new JLabel("Role:");
        frame.add(roleLabel, gbc);

        gbc.gridx = 1;
        String[] roles = {"Manager", "Participant"};
        JComboBox<String> roleBox = new JComboBox<>(roles);
        frame.add(roleBox, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        JButton enterButton = new JButton("Enter");
        frame.add(enterButton, gbc);

        enterButton.addActionListener(e -> {
            String ip = ipField.getText().trim();
            String portStr = portField.getText().trim();
            String username = nameField.getText().trim();
            String role = (String) roleBox.getSelectedItem();

            if (ip.isEmpty() || portStr.isEmpty() || username.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill in all fields.");
                return;
            }

            try {
                int port = Integer.parseInt(portStr);
                frame.dispose();
                onLaunch.accept(new LaunchConfig(ip, port, username, "Manager".equals(role)));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Port must be a number.");
            }
        });

        frame.setVisible(true);
    }

    public static class LaunchConfig {
        public final String ip;
        public final int port;
        public final String username;
        public final boolean isManager;

        public LaunchConfig(String ip, int port, String username, boolean isManager) {
            this.ip = ip;
            this.port = port;
            this.username = username;
            this.isManager = isManager;
        }
    }
}


