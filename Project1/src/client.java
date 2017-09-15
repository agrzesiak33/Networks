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
		String request = "C:/Users/agrze/Documents/Fall 2017/CS - 460 Networks/Projects/Project1/src/test.txt";
		int port = 5678;
		int chunkSize = 20;
		String ip =  "127.0.0.1";
		
		Socket socket = new Socket(ip, port);
		InputStream fromServer = socket.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		DataInputStream dataIn = new DataInputStream(socket.getInputStream());
		DataOutputStream dataOut = new DataOutputStream(socket.getOutputStream());
		
		PrintStream ps = new PrintStream(socket.getOutputStream());
		//ps.println(request);
		dataOut.writeUTF(request);
		
		byte[] buffer = new byte[chunkSize];
		//FileOutputStream out = new FileOutputStream(request);
		while(fromServer.read(buffer) >= 0)
		{
			System.out.println(Arrays.toString(buffer));
		}
		
		socket.close();
		reader.close();
		dataIn.close();
		ps.close();
		//out.close();
		
	}
	
}
