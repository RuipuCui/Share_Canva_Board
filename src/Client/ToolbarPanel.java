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

public class ToolbarPanel extends JPanel {
    public ToolbarPanel(RemoteWhiteBoards remoteWhiteBoards, List<WhiteBoardUI> whiteBoards, JTabbedPane tabbedPane) {
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        setBackground(new Color(245, 245, 245));
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));

        String[] tools = {"Freehand", "Line", "Rectangle", "Oval", "Triangle"};
        for (String tool : tools) {
            JButton btn = new JButton(tool);
            btn.addActionListener(e -> {
                WhiteBoardUI selected = (WhiteBoardUI) tabbedPane.getSelectedComponent();
                selected.setTool(tool);
            });
            add(btn);
        }

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
        add(Box.createHorizontalStrut(20));
        add(newBoardBtn);
    }
}

