import java.io.IOException;
import java.util.Scanner;

public class StartServer {
	
	public static void main(String args[])
	{
		GenericServer server = new GenericServer(5589);
		server.run();
		
		try {
			server.stop();
		} catch (Exception e) {}
	}
}
