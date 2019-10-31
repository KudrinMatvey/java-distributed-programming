package connect6;

import javafx.util.Pair;

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
    public Boolean makeTurn(Pair<Integer, Integer> point, String id) {
        synchronized (obj) {
            int x = point.getKey();
            int y = point.getValue();
            Integer a = list.get(x).get(y);
            if (a != 0) return false;
            if (id.equals(BLACK)) {
                list.get(x).set(y, BLACK_ID);
                winnerId = checkForWin(x, y, BLACK_ID) ? BLACK : null;
                blackTurnsCount++;
            } else {
                list.get(x).set(y, WHITE_ID);
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
            if (count == 6) return true;
            count = list.get(x).get(y1) == id ? count + 1 : 0;
        }
        count = 0;
        for (int x1 = startX; x1 < finishX; x1++) {
            if (count == 6) return true;
            count = list.get(x).get(x1) == id ? count + 1 : 0;
        }
        count = 0;

        for (int xy = startX; xy < finishX; xy++) {
            if (count == 6) return true;
            count = list.get(xy).get(xy) == id ? count + 1 : 0;
        }
        count = 0;

        return false;
    }

    @Override
    public String didAnyoneWin(String id) {
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
            if (id.equals(BLACK)) {
                while (whiteTurnsCount < blackTurnsCount) {
                    Thread.sleep(100);
                }
            } else {
                while (whiteTurnsCount == blackTurnsCount) {
                    Thread.sleep(100);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return list;
    }

}
