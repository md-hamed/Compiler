import lexicalAnalyzer.*;
import inputParser.*;

public class Main {

	public static void main(String[] args) throws Exception {

		String os = System.getProperty("os.name");
		String filePath = os.startsWith("Windows") ? "C:\\Users\\electric\\Dropbox\\College\\Term 9\\Programming�Languages�Translation\\Project phase 1\\Compiler\\rules.txt"
				: "/home/hamid/Desktop/rules.txt";
		NFAState rulesNFAInitialState = InfixEvaluator.getRulesNFA(filePath);
		DFAState DFAInitialState = DFAState.generateDFA(rulesNFAInitialState);
		System.out.println("\n\t\t** DFA Simulation **");
		DFAState DFAInitialStateMinimized = DFAState.minimizeDFA(DFAInitialState);
		DFASimulator dfaSimulator = new DFASimulator(
				"/home/hamid/Desktop/text_program.txt",
				DFAInitialStateMinimized, InfixEvaluator.getTokenNames());
		dfaSimulator.simulate();
		System.out.println("\n\t\t** DFA **");
		DFAInitialStateMinimized.printTable();
	}
}
