import java.util.Comparator;

public class TeamCompare implements Comparator<Team>{
	@Override
	public int compare(Team team1, Team team2){
		return team2.getTotalRating().compareTo(team1.getTotalRating());
	}
}