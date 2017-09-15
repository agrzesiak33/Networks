import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;  
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;

public class server {

	@SuppressWarnings("unused")
	public static void main(String args[]) throws UnknownHostException, IOException
	{
		int port = 5678;
		ServerSocket serverSock = new ServerSocket(port);
		Socket socket = null;
		Scanner scanner;
		PrintStream ps = null;
		DataInputStream dataIn = null;
		DataOutputStream dataOut = null;
		FileInputStream fileIn = null;
		BufferedInputStream fileInput = null;
		File tempFile = null;
		StringBuilder header;
		
		int chunkSize = 20; 
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

		while (true)
		{
			socket = serverSock.accept();
			ps = new PrintStream(socket.getOutputStream());
			dataIn = new DataInputStream(socket.getInputStream());
			dataOut = new DataOutputStream(socket.getOutputStream());
			
			
			try
			{
				scanner = new Scanner(socket.getInputStream());
				String request = dataIn.readUTF();
				
				//	TODO Deal with if the request is invalid
				//ps.println("*400");
				//System.out.printf("Request %s is invalid \n", request);
				
				// Open the file if possible
				//fileIn = new FileInputStream(request);
				fileInput = new BufferedInputStream(new FileInputStream(request));
				//tempFile = new File(request);
				
				//TODO Send a header 
				//header = getHeader(dateFormat, timeFormat, tempFile.length());
				
				//	Break up the message into 20kb chunks
				byte[] buff = new byte[chunkSize];
				int filelength = 0;
				
				while((filelength = fileInput.read(buff)) >= 0)
				{
					System.out.println(Arrays.toString(buff));
					dataOut.write(buff);
					dataOut.flush();
					//ps.print(buff);
					//ps.flush();
				}
				//System.out.printf("Succesfully sent %s over port %d", request, port);
				
				//	Close the connections
				dataOut.close();
				fileInput.close();
				//fileIn.close();
				scanner.close();
				socket.close();
				ps.close();
				
			}
			//	Send 404 if no file with that name
			catch(FileNotFoundException e)
			{
				ps.println("*404");
				System.out.printf(e.getLocalizedMessage().toString());
			}
			catch(Exception e)
			{
				ps.println("*400");
				System.out.println("Error with request" + e.getMessage().toString());
			}
		}
		//serverSock.close();
		//socket.close();
		
	}
	
	
	private static StringBuilder getHeader(DateFormat dateFormat, DateFormat timeFormat, long fileSize)
	{
		StringBuilder header = new StringBuilder();
		header.append("Date: ");
		Date date = new Date();
		header.append(dateFormat.format(date));
		header.append('\n');
		
		header.append("Time: ");
		header.append(timeFormat.format(date));
		header.append('\n');
		
		header.append("Size: ");
		header.append(String.valueOf(fileSize));
		header.append('\n');
		
		return header;
	}
}