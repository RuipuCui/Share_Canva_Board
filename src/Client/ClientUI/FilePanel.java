package Client.ClientUI;

import RMI.RemoteWhiteBoards;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;

public class FilePanel extends JPanel {
    public FilePanel(JTabbedPane tabbedPane, JFrame frame, RemoteWhiteBoards remote, String username) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setAlignmentX(Component.LEFT_ALIGNMENT);
        setMaximumSize(new Dimension(200, 150));  // limit height
        setBackground(new Color(250, 250, 255));

        JLabel label = new JLabel("File");
        label.setFont(new Font("SansSerif", Font.BOLD, 16));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(label);
        add(Box.createRigidArea(new Dimension(0, 10)));

        add(createButton("Save Canvas", tabbedPane, frame, FileAction.SAVE, remote, username));
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(createButton("Load Canvas", tabbedPane, frame, FileAction.LOAD, remote, username));
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(createButton("Export Image As", tabbedPane, frame, FileAction.EXPORT, remote, username));

        setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0)); // top, left, bottom, right

    }

    private JButton createButton(String label, JTabbedPane tabbedPane, JFrame frame, FileAction action, RemoteWhiteBoards remote, String username) {
        JButton btn = new JButton(label);
        btn.setMaximumSize(new Dimension(200, 30));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.addActionListener(e -> {
            try {
                if (!remote.checkPermission(username)) {
                    JOptionPane.showMessageDialog(null,
                            "Only the manager can perform this action.",
                            "Permission Denied",
                            JOptionPane.ERROR_MESSAGE);
                    return; // ❗ Skip the rest
                }
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }

            WhiteBoardUI canvas = (WhiteBoardUI) tabbedPane.getSelectedComponent();
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle(label);
            int result = (action == FileAction.LOAD)
                    ? chooser.showOpenDialog(frame)
                    : chooser.showSaveDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                String path = chooser.getSelectedFile().getAbsolutePath();
                try {

                    switch (action) {
                        case SAVE -> canvas.saveCanvasToFile(path);
                        case LOAD -> canvas.loadCanvasFromFile(path);
                        case EXPORT -> {
                            String format = path.endsWith(".jpg") ? "jpg" : "png";
                            if (!path.toLowerCase().endsWith("." + format)) path += "." + format;
                            canvas.exportAsImage(path, format);
                        }
                    }
                } catch (RemoteException ex) {
                    ex.printStackTrace();
                }
            }
        });
        return btn;
    }

    enum FileAction {
        SAVE, LOAD, EXPORT
    }
}

