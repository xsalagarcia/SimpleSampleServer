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
public class Server implements Runnable{

	/**Threads to be created*/
	private int threads = 10; //the number of created connection threads
	
	/**Server port*/
	private int port = 7878;
	//static Properties properties = null;
	/**Flag for stopping */
	private volatile boolean hasToStop = false;
	
	/**LinkedBlockingQueue for socket communications*/ 
	private static LinkedBlockingQueue<Socket> linkedBlockingQueue = new LinkedBlockingQueue<Socket>();
	
	/**Array of the threads who will communicate*/
	private static ArrayList<WorkerThread> workers;
	
	/**
	 * Constructor. Defines the port and the number of worker threads.
	 * @param port
	 * @param threads
	 */
	public Server (int port, int threads) {
		this.port = port;
		this.threads = threads;
		workers = new ArrayList<WorkerThread>(threads);
	}
	

	/**
	 * When it's called, the thread of this runnable will stop.
	 */
	public void stop() {
		workers.forEach(w->w.stop());
		hasToStop = true;
	}
	
	

	@Override
	public void run() {
		
		for (int i= 0; i<threads; i++) {
			workers.add(i, new WorkerThread(linkedBlockingQueue, i));
			new Thread (workers.get(i)).start();
		}
		

		try {
			ServerSocket serverSocket = new ServerSocket(port);
			while (!hasToStop) {
				Socket socket = serverSocket.accept();

				linkedBlockingQueue.add(socket);
							
			}
			serverSocket.close();
			System.out.println("bye");
		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
