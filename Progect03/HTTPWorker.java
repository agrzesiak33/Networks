import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class HTTPWorker implements Runnable
{
	protected Socket clientSocket;
	
	public HTTPWorker(Socket socket)
	{
		this.clientSocket = socket;
	}
	
	
	@Override
	public void run() {
		String header200 = "HTTP/1.1 200 OK\r\n\r\n";
		String header400 = "HTTP/1/1 400 Invalid Request\r\n\r\n";
		String header404 = "HTTP/1.1 404 Not Found\r\n\r\n";
		
		OutputStream out = null;
		InputStream dataIn = null;
		BufferedInputStream fileInput = null;
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter("output.txt"));
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try
		{
			// open data streams for input and output
			dataIn = clientSocket.getInputStream();
			out = clientSocket.getOutputStream();
			
			
			System.out.println("Waiting for the request");
			byte[] buffer = new byte[2000];
			int request = dataIn.read(buffer);
			
			for(int i = 0; i<request; i++)
				writer.write(buffer[i]);
			writer.close();
			//System.out.println("Request: " + buffer.);
			//System.out.println("Opening the file");
			fileInput = new BufferedInputStream(new FileInputStream("page.html"));
			
			out.write(header200.getBytes());
			out.flush();
			
			System.out.println("Outputting the file.");
			buffer = new byte[2000];
			@SuppressWarnings("unused")
			int fileLength;
			while((fileLength = fileInput.read(buffer)) >= 0)
			{
				out.write(buffer);
				out.flush();
			}	
		}
		catch(FileNotFoundException e)
		{
			try {
				System.out.println("Could not find the file");
				out.write(header404.getBytes());
				out.flush();
				
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} 
		catch (IOException e) {
			try {
				out.write(header400.getBytes());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		System.out.println("All done");
		try {
			dataIn.close();
			out.close();
			fileInput.close();
		} catch (Exception e) {

		}
		
	}

}
