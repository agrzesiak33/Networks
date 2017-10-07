import java.io.*;
import java.net.*;
import java.util.*;

public class smtp {

	@SuppressWarnings({ "resource", "unused" })
	public static void main(String args[]) throws UnknownHostException, IOException
	{
		String message = "\r\nHere is my test message";
		String mailServer = "";
		String hostName = "";
		String from = "";
		String to = "";
		if(args.length == 0)
		{
			mailServer = "localHost";
			hostName = "alex-VirtualBox";
			from = "alex@alex-VirtualBox";
			to = "alex@alex-VirtualBox";
		}
		else if(args.length == 4)
		{
			mailServer = args[0];
			hostName = args[1];
			from = args[2];
			to = args[3];
		}
		else
		{
			System.out.println("Enter 4 arguements.");
			System.out.println("1) Mail Server");
			System.out.println("2) Host Name");
			System.out.println("3) From email address");
			System.out.println("4) To email address");
			return;
		}

		System.out.print("Testing with the following paramaters: \n");
		System.out.println("Mail Server:	" + mailServer);
		System.out.println("Host Name:		" + hostName);
		System.out.println("From:			" + from);
		System.out.println("To: 			" + to);					
		
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
		
		//	Send HELO command and print response
		command = "HELO " + hostName + "\r\n";
		System.out.print(command);
		toMailServer.write(command.getBytes());		
		input = fromMailServer.readLine();
		System.out.println("After HELO: " + input + "\n");
		if(!input.startsWith("250"))
		{
			System.out.println("250 reply not received from server.");
		}
		
		//	Send MAIL FROM command and print response.
		command = "MAIL From: < " + from + ">\r\n";
		System.out.print(command);
		toMailServer.write(command.getBytes());
		input = fromMailServer.readLine();
		System.out.println("After MAIL From: " + input + "\n");
		if(!input.startsWith("250"))
		{
			System.out.println("250 reply not received from server.");
		}
		
		//Send RCPT TO command and print response.
		command = "RCPT TO:< " + to + ">\r\n";
		System.out.print(command);
		toMailServer.write(command.getBytes());
		input = fromMailServer.readLine();
		System.out.println("After RCPT TO: " + input + "\n");
		if(!input.startsWith("250"))
		{
			System.out.println("250 reply not received from server.");
		}
		
		//	Send the DATA command and print response.
		command = "DATA\r\n";
		System.out.print(command);
		toMailServer.write(command.getBytes());
		input = fromMailServer.readLine();
		System.out.println("After DATA: " + input + "\n");
		if(!input.startsWith("354"))
		{
			System.out.println("354 reply not received from server.");
		}

		//	Send the SUBJECT
		command = "SUBJECT:  Here is my subject\r\n";
		System.out.print(command);
		toMailServer.write(command.getBytes());

		//	Send the body of the email and the ending period and print the response
		toMailServer.write(message.getBytes());
		command = "\r\n.\r\n";
		toMailServer.write(command.getBytes());
		input = fromMailServer.readLine();
		System.out.println(input);
		if(!input.startsWith("250"))
		{
			System.out.println("250 reply not received from server.");
		}
		
		//	Send QUIT and print the response
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
