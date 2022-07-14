package NeuralNetworks;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class Main {
	public static void main(String [] args) throws IOException, URISyntaxException {
//		Path path = Paths.get("Tester.java");
		File pto = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI());
		System.out.println(pto.getAbsolutePath());
//		PrintWriter pw = new PrintWriter(new FileWriter());
//		pw.println("Get virused on man!");
//		pw.close();
	}
}
