package connect6.visual;

import connect6.GameEngine;
import javafx.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Connect6GUI extends JPanel {
    private static int amount = 19;
    private static int size = 40;
    private static int offset = 50;


    private static final String BLACK = "BLACK";
    private static final int BLACK_ID = 1;

    private static final String WHITE = "WHITE";
    private static final int WHITE_ID = 2;
    private String id;
    private GameEngine gameEngine;

    private static List<List<Integer>> list = null;

    public Connect6GUI() {
        super();

        Integer host = 2020;
        try {
            Registry registry = LocateRegistry.getRegistry(host);
            GameEngine stub = (GameEngine) registry.lookup("GameEngine");
            System.out.println(stub);
            this.id = stub.getId();
            this.gameEngine = stub;

        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }

    }

    private static Pair<Integer, Integer> makePoint(int x, int y) {
        return new Pair<>(x, y);
    }


    public void startGame() throws RemoteException {
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
            this.repaint();
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

    public static void main(String[] args) throws RemoteException {
//        Connect6GUI m = new Connect6GUI();
//        JFrame jp1 = new JFrame();
//
//        jp1.getContentPane().add(m, BorderLayout.CENTER);
//        jp1.setSize(new Dimension(900, 900));
//        jp1.setVisible(true);
//        jp1.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        m.startGame();

    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.RED);
        for (int x = 0; x <= amount; x++) {
            g.drawLine(x * size + offset, offset, x * size + offset, offset + amount * size);
        }
        for (int y = 0; y <= amount; y++) {
            g.drawLine(offset, y * size + offset, amount * size + offset, y * size + offset);
        }
        if (list != null) {
            for (int x = 0; x < amount; x++) {
                for (int y = 0; y < amount; y++) {
                    int i = list.get(x).get(y);
                    if (i == WHITE_ID) {
                        g.setColor(Color.WHITE);
                        g.fillRect(x * size + offset, y * size + offset, size, size);
                    } else if (i == BLACK_ID) {
                        g.setColor(Color.BLACK);
                        g.fillRect(x * size + offset, y * size + offset, size, size);
                    }
                }
            }
        }
    }

}
