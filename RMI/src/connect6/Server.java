package connect6;

import javafx.util.Pair;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server {
    public static void main(String[] args) {
        try {
            GameEngineImpl obj = new GameEngineImpl();
            obj.makeTurn(new Pair<>(1,3), "BLACK");
            GameEngine stub = (GameEngine) UnicastRemoteObject.exportObject(obj, 0);

            // Bind the remote object's stub in the registry
            Registry registry = null;
            registry = LocateRegistry.createRegistry(2020);
            registry.bind("GameEngine", stub);
            System.err.println("Server ready");
            Thread.sleep(Integer.MAX_VALUE);
        } catch (RemoteException | InterruptedException | AlreadyBoundException e) {
            e.printStackTrace();
        }
    }
}
