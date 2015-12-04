package lexicalAnalyzer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

public class DFAState {

	private static int count = 1;
	private int num;
	public String acceptingString = "";
	public HashMap<Character, DFAState> next;

	public DFAState() {
		this.next = new HashMap<Character, DFAState>();
		this.num = count++;
	}

	public static DFAState generateDFA(NFAState rulesNFAInitialState) throws Exception {
		DFAState DFAInitialState = new DFAState();
		Queue<DFAState> toExpandState = new LinkedList<DFAState>();
		toExpandState.add(DFAInitialState);

		HashMap<DFAState, HashSet<NFAState>> DFAStateSet = new HashMap<DFAState, HashSet<NFAState>>();

		HashSet<NFAState> epsilonTransitions = new HashSet<NFAState>();
		epsilonTransitions.addAll(getEpsilonTransitions(rulesNFAInitialState));
		epsilonTransitions.add(rulesNFAInitialState);
		DFAStateSet.put(DFAInitialState, epsilonTransitions);

		HashSet<Integer> visitedStatesNum = new HashSet<Integer>();
		visitedStatesNum.add(DFAInitialState.num);

		while (!toExpandState.isEmpty()) {
			DFAState currentDFAState = toExpandState.poll();

			epsilonTransitions = DFAStateSet.get(currentDFAState);
			currentDFAState.checkAcceptance(epsilonTransitions);
			HashMap<Character, HashSet<NFAState>> DFANext = new HashMap<Character, HashSet<NFAState>>();
			for (NFAState state : epsilonTransitions) { // Each state in current DFA states
				for (Character nextEdge : state.next.keySet()) { // Each input for each state
					if (nextEdge != null) { // DFA table does NOT have a column for epsilon input
						HashSet<NFAState> inputTransitions = new HashSet<NFAState>();

						inputTransitions.addAll(state.next.get(nextEdge));// input transitions without epsilons
						for (NFAState nextState : state.next.get(nextEdge)) {
							inputTransitions.addAll(getEpsilonTransitions(nextState));
						}

						if (DFANext.containsKey(nextEdge)) { // if a previous state had an input transition on this edge
							HashSet<NFAState> oldInputTransitions = DFANext.get(nextEdge);
							oldInputTransitions.addAll(inputTransitions);
						} else { // else this is the first state in DFA states to have this input transition
							DFANext.put(nextEdge, inputTransitions);
							// CHECK LATER, Remove duplicates in minimal DFA
							// DFAState newDFAState = new DFAState();
							// currentDFAState.next.put(nextEdge, newDFAState);
							//
							// DFAStateSet.put(newDFAState, inputTransitions);
							//
							// toExpandState.add(newDFAState);
						}
					}
				}
			}

			// check on DFANext if any hashset value is contained in DFAStateSet
			HashMap<DFAState, HashSet<NFAState>> newStates = new HashMap<DFAState, HashSet<NFAState>>();
			// Using this variable because we cannot add directly to DFAStateSet inside its loop due to java exception
			for (Character nextEdge : DFANext.keySet()) {
				boolean existingFlag = false;
				HashSet<NFAState> newTransition = DFANext.get(nextEdge);
				// Check to see that the transitions are not found in a previous ROW in the table
				for (HashSet<NFAState> existingTransition : DFAStateSet.values()) {
					if (areEquivalent(newTransition, existingTransition)) {
						DFAState existingState = null;
						for (DFAState state : DFAStateSet.keySet()) {
							if (DFAStateSet.get(state).equals(existingTransition)) {
								existingState = state;
								existingFlag = true;
								break;
							}
						}
						if (existingState == null)
							throw new Exception("Bug1");
						currentDFAState.next.put(nextEdge, existingState);
						break;
					}
				}

				// Check to see that the transitions are not found in a previous COLUMN in the table
				if (!existingFlag) {
					for (HashSet<NFAState> existingInNewTransition : newStates.values()) {
						if (areEquivalent(newTransition, existingInNewTransition)) {
							DFAState existingStateInNew = null;
							for (DFAState state : newStates.keySet()) {
								if (newStates.get(state).equals(existingInNewTransition)) {
									existingStateInNew = state;
									existingFlag = true;
									break;
								}
							}
							if (existingStateInNew == null)
								throw new Exception("Bug2");
							currentDFAState.next.put(nextEdge, existingStateInNew);
							break;
						}
					}
				}

				if (!existingFlag) {
					DFAState newDFAState = new DFAState();
					currentDFAState.next.put(nextEdge, newDFAState);
					newStates.put(newDFAState, newTransition);
					toExpandState.add(newDFAState);
				}
			}
			for (DFAState s : newStates.keySet()) {
				DFAStateSet.put(s, newStates.get(s));
			}

		}

		return DFAInitialState;
	}

