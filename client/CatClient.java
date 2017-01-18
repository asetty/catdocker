import java.util.Set;
import java.util.TreeSet;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.InputStreamReader;
import java.util.Date;
import java.lang.InterruptedException;

public class CatClient {

  private static final String END_SIG = "END";

	public static void main(String[] args) throws IOException, UnknownHostException {
		Set<String> lines = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
		BufferedReader bf = new BufferedReader(new FileReader(args[0]));
		String line;
		while((line = bf.readLine()) != null) {
			lines.add(line);
		}

		Socket socket = new Socket(args[1], Integer.parseInt(args[2]));
		BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		PrintWriter out = new PrintWriter(socket.getOutputStream());
		


		new LineResponseThread(input, lines, socket).start();

		try {
			Date end = null;
			Date start = new Date();
			do{
				out.write("LINE\n");
				out.flush();
				Thread.sleep(3000);
				end = new Date();
			} while ((end.getTime() - start.getTime()) < 30000);
			
      out.write(END_SIG +"\n");
      out.flush();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

  private static class LineResponseThread extends Thread {

    Set<String> lines;
    BufferedReader br;
    Socket socket;

    public LineResponseThread(BufferedReader br, Set<String> lines, Socket socket) {
      this.br = br;
      this.lines = lines;
      this.socket = socket;
    }

    @Override
    public void run() {
      try {
        String response = null;
        while(true) {
          response = br.readLine();

          if(response != null) {
            System.out.println("response: " + response);
            if(response.equals(CatClient.END_SIG)) {
              System.out.println("CLOSING CONNECTION TO HOST: " + 
                                  socket.getInetAddress().getHostName());
              socket.close();
              break;
            }
            if(lines.contains(response)) {
              System.out.println("OK");
            } else {
              System.out.println("MISSING");
            }
          } else {
              break;
          }
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    
  }
}

