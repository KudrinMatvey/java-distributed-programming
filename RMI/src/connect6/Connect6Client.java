package src.connect6;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Connect6Client {

    private Connect6Client() {}

    public static void main(String[] args) {

        Integer host = 2020;
        try {
            Registry registry = LocateRegistry.getRegistry(host);
            GameEngine stub = (GameEngine) registry.lookup("GameEngine");
            System.out.println(stub);

            String response = stub.getId();
            System.out.println("response:" + response);
//            System.out.println("response:\nSum="+ stub.summa(190,7));

        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }

    }
}