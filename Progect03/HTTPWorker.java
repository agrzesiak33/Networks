import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
		try
		{
			// open data streams for input and output
			dataIn = clientSocket.getInputStream();
			out = clientSocket.getOutputStream();
			
			//	Read in the GET request coming from the client
			byte[] buffer = new byte[2000];
			dataIn.read(buffer);
			String firstLine =  new String(buffer);
			
			// 	The file name we want is the 2nd word of the first line with the first char excluded
			String fileName = firstLine.split("\n")[0].split(" ")[1].substring(1);
			
			fileInput = new BufferedInputStream(new FileInputStream(fileName));
			
			//	Let the client know everything is ok and the data is coming.
			out.write(header200.getBytes());
			out.flush();
			
			//	Send the data to the client in 2k chunks
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
				System.out.println("Could not find the file. " + e.getLocalizedMessage());
				out.write(header404.getBytes());
				out.flush();
			} catch (IOException e1) {}
		} 
		catch (IOException e) {
			try {
				out.write(header400.getBytes());
			} catch (IOException e1) {}
		}
		System.out.println("All done");
		try {
			dataIn.close();
			out.close();
			fileInput.close();
		} catch (Exception e) {}
		
	}

}
