package serverSimpleSample;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This class implements Runnable. The overridden run() method will take a socket from linkedBlockingQueue.
 * If there is no socket available, the thread waits for a socket.
 * @author xsala
 *
 */
public class WorkerThread implements Runnable {
	/**A reference of linkedBlockingQueue*/
	private LinkedBlockingQueue<Socket> linkedBlockingQueue;
	
	/**If has to stop the thread, it will turn true*/
	private boolean hasToStop = false;
	
	/**just for instance identification*/
	private int workerNumber;

	/**
	 * Constructor with the linkedBlockingQueue reference and an idNumber.
	 * @param linkedBlockingQueue
	 * @param workerNumber
	 */
	public WorkerThread(LinkedBlockingQueue<Socket> linkedBlockingQueue, int workerNumber) {
		this.linkedBlockingQueue = linkedBlockingQueue;
		this.workerNumber = workerNumber;
	}
	
	
	@Override
	public void run() {
		while (!hasToStop) {
			try {
				Socket socket = linkedBlockingQueue.take(); //The thread is waiting for a socket, if there is no socket.

				//startCommunication will deal with the socket.
				startCommunication (socket);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * Called when thread has a new socket to deal. Deals with the socket.
	 * @param socket
	 */
	private void startCommunication (Socket socket) {
		//Some communication.	
				DataInputStream dis;
				try {
					dis = new DataInputStream(socket.getInputStream());
					String s = dis.readUTF();
					DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
					dos.writeUTF("Response to " + s + " from worker number " + workerNumber);
					dos.close();
					dis.close();
					//communication is finished
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	}

}
