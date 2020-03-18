import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

public class PowerProsDatabaseAnalyzer {
	static String os = System.getProperty("os.name").toUpperCase();
	static boolean onWindows = os.contains("WINDOWS");
	static String dataFile;
	static String savedTeamFile;
	static String playerData;
	static String pitcherData;
	static String hitterData;
	static String alData;
	static String nlData;
	
	
	public static void main(String[] args){
		if(onWindows){
			dataFile = System.getProperty("user.dir")+"\\data";
			savedTeamFile = System.getProperty("user.dir")+"\\data\\saved_teams";
			playerData = dataFile+"\\Players.csv";
			pitcherData = dataFile+"\\Pitchers.csv";
			hitterData = dataFile+"\\Hitters.csv";
			alData = dataFile+"\\AL.csv";
			nlData = dataFile+"\\NL.csv";
		}
		else{
			dataFile = System.getProperty("user.dir")+"/data";
			savedTeamFile = System.getProperty("user.dir")+"/data/saved_teams";
			playerData = dataFile+"/Players.csv";
			pitcherData = dataFile+"/Pitchers.csv";
			hitterData = dataFile+"/Hitters.csv";
			alData = dataFile+"/AL.csv";
			nlData = dataFile+"/NL.csv";
		}
		//Welcome Message
		System.out.println("Welcome to the MLB Power Pros Data Analysis Tool");
		System.out.println("To navigate the menus, press the number of the option you would like to select or answer -1 to quit\n");
		
		//Main Menu
		boolean quit = false;
		Scanner s = new Scanner(System.in);
		while(!quit){
			int choice;
			System.out.println("Main Menu:\n1. Statistics\n2. Player Search\n3. Custom Teams\n4. Draft Games\n5. Statistic Analysis");
			choice = atoi(s.nextLine());
			switch(choice){
				case -1:
					quit = true;
					break;
				case 1:
					quit = statistics();
					break;
				case 2:
					quit = playerSearch();
					break;
				case 3:
					quit = customTeams();
					break;
				case 4:
//					int n = 10000;
//					double[] things;
//					double avg = 0;
//					double ovr = 0;
//					int count50 = 0;
//					int count90 = 0;
//					int opponentCount = 0;
//					for(int i=0; i<n; i++){
//						things = draftTest();
//						avg += things[0];
//						if(things[0]>50)
//							count50++;
//						ovr += things[1];
//						if(things[1]>=90)
//							count90++;
//						if(things[2]>0)
//							opponentCount++;
//						if(i%1000 == 0)
//							System.out.println(i+",");
//					}
//					avg = avg/n;
//					ovr = ovr/n;
//					System.out.println("Average players to choose from: "+avg);
//					System.out.println("Overall: "+ovr);
//					System.out.println("50+ Options: "+count50);
//					System.out.println("90+ OVR:"+count90);
//					System.out.println("Opponent Picks");
					quit = draftGame();
					break;
				case 5:
					RatingsAnalysis.printPlayerAttributes("Mike", "Napoli", 2016);
					//RatingsAnalysis.overallAnalysis(playerData);
					//RatingsAnalysis.statisticAnalysis(playerData);
					//RatingsAnalysis.armAnalysis(playerData);
					quit = true;
					break;
				default:
					System.out.println();
					System.out.println("*Please enter a valid integer response based on the options given*\n");
					break;
			}
		}
	}
	
	//Statistics Section
	public static boolean statistics(){
		boolean quit = false;
		boolean up = false;
		Scanner s = new Scanner(System.in);
		while(!quit && !up){
			int choice;
			System.out.println("Statistics:\n1. Player Statistics\n2. Team Statistics\n3. Power Pros All-Star Teams\n4. Back to Main Menu");
			choice = atoi(s.nextLine());
			switch(choice){
				case -1:
					quit = true;
					break;
				case 1:
					quit = playerStatistics();
					break;
				case 2:
					quit = teamStatistics();
					break;
				case 3:
					quit = PowerProsAllStarTeams();
					break;
				case 4:
					up = true;
					break;
				default:
					System.out.println();
					System.out.println("*Please enter a valid integer response based on the options given*\n");
					break;
			}
		}
		return quit;
	}
	
	public static boolean playerStatistics(){
		boolean quit = false;
		boolean up = false;
		Scanner s = new Scanner(System.in);
		while(!quit && !up){
			int choice;
			System.out.println("Player Statistics:\n1. View By Team\n2. View Player Ranking Lists\n3. Back to Statistics Menu");
			choice = atoi(s.nextLine());
			switch(choice){
				case -1:
					quit = true;
					break;
				case 1:
					quit = playerStatisticsByTeam();
					break;
				case 2:
					quit = playerRankingLists();
					break;
				case 3:
					up = true;
					break;
				default:
					System.out.println();
					System.out.println("*Please enter a valid integer response based on the options given*\n");
					break;
			}
		}
		return quit;
	}
	
	public static boolean playerStatisticsByTeam(){
		//Setup for Search
		ArrayList<Team> teams = Analyzer.sortIntoTeams(playerData);
		boolean quit = false;
		boolean up = false;
		Scanner s = new Scanner(System.in);
		while(!quit && !up){
			int choice;
			int upInt;
			System.out.println("Teams:");
			int i;
			for(i=0; i<teams.size(); i++){
				Team team = teams.get(i);
				if(i+1>9)
					System.out.println((i+1)+". "+team.toStringLine());
				else
					System.out.println((i+1)+".  "+team.toStringLine());
			}
			upInt = i;
			System.out.println((upInt+1)+". Back to Player Statistics Menu");
			System.out.println("Enter the number of the team above you would like to select");
			choice = atoi(s.nextLine());
			if(choice>0 && choice<upInt+2){
				choice = choice-1;
				if(choice == upInt){
					up = true;
				}
				else{
					quit = playerStatisticsSelection(teams.get(choice).getRoster(), teams.size(), teams.get(choice).getName());
				}
			}
			else{
				if(choice == -1){
					quit = true;
				}
				else{
					System.out.println();
					System.out.println("*Please enter a valid integer response based on the options given*\n");
				}
			}
		}
		return quit;
	}
	
