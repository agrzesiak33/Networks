import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.util.Random;
import java.net.InetAddress;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

public class UDPServer
{
   private static final double LOSS_RATE = 4;
   private static final int AVERAGE_DELAY = 500;

   public static void main(String[] args) throws Exception
   {
      int port = 7777;

      Random generator = new Random();

      DatagramSocket serverSocket = new DatagramSocket(port);

      while (true)
      {
        int random = generator.nextInt(10) + 1;

        DatagramPacket request = new DatagramPacket(new byte[1024], 1024);

        serverSocket.receive(request);

        printData(request);

        if (random < LOSS_RATE)
        {
          System.out.println("   Reply not sent.");
          continue;
        }

        Thread.sleep(random * 2 * AVERAGE_DELAY);

        InetAddress clientHost = request.getAddress();
        int clientPort = request.getPort();
        byte[] buf = request.getData();
        DatagramPacket reply = new DatagramPacket(buf, buf.length, clientHost, clientPort);
        serverSocket.send(reply);

        System.out.println("   Reply sent.");
      }
   }

   private static void printData(DatagramPacket request) throws Exception
   {
      byte[] buf = request.getData();

      ByteArrayInputStream bais = new ByteArrayInputStream(buf);

      InputStreamReader isr = new InputStreamReader(bais);

      BufferedReader br = new BufferedReader(isr);

      String line = br.readLine();

      System.out.println("Received from " +
                         request.getAddress().getHostAddress() +
                         ": " + new String(line) );
   }
}
