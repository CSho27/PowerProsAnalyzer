import java.awt.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Team {
	private String name;
	private ArrayList<Hitter> hitters = new ArrayList<>();
	private ArrayList<Pitcher> pitchers = new ArrayList<>();
	private ArrayList<Player> roster = new ArrayList<>();
	private Double totalRating;
	
	private int overall;
	private int pitcherOverall;
	private int hitterOverall;
	boolean complete;
	
	
	public Team(String newName){
		name = newName.toUpperCase();
	}
	
	public ArrayList<Hitter> getHitters(){
		return hitters;
	}
	
	public ArrayList<Pitcher> getPitchers(){
		return pitchers;
	}

	
	public ArrayList<Player> getRoster(){
		return roster;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String newName){
		name = newName.toUpperCase();
	}
	
	public Double getTotalRating(){
		return totalRating;
	}
	
	public int getOverall(){
		return overall;
	}
	
	public int getHitterOverall(){
		return hitterOverall;
	}
	
	public int getPitcherOverall(){
		return pitcherOverall;
	}
	
	public boolean isComplete(){
		return complete;
	}
	
	public void addPlayer(Player player){
		if(player.getPrimary() > 1){
			hitters.add((Hitter) player);
			roster.add(player);
		}
		else{
			pitchers.add((Pitcher) player);
			roster.add(player);
		}	
	}
	
	public void addPlayers(ArrayList<Player> players){
		for(int i=0; i<players.size(); i++){
			addPlayer(players.get(i));
		}
	}
	
	public void overallSetter(){
		sort();
		Hitter[] lineup;
		complete = true;
		double sum = 0;
		double hitterRating;
		double pitcherRating = 0;
		if((lineup = generateLineup()) != null){
			for(int i=2; i<lineup.length; i++){
				sum += lineup[i].getTotalRating();
			}
			hitterRating = (sum/9);
			hitterRating = (-.01010101010101)*((hitterRating-99.0)*(hitterRating-99.0))+99;
			double pRat = (.0001)*(hitterRating*hitterRating);
			hitterRating = (hitterRating-((100-hitterRating)*pRat));
			hitterRating = hitterRating*(100.0/98);
			hitterOverall = (int) hitterRating;
		}
		else{
			if(hitters.size()<9){
				complete = false;
			}
			for(int i=0; i<hitters.size(); i++){
				sum += hitters.get(i).getTotalRating();
			}
			hitterRating = (sum/hitters.size());
			hitterRating = (-.01010101010101)*((hitterRating-99.0)*(hitterRating-99.0))+99;
			double pRat = (.0001)*(hitterRating*hitterRating);
			hitterRating = (hitterRating-((100-hitterRating)*pRat));
			hitterRating = hitterRating*(100.0/98);
			hitterOverall = (int) hitterRating;
		}
		Pitcher[] rotation;
		if((rotation = generateRotation()) != null){
			pitcherRating = rotation[0].getTotalRating()*.85;
			if(rotation.length > 1){
				pitcherRating += rotation[1].getTotalRating()*.14;
				if(rotation.length > 2){
					pitcherRating += rotation[2].getTotalRating()*.01;
				}
			}
			pitcherOverall = (int) (pitcherRating*(100.0/99));
		}
		else{
			complete = false;
		}
		
		if(hitterRating>0 && pitcherRating>0){
			totalRating = (pitcherRating + hitterRating)/2;
			totalRating = totalRating;
		}
		else{
			totalRating = (double) -1;
		}
		overall = (int) totalRating.intValue();
	}
	
	public String toString(){
		String teamString = "Team: "+name+"\nHitting: "+hitterOverall+"\nPitching: "+pitcherOverall+"\nOverall: "+overall+"\nHitters:\n";
		for(int i=0; i<hitters.size(); i++){
			Player player = hitters.get(i);
			if(i+1<10)
				teamString += (i+1)+".  "+player.toStringLine()+"\n";
			else
				teamString += (i+1)+". "+player.toStringLine()+"\n";
		}
		teamString += "Pitchers:\n";
		for(int i=0; i<pitchers.size(); i++){
			Player player = pitchers.get(i);
			if(i+1<10)
				teamString += (i+1)+".  "+player.toStringLine()+"\n";
			else
				teamString += (i+1)+". "+player.toStringLine()+"\n";
		}
		return teamString;
	}
	
	public String toPositionalString(){
		String teamString = "Team: "+name+"\nHitting: "+hitterOverall+"\nPitching: "+pitcherOverall+"\nOverall: "+overall+"\n";
		roster = Analyzer.sortByPoisition(roster);
		for(int i=0; i<roster.size(); i++){
			Player player = roster.get(i);
			teamString += (i+1)+".  "+player.toStringLine()+"\n";
		}
		return teamString;
	}
	
	public String toStringLine(){
		String teamString;
		if(name.length() == 2)
			teamString = name+"    O:"+overall+"  H:"+hitterOverall+"  P:"+pitcherOverall;
		else
			teamString = name+"   O:"+overall+"  H:"+hitterOverall+"  P:"+pitcherOverall;		
		return teamString;
	}
	
	public void print(){
		System.out.println(toString());
	}
	
	public void printSorted(){
		sort();
		System.out.println(toString());
	}
	
	public void sort(){
		Collections.sort(hitters, new PlayerCompare());
		Collections.sort(pitchers, new PlayerCompare());
		Collections.sort(roster, new PlayerCompare());
	}
	
	public void writeTeamFile(String filepath){	
		sort();
		PlayerWriter writer = new PlayerWriter(filepath);
		for(int i=0; i<hitters.size(); i++){
			Hitter hitter = hitters.get(i);
			writer.writeOne(hitter);
		}
		writer.close();
	}
	
	public Hitter[] generateLineup(){
		sort();
		Hitter[] lineup = new Hitter[11];
		int playersSet = 0;
		int current = 0;
		while(playersSet < 9 && current < hitters.size()){
			Hitter hitter = hitters.get(current);
			if(lineup[hitter.getPrimary()] == null){
				lineup[hitter.getPrimary()] = hitter;
				playersSet++;
			}
			else{
				if(hitter.getSecondary() > 1 && lineup[hitter.getSecondary()] == null){
					lineup[hitter.getSecondary()] = hitter;
					playersSet++;
				}
				else{
					if(lineup[10] == null){
						lineup[10] = hitter;
						playersSet++;
					}
					else{
						boolean set = false;
						int pos = 2;
						while(!set && pos<10){
							if(lineup[pos] == null && hitter.canPlay(pos)){
								lineup[pos] = hitter;
								set = true;
								playersSet++;
							}
							pos++;
						}
					}
				}
			}
			current++;
		}
		if(playersSet == 9){
			return lineup;
		}
		else{
			return null;
		}
	}
	
	public Pitcher[] generateRotation(){
		sort();
		Pitcher[] rotation;
		if(pitchers.size()<3)
			rotation = new Pitcher[pitchers.size()];
		else
			rotation = new Pitcher[3];
		boolean starterFilled = false;
		int i=0, starter = 0;
		while(i<pitchers.size() && !starterFilled){
			if(pitchers.get(i).getPrimary()==1){
				rotation[0] = pitchers.get(i);
				starter = i;
				starterFilled = true;
			}	
			i++;
		}
		if(!starterFilled){
			return null;
		}
		else{
			i=0;
			int filled = 1;
			while(i<pitchers.size() && filled < 3){
				if(i != starter){
					rotation[filled] = pitchers.get(i);
					filled++;
				}	
				i++;
			}
			return rotation;
		}
	}
	
	public String lineupString(){
		Hitter[] hitters;
		if((hitters = generateLineup()) != null){
			String lineupString = "Team: "+name+"\nHitting: "+hitterOverall+"\nPitching: "+pitcherOverall+"\nOverall: "+overall+"\nLineup:\n";
			for(int i=2; i<11; i++){
				lineupString += posNumToString(i)+" "+hitters[i].toStringLine()+"\n";
			}
			Pitcher[] rotation;
			if((rotation = generateRotation()) != null){
				for(int i=0; i<rotation.length; i++){
					lineupString += "P  "+rotation[i].toStringLine()+"\n";
				}
			}
			else{
				for(int i=0; i<pitchers.size() && i<3; i++){
					lineupString += "P  "+pitchers.get(i).toStringLine()+"\n";
				}
			}
			return lineupString;
		}
		else{
			return null;
		}
	}
	
	public void printLineup(){
		String lineupString = lineupString();
		if(lineupString != null)
			System.out.println(lineupString);
	}
	
	//Utilities
	public String posNumToString(int posNum)
	{
		String position;
		String[] posList = {"RP", "SP", "C ", "1B", "2B", "3B", "SS", "LF", "CF", "RF", "DH"};
		position = posList[posNum];
		return position;				
	}
}