	private static boolean areEquivalent(HashSet<NFAState> newTransition, HashSet<NFAState> existingTransition) {
		return newTransition.containsAll(existingTransition) && newTransition.size() == existingTransition.size();
	}

	// gets epsilon transitions on nested levels by BFS
	private static HashSet<NFAState> getEpsilonTransitions(NFAState state) {
		Queue<NFAState> toExpandState = new LinkedList<NFAState>();
		toExpandState.add(state);

		HashSet<NFAState> epsilonTransitions = new HashSet<NFAState>();
		HashSet<Integer> visitedStatesNum = new HashSet<Integer>();
		visitedStatesNum.add(state.num);

		while (!toExpandState.isEmpty()) {
			NFAState currentState = toExpandState.poll();

			if (currentState.next.containsKey(null)) {
				for (NFAState nextEpsilonsState : currentState.next.get(null)) {
					if (!visitedStatesNum.contains(nextEpsilonsState.num)) {
						epsilonTransitions.add(nextEpsilonsState);
						toExpandState.add(nextEpsilonsState);
						visitedStatesNum.add(nextEpsilonsState.num);
					}
				}
			}
		}

		return epsilonTransitions;
	}

	public void print() {
		Queue<DFAState> toExpandState = new LinkedList<DFAState>();
		toExpandState.add(this);

		HashSet<Integer> visitedStatesNum = new HashSet<Integer>();
		visitedStatesNum.add(this.num);

		while (!toExpandState.isEmpty()) {
			DFAState currentState = toExpandState.poll();
			System.out.print("Num: " + currentState.num + " ");
			System.out.println(currentState.next);
			String stateAcceptingString = currentState.getAcceptingString();
			if (stateAcceptingString.length() != 0) {
				System.out.println(" accepting: " + stateAcceptingString);
			}
			for (Character c : currentState.next.keySet()) {
				DFAState nextStateOnC = currentState.next.get(c);
				if (!visitedStatesNum.contains(nextStateOnC.num)) {
					toExpandState.add(nextStateOnC);
					visitedStatesNum.add(nextStateOnC.num);
				}
			}

		}
	}

	private void checkAcceptance(HashSet<NFAState> transitions) {
		int lineNumberMin = Integer.MAX_VALUE;
		for (NFAState state : transitions) {
			String NFAStateAcceptingString = state.getAcceptingString();
			if (NFAStateAcceptingString.length() != 0) {
				int NFALineNumber = Integer.parseInt(NFAStateAcceptingString.split(" ")[0]);
				String NFAStateAcceptingStringValue = NFAStateAcceptingString.split(" ")[1];
				if( NFALineNumber < lineNumberMin ){
					lineNumberMin = NFALineNumber;
					this.setAcceptingString(NFAStateAcceptingStringValue);
				}
			}
		}
	}
	
	public boolean accepting(){
		return acceptingString == "" ? false : true;
	}

	@Override
	public String toString() {
		return num + "";
	}

	public String getAcceptingString() {
		return acceptingString;
	}

	public void setAcceptingString(String acceptingString) {
		this.acceptingString = acceptingString;
	}
}
