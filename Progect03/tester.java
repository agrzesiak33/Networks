import java.io.IOException;
import java.util.Scanner;

public class tester {
	
	public static void main(String args[])
	{
		GenericServer server = new GenericServer(5589);
		server.run();
		
		Scanner scanner = new Scanner(System.in);
		try {
			server.stop();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		scanner.close();
	}
}
