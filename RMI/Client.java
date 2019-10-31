
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {

    private Client() {}

    public static void main(String[] args) {

	Integer host = 2020;
	try {
	    Registry registry = LocateRegistry.getRegistry(host);
	    Hello stub = (Hello) registry.lookup("Hello");
	    System.out.println(stub);

	    String response = stub.sayHello();
		System.out.println("response:" + response);
	    System.out.println("response:\nSum="+ stub.summa(190,7));

	} catch (Exception e) {
	    System.err.println("Client exception: " + e.toString());
	    e.printStackTrace();
	}
	
    }
}