package ii_01_WordNet;

import java.util.HashMap;
import java.util.Map.Entry;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class WordNet {
	// constructor takes the name of the two input files
	public WordNet(String synsets, String hypernyms) {
		this.wordMap = new HashMap<String, Bag<Integer> >();
		In inSynsets = new In(synsets);
		String delims = "[,]";
		int maxNumber = 0;
		while (inSynsets.hasNextLine()) {
			String[] tokens = inSynsets.readLine().split(delims);
			
			// DEBUG
//			for (int i = 0; i < tokens.length; i++) {
//				System.out.println(tokens[i]);
//			}
			
			int wordNumber = Integer.parseInt(tokens[0]);
			maxNumber = maxInteger(maxNumber, wordNumber);
			if (!wordMap.containsKey(tokens[1])) {
				Bag<Integer> bag = new Bag<Integer>();
				bag.add(wordNumber);
				wordMap.put(tokens[1], bag);
			} else {
				wordMap.get(tokens[1]).add(wordNumber);
			}
		}
		inSynsets.close();
		this.G = new Digraph(maxNumber + 1);
		In inHypernyms = new In(hypernyms);
		while (inHypernyms.hasNextLine()) {
			String[] tokens = inHypernyms.readLine().split(delims);
			int v = Integer.parseInt(tokens[0]);
			for (int i = 1; i < tokens.length; i++) {
				int w = Integer.parseInt(tokens[i]);
				//DEBUG
//				System.out.println("adding:" + v + " -> " + w);
				G.addEdge(v, w);
			}
		}
		inHypernyms.close();
		sap = new SAP(G);
	}

	// returns all WordNet nouns
	public Iterable<String> nouns() {
		return wordMap.keySet();
	}

	// is the word a WordNet noun?
	public boolean isNoun(String word) {
		if (wordMap.isEmpty()) return false;
		return wordMap.containsKey(word);
	}

	// distance between nounA and nounB (defined below)
	public int distance(String nounA, String nounB) {
		return sap.length(wordMap.get(nounA), wordMap.get(nounB));
	}

	// a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
	// in a shortest ancestral path (defined below)
	public String sap(String nounA, String nounB) {
		String result = "";
		int ancestor = sap.ancestor(wordMap.get(nounA), wordMap.get(nounB));
		boolean isFind = false;
		for(Entry<String, Bag<Integer> > entry : wordMap.entrySet()) {
			for (int x : entry.getValue()) {
				if (x == ancestor) {
					result = entry.getKey();
					isFind = true;
				}
				if (isFind) break;
			}
			if (isFind) break;
		}
		return result;
	}

	// do unit testing of this class
	public static void main(String[] args) {
		if(args.length < 2)
			throw new java.lang.NullPointerException("lack of arguments!");
		WordNet wordnet = new WordNet(args[0], args[1]);
		
		// DEBUG
//		for (String word : wordnet.nouns()) {
//			System.out.println(word);
//		}
//		for(int x : wordnet.G.adj(1))
//			System.out.println(x);
//		for(int x : wordnet.G.adj(5))
//			System.out.println(x);
		
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
	private int maxInteger(int a, int b) {
		return (a > b)?a:b;
	}
	
	/* Private instance variables */
	private HashMap<String, Bag<Integer> > wordMap;
	private Digraph G;
	private SAP sap;
}
