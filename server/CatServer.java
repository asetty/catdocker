import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;

public class CatServer {
	private ServerSocket serverSocket;
	private String filename;
	private int port;
	private static final String LN_RQST = "LINE";
  public static final String END_SIG = "END";

	public CatServer(String filename, int port) {
		this.filename = filename;
		this.port = port;
	}

	public static void main(String[] args) throws IOException { 
		CatServer cs = new CatServer(args[0], Integer.parseInt(args[1]));
		cs.run();

	}

	public void run() throws IOException {
		serverSocket = new ServerSocket(port);
			serverSocket.getInetAddress().getHostName();
		BufferedReader fileBuffer = null;

		try {
			fileBuffer = new BufferedReader(new FileReader(filename));
			while(true) {
				Socket socket = serverSocket.accept();
				BufferedReader in = new BufferedReader(
					new InputStreamReader(socket.getInputStream()));
				PrintWriter out = new PrintWriter(socket.getOutputStream());
				
				String input;
				String line;
				while((input = in.readLine()) != null) {

					if(input.equals(LN_RQST)) {

						if((line = fileBuffer.readLine()) != null) {
							out.write(line.toUpperCase() + "\n");
							out.flush();
						} else {
							break;
						}

					} else if(input.equals(END_SIG)) {
              out.write(END_SIG + "\n");
              out.flush();
          }
              

				}
				in.close();
			}
		} finally {
			serverSocket.close();
			if(fileBuffer != null) {
				fileBuffer.close();
			}
		}



	}
} 
