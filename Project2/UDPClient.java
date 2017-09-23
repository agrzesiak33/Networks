import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.Date;
import java.io.IOException;

public class UDPClient
{
	private static final int MAX_TIMEOUT = 1000;

	public static void main(String[] args) throws Exception
	{
		if (args.length != 2)
		{
			System.out.println("Missing arguments");
			return;
		}

		int port = Integer.parseInt(args[1]);
		InetAddress host;
		host = InetAddress.getByName(args[0]);

		DatagramSocket clientSocket = new DatagramSocket(port);

		int sequence_number = 0;
		while (sequence_number < 10)
		{
			Date timeNow = new Date();
			long msAtSend = timeNow.getTime();
			String pingString = "PING " + sequence_number + " \n";
			byte[] buf = new byte[1024];
			buf = pingString.getBytes();
			DatagramPacket ping = new DatagramPacket(buf, buf.length, host, port);

			clientSocket.send(ping);
			try
			{
				clientSocket.setSoTimeout(MAX_TIMEOUT);
				DatagramPacket response = new DatagramPacket(new byte[1024], 1024);
				clientSocket.receive(response);

				timeNow = new Date();
				long msAtReceived = timeNow.getTime();
				printData(response, msAtReceived - msAtSend);
			}
			catch (IOException e)
			{
				System.out.println("Timeout for packet " + sequence_number);
			}
			sequence_number ++;
		}
	}

   private static void printData(DatagramPacket request, long delayTime)
	 throws Exception
   {
      byte[] buf = request.getData();

      ByteArrayInputStream bais = new ByteArrayInputStream(buf);

      InputStreamReader isr = new InputStreamReader(bais);

      BufferedReader br = new BufferedReader(isr);

      String line = br.readLine();

      System.out.println("Received from " +
												 request.getAddress().getHostAddress() + ": " +
												 new String(line) + " Delay: " + delayTime );
   }
}
