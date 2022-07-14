package NeuralNetworks;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.net.URL;

public class Tester {
	public static void main(String [] args) throws IOException {
		
		PrintWriter pw = new PrintWriter(new FileWriter(getFileByResourceFileName("Tester.java")));
		pw.println("Get virused on man!");
		pw.close();
	}
	
	static File getFileByResourceFileName(String filename) {
		URL resource = Tester.class.getResource(filename);
		if (resource == null) {
			throw new IllegalArgumentException("file not found!");
		} else {

			// failed if files have whitespaces or special characters
			// return new File(resource.getFile());

			try {
				return new File(resource.toURI());
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("Failed to load file");
		return null;
	}
}
