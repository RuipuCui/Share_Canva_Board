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

        String[] tools = {"Freehand", "Line", "Rectangle", "Oval", "Triangle", "Eraser"};
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
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        return newBoardBtn;
    }
}

