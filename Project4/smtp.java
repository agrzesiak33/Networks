import java.io.*;
import java.net.*;
import java.util.*;

public class smtp {

	@SuppressWarnings({ "resource", "unused" })
	public static void main(String args[]) throws UnknownHostException, IOException
	{
		String message = "\r\nHere is my test message";
		
		String mailServer = "localhost";
		String hostName = "alex-VirtualBox";
		String from = "agrzesiak@hotmail.com";
		String to = "agrzesiak@hotmail.com";
		
		Socket mailSocket;
		InputStream inputStream;
		OutputStream toMailServer;
		BufferedReader fromMailServer;

		String command;
		
		mailSocket = new Socket(mailServer, 25);
		System.out.println("Connected to the server.");;
		
		inputStream = mailSocket.getInputStream();
		toMailServer = mailSocket.getOutputStream();
		fromMailServer = new BufferedReader(new InputStreamReader(inputStream));

		String input = fromMailServer.readLine();
		System.out.println("From server: " + input + "\n");
		
		command = "HELO " + hostName + "\r\n";
		System.out.print(command);
		toMailServer.write(command.getBytes());		
		input = fromMailServer.readLine();
		System.out.println("After HELO: " + input + "\n");
		if(!input.startsWith("250"))
		{
			System.out.println("250 reply not received from server.");
		}
		
		command = "MAIL From: < " + from + ">\r\n";
		System.out.print(command);
		toMailServer.write(command.getBytes());
		input = fromMailServer.readLine();
		System.out.println("After MAIL From: " + input + "\n");
		if(!input.startsWith("250"))
		{
			System.out.println("250 reply not received from server.");
		}
		
		command = "RCPT TO:< " + to + ">\r\n";
		System.out.print(command);
		toMailServer.write(command.getBytes());
		input = fromMailServer.readLine();
		System.out.println("After RCPT TO: " + input + "\n");
		if(!input.startsWith("250"))
		{
			System.out.println("250 reply not received from server.");
		}
		
		command = "DATA\r\n";
		System.out.print(command);
		toMailServer.write(command.getBytes());
		input = fromMailServer.readLine();
		System.out.println("After DATA: " + input + "\n");
		if(!input.startsWith("354"))
		{
			System.out.println("354 reply not received from server.");
		}

		command = "SUBJECT:  Here is my subject\r\n";
		System.out.print(command);
		toMailServer.write(command.getBytes());
		toMailServer.write(message.getBytes());
		command = "\r\n.\r\n";
		toMailServer.write(command.getBytes());
		
		input = fromMailServer.readLine();
		System.out.println(input);
		if(!input.startsWith("250"))
		{
			System.out.println("250 reply not received from server.");
		}
		
		command = "QUIT\r\n";
		System.out.print(command);		
		toMailServer.write(command.getBytes());
		input = fromMailServer.readLine();
		System.out.println("After QUIT: " + input);
		if(!input.startsWith("221"))
		{
			System.out.println("221 reply not received from receiver");
		}
		
	}
}
