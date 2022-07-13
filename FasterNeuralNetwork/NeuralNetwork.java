package FasterNeuralNetwork;

public class NeuralNetwork {
	int input_nodes;
	int hidden_nodes;
	int output_nodes;
	
	Matrix weights_ih;
	Matrix weights_ho;
	Matrix bias_h;
	Matrix bias_o;
	
	double learningRate;
	public NeuralNetwork(int input_nodes, int hidden_nodes, int output_nodes) {
		this.input_nodes = input_nodes;
		this.hidden_nodes = hidden_nodes;
		this.output_nodes = output_nodes;
		
		weights_ih = new Matrix(this.hidden_nodes, this.input_nodes);
		weights_ho = new Matrix(this.output_nodes, this.hidden_nodes);
		weights_ih.randomize();
		weights_ho.randomize();
		
		bias_h = new Matrix(this.hidden_nodes, 1);
		bias_o = new Matrix(this.output_nodes, 1);
		bias_h.randomize();
		bias_o.randomize();
		
		learningRate = 0.1;
	}
	
	double [] predict(Matrix input) throws Exception {
		Matrix hidden = Matrix.multiply(weights_ih, input);
		hidden.add(bias_h);
		sigmoidMap(hidden);
		
		Matrix output = Matrix.multiply(weights_ho, hidden);
		output.add(bias_o);
		sigmoidMap(output);
		
		return output.toArray();
	}
	
	double [] predict(double [] input_array) throws Exception {
		Matrix input = Matrix.fromArray(input_array);
		Matrix hidden = Matrix.multiply(weights_ih, input);
		hidden.add(bias_h);
		sigmoidMap(hidden);
		
		Matrix output = Matrix.multiply(weights_ho, hidden);
		output.add(bias_o);
		sigmoidMap(output);
		
		return output.toArray();
	}
	
	void train(double [] input_array, double [] target_array) throws Exception {
		Matrix inputs = Matrix.fromArray(input_array);
		Matrix hidden = Matrix.multiply(weights_ih, inputs);
		hidden.add(bias_h);
		sigmoidMap(hidden);
		
		Matrix outputs = Matrix.multiply(weights_ho, hidden);
		outputs.add(bias_o);
		sigmoidMap(outputs);
		
		// Calculate the output error and change the 
		// weights of the hidden - output by the 
		// derivative of the outputs * error * learning rate * weight of hidden node
		Matrix targets = Matrix.fromArray(target_array);
		Matrix output_errors = Matrix.subtract(targets, outputs);
		
		Matrix gradients = dsigmoidMap(outputs);
		gradients.multiply(output_errors);
		gradients.multiply(learningRate);
		
		Matrix hidden_T = Matrix.transpose(hidden);
		Matrix weight_ho_deltas = Matrix.multiply(gradients, hidden_T);
		
		weights_ho.add(weight_ho_deltas);
		bias_o.add(gradients);
		
		Matrix who_t = Matrix.transpose(weights_ho);
		Matrix hidden_errors = Matrix.multiply(who_t, output_errors);
		
		Matrix hidden_gradient = dsigmoidMap(hidden);
		hidden_gradient.multiply(hidden_errors);;
		hidden_gradient.multiply(learningRate);
		
		Matrix inputs_T = Matrix.transpose(inputs);
		Matrix weight_ih_deltas = Matrix.multiply(hidden_gradient, inputs_T);
		
		weights_ih.add(weight_ih_deltas);
		bias_h.add(hidden_gradient);
	}
	
	double sigmoid(double x) {
		return 1 / (1 + Math.exp(-x));
	}
	
	void sigmoidMap(Matrix m) {
		for(int i = 0; i < m.rows; i++) {
			for(int j = 0; j < m.cols; j++) {
				m.data[i][j] = sigmoid(m.data[i][j]);
			}
		}
	}
	
	double dsigmoid(double y) {
		// y has already been sigmoided
		return y * (1 - y);
	}
	
//	void dsigmoidMap(Matrix m) {
//		for(int i = 0; i < m.rows; i++) {
//			for(int j = 0; j < m.cols; j++) {
//				m.data[i][j] = dsigmoid(m.data[i][j]);
//			}
//		}
//	}
	
	Matrix dsigmoidMap(Matrix m) {
		Matrix output = new Matrix(m.rows, m.cols);
		for(int i = 0; i < m.rows; i++) {
			for(int j = 0; j < m.cols; j++) {
				output.data[i][j] = dsigmoid(m.data[i][j]);
			}
		}
		return output;
	}
	
	void setLearningRate(double learningRate) {
		this.learningRate = learningRate;
	}
	
}
