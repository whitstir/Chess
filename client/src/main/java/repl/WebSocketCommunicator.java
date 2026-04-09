package repl;

import com.google.gson.Gson;
import jakarta.websocket.*;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.net.URI;

public class WebSocketCommunicator extends Endpoint {
    public Session session;
    private final Gson gson = new Gson();

    public WebSocketCommunicator(String serverUrl, ServerMessageObserver observer) throws Exception {
        URI uri = new URI(serverUrl.replace("http", "ws") + "/ws");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        session = container.connectToServer(this, uri);
        this.session.addMessageHandler((MessageHandler.Whole<String>) message -> {
            try {
                ServerMessage baseMessage = gson.fromJson(message, ServerMessage.class);
                ServerMessage typed = null;
                if (baseMessage.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME) {
                    typed = gson.fromJson(message, LoadGameMessage.class);
                } else if (baseMessage.getServerMessageType() == ServerMessage.ServerMessageType.ERROR) {
                    typed = gson.fromJson(message, ErrorMessage.class);
                } else if (baseMessage.getServerMessageType() == ServerMessage.ServerMessageType.NOTIFICATION) {
                    typed = gson.fromJson(message, NotificationMessage.class);
                }
                if (typed != null) {
                    observer.onMessage(typed);
                }
            } catch (Exception e) {
                System.err.println("Error processing WebSocket message: " + e.getMessage());
                e.printStackTrace(System.err);
            }
        });
    }

    public void send(UserGameCommand command) throws IOException {
        String json = gson.toJson(command);
        session.getBasicRemote().sendText(gson.toJson(command));
    }

    public void close() throws IOException {
        if (session != null && session.isOpen()) {
            session.close();
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }
}
