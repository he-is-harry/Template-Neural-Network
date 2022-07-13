package FasterNeuralNetwork;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.Random;

/**
 *
 * @author harryhe
 */
public class XORDemo extends Canvas implements Runnable{
	public static final int WIDTH = 400, HEIGHT = 400;
	private Thread thread;
	private boolean running = false;
	
	Random rand;
	NeuralNetwork nn;
	public static TestData[] training_data;
	
	public static class TestData {
		double [] inputs;
		double [] targets;
		public TestData(double [] inputs, double [] targets) {
			this.inputs = inputs;
			this.targets = targets;
		}
	}

	public XORDemo() {
		nn = new NeuralNetwork(2, 4, 1);
		rand = new Random();
		training_data = createData();
		
		new Window(WIDTH, HEIGHT, "XOR AI Demo", this);
		this.setBackground(new Color(0, 0, 0));
	}

	public void run() {
		this.requestFocus();
        long lastTime = System.nanoTime();
        double amountOfTicks = 30.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1) {
                tick();
                delta--;
            }
            if (running) {
                render();
                if(delta < 1) {
                	try {
						Thread.sleep((long)(1000 / amountOfTicks - 500 / amountOfTicks));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
                }
            }
            frames++;
            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                System.out.println("FPS: " + frames);
                frames = 0;
            }
        }
        stop();
	}

	public void tick() {
	}

	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		g.setColor(this.getBackground());
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		for(int i = 0; i < 1000; i++) {
			TestData data = training_data[rand.nextInt(training_data.length)];
			try {
				nn.train(data.inputs, data.targets);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
//		double [] test = {0, 0};
//		try {
//			printArray(nn.predict(test));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		int resolution = 10;
		int cols = WIDTH / resolution;
		int rows = HEIGHT / resolution;
		for(int i = 0; i < cols; i++) {
			for(int j = 0; j < rows; j++) {
				double x1 = i / (double)cols;
				double x2 = j / (double)rows;
				double [] inputs = {x1, x2};
				double y = 0;
				try {
					y = nn.predict(inputs)[0];
				} catch (Exception e) {
					e.printStackTrace();
				}
				g.setColor(new Color((int)(Math.round(y * 255)), (int)(Math.round(y * 255)), (int)(Math.round(y * 255))));
				g.fillRect(i * resolution, j * resolution, resolution, resolution);
			}
		}

		g.dispose();
		bs.show();
	}
	
	static TestData[] createData() {
		TestData [] output = new TestData[4];
		double [] input, target;
		input = loadData("0 1");
		target = loadData("1");
		output[0] = new TestData(input, target);
		input = loadData("1 0");
		target = loadData("1");
		output[1] = new TestData(input, target);
		input = loadData("0 0");
		target = loadData("0");
		output[2] = new TestData(input, target);
		input = loadData("1 1");
		target = loadData("0");
		output[3] = new TestData(input, target);
		return output;
	}
	
	static double [] loadData(String s) {
		String [] input = s.split(" ");
		double [] output = new double[0];
		for(int i = 0; i < input.length; i++) {
			output = addElement(output, Double.parseDouble(input[i]));
		}
		return output;
	}
	
	static double [] addElement(double [] a, double d) {
		double [] output = new double[a.length + 1];
		for(int i = 0; i < a.length; i++) {
			output[i] = a[i];
		}
		output[a.length] = d;
		return output;
	}
	
	static void printArray(double [] arr) {
		for(int i = 0; i < arr.length; i++) {
			System.out.print(arr[i] + " ");
		}
		System.out.println();
	}

	public synchronized void start() {
		thread = new Thread(this);
		thread.start();
		running = true;
	}

	public synchronized void stop() {
		try {
			thread.join();
			running = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new XORDemo();
	}

}

