import com.sun.org.apache.xpath.internal.operations.Mod;
import gameModel.*;
import javafx.util.Pair;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

class Connection extends Thread {
    ObjectInputStream inWhite;
    ObjectOutputStream outWhite;
    Socket clientSocketWhite;
    ObjectInputStream inBlack;
    ObjectOutputStream outBlack;
    Socket clientSocketBlack;

    private static final int MAP_SIZE = 19;
    private static final String BLACK = "BLACK";
    private static final int BLACK_ID = 1;
    private static int blackTurnsCount = 0;

    private static final String WHITE = "WHITE";
    private static final int WHITE_ID = 2;
    private static int whiteTurnsCount = 0;

    public Connection(Socket aClientSocketWhite, Socket aClientSocketBlack) {
        try {
            //Creating connection with player white
            clientSocketWhite = aClientSocketWhite;
            // todo: создание входных и выходных потоков
            inWhite = new ObjectInputStream(clientSocketWhite.getInputStream());
            System.out.println("white input stream created");
            outWhite = new ObjectOutputStream(clientSocketWhite.getOutputStream());
            System.out.println("white output stream created");

            //Creating connection with player black
            clientSocketBlack = aClientSocketBlack;
            inBlack = new ObjectInputStream(clientSocketBlack.getInputStream());
            System.out.println("black input stream created");
            outBlack = new ObjectOutputStream(clientSocketBlack.getOutputStream());
            System.out.println("black output stream created");


            outWhite.writeObject("");
            outBlack.writeObject("");
            outWhite.writeObject(new Action(Id.WHITE));
            outBlack.writeObject(new Action(Id.BLACK));

            // reading start game flag
            inWhite.readObject();
            inBlack.readObject();

            this.start();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Connection:" + e.getMessage());
        }
    }