	public static boolean playerStatisticsSelection(ArrayList<Player> players, int length, String listTitle){
		System.out.println();
		Analyzer.sort(players);
		boolean quit = false;
		boolean up = false;
		Scanner s = new Scanner(System.in);
		while(!quit && !up){
			int choice;
			int upInt;
			System.out.println(listTitle+":");
			int i;
			for(i=0; i<players.size() && i<length; i++){
				System.out.println((i+1)+". "+players.get(i).toStringLine());
			}
			upInt = i;
			System.out.println((upInt+1)+". Back to Previous Menu");
			choice = atoi(s.nextLine());
			if(choice>0 && choice<upInt+2){
				choice = choice-1;
				if(choice == upInt){
					up = true;
				}
				else{
					players.get(choice).printAttributes();
					System.out.println("1. Add Player to custom team\n2. Back to player selection");
					int nextChoice = 0;
					nextChoice = atoi(s.nextLine());
					switch(nextChoice){
						case -1:
							quit = true;
							break;
						case 1:
							quit = addPlayerToTeam(players.get(choice));
							break;
						case 2:
							break;
						default:
							break;
						
					}
				}
			}
			else{
				if(choice == -1){
					quit = true;
				}
				else{
					System.out.println();
					System.out.println("*Please enter a valid integer response based on the options given*\n");
				}
			}
		}
		return quit;
	}
	
	//The boolean this returns is based on whether or not the pick was accepted
	public static boolean draftingPlayerSelection(ArrayList<Player> players, int length, Team team){
		System.out.println();
		Analyzer.sort(players);
		boolean reject = false;
		boolean up = false;
		Scanner s = new Scanner(System.in);
		while(!reject && !up){
			int choice;
			int upInt;
			System.out.println("Draft a Player:");
			int i;
			for(i=0; i<players.size() && i<length; i++){
				System.out.println((i+1)+". "+players.get(i).toStringLine());
			}
			upInt = i;
			System.out.println((upInt+1)+". Reject Pick");
			choice = atoi(s.nextLine());
			if(choice>0 && choice<upInt+2){
				choice = choice-1;
				if(choice == upInt){
					reject = true;
				}
				else{
					players.get(choice).printAttributes();
					System.out.println("1. Add Player to custom team\n2. Back to player selection");
					int nextChoice = 0;
					nextChoice = atoi(s.nextLine());
					switch(nextChoice){
						case 1:
							addPlayerToThisTeam(players.get(choice), team);
							up = true;
							break;
						case 2:
							break;
						default:
							break;
					}
				}
			}
			else{
				System.out.println();
				System.out.println("*Please enter a valid integer response based on the options given*\n");
			}
		}
		return reject;
	}
	
	public static boolean addPlayerToTeam(Player player){
		boolean quit = false;
		File savedTeams = new File(savedTeamFile);
		File[] files = savedTeams.listFiles();
		ArrayList<Team> teams = new ArrayList<Team>();
		int i=0, t=0;
		while(i<files.length){
			String name = files[i].getName();
			name = name.replace(".csv", "");
			if(name.length() >= 2 && name.length()<=3){
				teams.add(new Team(name));
				ArrayList<Player> players = Analyzer.readAll(files[i].getAbsolutePath());
				if(players != null){
					teams.get(t).addPlayers(players);
					teams.get(i).overallSetter();
					t++;
				}
			}
			i++;
		}
		quit = savedTeamStatistics(teams, true, player);
		return quit;
	}
	
	public static boolean addPlayerToThisTeam(Player player, Team team){
		team.addPlayer(player);
		team.overallSetter();
		TeamWriter writer;
		if(onWindows)
			writer = new TeamWriter(savedTeamFile+"\\"+team.getName()+".csv");
		else
			writer = new TeamWriter(savedTeamFile+"/"+team.getName()+".csv");
		writer.writeOne(team);
		System.out.println("Successfully Added "+player.getLastName()+", "+player.getFirstName()+" to "+team.getName()+"\n");
		return false;
	}
	
	public static boolean playerRankingLists(){
		System.out.println();	
		
		boolean quit = false;
		boolean up = false;
		Scanner s = new Scanner(System.in);
		while(!quit && !up){
			int choice;
			System.out.println("Player Ranking Lists:\n1. Overall\n2. Hitters\n3. Pitchers\n4. By Position\n5. By Attribute\n6. Back to Player Statistics Menu");
			choice = atoi(s.nextLine());
			int length = 0;
			switch(choice){
				case -1:
					quit = true;
					break;
				case 1:
					ArrayList<Player> players = Analyzer.readAll(playerData);
					boolean valid = false;
					while(!valid){
						System.out.println("Enter the number of players you would like to list (Max: "+players.size()+"):");
						length = atoi(s.nextLine());
						if(length>0 && length<=players.size()){
							valid = true;
						}
						else{
							if(length == -1){
								return true;
							}
							else{
								valid = false;
								System.out.println("Please enter a number between 1 and "+players.size());
							}
						}
					}
					quit = playerStatisticsSelection(players, length, "Overall Rankings (1-"+length+")");
					break;
				case 2:
					ArrayList<Player> hitters = Analyzer.readAll(hitterData);
					valid = false;
					while(!valid){
						System.out.println("Enter the number of players you would like to list (Max: "+hitters.size()+"):");
						length = atoi(s.nextLine());
						if(length>0 && length<=hitters.size()){
							valid = true;
						}
						else{
							if(length == -1){
								return true;
							}
							else{
								valid = false;
								System.out.println("Please enter a number between 1 and "+hitters.size());
							}
						}
					}
					quit = playerStatisticsSelection(hitters, length, "Overall Rankings (1-"+length+")");
					break;
				case 3:
					ArrayList<Player> pitchers = Analyzer.readAll(pitcherData);
					valid = false;
					while(!valid){
						System.out.println("Enter the number of players you would like to list (Max: "+pitchers.size()+"):");
						length = atoi(s.nextLine());
						if(length>0 && length<=pitchers.size()){
							valid = true;
						}
						else{
							if(length == -1){
								return true;
							}
							else{
								valid = false;
								System.out.println("Please enter a number between 1 and "+pitchers.size());
							}
						}
					}
					quit = playerStatisticsSelection(pitchers, length, "Overall Rankings (1-"+length+")");
					break;
				case 4:
					quit = positionRankings();
					break;
				case 5:
					quit = attributeRankings();
					break;
				case 6:
					up = true;
					break;
				default:
					System.out.println();
					System.out.println("*Please enter a valid integer response based on the options given*\n");
					break;
			}
		}
		return quit;
	}
	
