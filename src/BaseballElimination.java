import java.util.HashMap;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
public class BaseballElimination {
	// create a baseball division from given filename in format specified below
	public BaseballElimination(String filename) {
		In in = new In(filename);
		teamNumber = in.readInt();
		teams = new String[teamNumber];
		idMap = new HashMap<String, Integer>();
		w = new int[teamNumber];
		l = new int[teamNumber];
		r = new int[teamNumber];
		g = new int[teamNumber][teamNumber];
		for(int i = 0; in.hasNextChar(); i++) {
			teams[i] = in.readString();
			idMap.put(teams[i], i);
			w[i] = in.readInt();
			l[i] = in.readInt();
			r[i] = in.readInt();
			for(int j = 0; j < teamNumber; j++) {
				g[i][j] = in.readInt();
			}
		}
	}
	// number of teams
	public int numberOfTeams() {
		return teamNumber;
	}
	// all teams
	public Iterable<String> teams() {
		return idMap.keySet();
	}
	// number of wins for given team
	public int wins(String team) {
		validateTeam(team);
		return w[idMap.get(team)];
	}
	// number of losses for given team
	public int losses(String team) {
		validateTeam(team);
		return l[idMap.get(team)];
	}
	// number of remaining games for given team
	public int remaining(String team) {
		validateTeam(team);
		return r[idMap.get(team)];
	}
	// number of remaining games between team1 and team2
	public int against(String team1, String team2) {
		validateTeam(team1);
		validateTeam(team2);
		return g[idMap.get(team1)][idMap.get(team2)];
	}
	// is given team eliminated?
	public boolean isEliminated(String team) {
		validateTeam(team);
		return false;
	}
	// subset R of teams that eliminates given team; null if not eliminated
	public Iterable<String> certificateOfElimination(String team) {
		validateTeam(team);
		return null;
	}
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
	private void validateTeam(String team) {
		if (!idMap.containsKey(team)) 
			throw new java.lang.IllegalArgumentException();
	}
	/* Private Instance Variables */
	int teamNumber;				/* Total team number */
	String[] teams;				/* name of each team */
	HashMap<String, Integer> idMap; 	/* id of each team */
	int[] w;					/* wins of each team */
	int[] l;					/* loss of each team */
	int[] r;					/* remaining games of each team */
	int[][] g;					/* games left to play with each teams */
}