    public void run() { // an echo server
        List<List<PointFlag>> map = new ArrayList<>();
        for (int i = 0; i < MAP_SIZE; i++) {
            List<PointFlag> l = new ArrayList<>();
            for (int j = 0; j < MAP_SIZE; j++) {
                l.add(PointFlag.EMPTY);
            }
            map.add(l);
        }

        map.get((MAP_SIZE + 1) / 2).set((MAP_SIZE + 1) / 2, PointFlag.BLACK);
        boolean run = true;
        while (run) {
            try {
                //white turn
                boolean whiteTurn = true;
                Integer whiteAttemptX = 0;
                Integer whiteAttemptY = 0;
                while (whiteTurn) {
                    outWhite.writeObject(new Action(Flag.WAITING_FOR_YOUR_TURN));
                    Action actionWhite = (Action) inWhite.readObject();
                    if (actionWhite.getType().equals(Flag.TRY)) {

                        whiteAttemptX = actionWhite.getAttemptCoordinates().getKey();
                        whiteAttemptY = actionWhite.getAttemptCoordinates().getValue();
                        if (whiteAttemptX < MAP_SIZE && whiteAttemptY < MAP_SIZE && map.get(whiteAttemptX).get(whiteAttemptY) == PointFlag.EMPTY) {
                            map.get(whiteAttemptX).set(whiteAttemptY, PointFlag.WHITE);
                            whiteTurn = false;
                            run = !checkIfSomeoneWon(map, true, whiteAttemptX, whiteAttemptY);
                            outWhite.writeObject(new Action(Flag.ATTEMPT_SUCCESSFUL, new Pair<Integer, Integer>(whiteAttemptX, whiteAttemptY)));
                            outBlack.writeObject(new Action(Flag.ATTEMPT_OPPONENT_SUCCESSFUL, new Pair<Integer, Integer>(whiteAttemptX, whiteAttemptY)));
                        } else {
                            System.err.printf("Invalid turn");
                        }
                    }
                }

                //black turn
                boolean blackTurn = true;
                while (blackTurn) {
                    outBlack.writeObject(new Action(Flag.WAITING_FOR_YOUR_TURN));
                    Action actionBlack = (Action) inBlack.readObject();
                    if (actionBlack.getType().equals(Flag.TRY)) {

                        Integer blackAttemptX = actionBlack.getAttemptCoordinates().getKey();
                        Integer blackAttemptY = actionBlack.getAttemptCoordinates().getValue();
                        if (blackAttemptX < MAP_SIZE && blackAttemptY < MAP_SIZE && map.get(blackAttemptX).get(blackAttemptY) == PointFlag.EMPTY) {
                            map.get(blackAttemptX).set(blackAttemptY, PointFlag.BLACK);
                            blackTurn = false;
                            run = !checkIfSomeoneWon(map, false, blackAttemptX, blackAttemptY);
                            outBlack.writeObject(new Action(Flag.ATTEMPT_SUCCESSFUL, new Pair<>(blackAttemptX, blackAttemptY)));
                            outWhite.writeObject(new Action(Flag.ATTEMPT_OPPONENT_SUCCESSFUL, new Pair<>(blackAttemptX, blackAttemptY)));
                        } else {
                            System.err.printf("Invalid turn");
                        }
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }

    }

    private boolean checkIfSomeoneWon(List<List<PointFlag>> map, boolean whiteTurn, int x, int y) throws IOException {
        if (!whiteTurn && blackWon(map, x, y)) {
            someoneWon(true);
            outWhite.close();
            outBlack.close();
            clientSocketWhite.close();
            clientSocketBlack.close();
            return true;
        }
        if (whiteTurn && whiteWon(map, x, y)) {
            someoneWon(false);
            outWhite.close();
            outBlack.close();
            clientSocketWhite.close();
            clientSocketBlack.close();
            return true;
        }
        return false;
    }

    private boolean blackWon(List<List<PointFlag>> list, int x, int y) {
        int startY = y - 4 > 0 ? y - 4 : 0;
        int finishY = y + 4 < MAP_SIZE ? y + 4 : MAP_SIZE;
        PointFlag id = PointFlag.BLACK;


        int startX = x - 4 > 0 ? x - 4 : 0;
        int finishX = x + 4 < MAP_SIZE ? x + 4 : MAP_SIZE;

        int startXY = startY < startX ? startY : startX;
        int finishXY = finishY < finishX ? finishY : finishX;

        int count = 0;
        for (int y1 = startY; y1 < finishY; y1++) {
            if (count == 6) {
                System.out.println("Player " + id + " won horizontally " + (x) + " " + (y1 - 5) + " to " + (x) + " " + (y1));
                return true;
            }
            count = list.get(x).get(y1) == id ? count + 1 : 0;
        }
        count = 0;
        for (int x1 = startX; x1 < finishX; x1++) {
            if (count == 6) {
                System.out.println("Player " + id + " won vertically" + (x1 - 5) + " " + (y) + " to " + (x1) + " " + (y));
                return true;
            }
            count = list.get(x1).get(y) == id ? count + 1 : 0;
        }
        count = 0;

        for (int xy = startXY; xy < finishXY; xy++) {
            if (count == 6) {
                System.out.println("Player " + id + " won diagonally " + (xy - 5) + " " + (xy - 5) + " to " + (xy) + " " + (xy));
                return true;
            }
            count = list.get(xy).get(xy) == id ? count + 1 : 0;
        }
        count = 0;

        return false;
    }

    private boolean whiteWon(List<List<PointFlag>> list, int x, int y) {
        int startY = y - 4 > 0 ? y - 4 : 0;
        int finishY = y + 4 < MAP_SIZE ? y + 4 : MAP_SIZE;
        PointFlag id = PointFlag.WHITE;


        int startX = x - 4 > 0 ? x - 4 : 0;
        int finishX = x + 4 < MAP_SIZE ? x + 4 : MAP_SIZE;

        int startXY = startY < startX ? startY : startX;
        int finishXY = finishY < finishX ? finishY : finishX;

        int count = 0;
        for (int y1 = startY; y1 < finishY; y1++) {
            if (count == 6) {
                System.out.println("Player " + id + " won horizontally " + (x) + " " + (y1 - 5) + " to " + (x) + " " + (y1));
                return true;
            }
            count = list.get(x).get(y1) == id ? count + 1 : 0;
        }
        count = 0;
        for (int x1 = startX; x1 < finishX; x1++) {
            if (count == 6) {
                System.out.println("Player " + id + " won vertically" + (x1 - 5) + " " + (y) + " to " + (x1) + " " + (y));
                return true;
            }
            count = list.get(x1).get(y) == id ? count + 1 : 0;
        }
        count = 0;

        for (int xy = startXY; xy < finishXY; xy++) {
            if (count == 6) {
                System.out.println("Player " + id + " won diagonally " + (xy - 5) + " " + (xy - 5) + " to " + (xy) + " " + (xy));
                return true;
            }
            count = list.get(xy).get(xy) == id ? count + 1 : 0;
        }
        count = 0;

        return false;
    }


    private void someoneWon(boolean whiteWon) {
        try {
            outWhite.writeObject(new Action(whiteWon ? Flag.WIN : Flag.LOST));
            outBlack.writeObject(new Action(!whiteWon ? Flag.WIN : Flag.LOST));
            System.err.println("Game finished");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
