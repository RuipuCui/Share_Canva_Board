package Client.Poller;

import Client.ClientUI.WhiteBoardUI;
import RMI.RemoteWhiteBoard;
import RMI.RemoteWhiteBoards;

import javax.swing.*;

import java.util.List;

public class WhiteboardPoller implements Runnable {
    private final RemoteWhiteBoards remote;
    private final List<WhiteBoardUI> localList;
    private final JTabbedPane tabbedPane;

    public WhiteboardPoller(RemoteWhiteBoards remote, List<WhiteBoardUI> localList, JTabbedPane tabbedPane) {
        this.remote = remote;
        this.localList = localList;
        this.tabbedPane = tabbedPane;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
                int remoteCount = remote.getWhiteBoardNum();
                if (remoteCount > localList.size()) {
                    for (int i = localList.size(); i < remoteCount; i++) {
                        RemoteWhiteBoard board = remote.getOneWhiteBoard(i);
                        WhiteBoardUI ui = new WhiteBoardUI(board);
                        int index = i;
                        SwingUtilities.invokeLater(() -> {
                            localList.add(ui);
                            tabbedPane.addTab("Board " + (index + 1), ui);
                        });
                    }
                }
            } catch (Exception e) {
                System.err.println("Board polling error: " + e.getMessage());
            }
        }
    }
}

