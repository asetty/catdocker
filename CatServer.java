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
		System.out.println("hostname: " + 
			serverSocket.getInetAddress().getHostName());
		BufferedReader fileBuffer = null;

		try {
			fileBuffer = new BufferedReader(new FileReader(filename));
			while(true) {
				Socket socket = serverSocket.accept();
				System.out.println("after accept()");
				BufferedReader in = new BufferedReader(
					new InputStreamReader(socket.getInputStream()));
				PrintWriter out = new PrintWriter(socket.getOutputStream());
				
				String input;
				String line;
				System.out.println("before loop");
				while((input = in.readLine()) != null) {

					System.out.println(input);
					if(input.equals(LN_RQST)) {
						System.out.println("got line request");

						if((line = fileBuffer.readLine()) != null) {
							System.out.println("sending: " + line);
							out.write(line + "\n");
							out.flush();
						} else {
							break;
						}

					}

				}
				System.out.println("after loop");
				in.close();
				System.out.println("closed reader");
			}
		} finally {
			serverSocket.close();
			if(fileBuffer != null) {
				fileBuffer.close();
			}
		}



	}
} 