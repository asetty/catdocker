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

	public static void main(String[] args) throws IOException, UnknownHostException {
		Set<String> lines = new TreeSet<String>();
		BufferedReader bf = new BufferedReader(new FileReader(args[0]));
		String line;
		while((line = bf.readLine()) != null) {
			lines.add(line);
		}

		Socket socket = new Socket(args[1], Integer.parseInt(args[2]));
		BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		PrintWriter out = new PrintWriter(socket.getOutputStream());
		


		new Thread() {
			@Override
			public void run() {
				System.out.println("in run method");
				try {
					String response = null;
					while(true) {
						System.out.println("in run loop");
						response = input.readLine();

						if(response != null) {
							System.out.println("response: " + response);
							if(lines.contains(response)) {
								System.out.println("OK");
							} else {
								System.out.println("MISSING");
							}
						} else {
							System.out.println("about to yield");
							Thread.yield();
						}
						System.out.println("going to top of loop");
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();

		System.out.println("before write");
		try {
			Date end = null;
			Date start = new Date();
			do{
				out.write("LINE\n");
				out.flush();
				Thread.sleep(3000);
				end = new Date();
			} while ((end.getTime() - start.getTime()) < 30000);
			
			System.out.println("after write");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
}