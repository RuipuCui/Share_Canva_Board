package Client;

import RMI.RemoteWhiteBoard;
import RMI.RemoteWhiteBoards;
import Server.WhiteBoards;

import javax.swing.*;
import java.awt.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import java.util.ArrayList;
import java.util.List;

public class ClientUI {
    public static void launchUI(String ip, int port, String username, boolean isManager) {
        SwingUtilities.invokeLater(() -> {
            try {
                RemoteWhiteBoards remote = RemoteHandler.getRemoteWhiteBoards(ip, port);
                JFrame frame = new JFrame("Java Drawing Canvas");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(1200, 800);
                frame.setLayout(new BorderLayout(10, 10));

                List<WhiteBoardUI> whiteBoards = new ArrayList<>();
                JTabbedPane tabbedPane = new JTabbedPane();
                int count = remote.getWhiteBoardNum();
                for (int i = 0; i < count; i++) {
                    WhiteBoardUI ui = new WhiteBoardUI(remote.getOneWhiteBoard(i));
                    whiteBoards.add(ui);
                    tabbedPane.addTab("Board " + (i + 1), ui);
                }

                frame.add(new ToolbarPanel(remote, whiteBoards, tabbedPane), BorderLayout.NORTH);
                frame.add(tabbedPane, BorderLayout.CENTER);

                JPanel sidebar = new JPanel();
                sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
                sidebar.setPreferredSize(new Dimension(220, frame.getHeight()));
                sidebar.setBackground(new Color(250, 250, 255));

                // Add FilePanel and ChatPanel
                sidebar.add(new FilePanel(tabbedPane, frame));
                sidebar.add(Box.createRigidArea(new Dimension(0, 20)));
                sidebar.add(new ChatPanel(remote, username));


                frame.add(sidebar, BorderLayout.WEST);



                new Thread(new WhiteboardPoller(remote, whiteBoards, tabbedPane)).start();
                frame.setVisible(true);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}






