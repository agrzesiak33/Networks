import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class smtp {

	@SuppressWarnings({ "resource", "unused" })
	public static void main(String args[]) throws UnknownHostException, IOException
	{
		String message = "\r\nHere is my test message";
		
		String mailServer = "$@localhost";
		String hostName = "someName";
		String from = "someFrom";
		String to = "somtTo";
		
		Socket mailSocket;
		InputStream inputStream;
		OutputStream outputStream;
		BufferedReader fromMailServer;
		PrintWriter toMailServer;
		
		mailSocket = new Socket(mailServer, 6789);
		
		inputStream = mailSocket.getInputStream();
		outputStream = mailSocket.getOutputStream();
		fromMailServer = new BufferedReader(new InputStreamReader(inputStream));
		toMailServer = new PrintWriter(new OutputStreamWriter(outputStream));
		
		System.out.println("HELO " + hostName);
		toMailServer.println("HELO " + hostName);
		
		String input = fromMailServer.readLine();
		System.out.println(input);
		if(input.substring(0,3) != "250")
		{
			System.out.println("250 reply not received from server");
		}
		
		System.out.println("MAIL From:< " + from + ">");
		toMailServer.println("MAIL From:< " + from + ">");
		
		input = fromMailServer.readLine();
		System.out.println(input);
		if(input.substring(0,3) != "250")
		{
			System.out.println("250 reply not received from server");
		}
		
		System.out.println("RCPT TO:< " + to + ">");
		toMailServer.println("RCPT TO:< " + to + ">");
		
		input = fromMailServer.readLine();
		System.out.println(input);
		if(input.substring(0,3) != "250")
		{
			System.out.println("250 reply not received from server");
		}
		
		System.out.println("DATA");
		toMailServer.println("DATA");
		
		input = fromMailServer.readLine();
		System.out.println(input);
		if(input.substring(0,3) != "354")
		{
			System.out.println("354 reply not received from server");
		}

		toMailServer.println("SUBJECT: my subject");
		toMailServer.println(message);
		toMailServer.println(".");

		input = fromMailServer.readLine();
		System.out.println(input);
		if(input.substring(0,3) != "250")
		{
			System.out.println("250 reply not received from server");
		}
		
		System.out.println("QUIT");
		toMailServer.println("QUIT");

		input = fromMailServer.readLine();
		System.out.println(input);
		if(input.substring(0,3) != "221")
		{
			System.out.println("221 reply not received from server");
		}
		
	}
}