	public static boolean positionRankings(){
		System.out.println();
		boolean quit = false;
		boolean up = false;
		Scanner s = new Scanner(System.in);
		while(!quit && !up){
			int choice;
			int upInt;
			System.out.println("Select a Position:");
			int i;
			for(i=2; i<10; i++){
				System.out.println(i+". "+Analyzer.posNumToString(i));
			}
			System.out.println(i+". "+Analyzer.posNumToString(1));
			i++;
			System.out.println(i+". "+Analyzer.posNumToString(0));
			i++;
			upInt = i;
			System.out.println((upInt)+". Back to Player Statistics Menu");
			choice = atoi(s.nextLine());
			if(choice>1 && choice<upInt+1){
				if(choice == upInt){
					up = true;
				}
				else{
					if(choice == 10) choice = 1;
					if(choice == 11) choice = 0;
					ArrayList<Player> players = Analyzer.searchByPrimary(playerData, choice);
					int length = 0;
					boolean valid = false;
					while(!valid){
						System.out.println("Enter the number of players you would like to list (Max: "+players.size()+"):");
						length = atoi(s.nextLine());
						if(length>0 && length<=players.size()){
							valid = true;
						}
						else{
							if(length == -1){
								return true;
							}
							else{
								valid = false;
								System.out.println("Please enter a number between 1 and "+players.size());
							}
						}
					}
					quit = playerStatisticsSelection(players, length, Analyzer.posNumToString(choice));
				}
			}
			else{
				if(choice == -1){
					quit = true;
				}
				else{
					System.out.println();
					System.out.println("*Please enter a valid integer response based on the options given*\n");
				}
			}
		}
		return quit;
	}
	
	public static boolean attributeRankings(){
		boolean quit = false;
		Scanner s = new Scanner(System.in);
		ArrayList<Player> players = Analyzer.readAll(playerData);
		AttributeCompare compare = new AttributeCompare();
		String[] hitterAttributes = {
				"CON",
				"PWR",
				"SPD",
				"ARM",
				"FLD",
				"TRJ",
				"eRes"};
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
		int choice = 0;
		while(choice<1 || choice>3){
			System.out.println("*Note: Search Criteria can only be made more stringent.\nAdjusting previously searched attributes will not increase number of results.");
			System.out.println("Hitter attribute or a Pitcher attribute?\n1. Hitter \n2. Pitcher");
			choice = atoi(s.nextLine());
			if(choice<1 || choice>2)
				System.out.println("*Please enter a valid integer response based on the options given*");
		}
		if(choice == 2){
			int nextChoice = 0;
			boolean valid = false;
			int length=0;
			while(nextChoice<1 || nextChoice>99){
				System.out.println("Select an Attribute");
				for(int i=0; i<pitcherAttributes.length; i++)
					System.out.println((i+1)+". "+pitcherAttributes[i]);
				nextChoice = atoi(s.nextLine());
			}
			nextChoice--;
			compare.setKey(pitcherAttributes[nextChoice]);
			Collections.sort(players, compare);
			while(!valid){
				System.out.println("Enter the number of players you would like to list (Max: "+players.size()+"):");
				length = atoi(s.nextLine());
				if(length>0 && length<=players.size()){
					valid = true;
				}
				else{
					if(length == -1){
						return true;
					}
					else{
						valid = false;
						System.out.println("Please enter a number between 1 and "+players.size());
					}
				}
			}
			quit = playerAttributesSelection(players, length, pitcherAttributes[nextChoice]);
		}
		else{
			if(choice == 1){
				int nextChoice = 0;
				boolean valid = false;
				int length=0;
				while(nextChoice<1 || nextChoice>99){
					System.out.println("Select an Attribute");
					for(int i=0; i<hitterAttributes.length; i++)
						System.out.println((i+1)+". "+hitterAttributes[i]);
					nextChoice = atoi(s.nextLine());
				}
				nextChoice--;
				compare.setKey(hitterAttributes[nextChoice]);
				Collections.sort(players, compare);
				while(!valid){
					System.out.println("Enter the number of players you would like to list (Max: "+players.size()+"):");
					length = atoi(s.nextLine());
					if(length>0 && length<=players.size()){
						valid = true;
					}
					else{
						if(length == -1){
							return true;
						}
						else{
							valid = false;
							System.out.println("Please enter a number between 1 and "+players.size());
						}
					}
				}
				quit = playerAttributesSelection(players, length, hitterAttributes[nextChoice]);
			}
		}
		return quit;
	}
	
	public static boolean playerAttributesSelection(ArrayList<Player> players, int length, String attributeKey){
		System.out.println();
		boolean quit = false;
		boolean up = false;
		Scanner s = new Scanner(System.in);
		while(!quit && !up){
			int choice;
			int upInt;
			System.out.println(attributeKey+":");
			int i;
			for(i=0; i<players.size() && i<length; i++){
				System.out.println((i+1)+". "+players.get(i).toAttributeLine(attributeKey));
			}
			upInt = i;
			System.out.println((upInt+1)+". Back to Previous Menu");
			choice = atoi(s.nextLine());
			if(choice>0 && choice<upInt+2){
				choice = choice-1;
				if(choice == upInt){
					up = true;
				}
				else{
					players.get(choice).printAttributes();
					System.out.println("1. Add Player to custom team\n2. Back to player selection");
					int nextChoice = 0;
					nextChoice = atoi(s.nextLine());
					switch(nextChoice){
						case -1:
							quit = true;
							break;
						case 1:
							quit = addPlayerToTeam(players.get(choice));
							break;
						case 2:
							up = true;
							break;
						default:
							break;
						
					}
				}
			}
			else{
				if(choice == -1){
					quit = true;
				}
				else{
					System.out.println();
					System.out.println("*Please enter a valid integer response based on the options given*\n");
				}
			}
		}
		return quit;
	}
	
