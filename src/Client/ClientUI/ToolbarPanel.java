package Client.ClientUI;

import RMI.RemoteWhiteBoard;
import RMI.RemoteWhiteBoards;

import javax.swing.*;
import java.awt.*;

import java.util.List;

public class ToolbarPanel extends JPanel {
    public ToolbarPanel(RemoteWhiteBoards remoteWhiteBoards, List<WhiteBoardUI> whiteBoards, JTabbedPane tabbedPane) {
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        setBackground(new Color(245, 245, 245));
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));

        String[] tools = {"Freehand", "Line", "Rectangle", "Oval", "Triangle", "Eraser", "Color"};
        for (String tool : tools) {
            JButton btn = new JButton(tool);

            if ("Eraser".equals(tool)) {
                btn.addActionListener(e -> {
                    WhiteBoardUI selected = (WhiteBoardUI) tabbedPane.getSelectedComponent();
                    selected.setTool("Eraser");

                    // Create eraser size chooser popup
                    JSlider slider = new JSlider(5, 50, selected.getEraserSize());
                    slider.setMajorTickSpacing(20);
                    slider.setMinorTickSpacing(5);
                    slider.setPaintTicks(true);
                    slider.setPaintLabels(true);

                    JPanel panel = new JPanel(new BorderLayout());
                    panel.add(new JLabel("Eraser Size:"), BorderLayout.NORTH);
                    panel.add(slider, BorderLayout.CENTER);

                    int result = JOptionPane.showConfirmDialog(
                            this,
                            panel,
                            "Select Eraser Size",
                            JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.PLAIN_MESSAGE
                    );

                    if (result == JOptionPane.OK_OPTION) {
                        selected.setEraserSize(slider.getValue());
                    }
                });

            } else if ("Color".equals(tool)) {
                btn.addActionListener(e -> {
                    WhiteBoardUI selected = (WhiteBoardUI) tabbedPane.getSelectedComponent();

                    Color[] colors = {
                            Color.BLACK, Color.DARK_GRAY, Color.GRAY, Color.LIGHT_GRAY,
                            Color.WHITE, Color.RED, Color.PINK, Color.ORANGE,
                            Color.YELLOW, Color.GREEN, Color.MAGENTA, Color.CYAN,
                            Color.BLUE, new Color(139, 69, 19), // brown
                            new Color(255, 228, 181),           // moccasin
                            new Color(128, 0, 128)              // purple
                    };

                    JPanel colorPanel = new JPanel(new GridLayout(4, 4, 5, 5));
                    for (Color color : colors) {
                        JButton colorBtn = new JButton();
                        colorBtn.setBackground(color);
                        colorBtn.setPreferredSize(new Dimension(30, 30));
                        colorBtn.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                        // 🔧 Make the button show its background color
                        colorBtn.setOpaque(true);
                        colorBtn.setContentAreaFilled(true);
                        colorBtn.setFocusPainted(false);

                        colorBtn.addActionListener(ev -> {
                            selected.setCurrentColor(color);
                            SwingUtilities.getWindowAncestor(colorPanel).dispose();
                        });
                        colorPanel.add(colorBtn);
                    }


                    JOptionPane.showMessageDialog(
                            this,
                            colorPanel,
                            "Choose a Color",
                            JOptionPane.PLAIN_MESSAGE
                    );
                });

            } else {
                btn.addActionListener(e -> {
                    WhiteBoardUI selected = (WhiteBoardUI) tabbedPane.getSelectedComponent();
                    selected.setTool(tool);
                });
            }

            add(btn);
        }


        JButton newBoardBtn = getNewBoardBtn(remoteWhiteBoards, whiteBoards, tabbedPane);
        add(Box.createHorizontalStrut(20));
        add(newBoardBtn);
    }

    private static JButton getNewBoardBtn(RemoteWhiteBoards remoteWhiteBoards, List<WhiteBoardUI> whiteBoards, JTabbedPane tabbedPane) {
        JButton newBoardBtn = new JButton("+ New Board");
        newBoardBtn.addActionListener(e -> {
            try {
                remoteWhiteBoards.newWhiteBoard();
                int num = remoteWhiteBoards.getWhiteBoardNum();
                RemoteWhiteBoard board = remoteWhiteBoards.getOneWhiteBoard(num - 1);
                WhiteBoardUI ui = new WhiteBoardUI(board);
                whiteBoards.add(ui);
                tabbedPane.addTab("Board " + num, ui);
                tabbedPane.setSelectedComponent(ui);

                remoteWhiteBoards.sendGroupMessage("Manager created a new board " + num);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        return newBoardBtn;
    }
}

