import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

public class client
{
	public static void main(String args[]) throws IOException
	{
		// request used for writing the output
		String request = "something /test.txt";
		// the port and IP used for the client
		int port = 5678;
		String ip =  "127.0.0.1";

		// open the client socket
		Socket socket = new Socket(ip, port);
		//BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		// used to get the request message
		DataInputStream dataIn = new DataInputStream(socket.getInputStream());
		// used to output a message
		DataOutputStream dataOut = new DataOutputStream(socket.getOutputStream());

		// write the output
		dataOut.writeUTF(request);

		//	Wait for the server to send us back a response.
		String[] header = dataIn.readUTF().split(" ", 3);
		if(!header[1].equals("200"))
		{
			System.out.println(header);
			String error = dataIn.readUTF();
			System.out.println(error);
		}


		String bufferStr = new String();
		while((bufferStr = dataIn.readUTF()) != "")
		{
			System.out.println(bufferStr);
			//	TODO need to output somewhere else
		}

		socket.close();
		//reader.close();
		dataIn.close();
	}

}
