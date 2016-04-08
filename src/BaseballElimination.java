import java.util.ArrayList;

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
public class BaseballElimination {
	/**
	 * Constructor: 
	 * Usage: BaseballElimination be = new BaseballElimination(filename);
	 * @param filename create object from this file
	 * create a baseball division from given filename in format specified below
	 * 4
	 * Atlanta       83 71  8  0 1 6 1
	 * Philadelphia  80 79  3  1 0 0 2
	 * New_York      78 78  6  6 0 0 0
	 * Montreal      77 82  3  1 2 0 0
	 */
	@SuppressWarnings("unchecked")
	public BaseballElimination(String filename) {
		In in = new In(filename);
		N = in.readInt();
		teams = new ArrayList<String>(N);
		w = new int[N];
		l = new int[N];
		r = new int[N];
		g = new int[N][N];
		subsets = (ArrayList<String>[]) new ArrayList[N];
		leader = 0;
		leaderWins = 0;
		for(int i = 0; i < N; i++) {
			teams.add(in.readString());
			w[i] = in.readInt();
			if (w[i] > leaderWins) {
				leaderWins = w[i];
				leader = i;
			}
			l[i] = in.readInt();
			r[i] = in.readInt();
			for(int j = 0; j < N; j++) {
				g[i][j] = in.readInt();
			}
		}

		calcElimination();
	}
	/**
	 * Method: numbersOfTeams
	 * Usage: int n = be.numbersOfTeams();
	 * @return the total number of teams
	 */
	public int numberOfTeams() {
		return N;
	}
	/**
	 * Method: teams
	 * Usage: for (String team : be.teams()) ...
	 * @return all teams name
	 */
	public Iterable<String> teams() {
		return teams;
	}
	/**
	 * Method: wins
	 * Usage: int wins = be.wins(team);
	 * @param team name of the team
	 * @return the wins of this team
	 */
	public int wins(String team) {
		validateTeam(team);
		return w[teams.indexOf(team)];
	}
	/**
	 * Method: losses
	 * Usage: int losses = be.losses(team);
	 * @param team name of the team
	 * @return the loss of this team
	 */
	public int losses(String team) {
		validateTeam(team);
		return l[teams.indexOf(team)];
	}
	/**
	 * Method: remaining
	 * int remaining = be.remaining(team);
	 * @param team name of the team
	 * @return the remaining games of this team
	 */
	public int remaining(String team) {
		validateTeam(team);
		return r[teams.indexOf(team)];
	}
	/**
	 * Method: against
	 * Usage: int against = be.against(team1, team2);
	 * @param team1 team1's name
	 * @param team2 team2's name
	 * @return number of remaining games between team1 and team2
	 */
	public int against(String team1, String team2) {
		validateTeam(team1);
		validateTeam(team2);
		return g[teams.indexOf(team1)][teams.indexOf(team2)];
	}
	/**
	 * Method: isEliminated
	 * Usage: if(be.isEliminated(team))...
	 * @param team team name
	 * @return true if the given team eliminated, otherwise false.
	 */
	public boolean isEliminated(String team) {
		validateTeam(team);
		return (subsets[teams.indexOf(team)] != null);
	}
	// 
	/**
	 * Method: certificateOfElimination
	 * Usage: for(String team : be.certificateOfElimination(team)). . .
	 * @param team
	 * @return subset R of teams that eliminates given team; null if not eliminated
	 */
	public Iterable<String> certificateOfElimination(String team) {
		validateTeam(team);
		return subsets[teams.indexOf(team)];
	}
	/**
	 * Testing main function
	 * @param args args[0] filename
	 */
	public static void main(String[] args) {
	    BaseballElimination division = new BaseballElimination(args[0]);
	    for (String team : division.teams()) {
	        if (division.isEliminated(team)) {
	            StdOut.print(team + " is eliminated by the subset R = { ");
	            for (String t : division.certificateOfElimination(team)) {
	                StdOut.print(t + " ");
	            }
	            StdOut.println("}");
	        }
	        else {
	            StdOut.println(team + " is not eliminated");
	        }
	    }
	}
	/* Private Section */
	/**
	 * Method: validateTeam
	 * Usage: if (validateTeam(team)) . . .
	 * @param team name
	 */
	private void validateTeam(String team) {
		if (teams.indexOf(team) < 0) 
			throw new java.lang.IllegalArgumentException();
	}
	/**
	 * Method: trivialElimination
	 * @param x the current team index number
	 * @param i the leader team index number
	 * @return true if the current team is trivialEliminated.
	 * w[x] + r[x] < w[i]
	 */
	private boolean trivialElimination(int x, int i) {
		if (w[x] + r[x] - w[i] < 0) {
			subsets[x] = new ArrayList<String>();
			subsets[x].add(teams.get(i));
			return true;
		}
		return false;
	}
	/**
	 * Method: calcElimination
	 * Usage: calcElimination();
	 * construct the FlowNetwork to represent the elimination,use FordFulkerson algorithm to compute.
	 */
	private void calcElimination() {
		int fnV = (N * N - N) / 2 + 2 + 1;
		for(int i = 0; i < N; i++) {
			if (trivialElimination(i, leader)) continue;
			FlowNetwork fn = new FlowNetwork(fnV);
			int curV = 1, maxFlow = 0;
			for (int j = 0; j < N; j++) {
				if (j == i) continue;
				for (int k = j + 1; k < N; k++) {
					if (k == i || g[j][k] == 0) continue;
					maxFlow += g[j][k];
					fn.addEdge(new FlowEdge(0, curV, g[j][k]));
					fn.addEdge(new FlowEdge(curV, fnV - 2 - j, Double.POSITIVE_INFINITY));
					fn.addEdge(new FlowEdge(curV, fnV - 2 - k, Double.POSITIVE_INFINITY));
					curV++;
				}
			}
			for (int n = 0; n < N; n++) {
				if (n == i) continue;
				fn.addEdge(new FlowEdge(fnV - 2 - n, fnV - 1, w[i] + r[i] - w[n]));
			}
//			System.out.println(fn.toString());
//			System.out.println("******************");
			FordFulkerson ff = new FordFulkerson(fn, 0, fnV - 1);
			if (ff.value() == maxFlow) continue;
			subsets[i] = new ArrayList<String>();
			for (int n = 0; n < N; n++) {
				if (ff.inCut(fnV - 2 - n))
					subsets[i].add(teams.get(n));
			}
		}
	}
	/* Private Instance Variables */
	private int N;						/* Total team number */
	private ArrayList<String> teams;	/* name of each team */
	private int leader, leaderWins;		/* current leader and his wins */
	private int[] w;					/* wins of each team */
	private int[] l;					/* loss of each team */
	private int[] r;					/* remaining games of each team */
	private int[][] g;					/* games left to play with each teams */
	private ArrayList<String>[] subsets;/* the Elimination subset of each teams */
}