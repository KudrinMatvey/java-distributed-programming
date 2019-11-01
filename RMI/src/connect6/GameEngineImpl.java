package connect6;

import javafx.util.Pair;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class GameEngineImpl implements GameEngine {

    private static final String BLACK = "BLACK";
    private static final int BLACK_ID = 1;
    private static int blackTurnsCount = 0;

    private static final String WHITE = "WHITE";
    private static final int WHITE_ID = 2;
    private static int whiteTurnsCount = 0;

    private static String winnerId = null;
    private static Integer playersInGame = 0;
    private static List<List<Integer>> list = new ArrayList<>(19);
    private final Object obj = new Object();

    public GameEngineImpl() {
        for (int i = 0; i < 19; i++) {
            List<Integer> l = new ArrayList<>();
            for (int j = 0; j < 19; j++) {
                l.add(0);
            }
            list.add(l);
        }
    }

    @Override
    public String getId() {
        String res;
        synchronized (obj) {
            switch (playersInGame++) {
                case 0:
                    res = BLACK;
                    break;
                case 1:
                    res = WHITE;
                    break;
                default:
                    return null;
            }
        }
        System.out.printf("Player %s connected to game \n", res);
        if (res.equals(BLACK)) {
            while (playersInGame != 2) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return null;
                }
            }
            System.out.println("Game started");
        }
        return res;
    }

    @Override
    public List getList() throws RemoteException {
        return list;
    }

    @Override
    public Boolean makeTurn(Pair<Integer, Integer> point, String id) {
        synchronized (obj) {
            int x = point.getKey();
            int y = point.getValue();
            if(!(x >= 0 && x < 19 && y >= 0 && y < 19)) return false;
            Integer a = list.get(x).get(y);
            if (a != 0) return false;
            if (id.equals(BLACK)) {
                list.get(x).set(y, BLACK_ID);
                System.out.println("Player BLACK made his turn on point "+ x +" " + y);
                winnerId = checkForWin(x, y, BLACK_ID) ? BLACK : null;
                blackTurnsCount++;
            } else {
                list.get(x).set(y, WHITE_ID);
                System.out.println("Player WHITE made his turn on point "+ x +" " + y);
                winnerId = checkForWin(x, y, WHITE_ID) ? WHITE : null;
                whiteTurnsCount++;
            }
            return true;
        }
    }

    private boolean checkForWin(int x, int y, int id) {
        int startY = y - 5 > 0 ? y - 5 : 0;
        int finishY = y + 5 < 19 ? y + 5 : 19;

        int startX = x - 5 > 0 ? x - 5 : 0;
        int finishX = x + 5 < 19 ? x + 5 : 19;

        int startXY = startY < startX ? startY : startX;
        int finishXY = finishY < finishX ? finishY : finishX;

        int count = 0;
        for (int y1 = startY; y1 < finishY; y1++) {
            if (count == 6) {
                System.out.println("Player "+ id + " won horizontally "+ (x) +" " + (y1-5) + " to " + (x) +" " + (y1));
                return true;
            }
            count = list.get(x).get(y1) == id ? count + 1 : 0;
        }
        count = 0;
        for (int x1 = startX; x1 < finishX; x1++) {
            if (count == 6) {
                System.out.println("Player "+ id + " won vertically"+ (x1 - 5) +" " + (y) + " to " + (x1) +" " + (y));
                return true;
            }
            count = list.get(x1).get(y) == id ? count + 1 : 0;
        }
        count = 0;

        for (int xy = startXY; xy < finishXY; xy++) {
            if (count == 6) {
                System.out.println("Player "+ id + " won diagonally "+ (xy - 5) +" " + (xy-5) + " to " + (xy) +" " + (xy));
                return true;
            }
            count = list.get(xy).get(xy) == id ? count + 1 : 0;
        }
        count = 0;

        return false;
    }

    @Override
    public String didAnyoneWin(String id) {
        if (winnerId != null) System.err.printf("Player %s won", id);
        return winnerId;
//        for (int y1 = 0; y1 < 19; y1++) {
//            for (int x1 = 0; x1 < 19; x1++) {
//                Integer mainPoint = list.get(x1).get(y1);
//                if (mainPoint == 0) continue;
//
//                if (mainPoint == BLACK_ID) {
//                    // searching through vertical line
//                    if (19 - )
//                }
//
//
//            }
//        }

//        return null;
    }

    @Override
    public List<List<Integer>> waitForOpponentTurn(String id) {
        try {
            while ((!id.equals(whoseTurn()) && winnerId == null)) {
                Thread.sleep(100);

            }
//            if (id.equals(BLACK)) {
//                System.out.println("Waiting for Player WHITE");
//                while (whiteTurnsCount <= blackTurnsCount + 1 && winnerId == null) {
//                    Thread.sleep(100);
//                }
//            } else {
//                System.out.println("Waiting for Player BLACK");
//                while ( whiteTurnsCount + 1 > blackTurnsCount && winnerId == null) {
//                    Thread.sleep(100);
//                }
//            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return list;
    }

    private String whoseTurn() {
        return (whiteTurnsCount + 1 == blackTurnsCount || whiteTurnsCount == blackTurnsCount) && blackTurnsCount != 0 ? WHITE : BLACK;
    }

}