	public static boolean teamStatistics(){	
		//Setup for Search
		ArrayList<Team> teams = Analyzer.sortIntoTeams(playerData);
		boolean quit = false;
		boolean up = false;
		Scanner s = new Scanner(System.in);
		while(!quit && !up){
			int choice;
			int upInt;
			System.out.println("Team Statistics:");
			int i;
			for(i=0; i<teams.size(); i++){
				Team team = teams.get(i);
				if(i+1>9){
					if(team.isComplete())
						System.out.println((i+1)+". "+team.toStringLine());
					else
						System.out.println((i+1)+". "+team.toStringLine()+"   *incomplete");
				}
				else{
					if(team.isComplete())
						System.out.println((i+1)+".  "+team.toStringLine());
					else
						System.out.println((i+1)+".  "+team.toStringLine()+"   *incomplete");
				}
			}
			upInt = i;
			System.out.println((upInt+1)+". Back to Statistics Menu");
			System.out.println("Enter the number of the team above you would like to select");
			choice = atoi(s.nextLine());
			if(choice>0 && choice<upInt+2){
				choice = choice-1;
				if(choice == upInt){
					up = true;
				}
				else{
					quit = displayTeamStatistics(teams.get(choice), false);
				}
			}
		}
		return quit;
	}
	
	public static boolean displayTeamStatistics(Team team, boolean edit){
		boolean quit = false;
		boolean up = false;
		Scanner s = new Scanner(System.in);
		while(!quit && !up){
			team.overallSetter();
			team.printLineup();
			ArrayList<Player> players = team.getRoster();
			Analyzer.sort(players);
			System.out.println("25 Man Roster:");
			Analyzer.printListN(players, 25);
			System.out.println();
			int choice;
			if(edit){
				System.out.println("Options:\n1. Full Roster\n2. Add More Players\n3. Save Custom Team\n4. Delete Custom Team\n5. Back to previous menu (discards any changes)");
				choice = atoi(s.nextLine());
				switch(choice){
					case -1:
						quit = true;
						break;
					case 1:
						if(players.size()<25)
							quit = playerStatisticsSelection(players, players.size(), team.getName());
						else
							quit = playerStatisticsSelection(players, players.size(), team.getName());
						break;
					case 2:
						quit = addPlayersManually(team);
						break;
					case 3:
						TeamWriter writer;
						if(onWindows)
							writer = new TeamWriter(savedTeamFile+"\\"+team.getName()+".csv");
						else
							writer = new TeamWriter(savedTeamFile+"/"+team.getName()+".csv");
						writer.writeOne(team);
						System.out.println("Successfully Saved "+team.getName());
						break;
					case 4:
						System.out.println("Are you sure you'd like to delete "+team.getName()+"?\n1. Yes\n2. No");
						int nextChoice = atoi(s.nextLine());
						switch(nextChoice){
							case -1:
								quit = true;
								break;
							case 1:
								File file;
								if(onWindows)
									file = new File(savedTeamFile+"\\"+team.getName()+".csv");
								else
									file = new File(savedTeamFile+"/"+team.getName()+".csv");
								if(file.delete()){
									System.out.println("Successfully Deleted "+team.getName());
								}
								else{
									System.out.println("Could not delete "+team.getName()+"\nCheck if it is open in another program.");
								}
								break;
							case 2:
								break;
							default:
								break;
						}
					case 5:
						up = true;
						break;
					default:
						System.out.println();
						System.out.println("*Please enter a valid integer response based on the options given*\n");
						break;
				}
			}
			else{
				System.out.println("Options:\n1. Full Roster\n2. Back to Previous Menu");
				choice = atoi(s.nextLine());
				switch(choice){
					case -1:
						quit = true;
						break;
					case 1:
						if(players.size()<25)
							quit = playerStatisticsSelection(players, players.size(), team.getName());
						else
							quit = playerStatisticsSelection(players, players.size(), team.getName());
						break;
					case 2:
						up = true;
						break;
					default:
						System.out.println();
						System.out.println("*Please enter a valid integer response based on the options given*\n");
						break;
				}
			}
		}
		return quit;
	}
	
