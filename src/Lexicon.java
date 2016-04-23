
public class Lexicon {
	public Lexicon() {
		root = new LexNode();
	}
	
	public static void main(String[] args) {
		Lexicon lex = new Lexicon();
		lex.put("ABC");
		System.out.println(lex.containsPrefix("AB"));
		System.out.println(lex.contains("ABC"));
	}
	
	public boolean isEmpty() {
		return (N == 0);
	}
	
	public int size() {
		return N;
	}
	
	public boolean contains(String word) {
		LexNode n = get(root, word, 0);
		if (n == null) return false;
		return n.isWord;
	}

	public LexNode getLexNode(String word) {
		if (word.equals("")) return root;
		return get(root, word, 0);
	}
	
	public boolean containsPrefix(String prefix) {
		LexNode n = get(root, prefix, 0);
		return n != null;
	}
	
	public void put(String word) {
		if(!contains(word)) N++;
		put(root, word, 0);
	}
	
	private void put(LexNode cur, String word, int d) {
		char c = Character.toUpperCase(word.charAt(d));
		int childIndex = c - 'A';
		if (cur.next[childIndex] == null) {
			cur.next[childIndex] = new LexNode();
		}
		if (d == word.length() - 1) {
			cur.next[childIndex].isWord = true;
		} else {
			put(cur.next[childIndex], word, ++d);
		}
	}
	
	private LexNode get(LexNode cur, String word, int d) {
		if (cur == null) return null;
 		if (d == word.length()) {
			return cur;
		}
		char c = Character.toUpperCase(word.charAt(d));
		int childIndex = c - 'A';
		return get(cur.next[childIndex], word, ++d);
	}
	
	private LexNode root;
	private int N;
}