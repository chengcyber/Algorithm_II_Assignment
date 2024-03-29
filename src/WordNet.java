

import java.util.HashMap;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class WordNet {
	// constructor takes the name of the two input files
	/**
	 * Constructor : WordNet
	 * Usage: WordNet wordnet = WordNet(synsets, hypernyms);
	 * @param synsets 
	 * 		List of noun synsets. The file synsets.txt lists all the (noun) synsets in WordNet. 
	 * 		The first field is the synset id (an integer), the second field is the synonym set 
	 * 		(or synset), and the third field is its dictionary definition (or gloss). For example,
	 * 		the line
	 *  		36,AND_circuit AND_gate,a circuit in a computer that fires only when all of its inputs fire
	 * 		means that the synset { AND_circuit, AND_gate } has an id number of 36 and it's gloss is a circuit in a computer that fires only when all of its inputs fire. The individual nouns that comprise a synset are separated by spaces (and a synset element is not permitted to contain a space). The S synset ids are numbered 0 through S − 1; the id numbers will appear consecutively in the synset file.
	 * @param hypernyms
	 * 		List of hypernyms. The file hypernyms.txt contains the hypernym relationships: The first 
	 * 		field is a synset id; subsequent fields are the id numbers of the synset's hypernyms. For
	 * 		 example, the following line
	 *			164,21012,56099
	 *		means that the the synset 164 ("Actifed") has two hypernyms: 21012 ("antihistamine") and 56099 ("nasal_decongestant"), representing that Actifed is both an antihistamine and a nasal decongestant. The synsets are obtained from the corresponding lines in the file synsets.txt.
	 */
	public WordNet(String synsets, String hypernyms) {
		this.wordMap = new HashMap<String, Bag<Integer> >();
		this.idMap = new HashMap<Integer, String>();
		In inSynsets = new In(synsets);
		String delims = ",";
		int maxNumber = 0;
		while (inSynsets.hasNextLine()) {
			String[] tokens = inSynsets.readLine().split(delims);
			int wordNumber = Integer.parseInt(tokens[0]);
			maxNumber = maxInteger(maxNumber, wordNumber);
			idMap.put(wordNumber, tokens[1]);
			String[] nouns = tokens[1].split(" ");
			for (String noun : nouns) {
				if (!wordMap.containsKey(noun)) {
					Bag<Integer> bag = new Bag<Integer>();
					bag.add(wordNumber);
					wordMap.put(noun, bag);
				} else {
					wordMap.get(noun).add(wordNumber);
				}
			}
		}
		inSynsets.close();
		pointToOther = new boolean[maxNumber + 1];
		this.G = new Digraph(maxNumber + 1);
		In inHypernyms = new In(hypernyms);
		while (inHypernyms.hasNextLine()) {
			String[] tokens = inHypernyms.readLine().split(delims);
			int v = Integer.parseInt(tokens[0]);
			if (tokens.length >= 1) pointToOther[v] = true;
			for (int i = 1; i < tokens.length; i++) {
				int w = Integer.parseInt(tokens[i]);
				G.addEdge(v, w);
			}
		}
		inHypernyms.close();
		/* Using DirectedCycle Class to check DAG */
		finder = new DirectedCycle(G);
		if (finder.hasCycle())
			throw new java.lang.IllegalArgumentException("Digraph has cycle");
		/* Using pointToOther to check multi-root */
		int countRoot = 0;
		for (boolean bool : pointToOther) {
			if (!bool) countRoot++;
		}
		if (countRoot >=2)
			throw new java.lang.IllegalArgumentException("Digraph has multi-root");
		sap = new SAP(G);
	}

	/**
	 * all WordNet nouns
	 * Usage: Iterable<String> itr = wordnet.nouns();
	 * @return all WordNet nouns
	 */
	public Iterable<String> nouns() {
		return wordMap.keySet();
	}

	/**
	 * Usage: if (isNoun(word)) . . .
	 * @param word
	 * @return true if word is a WordNet noun. otherwise false;
	 */
	public boolean isNoun(String word) {
		if (word == null)
			throw new java.lang.NullPointerException();
		if (wordMap.isEmpty()) return false;
		return wordMap.containsKey(word);
	}

	/**
	 * Usage: int distance = wordnet.distance(nounA, nounB);
	 * @param nounA String type value
	 * @param nounB String type value
	 * @return distance between nounA and nounB (defined below)
	 */
	public int distance(String nounA, String nounB) {
		if (nounA == null || nounB == null)
			throw new java.lang.NullPointerException();
		if (!isNoun(nounA)){  
            throw new java.lang.IllegalArgumentException(nounA);  
        }  
        if (!isNoun(nounB)){  
            throw new java.lang.IllegalArgumentException(nounB);  
        }  
		return sap.length(wordMap.get(nounA), wordMap.get(nounB));
	}

	/**
	 * Usage: String anc = wordnet.sap(nounA, nounB);
	 * @param nounA String type value
	 * @param nounB String type value
	 * @return the common ancestor of nounA and nounB in a shortest ancestral path
	 */
	public String sap(String nounA, String nounB) {
		if (nounA == null || nounB == null)
			throw new java.lang.NullPointerException();
		if (!isNoun(nounA)){  
            throw new java.lang.IllegalArgumentException(nounA);  
        }  
        if (!isNoun(nounB)){  
            throw new java.lang.IllegalArgumentException(nounB);  
        }  
		int ancestor = sap.ancestor(wordMap.get(nounA), wordMap.get(nounB));
		if (ancestor < 0) return "no ancestor"; 
		return idMap.get(ancestor);
	}

	/**
	 * Testing main function
	 * @param args [0] synsets.txt , [1] hypernyms.txt
	 */
	public static void main(String[] args) {
		if(args.length < 2)
			throw new java.lang.NullPointerException("lack of arguments!");
		WordNet wordnet = new WordNet(args[0], args[1]);
		StdOut.printf("%d Verties, %d Edges", wordnet.G.V(), wordnet.G.E());
		while (!StdIn.isEmpty()) 
		{
			String v = StdIn.readString();
			String w = StdIn.readString();
			System.out.println(v + ", " + w);
			int length   = wordnet.distance(v, w);
			String ancestor = wordnet.sap(v, w);
			StdOut.printf("length = %d, ancestor = %s\n", length, ancestor);
		}
	}
	
	/* Private Methods */
	/**
	 * Usage: int max = maxInteger(a, b);
	 * @param a int type value
	 * @param b int type value
	 * @return the larger number between a and b
	 */
	private int maxInteger(int a, int b) {
		return (a > b)?a:b;
	}
	
	/* Private instance variables */
	private HashMap<String, Bag<Integer> > wordMap;		/* HashMap for noun to ints. */
	private HashMap<Integer, String> idMap;				/* HashMap for int to nouns  */
	private Digraph G;									/* instance of Digraph */
	private SAP sap;									/* instance of SAP */
	private DirectedCycle finder;						/* instance of DirectedCycle */
	private boolean pointToOther[];						/* array for every number check is(or not) point to other */
}
