package NeuralNetworks;

public class Equation {
	String equation;

	public Equation(String equation) {
		this.equation = equation;
	}

	double f(double x) {
		double output;
		String expression = Equation.fillSpecial(equation, x);
		expression.replaceAll("\\s+", " ");
		expression = Equation.padSpaces(expression);
		int curOrder, maxOrder, indexOfHighest;
		while (true) {
//			System.out.println(expression);
			curOrder = 0;
			maxOrder = 0;
			indexOfHighest = -1;
			for (int i = 0; i < expression.length(); i++) {
				if (expression.charAt(i) == '(') {
					curOrder++;
					if (curOrder > maxOrder) {
						maxOrder = curOrder;
						indexOfHighest = i;
					}
				} else if (expression.charAt(i) == ')' && curOrder > 0) {
					curOrder--;
				}
			}
			if (maxOrder == 0) {
				break;
			}
			for (int i = indexOfHighest + 1; i < expression.length(); i++) {
				if (expression.charAt(i) == ')') {
					Equation e = new Equation(expression.substring(indexOfHighest + 1, i));
					double val = e.f(x);
					expression = expression.substring(0, indexOfHighest) + val + expression.substring(i + 1);
					break;
				}
			}
		}
		return process(expression);
	}

	double f(double x, double i, double j) {
		double output;
		String expression = Equation.fillSpecial(equation, x, i, j);
		expression.replaceAll("\\s+", " ");
		expression = Equation.padSpaces(expression);
		int curOrder, maxOrder, indexOfHighest;
		while (true) {
//			System.out.println(expression);
			curOrder = 0;
			maxOrder = 0;
			indexOfHighest = -1;
			for (int k = 0; k < expression.length(); k++) {
				if (expression.charAt(k) == '(') {
					curOrder++;
					if (curOrder > maxOrder) {
						maxOrder = curOrder;
						indexOfHighest = k;
					}
				} else if (expression.charAt(k) == ')' && curOrder > 0) {
					curOrder--;
				}
			}
			if (maxOrder == 0) {
				break;
			}
			for (int k = indexOfHighest + 1; k < expression.length(); k++) {
				if (expression.charAt(k) == ')') {
					Equation e = new Equation(expression.substring(indexOfHighest + 1, k));
					double val = e.f(x);
					expression = expression.substring(0, indexOfHighest) + val + expression.substring(k + 1);
					break;
				}
			}
		}
		return process(expression);
	}

	double process(String expression) {
		double output;
		String[] terms = expression.split(" ");
//		for(int i = 0; i < terms.length; i++) {
//			System.out.print(terms[i] + " ");
//		}
//		System.out.println();
		double curNum;
		int operation = -1;
		// 0 - Add
		// 1 - Subtract
		// 2 - Multiply
		// 3 - Divide
		// 4 - Exponent
		// x^2 + 2
		// 1 / (1 + e^-x)
		output = Double.parseDouble(terms[0]);
		for (int i = 1; i < terms.length; i++) {
			if (isNumber(terms[i])) {
				curNum = Double.parseDouble(terms[i]);
				if (operation == 0) {
					output = output + curNum;
				} else if (operation == 1) {
					output = output - curNum;
				} else if (operation == 2) {
					output = output * curNum;
				} else if (operation == 3) {
					output = output / curNum;
				} else if (operation == 4) {
					output = Math.pow(output, curNum);
				}
			} else {
				if (terms[i].equals("+")) {
					operation = 0;
				} else if (terms[i].equals("m")) {
					operation = 1;
				} else if (terms[i].equals("*")) {
					operation = 2;
				} else if (terms[i].equals("/")) {
					operation = 3;
				} else if (terms[i].equals("^")) {
					operation = 4;
				}
			}
		}
		return output;
	}

	static boolean isNumber(String str) {
		if (str.charAt(0) == '-') {
			if (str.length() == 1) {
				return false;
			} else {
				for (int i = 1; i < str.length(); i++) {
					if (!Character.isDigit(str.charAt(i)) && str.charAt(i) != '.' && str.charAt(i) != 'E') {
						return false;
					}
				}
				return true;
			}
		} else {
			for (int i = 0; i < str.length(); i++) {
				if (!Character.isDigit(str.charAt(i)) && str.charAt(i) != '.' && str.charAt(i) != 'E') {
					return false;
				}
			}
			return true;
		}
	}

	static String fillSpecial(String eq, double x) {
		String output = "";
		for (int i = 0; i < eq.length(); i++) {
			if (eq.charAt(i) == 'x') {
				output += x;
			} else if (i < eq.length() - 1 && eq.substring(i, i + 2).equals("pi")) {
				output += Math.PI;
			} else if (eq.charAt(i) == 'e') {
				output += Math.E;
			} else {
				output += eq.charAt(i);
			}
		}
		return output;
	}

	static String fillSpecial(String eq, double x, double i, double j) {
		String output = "";
		for (int k = 0; k < eq.length(); k++) {
			if (eq.charAt(k) == 'x') {
				output += x;
			} else if (eq.charAt(k) == 'i') {
				output += i;
			} else if (eq.charAt(k) == 'j') {
				output += j;
			} else if (k < eq.length() - 1 && eq.substring(k, k + 2).equals("pi")) {
				output += Math.PI;
			} else if (eq.charAt(k) == 'e') {
				output += Math.E;
			} else {
				output += eq.charAt(k);
			}
		}
		return output;
	}

	static String padSpaces(String eq) {
		eq = eq.replaceAll(" ", "");
		eq = Equation.fixMinus(eq);
		String output = "";
		String[] nums = eq.split("/|\\+|m|\\*|\\^");
//		for(int i = 0; i < nums.length; i++) {
//			System.out.print(nums[i] + ";");
//		}
//		System.out.println();
		String searches = "+m*/^";
		int curNum = 0;
		for (int i = 0; i < eq.length(); i++) {
			if (searches.indexOf(eq.charAt(i)) >= 0) {
				output += nums[curNum] + " " + eq.charAt(i) + " ";
				curNum++;
			}
		}
		output += nums[curNum];
		return output;
	}

	static String fixMinus(String eq) {
		String output = "";
		output += eq.charAt(0);
		for (int i = 1; i < eq.length() - 1; i++) {
			if (Character.isDigit(eq.charAt(i - 1)) && eq.charAt(i) == '-' && Character.isDigit(eq.charAt(i + 1))) {
				output += "m";
			} else if (eq.charAt(i - 1) == ')' && eq.charAt(i) == '-' && eq.charAt(i + 1) == '(') {
				output += 'm';
			} else if (eq.charAt(i - 1) == '-' && eq.charAt(i) == '-') {
				output = output.substring(0, i - 1);
			} else {
				output += eq.charAt(i);
			}
		}
		output += eq.charAt(eq.length() - 1);
		return output;
	}
}
