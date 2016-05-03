import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {
	
	public BurrowsWheeler() {}
	
    // apply Burrows-Wheeler encoding, reading from standard input and writing to standard output
    public static void encode() {
    	String s = BinaryStdIn.readString();
    	CircularSuffixArray csa = new CircularSuffixArray(s);
    	int csaL = csa.length();
    	int[] codes = new int[csaL];
    	int first = 0;
    	for (int i = 0; i < csaL; i++) {
    		codes[i] = csa.index(i);
    		if (codes[i] == 0) { 
    			first = i;
    		}
    	}
    	BinaryStdOut.write(first);
    	for (int i = 0; i < csaL; i++) {
    		BinaryStdOut.write(lastCharAt(s, codes[i]), 8);
    	}
    	BinaryStdOut.close();
    }

    // apply Burrows-Wheeler decoding, reading from standard input and writing to standard output
    public static void decode() {
    	int first = BinaryStdIn.readInt();
    	String s = BinaryStdIn.readString();
    	char[] input = s.toCharArray();
    	
//    	System.out.println(first);
//    	for (int i = 0; i < input.length; i++) {
//    		System.out.println(input[i]);
//    	}
    	int csaL = input.length;
    	int[] count = new int[R + 1];
    	int[] next = new int[csaL];
    	for (int i = 0; i < csaL; i++) {
    		count[input[i] + 1]++;
    	}
    	for (int r = 0; r < R; r++) {
    		count[r + 1] += count[r];
    	}
    	for (int i = 0; i < csaL; i++) {
    		next[count[input[i]]++] = i;
    	}

    	for (int i = 0, cur = first; i < csaL - 1; i++) {
    		cur = next[cur];
    		BinaryStdOut.write(input[cur], 8);
    	}
    	BinaryStdOut.write(input[first]);
    	BinaryStdOut.close();
    }

    // if args[0] is '-', apply Burrows-Wheeler encoding
    // if args[0] is '+', apply Burrows-Wheeler decoding
    public static void main(String[] args) {
        if      (args[0].equals("-")) encode();
        else if (args[0].equals("+")) decode();
        else throw new IllegalArgumentException("Illegal command line argument");  
    }
    
    private static char lastCharAt(String s, int index) {
    	if (index == 0) {
    		return s.charAt(s.length() - 1);
    	}
    	return s.charAt(index - 1);
    }
    
    private static final int R = 256;
}
