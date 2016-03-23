

import java.util.ArrayList;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
	/**
	 * Constructor
	 * Usage Outcast outcast = new Outcast(wordnet);
	 * @param wordnet instance of WordNet Class
	 */
	public Outcast(WordNet wordnet) {
		this.wordnet = wordnet;
	}
	
	// given an array of WordNet nouns, return an outcast
	public String outcast(String[] nouns) {
		distArrs = new ArrayList< ArrayList<Integer> >();
		int[] countDist = new int[nouns.length];
		for (int i = 0; i < nouns.length; i++) {
			int count = 0;
			ArrayList<Integer> intList = new ArrayList<Integer>();
			distArrs.add(intList);
			for (int j = i + 1; j < nouns.length; j++) {
				int distance = wordnet.distance(nouns[i], nouns[j]);
				distArrs.get(i).add(distance);
				count += distance;
			}
			for (int j = i - 1; j >= 0; j--) {
				count += distArrs.get(j).get(nouns.length - 2 - j);
			}
			countDist[i] = count;
//			System.out.println(nouns[i] + " : " + count);
		}
		int maxDist = 0;
		int maxDistIndex = -1;
		for (int i = 0; i < countDist.length; i++) {
			if (countDist[i] >= maxDist) {
				maxDist = countDist[i];
				maxDistIndex = i;
			}
		}
		if (maxDistIndex == -1) return "All same distance, can not figure out!";
		return nouns[maxDistIndex];
	}
	
	/**
	 * Testing main function
	 * @param args [0] synset.txt [1] hypernym.txt [2] ... outcast*.txt
	 */
	public static void main(String[] args) {
	    WordNet wordnet = new WordNet(args[0], args[1]);
	    Outcast outcast = new Outcast(wordnet);
	    for (int t = 2; t < args.length; t++) {
	        In in = new In(args[t]);
	        String[] nouns = in.readAllStrings();
	    	StdOut.println(args[t] + ": " + outcast.outcast(nouns));
	    }
	}
	
	/* Private Instance variable */
	private WordNet wordnet;		/* instace of WordNet */
	private ArrayList< ArrayList<Integer> > distArrs;  /* data structure to store distance */
	
}