	public static boolean PowerProsAllStarTeams(){
		boolean quit = false;
		boolean up = false;
		Scanner s = new Scanner(System.in);
		while(!quit && !up){
			int choice;
			System.out.println("Power Pros All-Star Teams:\n1. Team All Power Pros\n2. Team All A.L.\n3. Team All N.L.\n4. Back to Statistics Menu");
			choice = atoi(s.nextLine());
			switch(choice){
				case -1:
					quit = true;
					break;
				case 1:
					ArrayList<Player> players = Analyzer.readAll(playerData);
					Team team = new Team("ALL");
					team.addPlayers(players);
					team.overallSetter();
					quit = displayTeamStatistics(team, false);
					break;
				case 2:
					ArrayList<Player> alPlayers = Analyzer.readAll(alData);
					Team al = new Team("AL");
					al.addPlayers(alPlayers);
					al.overallSetter();
					quit = displayTeamStatistics(al, false);
					break;
				case 3:
					ArrayList<Player> nlPlayers = Analyzer.readAll(nlData);
					Team nl = new Team("NL");
					nl.addPlayers(nlPlayers);
					nl.overallSetter();
					quit = displayTeamStatistics(nl, false);
					break;
				case 4:
					up = true;
					break;
				default:
					System.out.println();
					System.out.println("*Please enter a valid integer response based on the options given*\n");
					break;
			}
		}
		return quit;
	}
	
	
	//Player Search Section
	public static boolean playerSearch(){
		System.out.println("Player Search:");
		ArrayList<Player> players = Analyzer.readAll(playerData);
		String[] criteria = {
				"First Name",
				"Last Name",
				"First Letter of Last Name",
				"Team",
				"Primary Position",
				"Secondary Position",
				"Can Play Position",
				"Attributes",
				"Column",
				"Row"};
		
		boolean[] used = new boolean[10];
		for(int i=0; i<used.length; i++) 
			used[i] = false;
		
		boolean[] usedCapability = new boolean[10];
		for(int i=0; i<usedCapability.length; i++) 
			usedCapability[i] = false;
		
		boolean quit = false;
		boolean up = false;
		Scanner s = new Scanner(System.in);
		while(!quit && !up){
			int choice;
			System.out.println("Criteria:");
			int i;
			for(i=0; i<criteria.length; i++){
				if(!used[i])
					System.out.println((i+1)+". "+criteria[i]);
			}
			System.out.println((i+1)+". View "+players.size()+" players");
			i++;
			System.out.println((i+1)+". Reset search");
			i++;
			System.out.println((i+1)+". Exit search");
			choice = atoi(s.nextLine());
			switch(choice){
				case -1:
					quit = true;
					break;
				case 1:
					System.out.println("Enter First Name (or partial First Name):");
					String sequence = s.nextLine();
					players = Analyzer.NarrowByFirstName(players, sequence);
					used[choice-1] = true;
					break;
				case 2:
					System.out.println("Enter Last Name (or partial Last Name):");
					sequence = s.nextLine();
					players = Analyzer.NarrowByLastName(players, sequence);
					used[choice-1] = true;
					break;
				case 3:
					char letter = 0;
					while(letter==0){
						System.out.println("Enter First Letter of Last Name:");
						letter = aToLet(s.nextLine());
					}
					players = Analyzer.NarrowByFirstLetterOfLastName(players, letter);
					used[choice-1] = true;
					break;
				case 4:
					//Setup for Search
					ArrayList<Team> teams = Analyzer.sortIntoTeams(playerData);
					int teamChoice = -1;
					while(teamChoice<1 || teamChoice>teams.size()){
						System.out.println("Teams:");
						for(i=0; i<teams.size(); i++){
							Team team = teams.get(i);
							if(i+1>9)
								System.out.println((i+1)+". "+team.getName());
							else
								System.out.println((i+1)+".  "+team.getName());
						}
						System.out.println("Enter the number of the team above you would like to select");
						teamChoice = atoi(s.nextLine());
						if(teamChoice<1 || teamChoice>i-1)
							System.out.println("*Please enter a valid integer response based on the options given*");
					}
					teamChoice = teamChoice-1;
					players = Analyzer.NarrowByTeam(players, teams.get(teamChoice).getName());
					used[choice-1] = true;
					break;
				case 5:
					int position = -1;
					while(position<0 || position>9){
						System.out.println("Select a Position:");
						for(i=2; i<10; i++){
							System.out.println(i+". "+Analyzer.posNumToString(i));
						}
						System.out.println(i+". "+Analyzer.posNumToString(1));
						i++;
						System.out.println(i+". "+Analyzer.posNumToString(0));
						i++;
						position = atoi(s.nextLine());
						if(position>1 && position<i){
								if(position == 10) position = 1;
								if(position == 11) position = 0;
						}
						else{
							position = position-1;
						}
					}
					players = Analyzer.NarrowByPrimary(players, position);
					used[choice-1] = true;
					break;
				case 6:
					position = -1;
					while(position<2 || position>9){
						System.out.println("Select a Position:");
						for(i=2; i<10; i++){
							System.out.println(i+". "+Analyzer.posNumToString(i));
						}
						position = atoi(s.nextLine());
						position = position-1;
					}
					players = Analyzer.NarrowBySecondary(players, position);
					used[choice-1] = true;
					break;
				case 7:
					position = -1;
					while(position<0 || position>9){
						System.out.println("Select a Position:");
						for(i=2; i<10; i++){
							if(!usedCapability[i])
								System.out.println(i+". "+Analyzer.posNumToString(i));
						}
						if(!usedCapability[1])
							System.out.println(i+". "+Analyzer.posNumToString(1));
						i++;
						if(!usedCapability[0])
							System.out.println(i+". "+Analyzer.posNumToString(0));
						i++;
						position = atoi(s.nextLine());
						if(position>1 && position<i){
								if(position == 10) position = 1;
								if(position == 11) position = 0;
						}
						else{
							position = position-1;
						}
					}
					players = Analyzer.NarrowByCapability(players, position);
					usedCapability[position] = true;
					break;
				case 8:
					ArrayList<Player> test = attributeCriteria(players);
					if(test != null)
						players = test;
					else
						System.out.println("Error");
					break;
				case 9:
					int column = -1;
					while(column<0 || column>4){
						System.out.println("Enter a column number (1-4):");
						column = atoi(s.nextLine());
						if(column<1 || column>6)
							System.out.println("Please enter a valid column number between 1 and 4");
					}
					players = Analyzer.NarrowByColumn(players, column);
					used[choice-1] = true;
					break;
				case 10:
					int row = -1;
					while(row<0 || row>6){
						System.out.println("Enter a row number (1-6):");
						row = atoi(s.nextLine());
						if(row<1 || row>6)
							System.out.println("Please enter a valid row number between 1 and 6");
					}
					players = Analyzer.NarrowByRow(players, row);
					used[choice-1] = true;
					break;
				case 11:
					quit = playerStatisticsSelection(players, players.size(), "Enter Number of Player to View Attributes\nSearch Results");
					System.out.println();
					break;
				case 12:
					players = Analyzer.readAll(playerData);
					for(i=0; i<used.length; i++) 
						used[i] = false;
					for(i=0; i<usedCapability.length; i++) 
						usedCapability[i] = false;
					System.out.println("*Search Reset*");
					break;
				case 13:
					up = true;
					break;
				default:
					System.out.println();
					System.out.println("*Please enter a valid integer response based on the options given*");
					break;
			}
		}
		return quit;
	}
	
	
	public static ArrayList<Player> attributeCriteria(ArrayList<Player> players){
		Scanner s = new Scanner(System.in);
		int value = -1;
		int gtLtEt = -1;
		String[] hitterAttributes = {
				"CON",
				"PWR",
				"SPD",
				"ARM",
				"FLD",
				"TRJ",
				"eRES"};
		int[] hitterRanges = {
				15,
				255,
				15,
				15,
				15,
				4,
				15};
		int[] pitcherRanges = {
				105,
				255,
				255,
				3,
				7,
				7,
				7,
				7,
				7};
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
		int choice = 0;
		while(choice<1 || choice>3){
			System.out.println("*Note: Search Criteria can only be made more stringent.\nAdjusting previously searched attributes will not increase number of results.");
			System.out.println("Hitter attribute, Pitcher attribute, or Overall?\n1. Hitter \n2. Pitcher\n3. Overall");
			choice = atoi(s.nextLine());
			if(choice<1 || choice>3)
				System.out.println("*Please enter a valid integer response based on the options given*");
		}
		if(choice == 3){
			while(value<0 || value>99){
				System.out.println("Enter an Overall (0-99):");
				value = atoi(s.nextLine());
			}
			while(gtLtEt<0 || gtLtEt>2){
				System.out.println("1. Greater than (or equal to)\n2. Less than (or equal to)\n3. Equal to");
				gtLtEt = atoi(s.nextLine())-1;
			}
			return Analyzer.NarrowByOverall(players, value, gtLtEt);
		}
		else{
			if(choice == 1){
				int nextChoice = 0;
				while(nextChoice<1 || nextChoice>99){
					System.out.println("Select an Attribute");
					for(int i=0; i<hitterAttributes.length; i++)
						System.out.println((i+1)+". "+hitterAttributes[i]);
					nextChoice = atoi(s.nextLine());
				}
				nextChoice--;
				while(value<0 || value>hitterRanges[nextChoice]){
					System.out.println("Enter a "+hitterAttributes[nextChoice]+" value from 1-"+hitterRanges[nextChoice]+":");
					value = atoi(s.nextLine());
				}
				while(gtLtEt<0 || gtLtEt>2){
					System.out.println("1. Greater than (or equal to)\n2. Less than (or equal to)\n3. Equal to");
					gtLtEt = atoi(s.nextLine())-1;
				}
				return Analyzer.narrowByAttribute(players, hitterAttributes[nextChoice], choice==2, value, gtLtEt);
			}
			else{
				if(choice == 2){
					int nextChoice = 0;
					while(nextChoice<1 || nextChoice>99){
						System.out.println("Select an Attribute");
						for(int i=0; i<pitcherAttributes.length; i++)
							System.out.println((i+1)+". "+pitcherAttributes[i]);
						nextChoice = atoi(s.nextLine());
					}
					nextChoice--;
					while(value<0 || value>pitcherRanges[nextChoice]){
						System.out.println("Enter a "+pitcherAttributes[nextChoice]+" value from 1-"+pitcherRanges[nextChoice]+":");
						value = atoi(s.nextLine());
					}
					while(gtLtEt<0 || gtLtEt>2){
						System.out.println("1. Greater than (or equal to)\n2. Less than (or equal to)\n3. Equal to");
						gtLtEt = atoi(s.nextLine())-1;
					}
					return Analyzer.narrowByAttribute(players, pitcherAttributes[nextChoice], choice==2, value, gtLtEt);
				}
				else{
					return null;
				}
			}
		}
	}
	
