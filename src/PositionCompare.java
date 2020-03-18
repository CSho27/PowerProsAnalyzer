import java.util.Comparator;

public class PositionCompare implements Comparator<Player>{
	private String key;
	@Override
	public int compare(Player player1, Player player2){
		Double zero = 0.0;
		if(player1.getPrimary()>1 && player2.getPrimary()>1)
		{
			if(player1.getPrimary() == player2.getPrimary())
				return player2.getTotalRating().compareTo(player1.getTotalRating());
			else
				return player1.getPrimary().compareTo(player2.getPrimary());
		}
		else{
			if(player1.getPrimary()<2 && player2.getPrimary()<2){
				if(player1.getPrimary() == player2.getPrimary())
					return player2.getTotalRating().compareTo(player1.getTotalRating());
				else
					return player2.getPrimary().compareTo(player1.getPrimary());
			}
			else{
				if(player1.getPrimary()>1)
					return zero.compareTo(player1.getTotalRating());
				else
					return player2.getTotalRating().compareTo(zero);
			}
		}
	}
	
	public void setKey(String newKey){
		key = newKey;
	}
	
	public boolean isHitterKey(String key){
		String[] hitterAttributes = {
				"CON",
				"PWR",
				"SPD",
				"ARM",
				"FLD",
				"TRJ",
				"eRes"};
		for(int i=0; i<hitterAttributes.length; i++){
			if(hitterAttributes[i].equals(key))
				return true;
		}
		return false;
	}
	
	public boolean isPitcherKey(String key){
		String[] pitcherAttributes = {
				"VELO",
				"CTRL",
				"STAM",
				"STRT",
				"SLD",
				"CRV",
				"FORK",
				"SINK",
				"SKFB"};
		for(int i=0; i<pitcherAttributes.length; i++){
			if(pitcherAttributes[i].equals(key))
				return true;
		}
		return false;
	}
	
	public String getKey(){
		return key;
	}
	
	
}
