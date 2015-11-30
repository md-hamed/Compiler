import java.awt.List;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class Main {
	private static HashMap<String, String> symbolTable;
	private static Stack<Double> operands = new Stack<Double>();
	private static Stack<Character> operators = new Stack<Character>();
	private static Character[] seps = { ' ', '-', '|', '+', '*', '(', ')' };
	private static ArrayList<Character> separators = new ArrayList<Character>(
			Arrays.asList(seps));
	private static HashMap<String, Double> definitions = new HashMap<String, Double>(); // TC
																						// Double
																						// =>
																						// NFA

	public static void main(String[] args) throws Exception {
		symbolTable = new HashMap<String, String>();
		try (BufferedReader br = new BufferedReader(
				new FileReader(
						"C:\\Users\\electric\\Dropbox\\College\\Term 9\\Programming�Languages�Translation\\Project phase 1\\rules.txt"))) {
			// StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			// sb.append(line);
			// sb.append(System.lineSeparator());
			// line = br.readLine();
			while (line != null) {

				if (line.charAt(0) == '{') {
					for (String token : line.substring(1, line.length() - 1)
							.trim().split(" ")) {
						// System.out.println("==="+ token+"===");
						// NFA concatenation of chars
					}
				} else if (line.charAt(0) == '[') {
					for (int i = 1; i < line.length() - 1; i++) {
						char parsedChar = line.charAt(i);
						if (parsedChar != '\\' && parsedChar != ' ') {
							symbolTable.put(parsedChar + "", parsedChar
									+ "_PUNCT");
						}
					}
				} else { // Expressions OR Definitions
					Boolean isDefinition;
					if ((line.indexOf('=') != -1 && line.indexOf('=') < line
							.indexOf(':')) || line.indexOf(':') == -1) {
						isDefinition = true;
					} else {
						isDefinition = false;
					}
					if (isDefinition) {
						definitions.put(line.substring(0, line.indexOf('='))
								.trim(), 1.0);
						// System.out.println(line.substring(0,
						// line.indexOf('=')).trim() + "=");
						line = line.substring(line.indexOf('=') + 1);
					} else {
						line = line.substring(line.indexOf(':') + 1);
					}

					// Tokenize line, separate on operators or space
					ArrayList<String> lineTokens = new ArrayList<String>();
					StringBuilder tempChars = new StringBuilder();
					for (int i1 = 0, i2 = 1; i1 < line.length(); i1++, i2++) {
						char currentChar = line.charAt(i1);
						char nextChar = i2 < line.length() ? line.charAt(i2)
								: ' ';
						if (currentChar == ' ') {
							continue;
						} else {
							// if (currentChar == '\\' && nextChar != 'L') {
							// continue;
							// } else {
							tempChars.append(currentChar);
							// }
							if ((separators.contains(nextChar) && currentChar != '\\')
									|| separators.contains(currentChar)) {
								lineTokens.add(tempChars.toString());
								// System.out.println(tempChars);
								tempChars = new StringBuilder();
							}
						}
					}
					System.out.println(lineTokens);

					// Parse tokens with precedence
					String token;
					for (int i = 0; i < lineTokens.size(); i++) {
						token = lineTokens.get(i);
						if (isOperator(token)) { // operator
							char operator = token.charAt(0);
							// TC
							if (operator == ' ') {
								throw new Exception(
										"CANNOT BE A SPACE AS OPERATOR!");
							} else if (operator == '-') {
								char begin = lineTokens.get(i - 1).charAt(0), end = lineTokens
										.get(i + 1).charAt(0);
								i++;
//								operands.pop();
								char index = (char) (begin + 1);
								// NFA tempNFA = NFA(begin.toString());
								while (index <= end) {
									// NFA indexNFA = NFA(index.toString());
									// tempNFA = NFAor(tempNFA, indexNFA);
									 index = (char) (index + 1);
								}
								// operands.push(tempNFA);
							} else if (operator == '*') {
								// NFA tempNFA= operands.pop();
								// operands.push(NFAkleene(tempNFA));
							} else if (operator == '+') {
								// NFA tempNFA= operands.pop();
								// operands.push(NFAplus(tempNFA));
							} else {
								if (operators.isEmpty()
										|| operators.peek() == '(') {
									operators.push(operator);
								}
							}

							//System.out.println("or: " + token);
						} else { // operand
							//System.out.println("od: " + token);
							// TC
							// NFA operandNFA = getOperandNFA(token);
							// operands.push(operandNFA);

							// Check for concatenation operator after the
							// operand
							if (i + 1 < lineTokens.size()) {
								String nextToken = lineTokens.get(i + 1);
								if (!isOperator(nextToken)) {
									System.out.println("CONCAT: " + nextToken);
									// TC
									// nextOperandNFA = getOperandNFA(nextToken)
									// NFA resultNFA = NFAconcat(operandNFA,
									// nextOperandNFA);
									// operands.pop();
									// operands.push(resultNFA);
								}
							}
						}
					}
				}
				// sb.append(line);
				// sb.append(System.lineSeparator());
				line = br.readLine();
			}
			// for(String key: symbolTable.keySet()){
			// System.out.println(key + " => " + symbolTable.get(key));
			// }
			// String everything = sb.toString();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static boolean isOperator(String token) {
		return token.length() == 1 && separators.contains(token.charAt(0));
	}

	// TC change boolean to NFA
	private static boolean getOperandNFA(String token) {
		// NFA operandNFA;
		if (definitions.containsKey(token)) {
			// operandNFA = definitions.get(token);
		} else {
			// Change operand to a NFA
			// operandNFA = NFA(token);
		}
		// return operandNFA
		return true;

	}
}