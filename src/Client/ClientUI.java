package Client;

import WhiteBoard.DrawingWhiteBoard;
import Server.RemoteWhiteBoards;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;

public class ClientUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Java Drawing Canvas");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 800);
            frame.setLayout(new BorderLayout(10, 10));

            // === Tabbed Whiteboard Panels ===
            JTabbedPane tabbedPane = new JTabbedPane();
            frame.add(tabbedPane, BorderLayout.CENTER);

            // === Initial Board ===
            final RemoteWhiteBoards[] whiteBoardsContainer = new RemoteWhiteBoards[1];
            try {
                whiteBoardsContainer[0] = new RemoteWhiteBoards();
                //RemoteWhiteBoards whiteBoards = whiteBoardsContainer[0];
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }

//            int[] boardCount = {1};
//            DrawingWhiteBoard firstBoard = new DrawingWhiteBoard();
            tabbedPane.addTab("Board " + whiteBoardsContainer[0].getWhiteBoardNum(), whiteBoardsContainer[0].getWhiteBoards().get(0));

            // === Top Tool Bar ===
            JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
            topBar.setBackground(new Color(245, 245, 245));
            topBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));

            String[] tools = {"Freehand", "Line", "Rectangle", "Oval", "Triangle"};
            for (String tool : tools) {
                JButton btn = new JButton(tool);
                btn.setFocusPainted(false);
                btn.setPreferredSize(new Dimension(100, 30));
                btn.addActionListener(e -> {
                    DrawingWhiteBoard selected = (DrawingWhiteBoard) tabbedPane.getSelectedComponent();
                    selected.setTool(tool);
                });
                topBar.add(btn);
            }

            JButton newBoardBtn = new JButton("+ New Board");
            newBoardBtn.setFocusPainted(false);
            newBoardBtn.setPreferredSize(new Dimension(120, 30));
            newBoardBtn.addActionListener(e -> {
                //boardCount[0]++;
                //DrawingWhiteBoard newBoard = new DrawingWhiteBoard();
                try {
                    whiteBoardsContainer[0].newWhiteBoard();
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
                int num = whiteBoardsContainer[0].getWhiteBoardNum();
                tabbedPane.addTab("Board " + num, whiteBoardsContainer[0].getWhiteBoards().get(num - 1));
                tabbedPane.setSelectedComponent(whiteBoardsContainer[0].getWhiteBoards().get(num - 1));
            });
            topBar.add(Box.createHorizontalStrut(20));
            topBar.add(newBoardBtn);

            frame.add(topBar, BorderLayout.NORTH);

            // === Sidebar ===
            JPanel sidebar = new JPanel();
            sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
            sidebar.setPreferredSize(new Dimension(220, frame.getHeight()));
            sidebar.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            sidebar.setBackground(new Color(250, 250, 255));

            JLabel fileLabel = new JLabel("File");
            fileLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
            fileLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            JButton saveBtn = new JButton("Save Canvas");
            saveBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
            saveBtn.setMaximumSize(new Dimension(200, 30));
            saveBtn.addActionListener(e -> {
                DrawingWhiteBoard canvas = (DrawingWhiteBoard) tabbedPane.getSelectedComponent();
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Save Canvas");
                if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
                    String path = fileChooser.getSelectedFile().getAbsolutePath();
                    canvas.saveCanvasToFile(path);
                }
            });

            JButton loadBtn = new JButton("Load Canvas");
            loadBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
            loadBtn.setMaximumSize(new Dimension(200, 30));
            loadBtn.addActionListener(e -> {
                DrawingWhiteBoard canvas = (DrawingWhiteBoard) tabbedPane.getSelectedComponent();
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Load Canvas");
                if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                    String path = fileChooser.getSelectedFile().getAbsolutePath();
                    canvas.loadCanvasFromFile(path);
                }
            });

            JButton exportBtn = new JButton("Export Image As");
            exportBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
            exportBtn.setMaximumSize(new Dimension(200, 30));
            exportBtn.addActionListener(e -> {
                DrawingWhiteBoard canvas = (DrawingWhiteBoard) tabbedPane.getSelectedComponent();
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Export as Image");
                fileChooser.setAcceptAllFileFilterUsed(false);
                fileChooser.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PNG Image (*.png)", "png"));
                fileChooser.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("JPEG Image (*.jpg)", "jpg", "jpeg"));

                if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
                    String path = fileChooser.getSelectedFile().getAbsolutePath();
                    String format = "png";
                    String selectedExt = ((javax.swing.filechooser.FileNameExtensionFilter) fileChooser.getFileFilter()).getExtensions()[0];
                    if (selectedExt.equals("jpg") || selectedExt.equals("jpeg")) {
                        format = "jpg";
                    }
                    if (!path.toLowerCase().endsWith("." + format)) {
                        path += "." + format;
                    }
                    canvas.exportAsImage(path, format);
                }
            });

            sidebar.add(fileLabel);
            sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
            sidebar.add(saveBtn);
            sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
            sidebar.add(loadBtn);
            sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
            sidebar.add(exportBtn);
            sidebar.add(Box.createRigidArea(new Dimension(0, 30)));

            JLabel chatLabel = new JLabel("Chat (Coming Soon)");
            chatLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
            chatLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            JTextArea chatArea = new JTextArea(12, 20);
            chatArea.setLineWrap(true);
            chatArea.setWrapStyleWord(true);
            chatArea.setEditable(false);
            chatArea.setBackground(Color.WHITE);
            chatArea.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

            JScrollPane chatScroll = new JScrollPane(chatArea);
            chatScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
            chatScroll.setPreferredSize(new Dimension(200, 250));
            chatScroll.setMaximumSize(new Dimension(200, 300));

            sidebar.add(chatLabel);
            sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
            sidebar.add(chatScroll);

            frame.add(sidebar, BorderLayout.WEST);
            frame.setVisible(true);
        });
    }
}





