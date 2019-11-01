package connect6.visual;

import connect6.GameEngine;

import javax.swing.*;
import java.awt.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Connect6GUI extends JPanel {
    private static int amount = 19;
    private static int size = 40;
    private static int offset = 50;


    private static final String BLACK = "BLACK";
    private static final int BLACK_ID = 1;

    private static final String WHITE = "WHITE";
    private static final int WHITE_ID = 2;

    private static List<List<Integer>> list = new ArrayList<>(19);

    public Connect6GUI() {
        super();
//        for (int i = 0; i < 19; i++) {
//            List<Integer> l = new ArrayList<>();
//            for (int j = 0; j < 19; j++) {
//                l.add(0);
//            }
//            list.add(l);
//        }






    }

    public static void main(String[] args) {

        Integer host = 2020;
        try {
            Registry registry = LocateRegistry.getRegistry(host);
            GameEngine stub = (GameEngine) registry.lookup("GameEngine");
            System.out.println(stub);
            Connect6GUI m = new Connect6GUI(stub.getId(), stub);
            JFrame jp1 = new JFrame();

            jp1.getContentPane().add(m, BorderLayout.CENTER);
            jp1.setSize(new Dimension(900, 900));
            jp1.setVisible(true);
            jp1.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.WHITE);
        for (int x = 0; x <= amount; x++) {
            g.drawLine(x * size + offset, offset, x * size + offset, offset + amount * size);
        }
        for (int y = 0; y <= amount; y++) {
            g.drawLine(offset, y * size + offset, amount * size + offset, y * size + offset);
        }
//        list.get(0).set(1, WHITE_ID);
        for (int x = 0; x < amount; x++) {
            for (int y = 0; y < amount; y++) {
                int i = list.get(x).get(y);
                if (i == WHITE_ID) {
                    g.fillRect(x * size + offset,y * size + offset  ,size ,size );
                }
            }
        }
    }

}
