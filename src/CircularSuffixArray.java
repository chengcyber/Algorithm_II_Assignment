
public class CircularSuffixArray {
	// circular suffix array of s
    public CircularSuffixArray(String s) {
    	if (s == null) 
    		throw new java.lang.NullPointerException();
    	L = s.length();
    	org = new char[L];
    	for (int i = 0; i < L; i++) {
    		org[i] = s.charAt(i);
    	}
    	idx = new int[L];
    	for (int i = 0; i < L; i++) {
    		idx[i] = i;
    	}
    	sortToIndex();
    }
    // length of s
    public int length() {
    	return L;
    }
    // returns index of ith sorted suffix
    public int index(int i) {
    	if (i < 0 || i >= L)
    		throw new java.lang.IndexOutOfBoundsException();
    	return idx[i];
    }
    // unit testing of the methods (optional)
    public static void main(String[] args) {
    	CircularSuffixArray csa = new CircularSuffixArray("ABRACADABRA!");
    	for (int i = 0; i < csa.length(); i++) {
    		System.out.println(csa.index(i));
    	}
    }

    /* Private Section */
    private void sortToIndex() {
    	int[] aux = new int[L];
    	sort(org, idx, 0, L - 1, 0, aux);
    }

 
    private void sort(char[] org, int[] idx, int lo, int hi, int d, int[] aux) {
    	if (hi <= lo) return;
    	if (hi <= lo + CUTOFF) {
    		insertion(org, idx, lo, hi, d);
    		return;
    	}
    	System.out.println("sorting:" + lo + ", " + hi);
    	int[] count = new int[R+2];
    	for (int i = lo; i <= hi; i++) {
    		count[charAt(org, i, d) + 2]++;
    	}
    	for (int r = 0; r < R + 1; r++) {
    		count[r+1] += count[r];
    	}
    	for (int i = lo; i <= hi; i++) {
    		int index = count[charAt(org, i, d) + 1]++;
    		aux[lo + index] = idx[i];
    	}
    	for (int i = lo; i <= hi; i++) {
    		idx[i] = aux[i];
    	}
    	for (int r = 0; r < R; r++) {
    		sort(org, idx, lo + count[r], lo + count[r + 1] - 1, d + 1, aux);
    	}
    }

    private void insertion(char[] org, int[] idx, int lo, int hi, int d) {
    	System.out.println("insertion:" + lo + ", " + hi);
    	for (int i = lo; i <= hi; i++) {
    		for (int j = i; j > lo && less(org, j, j - 1, d); j--) {
    			exch(idx, j, j - 1);
    		}
    	}
    }

    private boolean less(char[] org, int i, int j, int d) {
    	for (int k = 0; k < L - d; k++) {
    		int a = charAt(org, i, k);
    		int b = charAt(org, j, k);
    		if (a < b) return true;
    		if (a > b) return false;
    	}
    	return false;
    }

    private void exch(int[] idx, int i, int j) {
    	int temp = idx[i];
    	idx[i] = idx[j];
    	idx[j] = temp;
    }

    private int charAt(char[] org, int i, int d) {
    	if (d >= org.length) return -1;
    	int index = (idx[i] + d) % L;
    	System.out.println(org[index]);
    	return org[index];
    }

    /* Private Instance Variables */
    private static final int R = 256;
    private static final int CUTOFF = 15;
    private int L;
    private char[] org;
    private int[] idx;
}
