import lexicalAnalyzer.*;
import inputParser.*;

public class Main {

	public static void main(String[] args) throws Exception {

		String os = System.getProperty("os.name");
		String filePath = os.startsWith("Windows") ? "C:\\Users\\electric\\Dropbox\\College\\Term 9\\Programming Languages Translation\\Project phase 1\\rules.txt"
				: "/home/hamid/Desktop/rules.txt";
		NFA rulesNFA = InfixEvaluator.getRulesNFA(filePath);
		
		System.out.println("end");
	}

}
