package src.connect6;

import javafx.util.Pair;

import java.util.List;

public class GameEngineImpl implements GameEngine {
    private static final String BLACK = "BLACK";
    private static final String WHITE = "WHITE";
    private static Integer playersInGame = 0;
    private static List<List<Integer>> list;
    private final Object obj = new Object();

    public GameEngineImpl() {
    }

    @Override
    public String getId() {
        String res;
        synchronized (obj) {
            switch (playersInGame++){
                case 0: res = BLACK; break;
                case 1: res = WHITE; break;
                default: return null;
            }
        }
        if(res.equals(BLACK)) {
            while (playersInGame != 2){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }
        return res;
    }

    @Override
    public Boolean makeTurn(Pair<Integer, Integer> point, String id) {
        return null;
    }

    @Override
    public Integer didAnyoneWin(String id) {
        return null;
    }

    @Override
    public Pair<Integer, Integer> waitForOpponentTurn() {
        return null;
    }
}
