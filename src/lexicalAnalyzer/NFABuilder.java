package lexicalAnalyzer;

public class NFABuilder {
	
	/* Base Case 1: Empty String */
	public static final NFA e() {
		NFAState inputState  = new NFAState() ;
		NFAState outputState = new NFAState() ;
		inputState.addEpsilonTranisition(outputState);
		outputState.setLast(true);
		return new NFA(inputState ,outputState) ;
    }
	
	/* Base Case 2: Single Character */
	public static final NFA c(char c) {
		NFAState inputState = new NFAState();
		NFAState outputState = new NFAState();
		outputState.setLast(true);
		inputState.addTransition(outputState, c) ;
		return new NFA(inputState ,outputState) ;
    }
	
	/* Base Case 3: Concatenation */
	public static final NFA concat(NFA first, NFA second) {
		first.outputState.setLast(false);
		second.outputState.setLast(true);
		first.outputState.addEpsilonTranisition(first.inputState);
		return new NFA(first.inputState ,second.outputState) ;
    }
	
	/* Base Case 4: Orring */
	public static final NFA or(NFA first, NFA second) {
		first.outputState.setLast(false);
		second.outputState.setLast(false);
		
		NFAState inputState = new NFAState();
		NFAState outputState = new NFAState();
		
		inputState.addEpsilonTranisition(first.inputState);
		inputState.addEpsilonTranisition(second.inputState);
		
		first.outputState.addEpsilonTranisition(outputState);
		second.outputState.addEpsilonTranisition(outputState);
		
		outputState.setLast(true);
		
		NFA newNFA = new NFA(inputState, outputState);
		
		return newNFA;
    }
	
	/* Base Case 5: Kleene Closure */
	public static final NFA kleene(NFA nfa) {
		NFA newNFA = new NFA(nfa.inputState, nfa.outputState);
		newNFA.outputState.addEpsilonTranisition(nfa.inputState);
        newNFA.inputState.addEpsilonTranisition(nfa.outputState);;
		return newNFA;	
    }
	
	/* String */
	public static final NFA s(String str) {
		return str.length() == 0 ? e() : concat(c(str.charAt(0)), s(str.substring(1)));
    }
	
	/* Orring between multiple NFAs */
	public static final NFA or(Object... regexs) {
		NFA exp = regex(regexs[0]);
		for (int i = 1; i < regexs.length; i++) {
		    exp = or(exp, regex(regexs[i])) ;
		}
		return exp ;
    }

	/* Concatenation between multiple NFAs */
	public static final NFA concat(Object... regexs) {
		NFA exp = regex(regexs[0]);
		for (int i = 1; i < regexs.length; i++) {
		    exp = concat(exp, regex(regexs[i])) ;
		}
		return exp ;
    }
	
	/* Regex conversion */
    private static final NFA regex(Object o) {
		if (o instanceof NFA)
		    return (NFA)o ;
		else if (o instanceof Character)
		    return c((Character)o) ;
		else if (o instanceof String)
		    return s((String)o) ;
		else {
		    throw new RuntimeException("bad regexp") ;
		}
    }

}