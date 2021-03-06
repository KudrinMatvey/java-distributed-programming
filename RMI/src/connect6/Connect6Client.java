package connect6;

import javafx.util.Pair;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Scanner;

public class Connect6Client {

    private static final String BLACK = "BLACK";
    private static final String WHITE = "WHITE";

    private String id;
    private GameEngine gameEngine;

    private static List<List<Integer>> list = null;

    private Connect6Client() throws RemoteException, NotBoundException {
        Integer host = 2020;
        Registry registry = LocateRegistry.getRegistry(host);
        GameEngine stub = (GameEngine) registry.lookup("GameEngine");
        System.out.println(stub);
        this.id = stub.getId();
        this.gameEngine = stub;
        Scanner sc = new Scanner(System.in);
        System.out.println("I am player:" + id);

        if (id.equals(BLACK)) {
            gameEngine.makeTurn(makePoint(9, 9), id);
            list = gameEngine.getList();
        }
        while (gameEngine.didAnyoneWin(id) == null) {
            System.out.println("Waiting for other player ");
            list = gameEngine.waitForOpponentTurn(id);
            if (gameEngine.didAnyoneWin(id) != null) {
                System.out.println("Player " + gameEngine.didAnyoneWin(id) + " won");
                return;
            }
            printList(list);
            int x, y;
            System.out.print("Other player made his turn, make your turn x: ");
            x = sc.nextInt();
            System.out.println("y: ");
            y = sc.nextInt();
            while (!gameEngine.makeTurn(makePoint(x, y), id)) {
                System.out.print("You made an invalid turn, please make another one x: ");
                x = sc.nextInt();
                System.out.println("y: ");
                y = sc.nextInt();
            }
            if (gameEngine.didAnyoneWin(id) != null) {
                System.out.println("Player " + gameEngine.didAnyoneWin(id) + " won");
                return;
            }
            System.out.print("You made your first turn, make the second one x: ");
            x = sc.nextInt();
            System.out.println("y: ");
            y = sc.nextInt();
            while (!gameEngine.makeTurn(makePoint(x, y), id)) {
                System.out.print("You made an invalid turn, please make another one x: ");
                x = sc.nextInt();
                System.out.println("y: ");
                y = sc.nextInt();
            }

        }
        System.out.printf("Player %s won", gameEngine.didAnyoneWin(id));
    }

    private static Pair<Integer, Integer> makePoint(int x, int y) {
            return new Pair<>(x, y);
    }

    private static void printList(List<List<Integer>> list) {
        for (int y = 0; y < 19; y++) {
            for (int x = 0; x < 19; x++) {
                System.out.printf("%d ", list.get(x).get(y));
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {

        try {
            Connect6Client connect6Client = new Connect6Client();
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }

    }
}