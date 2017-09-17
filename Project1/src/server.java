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
		// the port number chosen to use
		int port = 5678;
		// the socket on the server side
		ServerSocket serverSock = new ServerSocket(port);
		// the clients socket
		Socket socket = null;
		//Scanner scanner;
		//PrintStream ps = null;
		// used to get the request message
		DataInputStream dataIn = null;
		// used to output a message
		DataOutputStream dataOut = null;
		//FileInputStream fileIn = null;
		// used to hold the information from the file
		BufferedInputStream fileInput = null;
		//File tempFile = null;
		// strings for each possible header
		String header200 = "HTTP/1.1 200 OK \r\n\r\n";
		String header404 = "HTTP/1.1 404 Not Found\r\n\r\n";
		String header400 = "HTTP/1.1 400 Invalid Request";

		// hardcoded chunksize to split up the file
		int chunkSize = 20000;

		while (true)
		{
			// let the server accept a client request
			socket = serverSock.accept();
			//ps = new PrintStream(socket.getOutputStream());
			// open data streams for input and output
			dataIn = new DataInputStream(socket.getInputStream());
			dataOut = new DataOutputStream(socket.getOutputStream());


			try
			{
				// get the request message
				String request = dataIn.readUTF();
				System.out.printf("Message: %s\n", request);

				// open the file in the directory
				String fileName = request.split(" ")[1];
				fileName = fileName.substring(1, fileName.length());
				String directory = System.getProperty("user.dir");
				System.out.printf("Filename : %s", fileName);


				//	TODO Find out when the request is invalid
				// write that its a bad request to the output
				boolean isBadRequest = false;
				if(isBadRequest)
				{
					dataOut.writeUTF(header400);
					dataOut.writeUTF("<html><head></head><body><h1>400 Bad Request</h1></body></html>\r\n");
				}

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

				// keep writing the file until its been gone through completely
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
				//ps.close();

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
