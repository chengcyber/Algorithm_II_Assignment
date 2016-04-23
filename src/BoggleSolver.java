import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

public class BoggleSolver {
	/**
	 * Constructor: BoggleSlover
	 * Usage: BoggleSlover slover = new BoggleSlover(dictionary);
	 * @param dictionary
	 * Initializes the data structure using the given array of strings as the dictionary.
     * (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
	 */
	public BoggleSolver(String[] dictionary) {
		lex = new Lexicon();
		for(String str : dictionary) {
			lex.put(str);
		}
	}
	
	/**
	 * Method: getAllValidWords
	 * Usage: for (String str : slover.getAllValidWords(board)) . . .
	 * @param board
	 * @return the set of all valid words in the given Boggle board, as an Iterable.
	 */
	public Iterable<String> getAllValidWords(BoggleBoard board) {
		int row = board.rows();
		int col = board.cols();
		boggle = new char[row][col];
		for (int i = 0;i < row; i++) {
			for (int j = 0;j < col; j++) {
				boggle[i][j] = board.getLetter(i,j);
			}
		}
//		displayBoard(boggle);
		Bag<String> words = new Bag<String>();
		Bag<LexNode> nodes = new Bag<LexNode>();
		boolean[][] marked = new boolean[row][col];
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				StringBuilder sb = new StringBuilder();
				sb.append(boggle[i][j]);
				LexNode n = lex.getLexNode(sb.toString());
				if (n == null) continue;
				if (boggle[i][j] == 'Q') {
					sb.append("U");
					n = n.next['U' - 'A'];
				}
				marked[i][j] = true;
				dfs(boggle, i, j, n, nodes, sb, words, marked);
				marked[i][j] = false;
			}
		}
		for (LexNode n : nodes) {
			n.isDetected = false;
		}
		return words;
	}
	/**
	 * Method: scoreOf
	 * Usage: int score = slover.scoreOf(String word); 
	 * @param word
	 * @return the score of the given word if it is in the dictionary, zero otherwise.
     * (You can assume the word contains only the uppercase letters A through Z.)
	 */
	public int scoreOf(String word) {
		int score = 0;
		if (lex.contains(word)) {
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
		}
		return score;
	}
	
	/**
	 * Testing main method
	 * @param args [0]dictionary.txt [1]board.txt
	 */
	public static void main(String[] args)
	{
	    In in = new In(args[0]);
	    String[] dictionary = in.readAllStrings();
    	BoggleSolver solver = new BoggleSolver(dictionary);
    	if (args.length > 2) {
    		int N = Integer.parseInt(args[2]);
    		BoggleBoard[] boards = new BoggleBoard[N];
    		for (int i = 0; i < N; i++)
    			boards[i] = new BoggleBoard();
    		Stopwatch sw = new Stopwatch();
    		for (int i = 0; i < N; i++)
    			solver.getAllValidWords(boards[i]);
    		StdOut.println(sw.elapsedTime());
    	} else {
    		BoggleBoard board = new BoggleBoard(args[1]);
	    	int score = 0;
	    	for (String word : solver.getAllValidWords(board))
	    	{
	    		StdOut.println(word);
	    		score += solver.scoreOf(word);
	    	}
	    		StdOut.println("Score = " + score);
	    }
	}
	
	/* Private Section */
	private void dfs(char[][] boogle, int i, int j, LexNode n, Bag<LexNode> nodes, StringBuilder word, Bag<String> words, boolean[][] marked) {
//		System.out.println("current: " + i + ", " + j + ", " + word.toString());
		if (word.length() >= 3 && n.isWord && !n.isDetected) {
			words.add(word.toString());
			n.isDetected = true;
			nodes.add(n);
		}
		for (Coordinate coord : getNeighbors(i, j, boggle.length, boggle[0].length)) {
			int x = coord.x;
			int y = coord.y;
			word.append(boggle[x][y]);
			LexNode nextN = n.next[boggle[x][y] - 'A'];
			if(boggle[x][y] == 'Q') {
				word.append("U");
				if (nextN != null)
					nextN = nextN.next['U' - 'A'];
			}
			if (!marked[x][y] &&  nextN != null) {
				marked[x][y] = true;
				dfs(boggle, coord.x, coord.y, nextN, nodes, word, words, marked);
				marked[x][y] = false;
			}
			word.deleteCharAt(word.length() - 1);
			if (word.length() >= 2 && (word.charAt(word.length() - 1) == 'Q')) {
				word.deleteCharAt(word.length() - 1);
			}
		}
	}

	private Iterable<Coordinate> getNeighbors(int i, int j, int row, int col) {
		Bag<Coordinate> neighbors = new Bag<Coordinate>();
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
		return neighbors;
	}


	/* Private instance variables */
	private Lexicon lex;
	private char[][] boggle;

	private static class Coordinate {
		public Coordinate(int x, int y) {
			this.x = x;
			this.y = y;
		}
		int x;
		int y;
	}
	
//	private void displayBoard(char[][] boogle) {
//		for (int i = 0; i < boggle.length; i++) {
//			for (int j = 0; j < boggle[0].length; j++) {
//				System.out.print(boggle[i][j] + " ");
//			}
//			System.out.println();
//		}
//	}
}


