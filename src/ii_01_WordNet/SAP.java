package ii_01_WordNet;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
	// constructor takes a digraph (not necessarily a DAG)
	public SAP(Digraph G)
	{
		this.G = new Digraph(G);
	}

	// length of shortest ancestral path between v and w; -1 if no such path
	public int length(int v, int w)
	{
		validateVertex(v);
		validateVertex(w);
		BreadthFirstDirectedPaths ancestorV = new BreadthFirstDirectedPaths(G, v);
		BreadthFirstDirectedPaths ancestorW = new BreadthFirstDirectedPaths(G, w);
		int result = -1;
		for (int anc = 0; anc < G.V(); anc++) {
			if (ancestorV.hasPathTo(anc) && ancestorW.hasPathTo(anc)) {
				int currentDist = ancestorV.distTo(anc) + ancestorW.distTo(anc);
				if (result < 0||currentDist < result) {
					result = currentDist;
				}
			}
		}
        return result;
	}

	// a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
	public int ancestor(int v, int w)
	{
		validateVertex(v);
		validateVertex(w);
		BreadthFirstDirectedPaths ancestorV = new BreadthFirstDirectedPaths(G, v);
		BreadthFirstDirectedPaths ancestorW = new BreadthFirstDirectedPaths(G, w);
		int dist = -1,ancResult = -1;
		for (int anc = 0; anc < G.V(); anc++) {
			if (ancestorV.hasPathTo(anc) && ancestorW.hasPathTo(anc)) {
				int currentDist = ancestorV.distTo(anc) + ancestorW.distTo(anc);
				if (dist < 0||currentDist < dist) {
					dist = currentDist;
					ancResult = anc;
				}
			}
		}
        return ancResult;
	}

	// length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
	public int length(Iterable<Integer> v, Iterable<Integer> w)
	{
		for(int x : v)
			validateVertex(x);
		for(int y : w)
			validateVertex(y);
		BreadthFirstDirectedPaths ancestorV = new BreadthFirstDirectedPaths(G, v);
		BreadthFirstDirectedPaths ancestorW = new BreadthFirstDirectedPaths(G, w);
		int result = -1;
		for (int anc = 0; anc < G.V(); anc++) {
			if (ancestorV.hasPathTo(anc) && ancestorW.hasPathTo(anc)) {
				int currentDist = ancestorV.distTo(anc) + ancestorW.distTo(anc);
				if (result < 0||currentDist < result) {
					result = currentDist;
				}
			}
		}
		return result;
	}

	// a common ancestor that participates in shortest ancestral path; -1 if no such path
	public int ancestor(Iterable<Integer> v, Iterable<Integer> w)
	{
		for(int x : v)
			validateVertex(x);
		for(int y : w)
			validateVertex(y);
		BreadthFirstDirectedPaths ancestorV = new BreadthFirstDirectedPaths(G, v);
		BreadthFirstDirectedPaths ancestorW = new BreadthFirstDirectedPaths(G, w);
		int dist = -1,ancResult = -1;
		for (int anc = 0; anc < G.V(); anc++) {
			if (ancestorV.hasPathTo(anc) && ancestorW.hasPathTo(anc)) {
				int currentDist = ancestorV.distTo(anc) + ancestorW.distTo(anc);
				if (dist < 0||currentDist < dist) {
					dist = currentDist;
					ancResult = anc;
				}
			}
		}
        return ancResult;
	}

	// do unit testing of this class
	public static void main(String[] args)
	{
		In in = new In(args[0]);
		Digraph G = new Digraph(in);
		SAP sap = new SAP(G);
		while (!StdIn.isEmpty()) 
		{
			int v = StdIn.readInt();
			int w = StdIn.readInt();
			System.out.println(v + ", " + w);
			int length   = sap.length(v, w);
			int ancestor = sap.ancestor(v, w);
			StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
		}
	}


	/* Private Section */
	private void validateVertex(int v) {
		if (v < 0 || v >= G.V())
            throw new IndexOutOfBoundsException("vertex " + v + " is not between 0 and " + (G.V()-1));
	}

	/* Private Instance variables */
	private Digraph G;
}
