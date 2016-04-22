import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.TST;

public class BoggleSolver {
	// Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
	public BoggleSolver(String[] dictionary) {
		lex = new TST<Boolean>();
		for(String str : dictionary) {
			lex.put(str, true);
		}
		
	}
	// Returns the set of all valid words in the given Boggle board, as an Iterable.
	public Iterable<String> getAllValidWords(BoggleBoard board) {
		int row = board.rows();
		int col = board.cols();
		boggle = new char[row][col];
		for (int i = 0;i < row; i++) {
			for (int j = 0;j < col; j++) {
				boggle[i][j] = board.getLetter(i,j);
			}
		}
		Set<String> words = new TreeSet<String>();
		boolean[][] marked = new boolean[row][col];
		String word = "";
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				dfs(boggle, i, j, word, words, marked);
			}
		}
		return words;
	}
	// Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
	public int scoreOf(String word) {
		int score = 0;
		switch(word.length()) {
		case 0 : 
		case 1 : 
		case 2 : score = 0; break;
		case 3 : 
		case 4 : score = 1; break;
		case 5 : score = 2; break;
		case 6 : score = 3; break;
		case 7 : score = 5; break;
		case 8 : score = 11; break;
		default: score = 11; break;
		}
		return score;
	}
	
	
	public static void main(String[] args)
	{
	    In in = new In(args[0]);
	    String[] dictionary = in.readAllStrings();
	    BoggleSolver solver = new BoggleSolver(dictionary);
	     BoggleBoard board = new BoggleBoard(args[1]);
	     int score = 0;
	     for (String word : solver.getAllValidWords(board))
	     {
	         StdOut.println(word);
	         score += solver.scoreOf(word);
	     }
	     StdOut.println("Score = " + score);

	}
	
	/* Private Section */
	private void dfs(char[][] boogle, int i, int j, String word, Set<String> words, boolean[][] marked) {
		if (marked[i][j]) return;
		marked[i][j] = true;
		if (boggle[i][j] == 'Q') {
			word += "QU";
		} else {
			word = word + boggle[i][j];
		}
		if (lex.keysWithPrefix(word).iterator().hasNext()) {
			if (lex.contains(word)) {
				words.add(word);
			}
			for (Coordinate coord : getNeighbors(i, j, boggle.length, boggle[0].length)) {
				dfs(boggle, coord.getX(), coord.getY(), word, words, marked);
			}
		}
 		marked[i][j] = false;
 		if (word.charAt(word.length() - 2) == 'Q') {
 			word = word.substring(0, word.length() - 2);
 		} else {
			word = word.substring(0, word.length() - 1);
 		}
	}

	private Iterable<Coordinate> getNeighbors(int i, int j, int row, int col) {
		ArrayList<Coordinate> neighbors = new ArrayList<Coordinate>();
		for (int dx = -1; dx <= 1; dx++) {
			int x = i + dx;
			if(x < 0 || x >= row) continue;
			for (int dy = -1; dy <= 1; dy++) {
				int y = j + dy;
				if (y < 0 || y >= col) continue;
				if (x == i && y == j) continue;
				neighbors.add(new Coordinate(x, y));
			}
		}
//		System.out.println(i + ", " + j);
//		for (Coordinate coord : neighbors) {
//			System.out.println(coord.getX() + ".. " + coord.getY());
//		}
//		System.out.println();
		return neighbors;
	}


	/* Private instance variables */
	private TST<Boolean> lex;
	private char[][] boggle;
}

class Coordinate {
	public Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	private int x;
	private int y;
}