	//Custom Team Rating Section
	public static boolean customTeams(){
		boolean quit = false;
		boolean up = false;
		Scanner s = new Scanner(System.in);
		while(!quit && !up){
			int choice;
			System.out.println("Custom Team Rating:\n1. Saved Teams\n2. Import New Team From File\n3. Enter New Team Manually\n4. Back to Main Menu");
			choice = atoi(s.nextLine());
			switch(choice){
				case -1:
					quit = true;
					break;
				case 1:
					quit = savedTeams();
					break;
				case 2:
					quit = importFromFile();
					break;
				case 3:
					quit = addPlayersManually(new Team("new"));
					break;
				case 4:
					up = true;
					break;
				default:
					System.out.println();
					System.out.println("*Please enter a valid integer response based on the options given*\n");
					break;
			}
		}
		return quit;
	}
	
	
	public static boolean savedTeams(){
		boolean quit = false;
		File savedTeams = new File(savedTeamFile);
		File[] files = savedTeams.listFiles();
		ArrayList<Team> teams = new ArrayList<Team>();
		int i=0, t=0;
		while(i<files.length){
			String name = files[i].getName();
			name = name.replace(".csv", "");
			if(name.length() >= 2 && name.length()<=3){
				teams.add(new Team(name));
				ArrayList<Player> players = Analyzer.readAll(files[i].getAbsolutePath());
				if(players != null){
					teams.get(t).addPlayers(players);
					teams.get(i).overallSetter();
					t++;
				}
			}
			i++;
		}
		quit = savedTeamStatistics(teams, false, new Pitcher(0, "null"));
		return quit;
	}
	
