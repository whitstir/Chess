package repl;

public class ClientMain {
    public static void main(String[] args) {
        System.out.println("♕ Welcome to 240 Chess");
        ServerFacade serverFacade =
                new ServerFacade(8080);
        ChessClient client =
                new ChessClient(serverFacade);
        try {
            client.run();
        } catch (Exception e) {
            System.out.println("Client error: " + e.getMessage());
        }
    }
}
