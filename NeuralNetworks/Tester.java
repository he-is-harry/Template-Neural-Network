package NeuralNetworks;

public class Tester {
	public static void main(String [] args) {
		Equation e = new Equation("(x^5) - (x^3) + 2");
		for(int i = -10; i < 10; i++) {
			System.out.println(e.f(i) == Math.pow(i, 5) - Math.pow(i, 3) + 2);
		}
	}
}
