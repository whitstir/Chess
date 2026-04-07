package repl;

import websocket.messages.ServerMessage;

public interface ServerMessageObserver {
    void onMessage(ServerMessage message);
}
