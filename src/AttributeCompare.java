import java.util.Comparator;

public class AttributeCompare implements Comparator<Player>{
	private String key;
	@Override
	public int compare(Player player1, Player player2){
		Double zero = 0.0;
		if(isHitterKey(key)){
			if(player1.getPrimary()>1 && player2.getPrimary()>1)
			{
				Hitter hitter1 = (Hitter) player1;
				Hitter hitter2 = (Hitter) player2;
				if(hitter2.getAttribute(key) == hitter1.getAttribute(key))
					return hitter2.getTotalRating().compareTo(hitter1.getTotalRating());
				else
					return hitter2.getAttribute(key).compareTo(hitter1.getAttribute(key));
			}
			else{
				if(player1.getPrimary()<2 && player2.getPrimary()<2){
					return player2.getTotalRating().compareTo(player1.getTotalRating());
				}
				else{
					if(player1.getPrimary()>1)
						return zero.compareTo(player1.getTotalRating());
					else
						return player2.getTotalRating().compareTo(zero);
				}
			}
		}else{
			if(player1.getPrimary()<2 && player2.getPrimary()<2)
			{
				Pitcher pitcher1 = (Pitcher) player1;
				Pitcher pitcher2 = (Pitcher) player2;
				if(pitcher2.getAttribute(key) == pitcher1.getAttribute(key))
					return pitcher2.getTotalRating().compareTo(pitcher1.getTotalRating());
				else
					return pitcher2.getAttribute(key).compareTo(pitcher1.getAttribute(key));
			}
			else{
				if(player1.getPrimary()>1 && player2.getPrimary()>1){
					return player2.getTotalRating().compareTo(player1.getTotalRating());
				}
				else{
					if(player1.getPrimary()<2)
						return zero.compareTo(player1.getTotalRating());
					else
						return player2.getTotalRating().compareTo(zero);
				}
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
