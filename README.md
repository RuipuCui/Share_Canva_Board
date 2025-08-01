# Shared Whiteboard (Java RMI)

A real-time collaborative whiteboard application built using **Java RMI (Remote Method Invocation)**. This application allows multiple users to draw together on shared canvases, communicate via chat, manage participants, and handle whiteboard files over a network.

---

## Features

- 🎨 Freehand, line, rectangle, oval, triangle drawing with color selection
- 👥 User roles: Manager & Participant
- ✅ Manager approval system (waiting room, kick users)
- 💬 Real-time group chat
- 📁 File operations: Create, Save, Load, Export whiteboards
- 🧑‍🤝‍🧑 Collaborator viewer
- 📡 Real-time canvas sync via background polling threads

---

## Project Structure

### Client Components
- `CreateWhiteBoard.java`: Main entry class for manager/participant launching
- `LaunchUI.java`: Connection UI for role selection
- `MainClientUI.java`: Main whiteboard interface with toolbars, chat, and tabs
- `UserManagementDialog.java`: Manager's UI for approving/kicking users
- `RemoteHandler.java`: RMI utility for connecting to server
- **Other Panels**: 
  - `ChatPanel`: Live message exchange
  - `CollaboratorPanel`: View active users
  - `FilePanel`: Save/load/export functions
  - `ToolbarPanel`: Tool selection & board controls

### Thread Synchronization (Pollers)
- `ChatPoller`: Updates chat in real-time
- `CollaboratorPoller`: Updates user list
- `UserKickPoller`: Detects and reacts to manager kicks
- `WaitingApprovalPoller`: Participant approval status checker
- `WhiteboardPoller`: Syncs all board content
- `WhiteboardRepaintPoller`: Smooth canvas updates

---

### Server Components
- `RMIServer.java`: Initializes RMI registry and binds services
- `WhiteBoards.java`: Manages users, whiteboards, and sessions (implements `RemoteWhiteBoards`)
- `WhiteBoard.java`: Manages canvas data, shapes, and persistence (implements `RemoteWhiteBoard`)
- `DrawableShape.java`: Abstract base class for drawable objects with shape ID, color, and behavior

---

## RMI Communication

Java RMI enables seamless remote method calls between client and server:
- **Remote Interfaces**:
  - `RemoteWhiteBoards`: Manages sessions, users, and chat
  - `RemoteWhiteBoard`: Handles drawing, shape data, and file ops
- **Clients**:
  - Lookup remote interfaces via `Naming.lookup`
  - Invoke drawing or user methods remotely
- **Server**:
  - Executes requests and returns synced data

---

## How to Run

### Prerequisites
- Java 8 or later
- `rmiregistry` available in system path

### Steps

1. **Start Server**
   ```bash
   javac RMIServer.java
   java RMIServer
