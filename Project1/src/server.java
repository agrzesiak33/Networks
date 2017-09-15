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
import java.nio.file.Path;
import java.nio.file.Paths;
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
		String header200 = "HTTP/1.1 200 OK \r\n\r\n";
		String header404 = "HTTP/1.1 404 Not Found\r\n\r\n";
		String header400 = "HTTP/1.1 400 Invalid Request";
		
		int chunkSize = 20; 

		while (true)
		{
			socket = serverSock.accept();
			ps = new PrintStream(socket.getOutputStream());
			dataIn = new DataInputStream(socket.getInputStream());
			dataOut = new DataOutputStream(socket.getOutputStream());
			
			
			try
			{
				String request = dataIn.readUTF();
				System.out.printf("Message: %s\n", request);
				
				String fileName = request.split(" ")[1];
				fileName = fileName.substring(1, fileName.length());
				String directory = System.getProperty("user.dir");
				System.out.printf("Filename : %s", fileName);
				
				
				//	TODO Deal with if the request is invalid
				//ps.println("*400");
				//System.out.printf("Request %s is invalid \n", request);
				
				// Open the file if possible
				String fullPath = directory + File.separator + fileName;
				fullPath = fullPath.replace("\\", "/");
				fileInput = new BufferedInputStream(new FileInputStream(fullPath));
				//C:\Users\agrze\Documents\Fall 2017\CS - 460 Networks\Networks
				
				//TODO Send a header 
				dataOut.writeUTF(header200);
				
				//	Break up the message into 20kb chunks
				byte[] buff = new byte[chunkSize];
				int filelength = 0;
				
				while((filelength = fileInput.read(buff)) >= 0)
				{
					System.out.println(Arrays.toString(buff));
					dataOut.writeUTF(new String(buff));
					//dataOut.write(buff);
					dataOut.flush();

				}
				System.out.printf("Succesfully sent %s over port %d", request, port);
				
				//	Close the connections
				dataOut.close();
				fileInput.close();
				socket.close();
				ps.close();
				
			}
			//	Send 404 if no file with that name
			catch(FileNotFoundException e)
			{
				dataOut.writeUTF(header404);
				dataOut.writeUTF("<html><head></head><body><h1>404 Not Found</h1></body></html>\r\n");
				System.out.printf(e.getMessage().toString());
			}
			catch(Exception e)
			{
				dataOut.writeUTF(header400);
				System.out.println("Error with request" + e.getMessage().toString());
			}
		}
		//serverSock.close();
		//socket.close();
		
	}
}