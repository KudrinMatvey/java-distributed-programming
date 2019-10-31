package connect6;

import javafx.util.Pair;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface GameEngine extends Remote {
    String getId() throws RemoteException;
    Boolean makeTurn(Pair<Integer, Integer> point, String id) throws RemoteException;
    String didAnyoneWin(String id) throws RemoteException;
    List<List<Integer>> waitForOpponentTurn(String id) throws RemoteException;
}
