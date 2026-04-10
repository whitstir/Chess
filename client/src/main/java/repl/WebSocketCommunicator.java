package repl;

import com.google.gson.Gson;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.CountDownLatch;

public class WebSocketCommunicator {
    private WebSocket webSocket;
    private final Gson gson = new Gson();

    public WebSocketCommunicator(String serverUrl, ServerMessageObserver observer, UserGameCommand initialCommand) throws Exception {
        URI uri = new URI(serverUrl.replace("http", "ws") + "/ws");
        HttpClient client = HttpClient.newHttpClient();
        CountDownLatch openLatch = new CountDownLatch(1);

        webSocket = client.newWebSocketBuilder()
                .buildAsync(uri, new WebSocket.Listener() {
                    private final StringBuilder buffer = new StringBuilder();

                    @Override
                    public void onOpen(WebSocket ws) {
                        openLatch.countDown();
                        ws.request(Long.MAX_VALUE);
                    }

                    @Override
                    public CompletionStage<?> onText(WebSocket ws, CharSequence data, boolean last) {
                        buffer.append(data);
                        if (last) {
                            String message = buffer.toString();
                            buffer.setLength(0);
                            try {
                                ServerMessage base = gson.fromJson(message, ServerMessage.class);
                                ServerMessage typed = null;
                                if (base.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME) {
                                    typed = gson.fromJson(message, LoadGameMessage.class);
                                } else if (base.getServerMessageType() == ServerMessage.ServerMessageType.ERROR) {
                                    typed = gson.fromJson(message, ErrorMessage.class);
                                } else if (base.getServerMessageType() == ServerMessage.ServerMessageType.NOTIFICATION) {
                                    typed = gson.fromJson(message, NotificationMessage.class);
                                }
                                if (typed != null) {
                                    observer.onMessage(typed);
                                }
                            } catch (Exception e) {
                                System.out.println("WebSocket message error: " + e.getMessage());
                            }
                        }
                        return null;
                    }

                    @Override
                    public void onError(WebSocket ws, Throwable error) {
                        System.out.println("WebSocket error: " + error.getMessage());
                    }
                }).join();

        openLatch.await();
        webSocket.sendText(gson.toJson(initialCommand), true).join();
    }

    public void send(UserGameCommand command) throws IOException {
        try {
            webSocket.sendText(gson.toJson(command), true).join();
        } catch (Exception e) {
            throw new IOException("Send failed: " + e.getMessage(), e);
        }
    }

    public void close() throws IOException {
        if (webSocket != null) {
            try {
                webSocket.sendClose(WebSocket.NORMAL_CLOSURE, "done").join();
            } catch (Exception ignored) {}
        }
    }
}