	public static boolean savedTeamStatistics(ArrayList<Team> teams, boolean adding, Player player){	
		boolean quit = false;
		boolean up = false;
		Scanner s = new Scanner(System.in);
		while(!quit && !up){
			int choice;
			int upInt;
			System.out.println("Team Statistics:");
			int i;
			for(i=0; i<teams.size(); i++){
				Team team = teams.get(i);
				if(i+1>9){
					if(team.isComplete())
						System.out.println((i+1)+". "+team.toStringLine());
					else
						System.out.println((i+1)+". "+team.toStringLine()+"   *incomplete");
				}
				else{
					if(team.isComplete())
						System.out.println((i+1)+".  "+team.toStringLine());
					else
						System.out.println((i+1)+".  "+team.toStringLine()+"   *incomplete");
				}
			}
			upInt = i;
			System.out.println((upInt+1)+". Back to Previous Menu");
			System.out.println("Enter the number of the team above you would like to select");
			choice = atoi(s.nextLine());
			if(choice>0 && choice<upInt+2){
				choice = choice-1;
				if(choice == upInt){
					up = true;
				}
				else{
					if(adding){
						TeamWriter writer;
						if(onWindows)
							writer = new TeamWriter(savedTeamFile+"\\"+teams.get(choice).getName()+".csv");
						else
							writer = new TeamWriter(savedTeamFile+"/"+teams.get(choice).getName()+".csv");
						teams.get(choice).addPlayer(player);
						writer.writeOne(teams.get(choice));
						System.out.println("Successfully Added "+player.getLastName()+", "+player.getFirstName()+" to "+teams.get(choice).getName()+"\n");
						up = true;
					}
					else{
						quit = displayTeamStatistics(teams.get(choice), true);
						up = true;
					}
				}
			}
		}
		return quit;
	}
	
	public static boolean importFromFile(){
		boolean quit = false;
		boolean up = false;
		Scanner s = new Scanner(System.in);
		Team team = null;
		while(team == null){
			System.out.println("Enter the filepath of your team:");
			String filepath = s.nextLine();
			filepath.replace("\\","\\\\");
			System.out.println("Enter a 2-3 character abbreviation for your team:");
			String name = s.nextLine();
			ArrayList<Player> players = Analyzer.readAll(filepath);
			team = Analyzer.listToTeam(playerData, players, name);
			team.setName(name);
			team.overallSetter();
		}
		quit = displayTeamStatistics(team, true);
		return quit;
	}
	
	public static boolean addPlayersManually(Team team){
		System.out.println("\n\nWelcome to Manual Team Creation.");
		System.out.println("\nA Team must have 9 Hitters and a Starting Pitcher to be considered complete.");
		System.out.println("For a more accurate rating, a starter, 2 other pitchers,\nand a full lineup of players capable of playing their positions are required.");
		Scanner s = new Scanner(System.in);
		boolean done = false;
		boolean quit = false;
		boolean up = false;
		if(team.getName().equals("NEW")){
			System.out.println("\nEnter a 2-3 character abbreviation for your team:");
			String name = s.nextLine();
			team.setName(name);
		}
		ArrayList<Player> players = team.getRoster();
		while(!done){
			System.out.println("\n"+team.getName()+":");
			team.overallSetter();
			if(team.isComplete())
				System.out.println("Complete: yes");
			else{
				System.out.println("Complete: no");
			}
			System.out.println("O:"+team.getOverall()+"  H:"+team.getHitterOverall()+"  P: "+team.getPitcherOverall());
			System.out.println("Number of Players: "+players.size());
			players = Analyzer.sortByPoisition(players);
			Analyzer.printList(players);
			System.out.println("\nEnter a name in 'First,Last' format to add him to your team (ex. Albert,Pujols)\nOr type 'done' when done adding players:");
			String name = s.nextLine();
			String[] parts = name.split(",");
			if(parts.length==2){
				ArrayList<Player> searchPlayers = Analyzer.readAll(playerData);
				searchPlayers = Analyzer.NarrowByLastName(searchPlayers, parts[1]);
				if(searchPlayers.size()>1){
					searchPlayers = Analyzer.NarrowByFirstName(searchPlayers, parts[0]);
				}
				if(searchPlayers.size() == 1){
					team.addPlayer(searchPlayers.get(0));
					System.out.println("Successfully added:    "+searchPlayers.get(0).toStringLine());
				}
				else{
					System.out.println("failed to add "+name);
				}
			}
			else{
				if(parts.length==1){
					if(parts[0].equalsIgnoreCase("done"))
						done = true;
					else
						System.out.println("failed to add "+name);
				}
				else{
					System.out.println("failed to add "+name);
				}
			}
		}
		team.overallSetter();
		quit = displayTeamStatistics(team, true);
		return quit;
	}
	
	public static boolean saveTeamOptions(Team team){
		boolean quit = false;
		boolean up = false;
		Scanner s = new Scanner(System.in);
		while(!quit && !up){
			int choice;
			System.out.println("Options:\n1. Save Custom Team\n2. Add More Players\n3. Back to previous menu (discards any new changes)");
			choice = atoi(s.nextLine());
			switch(choice){
				case -1:
					quit = true;
					break;
				case 1:
					TeamWriter writer;
					if(onWindows)
						writer = new TeamWriter(savedTeamFile+"\\"+team.getName()+".csv");
					else
						writer = new TeamWriter(savedTeamFile+"/"+team.getName()+".csv");
					writer.writeOne(team);
					up = true;
					break;
				case 2:
					quit = addPlayersManually(team);
					up= true;
					break;
				case 3:
					up = true;
					break;
				default:
					System.out.println();
					System.out.println("*Please enter a valid integer response based on the options given*\n");
					break;
			}
		}
		return quit;
	}
	
