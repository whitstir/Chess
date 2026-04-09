package repl;

import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import websocket.commands.UserGameCommand;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Map;

import static java.lang.System.out;

public class ServerFacade {
    private final String serverUrl;
    private String authToken;
    private final Gson gson = new Gson();

    public record CreateGame(int gameID) {};
    public record ListGames(Collection<GameData> games) {}

    public ServerFacade(int port) {
        this.serverUrl = "http://localhost:" + port;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void clear() throws URISyntaxException, IOException {
        request("DELETE", "/db", null);
    }

    public AuthData register(String username, String password, String email) throws URISyntaxException, IOException {
        var body = Map.of("username", username, "password", password, "email", email);
        String response = request("POST", "/user", gson.toJson(body));
        AuthData auth = gson.fromJson(response, AuthData.class);
        this.authToken = auth.authToken();

        return auth;
    }

    public AuthData login(String username, String password) throws URISyntaxException, IOException {
        var body = Map.of("username", username, "password", password);
        String response = request("POST", "/session", gson.toJson(body));
        AuthData auth = gson.fromJson(response, AuthData.class);
        this.authToken = auth.authToken();

        return auth;
    }

    public void logout() throws URISyntaxException, IOException {
        request("DELETE", "/session", null);
        this.authToken = null;
    }


    public int createGame(String gameName) throws URISyntaxException, IOException {
        var body = Map.of("gameName", gameName);
        String response = request("POST", "/game", gson.toJson(body));
        CreateGame game = gson.fromJson(response, CreateGame.class);

        return game.gameID;
    }

    public Collection<GameData> listGames() throws URISyntaxException, IOException {
        var body = request("GET", "/game", null);
        ListGames response = gson.fromJson(body, ListGames.class);

        return response.games;
    }

    public void joinGame(String playerColor, int gameID) throws URISyntaxException, IOException {
        var body = Map.of("playerColor", playerColor, "gameID", gameID);
        request("PUT", "/game", gson.toJson(body));
    }

    private String request(String method, String endpoint, String body) throws URISyntaxException, IOException {
        URI uri = new URI(serverUrl + endpoint);
        HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();
        connection.setRequestMethod(method);

        if (authToken != null) {
            connection.addRequestProperty("authorization", authToken);
        }
        if (body != null) {
            connection.setDoOutput(true);
            connection.addRequestProperty("Content-Type", "application/json");
            try (OutputStream os = connection.getOutputStream()) {
                os.write(body.getBytes());
            }
        }

        connection.connect();

        if (connection.getResponseCode() != 200) {
            throw new RuntimeException("Request failed");
        }
        try (InputStream response = connection.getInputStream()) {
            return new String(response.readAllBytes());
        }
    }

    public WebSocketCommunicator connect(int gameId, ServerMessageObserver observer) throws Exception {
        WebSocketCommunicator webSocketCommunicator = new WebSocketCommunicator(serverUrl, observer);
        UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameId);
        webSocketCommunicator.send(command);
        return webSocketCommunicator;
    }
}
