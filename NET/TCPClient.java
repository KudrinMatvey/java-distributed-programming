import gameModel.*;
import javafx.util.Pair;

import javax.naming.OperationNotSupportedException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.*;

public class TCPClient {
    private static final int MAP_SIZE = 19;
    private static Scanner scanner = new Scanner(System.in);
    private static int testc = 0;
    public static void main(String args[]) throws ClassNotFoundException {
        Socket s = null;
        try {
            int serverPort = 7896;
            // todo: подключение сокета
            s = new Socket(InetAddress.getLocalHost(), serverPort);
            ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
            System.out.println("OUT");
            ObjectInputStream in = new ObjectInputStream(s.getInputStream());
            Model m = new Model(10);
            System.out.println("IN");
            out.writeObject(m);
            System.out.println("wrote");
            String str = (String) in.readObject();
            System.out.println(str);
            startGame(in, out);
        } catch (UnknownHostException | OperationNotSupportedException e) {
            System.out.println("Socket:" + e.getMessage()); // host cannot be resolved
        } catch (EOFException e) {
            System.out.println("EOF:" + e.getMessage()); // end of stream reached
        } catch (IOException e) {
            e.printStackTrace(); // error in reading the stream
        } finally {
            if (s != null)
                try {
                    s.close();
                } catch (IOException e) {
                    System.out.println("close:" + e.getMessage());
                }
        }
    }

    private static void startGame(ObjectInputStream in, ObjectOutputStream out) throws IOException, ClassNotFoundException, OperationNotSupportedException {
        Action a = (Action) in.readObject();
        Id id = null;

        List<List<PointFlag>> map = null;

        if (!a.getType().equals(Flag.START)) {
            throw new OperationNotSupportedException("game has not started");
        }

        id = a.getId();
        map = setMap();
        final boolean white = id.equals(Id.WHITE);

        while (true) {
            Action nextAction = (Action) in.readObject();
//            System.out.println(nextAction.toString());
            switch (nextAction.getType()) {
                case WAITING_FOR_YOUR_TURN:
                    System.out.println("Waiting for you");
                    out.writeObject(new Action(getTurnCoordinates(map)));
                    break;
                case WIN:
                    System.err.println("you won");
                    return;
                case LOST:
                    System.err.println("you lost");
                    return;
                case INVALID_TURN:
                    System.err.println("you made wrong turn");
                    break;
                case ATTEMPT_SUCCESSFUL:
                    System.out.println("Your attempt was successful"); markPoint(true, white, nextAction.getAttemptCoordinates(), map); break;
                case ATTEMPT_OPPONENT_SUCCESSFUL: markPoint(false, white, nextAction.getAttemptCoordinates(), map); break;
                default:
                    System.err.println("something wrong" + nextAction.toString());
            }
        }
    }

    private static void markPoint(boolean mine, boolean white, Pair<Integer, Integer> attemptCoordinates , List<List<PointFlag>> map) {
        map.get(attemptCoordinates.getKey()).set(attemptCoordinates.getValue(), (mine && white) || (!mine && !white) ? PointFlag.WHITE : PointFlag.BLACK);
    }


    private static Pair<Integer, Integer> getTurnCoordinates(List<List<PointFlag>> map) {
        StringBuilder stringBuilder = new StringBuilder("Current map state is :\n");
        for (List<PointFlag> pointFlagList : map) {
            for (PointFlag pointFlag : pointFlagList) {
                switch (pointFlag) {
                    case EMPTY: stringBuilder.append('.'); break;
                    case WHITE: stringBuilder.append('w'); break;
                    case BLACK: stringBuilder.append('b'); break;
                }
            }
            stringBuilder.append('\n');
        }
        System.out.println(stringBuilder);

        System.out.print("Enter next coordinates, x: ");
        int x = scanner.nextInt();
        System.out.println();
        System.out.print("y:");
        int y = scanner.nextInt();
        System.out.println();

        return new Pair<>(x,y);
    }

    private static List<List<PointFlag>> setMap() {
        List<List<PointFlag>> map = new ArrayList<>();
        for (int i = 0; i < MAP_SIZE; i++) {
            List<PointFlag> l = new ArrayList<>();
            for (int j = 0; j < MAP_SIZE; j++) {
                l.add(PointFlag.EMPTY);
            }
            map.add(l);
        }

        map.get((MAP_SIZE + 1) / 2).set((MAP_SIZE + 1) / 2, PointFlag.BLACK);

        return map;
    }


    private static Pair<Integer, Integer> getMockPoint() {
        List<Pair<Integer, Integer>> ml = new ArrayList<>();
        ml.add(new Pair<>(0,0));
        ml.add(new Pair<>(0,1));
        ml.add(new Pair<>(0,2));
        ml.add(new Pair<>(0,3));

        ml.add(new Pair<>(3,3));
        ml.add(new Pair<>(3,4));
        ml.add(new Pair<>(3,2));

        ml.add(new Pair<>(1,5));
        ml.add(new Pair<>(1,6));
        ml.add(new Pair<>(1,7));

        ml.add(new Pair<>(5,5));
        ml.add(new Pair<>(5,6));

        ml.add(new Pair<>(5,8));
        ml.add(new Pair<>(5,9));

        ml.add(new Pair<>(2,5));
        ml.add(new Pair<>(2,6));

        ml.add(new Pair<>(2,8));
        ml.add(new Pair<>(2,9));

        ml.add(new Pair<>(8,7));
        ml.add(new Pair<>(8,9));
        ml.add(new Pair<>(9,6));
        ml.add(new Pair<>(9,1));

        if (testc  + 1 == ml.size() ) {
            testc = 0;
        }
        return ml.get(testc ++);
    }
}
