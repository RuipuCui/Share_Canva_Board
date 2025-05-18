// JoinWhiteBoard.java
package Client;

public class JoinWhiteBoard {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: java JoinWhiteBoard <serverIP> <serverPort> <username>");
            return;
        }

        String ip = args[0];
        int port = Integer.parseInt(args[1]);
        String username = args[2];

        try {
            System.out.println("Joining whiteboard as '" + username + "'...");
            ClientUI.launchUI(ip, port, username, false);  // isManager = false
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
