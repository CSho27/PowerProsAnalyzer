import java.util.Comparator;

public class PlayerCompare implements Comparator<Player>{
	@Override
	public int compare(Player player1, Player player2){
		return player2.getTotalRating().compareTo(player1.getTotalRating());
	}
}

