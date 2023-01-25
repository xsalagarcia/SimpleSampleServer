package serverSimpleSample;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Properties;

public class Starter {

	private static Properties properties;

	
	public static void main(String[] args) {

		
		Server s = new Server(7878, 10);
		
		
		new Thread(s).start();
		try {
			Thread.sleep(6000); //the server will run for n milliseconds
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		s.stop();
		System.out.println("Server turned off");

		

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


