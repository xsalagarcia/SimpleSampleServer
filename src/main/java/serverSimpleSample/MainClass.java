package serverSimpleSample;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * Just a simple server, here the main class.
 * Creates n WorkerThread instances. WorkerThread instances have the linkedBlockingQueue reference.
 * Opens a port and wait for some communication. When communication arrives, the new socket is put
 * to the linkedBlockingQueue. Any available workerThread will deal with the communication.
 * @author xsala
 *
 */
public class MainClass {

	final static int WORKER_THREADS = 10; //the number of created connection threads
	static Properties properties = null;
	private static boolean hasToStop = false;
	private static LinkedBlockingQueue<Socket> linkedBlockingQueue = new LinkedBlockingQueue<Socket>();
	private static ArrayList<WorkerThread> workers = new ArrayList<WorkerThread>(WORKER_THREADS);
	
	
	public static void main(String[] args) {


		for (int i= 0; i<WORKER_THREADS; i++) {
			workers.add(i, new WorkerThread(linkedBlockingQueue, i));
			new Thread (workers.get(i)).start();
		}
		
		properties = getProperties();
		
		
		try {
			ServerSocket serverSocket = new ServerSocket(Integer.parseInt(properties.getProperty("port", "7878")));
			while (true) {
				Socket socket = serverSocket.accept();

				linkedBlockingQueue.add(socket);
				

				
			}
		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	
	
	
	
	private static Properties setFirstTimeProperties() throws IOException {
		Files.createDirectories(new File("resources").toPath());
		Properties p = new Properties();
		p.setProperty("port", "7878");
		p.setProperty("codeAut", "some code auth");
		FileWriter fw = new FileWriter("resources/values.properties", Charset.forName("UTF-8"));
		p.store(fw, "Properties file");
		fw.close();
		return p;
	}
	
	
	private static void LoadProperties () throws IOException {
		Properties p = new Properties();
		FileReader fr = new FileReader("resources/values.properties", Charset.forName("UTF-8"));
		p.load(fr);
		fr.close();
		properties = p;

	}
	
	private static Properties getProperties() {
		if (properties == null) {
			try {
				LoadProperties();
			} catch (IOException e) {
				try {
					setFirstTimeProperties();
					LoadProperties();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		
		return properties;
	}

}
