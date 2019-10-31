package src.connect6;

import javafx.util.Pair;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GameEngine extends Remote {
    String getId() throws RemoteException;
    Boolean makeTurn(Pair<Integer, Integer> point, String id) throws RemoteException;
    Integer didAnyoneWin(String id) throws RemoteException;
    Pair<Integer, Integer> waitForOpponentTurn() throws RemoteException;
}