	//Draft Game Section
	public static boolean draftGame(){
		boolean reject;
		boolean done = false;
		Scanner s = new Scanner(System.in);
		System.out.println("Enter a 2-3 character abbreviation for the first team:");
		String name1 = s.nextLine();
		Team team1 = new Team(name1);
		System.out.println("Enter a 2-3 character abbreviation for the second team:");
		String name2 = s.nextLine();
		Team team2 = new Team(name2);
		
		while(team2.getRoster().size()<15 && !done){
			System.out.println(DisplayLine.displaySideBySide(team1.toPositionalString(), team2.toPositionalString(), 10));
			CriteriaGenerator generator = new CriteriaGenerator();
			for(int i=0; i<2;){
				reject = true;
				ArrayList<Player> players = new ArrayList<Player>();
				ArrayList<Criteria> criteria = new ArrayList<Criteria>();
				if(i==0)
					System.out.println("Team "+team1.getName()+"'s pick\nPress Enter to Receive Criteria:");
				else
					System.out.println("Team "+team2.getName()+"'s pick\nPress Enter to Receive Criteria:");
				s.nextLine();
				while(players.size() == 0){
					players = Analyzer.readAll(playerData);
					criteria = new ArrayList<Criteria>(); 
					for(int j=0; j<4;){
						int remaining = 0;
						while(remaining == 0){
							ArrayList<Player> hold = players;
							Criteria next = generator.generateCriteria(players.size());
							String name = next.getName();
							String key = next.getKey();
							criteria.add(next);
							switch(name){
								case "Team":
									players = Analyzer.NarrowByTeam(players, key);
									break;
								case "FofL":
									players = Analyzer.NarrowByFirstLetterOfLastName(players, key.charAt(0));
									break;
								case "LofL":
									players = Analyzer.NarrowByLastLetterOfLastName(players, key.charAt(0));
									break;
								case "FofF":
									players = Analyzer.NarrowByFirstLetterOfFirstName(players, key.charAt(0));
									break;
								case "LofF":
									players = Analyzer.NarrowByLastLetterOfFirstName(players, key.charAt(0));
									break;
								case "Name Length":
									players = Analyzer.NarrowByLastNameLength(players, atoi(key));
									break;
								case "Position":
									if(atoi(key)>1)
										players = Analyzer.NarrowByCapability(players, atoi(key));
									else
										players = Analyzer.NarrowByPrimary(players, atoi(key));
									break;
								case "Column":
									players = Analyzer.NarrowByColumn(players, atoi(key));
									break;
								case "Row":
									players = Analyzer.NarrowByRow(players, atoi(key));
									break;
								default:
									break;					
							}
							remaining = players.size();
							if(remaining==0){
								players = hold;
								criteria.remove(j);
							}
							else{
								remaining = players.size();
								j++;
							}
						}
					}
					generator.reset();
				}
				for(int j=0; j<criteria.size(); j++){
					if(!criteria.get(j).toString().equals("Nothing"))
						System.out.println(criteria.get(j).toString());
				}
				if(i==0)
					reject = draftingPlayerSelection(players, players.size(), team1);
				else
					reject = draftingPlayerSelection(players, players.size(), team2);
				if(!reject)
					i++;
			}
			boolean thisUp = false;
			while(!thisUp){
				System.out.println("Options:\n1. Continue\n2. Quit Draft");
				int choice = atoi(s.nextLine());
				switch(choice){
					case 1:
						thisUp = true;
						break;
					case 2:
						thisUp = true;
						done = true;
						break;
					default:
						System.out.println();
						System.out.println("*Please enter a valid integer response based on the options given*\n");
						break;
				}
			}
		}
		return false;
	}
	
	
	public static double[] draftTest(){
		CriteriaGenerator generator = new CriteriaGenerator();
		ArrayList<Player> players = new ArrayList<Player>();
		ArrayList<Criteria> criteria = new ArrayList<Criteria>();
		players = Analyzer.readAll(playerData);
		criteria = new ArrayList<Criteria>(); 
		for(int j=0; j<4;){
			int remaining = 0;
			while(remaining == 0){
				ArrayList<Player> hold = players;
				Criteria next = generator.generateCriteria(players.size());
				String name = next.getName();
				String key = next.getKey();
				criteria.add(next);
				switch(name){
					case "Team":
						players = Analyzer.NarrowByTeam(players, key);
						break;
					case "FofL":
						players = Analyzer.NarrowByFirstLetterOfLastName(players, key.charAt(0));
						break;
					case "LofL":
						players = Analyzer.NarrowByLastLetterOfLastName(players, key.charAt(0));
						break;
					case "FofF":
						players = Analyzer.NarrowByFirstLetterOfFirstName(players, key.charAt(0));
						break;
					case "LofF":
						players = Analyzer.NarrowByLastLetterOfFirstName(players, key.charAt(0));
						break;
					case "Name Length":
						players = Analyzer.NarrowByLastNameLength(players, atoi(key));
						break;
					case "Position":
						if(atoi(key)>1)
							players = Analyzer.NarrowByCapability(players, atoi(key));
						else
							players = Analyzer.NarrowByPrimary(players, atoi(key));
						break;
					case "Column":
						players = Analyzer.NarrowByColumn(players, atoi(key));
						break;
					case "Row":
						players = Analyzer.NarrowByRow(players, atoi(key));
						break;
					default:
						break;					
				}
				remaining = players.size();
				if(remaining==0){
					players = hold;
				}
				else{
					remaining = players.size();
					j++;
				}
			}
		}
		generator.reset();
		long ovr = 0;
		int opponent = 0;
		ovr = players.get(0).getOverall();
		for(int k=0; k<criteria.size(); k++){
			if(criteria.get(k).getName().equals("Opponent Picks"))
				opponent++;
		}
		double playersLeft = players.size();
		double[] ret = {playersLeft, ovr, opponent};
		return ret;
	}
	
	//Utilities
	public static int atoi(String string){
		int i = 0;
		if(string != ""){
			try{
				i = Integer.parseInt(string);
				return i;
			}catch(Exception e){
				return 0;
			}
		}
		else{
			return 0;
		}
	}
	
	//Utilities
	public static char aToLet(String string){
			int i = 0;
			if(string != "" && string.length()==1){
				try{
					i = string.charAt(0);
					if(i>90){
						i = i-32;
					}
					
					if(i>64 && i<91)
						return (char) i;
					else
						return 0;
				}catch(Exception e){
					return 0;
				}
			}
			else{
				return 0;
			}
		}
	
	//Utilities
	public static int atoi_1(String string){
		int i = 0;
		if(string != ""){
			try{
				i = Integer.parseInt(string);
				return i;
			}catch(Exception e){
				return 0;
			}
		}
		else{
			return 0;
		}
	}

	public static boolean isUsed(int nextInt, int[] used_cats){
		boolean used = false;
		for(int i=0; i<4; i++){
			if(nextInt == used_cats[i])
				used = true;
		}
		return used;
	}
}