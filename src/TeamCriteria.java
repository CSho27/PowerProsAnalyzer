
public class TeamCriteria extends Criteria{
	String[] ALEast = {"NYY", "BOS", "BAL", "TB", "TOR"};
	String[] ALCentral = {"CLE", "CHW", "MIN", "DET", "KC"};
	String[] ALWest = {"TEX", "SEA", "LAA", "HOU", "OAK"};
	String[] NLEast = {"Phi", "WAS", "NYM", "FL", "ATL"};
	String[] NLCentral = {"CHC", "STL", "PIT", "CIN", "MIL"};
	String[] NLWest = {"SF", "ARI", "SD", "COL", "LA"};
	String[][] divisions = {ALEast, ALCentral, ALWest, NLEast, NLCentral, NLWest};
	
	public TeamCriteria(double newStartingProb) {
		super("Team", newStartingProb);
	}

	@Override
	public void generate() {
		int div = (int) (Math.random()*6);
		int t = (int) (Math.random()*5);
		key = divisions[div][t];
	}

}
