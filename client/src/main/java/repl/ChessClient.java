package repl;

import chess.ChessGame;
import model.GameData;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

import static java.lang.String.valueOf;
import static java.lang.System.out;

public class ChessClient {
    private final Scanner scanner = new Scanner(System.in);
    private final ServerFacade serverFacade;
    private boolean loggedIn = false;
    private final Map<Integer, Integer> gameMap = new HashMap<>();


    public ChessClient(ServerFacade serverFacade) {
        this.serverFacade = serverFacade;
    }

    public void run() throws URISyntaxException, IOException {
        while (true) {
            if (!loggedIn) {
                preLogin();
            } else {
                postLogin();
            }
        }
    }

    private void preLogin() {
        String[] input = getInput();
        switch (input[0].toLowerCase()) {
            case "help" -> {
                printRegister();
                printLogin();
                printHelp();
                printQuit();
                break;
            }
            case "quit" -> {
                System.exit(0);
            }
            case "login" -> {
                if (input.length != 3) {
                    out.println("Please enter a username and password");
                    printLogin();
                    break;
                }
                loginUser(input);
            }
            case "register" -> {
                if (input.length != 4) {
                    out.println("Please enter a username, password, and email");
                    printRegister();
                    break;
                }
                try {
                    serverFacade.register(input[1], input[2], input[3]);
                    out.println("Successfully registered");
                    loggedIn = true;
                } catch (Exception e) {
                    out.println("Invalid username. Please try again.");
                }
            }
            default -> out.println("Unknown command. Type help.");
        }
    }

    private void postLogin() throws URISyntaxException, IOException {
        String[] input = getInput();
        switch (input[0].toLowerCase()) {
            case "create" -> {
                if (input.length != 2) {
                    out.println("Please enter a game name");
                    printCreate();
                    break;
                }
                try {
                    serverFacade.createGame(input[1]);
                    out.println("New game " + input[1] + " created");
                } catch (Exception e) {
                    out.println("Could not create game. Please try again");
                    printCreate();
                }
            }
            case "list" -> {
                if (input.length != 1) {
                    out.println("No additional arguments required");
                    printList();
                    break;
                }
                try {
                    Collection<GameData> games = serverFacade.listGames();
                    gameMap.clear();
                    int index = 1;
                    for (GameData game : games) {
                        gameMap.put(index, index);
                        String white = game.whiteUsername() == null ? "-" : game.whiteUsername();
                        String black = game.blackUsername() == null ? "-" : game.blackUsername();
                        out.println(index + ": " + game.gameName() + " (" + white + " vs " + black + ")");
                        index++;
                    }
                    if (games.isEmpty()) {
                        out.println("No games available.");
                    }
                } catch (Exception e) {
                    out.println("Could not retrieve games list.");
                }
            }
            case "join" -> {
                if (input.length != 3) {
                    out.println("Please enter a game number and your player color");
                    printJoin();
                    break;
                }
                if (!input[2].equalsIgnoreCase("WHITE") && !input[2].equalsIgnoreCase("BLACK")) {
                    out.println("Color must be WHITE or BLACK");
                    return;
                }
                joinGame(input);
            }
            case "observe" -> {
                if (input.length != 2) {
                    out.println("Please enter a game id to observe");
                    printObserve();
                    break;
                }
                observeUser(input);
            }
            case "logout" -> {
                if (input.length != 1) {
                    out.println("Please don't enter additional arguments");
                    printLogout();
                    break;
                }
                try {
                    serverFacade.logout();
                    loggedIn = false;
                    out.println("Logged out successfully.");
                } catch (Exception e) {
                    out.println("Logout failure");
                }
            }
            case "quit" -> {
                System.exit(0);
            }
            case "help" -> {
                printCreate();
                printList();
                printJoin();
                printObserve();
                printLogout();
                printQuit();
                printHelp();
                break;
            }
            default -> out.println("Unknown command. Type help.");

        }
    }

    private String[] getInput() {
        if (loggedIn) {
            out.print("\n[LOGGED IN] >>> ");
        } else {
            out.print("\n[LOGGED OUT] >>> ");
        }
        return scanner.nextLine().split(" ");
    }

    private void printRegister() {
        out.println("register <USERNAME> <PASSWORD> <EMAIL> - to create an account");
    }

    private void printLogin() {
        out.println("login <USERNAME> <PASSWORD> - to play chess");
    }

    private void printQuit() {
        out.println("quit - playing chess");
    }

    private void printHelp() {
        out.println("help - with possible commands");
    }

    private void printCreate() {
        out.println("create <NAME> - a game");
    }

    private void printList() {
        out.println("list - games");
    }

    private void printJoin() {
        out.println("join <ID> [WHITE|BLACK] - a game");
    }

    private void printObserve() {
        out.println("observe <ID> - a game");
    }

    private void printLogout() {
        out.println("logout - when you are done");
    }

    private void loginUser(String[] input) {
        try {
            serverFacade.login(input[1], input[2]);
            loggedIn = true;
            out.println("Successfully logged in.");
        } catch (Exception e) {
            out.println("Incorrect username or password.");
        }
    }

    private void observeUser(String[] input) {
        try {
            int index = Integer.parseInt(input[1]);
            Integer gameID = gameMap.get(index);
            if (gameID == null) {
                out.println("Invalid game number. Run 'list' first.");
                return;
            }
            Collection<GameData> games = serverFacade.listGames();
            GameData target = games.stream().filter(g -> g.gameID() == gameID)
                    .findFirst().orElseThrow(() -> new RuntimeException("Game not found"));
            BoardDrawing.drawBoard(target.game(), ChessGame.TeamColor.WHITE);
            out.println("Observing game " + index);
        } catch (Exception e) {
            out.println("Could not observe game. Please try again.");
            printObserve();
        }
    }

    private void joinGame(String[] input) {
        try {
            int index = Integer.parseInt(input[1]);
            Integer gameID = gameMap.get(index);
            if (gameID == null) {
                out.println("Invalid game number. Run 'list' first.");
                return;
            }
            serverFacade.joinGame(input[2], gameID);
            Collection<GameData> games = serverFacade.listGames();
            GameData target = games.stream().filter(g -> g.gameID() == gameID)
                    .findFirst().orElseThrow(() -> new RuntimeException("Game not found"));
            BoardDrawing.drawBoard(target.game(), ChessGame.TeamColor.valueOf(input[2].toUpperCase()));
            out.println("Successfully joined game " + index);
        } catch (Exception e) {
            out.println("Could not join game. Please try again.");
            printJoin();
        }
    }
}
