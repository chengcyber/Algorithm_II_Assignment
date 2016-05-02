import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    // apply move-to-front encoding, reading from standard input and writing to standard output
//     public static void encode() {
//     	String s = BinaryStdIn.readString();
//         char[] input = s.toCharArray();

// //        int[] serial = new int[R];
// //        for (int i = 0; i < R; i++) {
// //            serial[i] = i;
// //        }

//         BinaryStdOut.write(input.length);
        
//         for (int i = 0; i < input.length; i++) {
// //        	BinaryStdOut.write((char)mtf(serial, input[i]), 8);
//         	BinaryStdOut.write(input[i]);
//         }

//         BinaryStdOut.close();
//     }



    private static char[] ori, seq;
    
    static {
        ori = new char[256];
        seq = new char[256];
        for (char i = 0; i < 256; i++)
            ori[i] = i;
    }
    
    // apply move-to-front encoding, 
    // reading from standard input and writing to standard output
    public static void encode() {
        System.arraycopy(ori, 0, seq, 0, 256);
        while (!BinaryStdIn.isEmpty()) {
            int idx = 0;
            char c = BinaryStdIn.readChar();
            for (; idx < 256; idx++) 
                if (seq[idx] == c) break;
            BinaryStdOut.write(idx, 8);
            System.arraycopy(seq, 0, seq, 1, idx);
            seq[0] = c;
        }
        BinaryStdOut.close();
    }



    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
    	
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
    	System.out.println("inputchar:" + input);
    	System.out.println("index:" + index);
        for (int i = index; i > 0; i--) {
            int temp = serial[i];
            serial[i] = serial[i - 1];
            serial[i - 1] = temp;
        }
        return index;
    }
    
    private MoveToFront() {}

    /* Private instance variables */
    private static final int R = 256;
}
