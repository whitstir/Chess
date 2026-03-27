package client;

import java.util.Scanner;

import static java.lang.System.out;

public class ChessClient {
    private final ServerFacade serverFacade;
    private boolean loggedIn = false;

    public ChessClient(ServerFacade serverFacade) {
        this.serverFacade = serverFacade;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            out.print("> ");
            String input = scanner.nextLine();
            evaluate(input);
        }
    }

    private void evaluate(String input) {
        if (!loggedIn) {
            preLogin();
        } else {
            postLogin();
        }
    }

    private void preLogin() {
        String[] input = getInput();
        switch(input[0]) {
            case "help":
            case "quit":
            case "login":
            case "register":
        }
    }

    private void postLogin() {
        String[] input = getInput();
        switch(input[0]) {
            case "help" -> printHelp();
            case "quit":
            case "login":
            case "register":
        }
    }

    private String[] getInput() {
        out.print("\n[LOGGED OUT] >>> ");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine().split(" ");
    }

    private void printHelp() {
        out.println("register <USERNAME> <PASSWORD> <EMAIL> - to create an account");
        out.println("login <USERNAME> <PASSWORD> - to play chess");
        out.println("quit - playing chess");
        out.println("help - with possible commands");
    }


}
