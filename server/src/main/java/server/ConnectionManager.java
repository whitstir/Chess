package server;

import io.javalin.websocket.WsContext;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    private final ConcurrentHashMap<Integer, Set<WsContext>> connections = new ConcurrentHashMap<>();

    public void add(int gameID, WsContext session) {
        connections.computeIfAbsent(gameID, k -> ConcurrentHashMap.newKeySet()).add(session);
    }

    public void remove(int gameID, WsContext session) {
        Set<WsContext> sessions = connections.get(gameID);
        if (sessions != null) {
            sessions.remove(session);
        }
    }

    public void removeFromAll(WsContext session) {
        connections.values().forEach(set -> set.remove(session));
    }

    public void broadcast(int gameID, WsContext exclude, String message) {
        Set<WsContext> sessions = connections.getOrDefault(gameID, Set.of());
        for (WsContext s : sessions) {
            if (s.session.isOpen() && !s.equals(exclude)) {
                s.send(message);
            }
        }
    }

    public void broadcastAll(int gameID, String message) {
        for (WsContext s : connections.getOrDefault(gameID, Set.of())) {
            if (s.session.isOpen()) {
                s.send(message);
            }
        }
    }
}