import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Analyzer {
	public static ArrayList<Player> searchByFirstLetter(String filename, char character){
		PlayerReader reader = new PlayerReader(filename);
		ArrayList<Player> playerList = new ArrayList<Player>();
		Player player;
		while((player = reader.readByFirstLetter(character)) != null){
			playerList.add(player);
		}
		reader.reset();
		playerList = sort(playerList);
		return playerList;
	}
	
	public static ArrayList<Player> NarrowByFirstName(ArrayList<Player> players, String sequence){
		ArrayList<Player> playerList = new ArrayList<Player>();
		for(int i=0; i<players.size(); i++){
			if(players.get(i).getFirstName().toUpperCase().contains(sequence.toUpperCase()))
				playerList.add(players.get(i));
		}
		playerList = sort(playerList);
		return playerList;
	}
	
	public static ArrayList<Player> NarrowByLastName(ArrayList<Player> players, String sequence){
		if(players != null){
			ArrayList<Player> playerList = new ArrayList<Player>();
			for(int i=0; i<players.size(); i++){
				if(players.get(i).getLastName().toUpperCase().contains(sequence.toUpperCase()))
					playerList.add(players.get(i));
			}
			playerList = sort(playerList);
			return playerList;
		}
		else
		{
			return null;
		}
	}
	
	public static ArrayList<Player> NarrowByLastNameLength(ArrayList<Player> players, int length){
		if(players != null){
			ArrayList<Player> playerList = new ArrayList<Player>();
			for(int i=0; i<players.size(); i++){
				if(players.get(i).getLastName().length() == length)
					playerList.add(players.get(i));
			}
			playerList = sort(playerList);
			return playerList;
		}
		else
		{
			return null;
		}
	}
	
	public static ArrayList<Player> NarrowByFirstLetterOfLastName(ArrayList<Player> players, char letter){
		ArrayList<Player> playerList = new ArrayList<Player>();
		for(int i=0; i<players.size(); i++){
			if(players.get(i).getLastName().charAt(0) == letter)
				playerList.add(players.get(i));
		}
		playerList = sort(playerList);
		return playerList;
	}
	
	public static ArrayList<Player> NarrowByFirstLetterOfFirstName(ArrayList<Player> players, char letter){
		ArrayList<Player> playerList = new ArrayList<Player>();
		for(int i=0; i<players.size(); i++){
			if(players.get(i).getFirstName().charAt(0) == letter)
				playerList.add(players.get(i));
		}
		playerList = sort(playerList);
		return playerList;
	}
	
	public static ArrayList<Player> NarrowByLastLetterOfLastName(ArrayList<Player> players, char letter){
		ArrayList<Player> playerList = new ArrayList<Player>();
		for(int i=0; i<players.size(); i++){
			if(players.get(i).getLastName().charAt(players.get(i).getLastName().length()-1) == letter)
				playerList.add(players.get(i));
		}
		playerList = sort(playerList);
		return playerList;
	}
	
	public static ArrayList<Player> NarrowByLastLetterOfFirstName(ArrayList<Player> players, char letter){
		ArrayList<Player> playerList = new ArrayList<Player>();
		for(int i=0; i<players.size(); i++){
			if(players.get(i).getFirstName().charAt(players.get(i).getFirstName().length()-1) == letter)
				playerList.add(players.get(i));
		}
		playerList = sort(playerList);
		return playerList;
	}
	
	public static ArrayList<Player> NarrowByTeam(ArrayList<Player> players, String team){
		ArrayList<Player> playerList = new ArrayList<Player>();
		for(int i=0; i<players.size(); i++){
			if(players.get(i).getTeam().toUpperCase().contains(team.toUpperCase()))
				playerList.add(players.get(i));
		}
		playerList = sort(playerList);
		return playerList;
	}
	
	public static ArrayList<Player> NarrowByPrimary(ArrayList<Player> players, int primary){
		ArrayList<Player> playerList = new ArrayList<Player>();
		for(int i=0; i<players.size(); i++){
			if(players.get(i).getPrimary() == primary)
				playerList.add(players.get(i));
		}
		playerList = sort(playerList);
		return playerList;
	}
	
	public static ArrayList<Player> NarrowByRow(ArrayList<Player> players, int row){
		ArrayList<Player> playerList = new ArrayList<Player>();
		for(int i=0; i<players.size(); i++){
			if(players.get(i).getRow() == row)
				playerList.add(players.get(i));
		}
		playerList = sort(playerList);
		return playerList;
	}
	
	public static ArrayList<Player> NarrowByColumn(ArrayList<Player> players, int column){
		ArrayList<Player> playerList = new ArrayList<Player>();
		for(int i=0; i<players.size(); i++){
			if(players.get(i).getColumn() == column)
				playerList.add(players.get(i));
		}
		playerList = sort(playerList);
		return playerList;
	}
	
	public static ArrayList<Player> NarrowBySecondary(ArrayList<Player> players, int secondary){
		ArrayList<Player> playerList = new ArrayList<Player>();
		for(int i=0; i<players.size(); i++){
			if(players.get(i).getPrimary()>1){
				Hitter hitter = (Hitter) players.get(i);
				if(hitter.getSecondary() == secondary)
					playerList.add(players.get(i));
			}
		}
		playerList = sort(playerList);
		return playerList;
	}
	
	public static ArrayList<Player> NarrowByCapability(ArrayList<Player> players, int position){
		ArrayList<Player> playerList = new ArrayList<Player>();
		for(int i=0; i<players.size(); i++){
			if(players.get(i).getPrimary() == position)
				playerList.add(players.get(i));
			else{
				if(players.get(i).getPrimary()>1){
					Hitter hitter = (Hitter) players.get(i);
					if(hitter.canPlay(position))
						playerList.add(players.get(i));
				}
				else{
					if(players.get(i).getPrimary()>-1 && position<2 && position>-1)
						playerList.add(players.get(i));
				}
			}
		}
		playerList = sort(playerList);
		return playerList;
	}
	
	public static ArrayList<Player> NarrowByOverall(ArrayList<Player> players, int overall, int gtLtEt){
		if(gtLtEt<0 || gtLtEt>2){
			System.out.println(gtLtEt);
			return null;
		}
		else{
			ArrayList<Player> playerList = new ArrayList<Player>();
			for(int i=0; i<players.size(); i++){
				switch(gtLtEt){
				//Greater than
				case 0:
					if(players.get(i).getOverall() >= overall)
						playerList.add(players.get(i));
					break;
				//Less than
				case 1:
					if(players.get(i).getOverall() <= overall)
						playerList.add(players.get(i));
					break;
				case 2:
					if(players.get(i).getOverall() == overall)
						playerList.add(players.get(i));
					break;
				default:
					break;
				}
			}
			playerList = sort(playerList);
			return playerList;
		}
	}
	
	public static ArrayList<Player> narrowByAttribute(ArrayList<Player> players, String statKey, boolean isPitcher, int value, int gtLtEt){
		if(gtLtEt<0 || gtLtEt>2){
			System.out.println(gtLtEt);
			return null;
		}
		else{
			ArrayList<Player> playerList = new ArrayList<Player>();
			for(int i=0; i<players.size(); i++){
				int attribute = 0;
				switch(gtLtEt){
				//Greater than
				case 0:
					if(players.get(i).getPrimary()<2 == isPitcher){
						if(players.get(i).getAttribute(statKey) >= value)
							playerList.add(players.get(i));
					}
					break;
				//Less than
				case 1:
					if(players.get(i).getPrimary()<2 == isPitcher){
						if(players.get(i).getAttribute(statKey) <= value)
							playerList.add(players.get(i));
					}
					break;
				//equal to
				case 2:
					if(players.get(i).getPrimary()<2 == isPitcher){
						if(players.get(i).getAttribute(statKey) == value)
							playerList.add(players.get(i));
					}
					break;
				default:
					break;
				}
			}
			playerList = sort(playerList);
			return playerList;
		}
	}
	
	public static ArrayList<Player> searchByPosition(String filename, int position){
		PlayerReader reader = new PlayerReader(filename);
		ArrayList<Player> playerList = new ArrayList<Player>();
		Player player;
		while((player = reader.readByCapability(position)) != null){
			playerList.add(player);
		}
		reader.reset();
		playerList = sort(playerList);
		return playerList;
	}
	
	public static ArrayList<Player> searchByPrimary(String filename, int position){
		PlayerReader reader = new PlayerReader(filename);
		ArrayList<Player> playerList = new ArrayList<Player>();
		Player player;
		while((player = reader.readByPosition(position)) != null){
			playerList.add(player);
		}
		reader.reset();
		playerList = sort(playerList);
		return playerList;
	}
	
	public static ArrayList<Player> searchByColumn(String filename, int column){
		PlayerReader reader = new PlayerReader(filename);
		ArrayList<Player> playerList = new ArrayList<Player>();
		Player player;
		while((player = reader.readByColumn(column)) != null){
			playerList.add(player);
		}
		reader.reset();
		playerList = sort(playerList);
		return playerList;
	}
	
	public static ArrayList<Player> searchByRow(String filename, int row){
		PlayerReader reader = new PlayerReader(filename);
		ArrayList<Player> playerList = new ArrayList<Player>();
		Player player;
		while((player = reader.readByRow(row)) != null){
			playerList.add(player);
		}
		reader.reset();
		playerList = sort(playerList);
		return playerList;
	}
	
	public static ArrayList<Player> searchByTeam(String filename, String team){
		PlayerReader reader = new PlayerReader(filename);
		ArrayList<Player> playerList = new ArrayList<Player>();
		Player player;
		while((player = reader.readByTeam(team)) != null){
			playerList.add(player);
		}
		reader.reset();
		playerList = sort(playerList);
		return playerList;
	}
	

	
	public static ArrayList<Player> sort(ArrayList<Player> players){
		Collections.sort(players, new PlayerCompare());
		return players;
	}
	
	public static ArrayList<Player> sortByPoisition(ArrayList<Player> players){
		Collections.sort(players, new PositionCompare());
		return players;
	}
	
	public static String listToString(ArrayList<Player> players){
		String string = "";
		for(int i=0; i<players.size(); i++){
			string += players.get(i).toStringLine()+"\n";
		}
		return string;
	}
	
	public static void printTeams(ArrayList<Team> teams){
		Collections.sort(teams, new TeamCompare());
		for(int i=0; i<teams.size(); i++){
			teams.get(i).print();
		}
	}
	
	public static void printTeamLineups(ArrayList<Team> teams){
		Collections.sort(teams, new TeamCompare());
		for(int i=0; i<teams.size(); i++){
			teams.get(i).printLineup();
		}
	}
	
	public static void printList(ArrayList<Player> players){
		for(int i=0; i<players.size(); i++)
			System.out.println((i+1)+". "+players.get(i).toStringLine()+" "+players.get(i).getTeam());
	}
	
	public static void printListN(ArrayList<Player> players, int n){
		for(int i=0; i<players.size() && i<n; i++)
			System.out.println((i+1)+". "+players.get(i).toStringLine()+" "+players.get(i).getTeam());
	}
	
	public static void printListWithAttributes(ArrayList<Player> players){
		for(int i=0; i<players.size(); i++){
			System.out.println((i+1)+". ");
			players.get(i).printAttributes();
		}
	}
	
	public static void writeList(String filename, ArrayList<Player> players){
		PlayerWriter writer = new PlayerWriter(filename);
		for(int i=0; i<players.size(); i++)
			writer.writeOne(players.get(i));
		writer.close();
	}
	
	public static void overwriteList(String filename, ArrayList<Player> players){
		try{
			BufferedWriter w = new BufferedWriter(new FileWriter(new File(filename)));
			w.write("");
			w.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		PlayerWriter writer = new PlayerWriter(filename);
		for(int i=0; i<players.size(); i++)
			writer.writeOne(players.get(i));
		writer.close();
	}
	
	public static void generateAllTeamFiles(String dataFilepath){
		ArrayList<Team> teams = sortIntoTeams(dataFilepath);
		for(int i=0; i<teams.size(); i++){
			teams.get(i).sort();
			teams.get(i).writeTeamFile("C:\\Users\\short\\OneDrive\\Documents\\PowerPros\\"+teams.get(i).getName()+".csv");
			Hitter[] hitters = teams.get(i).generateLineup();
			System.out.println();
		}
	}
	
	public static void generateAllLineups(String dataFilepath){
		ArrayList<Team> teams = sortIntoTeams("dataFilepath");
		for(int i=0; i<teams.size(); i++){
			teams.get(i).overallSetter();
			teams.get(i).printLineup();
			System.out.println();
		}
	}
	
	public static void generateOptimalTeam(String dataFilepath){
		PlayerReader reader = new PlayerReader(dataFilepath);
		Team all =  new Team("ALL");
		Player player;
		while((player = reader.readOne()) != null){
			all.addPlayer(player);
		}
		all.overallSetter();
		all.printLineup();
	}
	
	public static String[] importNamesFromFile(String teamFilepath){
		PlayerReader reader = new PlayerReader(teamFilepath);
		String[] names = reader.readNames();
		return names;
	}
	
	public static Team importTeamFromFile(String dataFilepath, String teamFilepath, String teamName){
		ArrayList<Player> players = readAll(teamFilepath);
		System.out.println(teamFilepath);
		printList(players);
		Team team = listToTeam(dataFilepath, players, teamName);
		return team;
	}
	
	public static Team listToTeam(String dataFilepath, ArrayList<Player> players, String teamName){
		Team team = new Team(teamName);
		for(int i=0; i<players.size(); i++){
			ArrayList<Player> searchPlayers = readAll(dataFilepath);
			//printList(players);
			searchPlayers = NarrowByLastName(searchPlayers, players.get(i).getLastName());
			if(searchPlayers.size()>1){
				searchPlayers = NarrowByFirstName(searchPlayers, players.get(i).getFirstName());
			}
			if(searchPlayers.size() == 1)
				team.addPlayer(searchPlayers.get(0));
			else
				System.out.println("Failed to add "+players.get(i).getFirstName()+" "+players.get(i).getLastName());
		}
		team.overallSetter();
		return team;
	}
	
	public static ArrayList<Team> sortIntoTeams(String filename){
		PlayerReader reader = new PlayerReader(filename);
		ArrayList<Team> teams = new ArrayList<Team>();
		Player player;
		int totalTeams = 0;
		reader.setupReader(filename);
		while((player = reader.readOne()) != null){
			boolean newTeam = true;
			int index = -1;
			for(int i=0; i<teams.size(); i++){
				if(teams.get(i).getName().equals(player.getTeam())){
					newTeam = false;
					index = i;
				}
			}
			if(newTeam){
				teams.add(new Team(player.getTeam()));
				teams.get(totalTeams).addPlayer(player);
				totalTeams++;
			}
			else{
				teams.get(index).addPlayer(player);
			}
		}
		Team current;
		for(int i=0; i<teams.size(); i++){
			current = teams.get(i);
			current.overallSetter();
		}
		return teams;
	}
	
	public static ArrayList<Player> readAll(String filename){
		PlayerReader reader = new PlayerReader(filename);
		reader.setupReader(filename);
		ArrayList<Player> players = new ArrayList<Player>();
		Player player;
		while((player = reader.readOne()) != null){
			players.add(player);
		}
		reader.close();
		return players;
	}
	
	public static String posNumToString(int posNum)
	{
		String position;
		String[] posList = {"RP", "SP", "C ", "1B", "2B", "3B", "SS", "LF", "CF", "RF", "DH"};
		position = posList[posNum];
		return position;				
	}
}
