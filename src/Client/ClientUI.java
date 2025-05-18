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
            JFrame frame = new JFrame("Java Drawing Canvas");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 800);
            frame.setLayout(new BorderLayout(10, 10));

            // === Tabbed Whiteboard Panels ===
            JTabbedPane tabbedPane = new JTabbedPane();
            frame.add(tabbedPane, BorderLayout.CENTER);

            //System.out.println("111");
            RemoteWhiteBoards remoteWhiteBoards;
            List<WhiteBoardUI> whiteBoardUIS = new ArrayList<>();
            try {
                remoteWhiteBoards = RemoteHandler.getRemoteWhiteBoards(ip, port);
                int numBoards = remoteWhiteBoards.getWhiteBoardNum();

                for (int i = 0; i < numBoards; i++) {
                    RemoteWhiteBoard board = remoteWhiteBoards.getOneWhiteBoard(i);
                    WhiteBoardUI ui = new WhiteBoardUI(board);
                    whiteBoardUIS.add(ui);
                    tabbedPane.addTab("Board " + (i + 1), ui);
                }

            } catch (RemoteException | NotBoundException e) {
                throw new RuntimeException("Failed to connect to remote whiteboards", e);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }


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
                    WhiteBoardUI selected = (WhiteBoardUI) tabbedPane.getSelectedComponent();
                    selected.setTool(tool);
                });
                topBar.add(btn);
            }

            JButton newBoardBtn = getNewBoardBtn(remoteWhiteBoards, whiteBoardUIS, tabbedPane);
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

            JButton saveBtn = getSaveBtn(tabbedPane, frame);

            JButton loadBtn = getLoadBtn(tabbedPane, frame);

            JButton exportBtn = getExportBtn(tabbedPane, frame);

            sidebar.add(fileLabel);
            sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
            sidebar.add(saveBtn);
            sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
            sidebar.add(loadBtn);
            sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
            sidebar.add(exportBtn);
            sidebar.add(Box.createRigidArea(new Dimension(0, 30)));

            JLabel chatLabel = new JLabel("Chat");
            chatLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
            chatLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            final JTextArea chatArea = new JTextArea(12, 20);
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

            JPanel chatInputPanel = new JPanel();
            chatInputPanel.setLayout(new BoxLayout(chatInputPanel, BoxLayout.X_AXIS));
            chatInputPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            chatInputPanel.setMaximumSize(new Dimension(200, 30));
            chatInputPanel.setBackground(new Color(250, 250, 255));

            JTextField chatInput = new JTextField();
            chatInput.setMaximumSize(new Dimension(Short.MAX_VALUE, 30));

            JButton sendButton = new JButton("Send");
            sendButton.setPreferredSize(new Dimension(60, 30));

            chatInputPanel.add(chatInput);
            chatInputPanel.add(Box.createRigidArea(new Dimension(5, 0)));
            chatInputPanel.add(sendButton);

            // Add to sidebar
            sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
            sidebar.add(chatInputPanel);

            // Send message using RMI
            sendButton.addActionListener(e -> {
                String message = chatInput.getText().trim();
                if (!message.isEmpty()) {
                    try {
                        remoteWhiteBoards.sendChatMessage(username, message);
                        System.out.println(username + "message sent");
                        chatInput.setText("");
                    } catch (RemoteException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            chatInput.addActionListener(e -> sendButton.doClick());  // Support Enter key


            frame.add(sidebar, BorderLayout.WEST);
            // === Polling thread to detect new whiteboards added on the server ===
            new Thread(() -> {
                while (true) {
                    try {
                        Thread.sleep(1000);  // Poll every second (tune as needed)

                        int remoteNum = remoteWhiteBoards.getWhiteBoardNum();
                        int localNum = whiteBoardUIS.size();

                        if (remoteNum > localNum) {
                            for (int i = localNum; i < remoteNum; i++) {
                                RemoteWhiteBoard board = remoteWhiteBoards.getOneWhiteBoard(i);
                                WhiteBoardUI ui = new WhiteBoardUI(board);

                                int boardIndex = i + 1;
                                SwingUtilities.invokeLater(() -> {
                                    whiteBoardUIS.add(ui);
                                    tabbedPane.addTab("Board " + boardIndex, ui);
                                });
                            }
                        }

                    } catch (RemoteException | InterruptedException e) {
                        System.err.println("Polling error: " + e.getMessage());
                        break; // optionally retry instead of exiting
                    }
                }
            }).start();

            // === Chat polling thread (fetches chat history from server) ===
            new Thread(() -> {
                List<String> previousMessages = new ArrayList<>();

                while (true) {
                    try {
                        Thread.sleep(1000); // Poll every second

                        List<String> serverMessages = remoteWhiteBoards.getChatMessages();
                        if (!serverMessages.equals(previousMessages)) {
                            previousMessages = new ArrayList<>(serverMessages);  // defensive copy
                            SwingUtilities.invokeLater(() -> {
                                chatArea.setText("");
                                for (String msg : serverMessages) {
                                    chatArea.append(msg + "\n");
                                }
                            });
                        }

                    } catch (RemoteException | InterruptedException e) {
                        System.err.println("Chat polling error: " + e.getMessage());
                        break;
                    }
                }
            }).start();


            frame.setVisible(true);
        });
    }

    private static JButton getExportBtn(JTabbedPane tabbedPane, JFrame frame) {
        JButton exportBtn = new JButton("Export Image As");
        exportBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        exportBtn.setMaximumSize(new Dimension(200, 30));
        exportBtn.addActionListener(e -> {
            WhiteBoardUI canvas = (WhiteBoardUI) tabbedPane.getSelectedComponent();
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
                try {
                    canvas.exportAsImage(path, format);
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        return exportBtn;
    }

    private static JButton getLoadBtn(JTabbedPane tabbedPane, JFrame frame) {
        JButton loadBtn = new JButton("Load Canvas");
        loadBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        loadBtn.setMaximumSize(new Dimension(200, 30));
        loadBtn.addActionListener(e -> {
            WhiteBoardUI canvas = (WhiteBoardUI) tabbedPane.getSelectedComponent();
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Load Canvas");
            if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                String path = fileChooser.getSelectedFile().getAbsolutePath();
                try {
                    canvas.loadCanvasFromFile(path);
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        return loadBtn;
    }

    private static JButton getSaveBtn(JTabbedPane tabbedPane, JFrame frame) {
        JButton saveBtn = new JButton("Save Canvas");
        saveBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        saveBtn.setMaximumSize(new Dimension(200, 30));
        saveBtn.addActionListener(e -> {
            WhiteBoardUI canvas = (WhiteBoardUI) tabbedPane.getSelectedComponent();
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Canvas");
            if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
                String path = fileChooser.getSelectedFile().getAbsolutePath();
                try {
                    canvas.saveCanvasToFile(path);
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        return saveBtn;
    }

    private static JButton getNewBoardBtn(RemoteWhiteBoards remoteWhiteBoards, List<WhiteBoardUI> whiteBoardUIS, JTabbedPane tabbedPane) {
        JButton newBoardBtn = new JButton("+ New Board");
        newBoardBtn.setFocusPainted(false);
        newBoardBtn.setPreferredSize(new Dimension(120, 30));
        newBoardBtn.addActionListener(e -> {
            int num = 0;
            RemoteWhiteBoard currentBoard;
            try {
                remoteWhiteBoards.newWhiteBoard();
                Thread.sleep(100);
                num = remoteWhiteBoards.getWhiteBoardNum();
                currentBoard = remoteWhiteBoards.getOneWhiteBoard(num - 1);
            } catch (RemoteException | InterruptedException ex) {
                throw new RuntimeException(ex);
            }

            whiteBoardUIS.add(new WhiteBoardUI(currentBoard));
            tabbedPane.addTab("Board " + num, whiteBoardUIS.get(num - 1));
            tabbedPane.setSelectedComponent(whiteBoardUIS.get(num - 1));
        });
        return newBoardBtn;
    }
}





