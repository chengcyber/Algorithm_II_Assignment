import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
	
    /* Private instance variables */   
	private static final int R = 256;
   	private static int[] org,serial;

   	static {
   		org = new int[R];
   		serial = new int[R];
		for (int i = 0; i < R; i++) {
			org[i] = i;
		}
   	}

    public MoveToFront() {}
    
   // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
    	String s = BinaryStdIn.readString();
        char[] input = s.toCharArray();

        System.arraycopy(org, 0, serial, 0, R);
        for (int i = 0; i < input.length; i++) {
      		BinaryStdOut.write(mtf(serial, input[i]), 8);
        }
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
    	String s = BinaryStdIn.readString();
    	char[] input = s.toCharArray();

    	System.arraycopy(org, 0, serial, 0, R);
    	for (int i = 0; i < input.length; i++) {
    		int index = input[i];
    		int temp = serial[index];
	    	System.arraycopy(serial, 0, serial, 1, index);
	    	serial[0] = temp;
	    	BinaryStdOut.write(temp, 8);
    	}
    	BinaryStdOut.close();
    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args) {
        if      (args[0].equals("-")) encode();
        else if (args[0].equals("+")) decode();
        else throw new IllegalArgumentException("Illegal command line argument");   	
    }

    /* Private section */
    private static int mtf(int[] serial, char input) {
    	int index = 0;
    	for (int i = 0; i < serial.length; i++) {
    		if (input == serial[i]) {
    			index = i;
    			break;
    		}
    	}
    	int temp = serial[index];
    	System.arraycopy(serial, 0, serial, 1, index);
    	serial[0] = temp;
        return index;
    }


    
}
