import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class PlayerReader {
	private String filepath;
	private BufferedReader reader;
	
	//Sets up for first use
	public void setupReader(String newFilepath){
		filepath = newFilepath;
		try{
			File file = new File(filepath);
			BufferedReader breader = new BufferedReader(new FileReader(file));
			reader = breader;
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public PlayerReader(String newFilepath){
		filepath = newFilepath;
	}
	
	//resets the reader
	public void reset(){
		setupReader(filepath);
	}
	
	public String[] readNames(){
		setupReader(filepath);
		String firstLast;
		String line;
		String[] names = new String[1];
		int num = 0;
		String[] arguments;
		try{
			while((line = reader.readLine()) != null){
				arguments = line.split(",");
				if(arguments.length > 2){
					String[] newNames = new String[num+1];
					for(int i=0; i<names.length; i++){
						newNames[i] = names[i];
					}
					firstLast = arguments[1]+","+arguments[2];
					newNames[num] = firstLast;
					names = newNames;
					num++;
				}
			}
		}catch (IOException e) {
			e.printStackTrace();
			return null;
		}
//		for(int i=0; i<names.length; i++){
//			System.out.println(names[i]);
//		}
		return names;
	}
	
	//The basic Reading in Method
	public Player readOne(){
		String line;
		char[] characters;
		String[] arguments = new String[17];
		
		try {
			line = reader.readLine();
			if(line!=null)
				characters = line.toCharArray();
			else
				return null;
		}catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		//Reading Arguments of File into Array
		int charNum=0, argNum=0;
		char current;
		String argument = "";
		while(charNum < characters.length && characters[charNum] != '\n' && argNum < 18){
			if(characters[charNum] == ','){
				arguments[argNum] = argument;
				argument = "";
				charNum++;
				argNum++;
			}
			else{
				argument += characters[charNum];
				charNum++;
			}
		}
		if(argNum == 14 || argNum == 15)
			arguments[argNum] = argument;
		argNum++;
		
		//Turning Arguments into a Player
		if(argNum <= 19 && argNum >= 15){
			if(atoi(arguments[4]) > 1){
				Hitter hitter = new Hitter(atoi(arguments[0]), arguments[2]);
				hitter.setFirstName(arguments[1]);
				hitter.setTeam(arguments[3]);
				hitter.setPositions(atoi(arguments[4]), atoi(arguments[5]));
				hitter.setAttributes(
						atoi(arguments[6]),
						atoi(arguments[7]),
						atoi(arguments[8]), 
						atoi(arguments[9]), 
						atoi(arguments[10]), 
						atoi(arguments[11]), 
						atoi(arguments[12])
						);
				hitter.setGrid(atoi(arguments[13]), atoi(arguments[14]));
				return hitter;
			}
			else{
				Pitcher pitcher = new Pitcher(atoi(arguments[0]), arguments[2]);
				pitcher.setFirstName(arguments[1]);
				pitcher.setTeam(arguments[3]);
				pitcher.setRole(atoi(arguments[4]));
				pitcher.setAttributes(
						atoi(arguments[5]),
						atoi(arguments[6]),
						atoi(arguments[7]), 
						atoi(arguments[8]), 
						atoi(arguments[9]), 
						atoi(arguments[10]), 
						atoi(arguments[11]),
						atoi(arguments[12]),
						atoi(arguments[13])
						);
				pitcher.setGrid(atoi(arguments[14]), atoi(arguments[15]));
				return pitcher;
			}
		}
		else{
			arguments = line.split(",");
			if(arguments.length>=3){
				Player player = new Pitcher(atoi(arguments[0]), arguments[2]);
				player.setFirstName(arguments[1]);
				return player;
			}
			else{
				return null;
			}
		}
	}
	
	
	//Specific Searches. Reset reader for next use
	public Player readById(int id){
		setupReader(filepath);
		Player player;
		boolean found = false;
		
		while(!found){
			player = readOne();
			if(player.getId() == id){
				found = true;
				reset();
				return player;
			}
		}
		reset();
		return null;
	}
	
	public Player readByFirstContains(String sequence){
		setupReader(filepath);
		Player player;
		boolean found = false;
		while(!found){
			player = readOne();
			if(player.getFirstName().contains(sequence)){
				found = true;
				reset();
				return player;
			}
		}
		reset();
		return null;
	}
	
	public Player readByLastContains(String sequence){
		setupReader(filepath);
		Player player;
		boolean found = false;
		while(!found){
			player = readOne();
			if(player.getLastName().contains(sequence)){
				found = true;
				reset();
				return player;
			}
		}
		reset();
		return null;
	}
	
	
	//infinite loop
	public Player readByName(String first, String last){
		setupReader(filepath);
		Player player;
		boolean found = false;
		
		while(!found){
			player = readOne();
			if(player != null){
				if(player.getLastName().equalsIgnoreCase(last)){
					if(player.getFirstName().equalsIgnoreCase(first)){
						found = true;
						reset();
						return player;
					}
				}
			}
			else{
				reset();
				return null;
			}
		}
		reset();
		return null;
	}
	
	
	//List Readers. These don't reset the reader
	public Player readByFirstLetter(char letter){
		setupReader(filepath);
		boolean valid = false;
		if(letter > 40){
			if(letter < 91){
				valid = true;
			}
			else{
				if(letter > 60){
					if(letter < 123){
						valid = true;
						letter = (char) (letter+32);
					}
				}
			}
		}
		if(!valid){
			return null;
		}
		else{
			Player player;
			boolean found = false;
			
			while(!found && (player = readOne()) != null){
				if(player.getLastName().charAt(0) == letter){
						found = true;
						return player;
				}
			}
			reset();
			return null;
		}
	}
	
	public Player readByRow(int row){
		setupReader(filepath);
		boolean valid = false;
		if(row>0){
			if(row<7){
				valid = true;
			}
		}
		
		if(!valid){
			return null;
		}
		else{
			Player player;
			boolean found = false;
			
			while(!found && (player = readOne()) != null){
				if(player.getRow() == row){
						found = true;
						return player;
				}
			}
			reset();
			return null;
		}
	}
	
	public Player readByColumn(int column){
		setupReader(filepath);
		boolean valid = false;
		if(column>0){
			if(column<4){
				valid = true;
			}
		}
		
		if(!valid){
			return null;
		}
		else{
			Player player;
			boolean found = false;
			
			while(!found && (player = readOne()) != null){
				System.out.println(player.getColumn());
				if(player.getColumn() == column){
						found = true;
						return player;
				}
			}
			reset();
			return null;
		}
	}
	
	public Player readByCapability(int position){
		setupReader(filepath);
		boolean valid = false;
		if(position>=0){
			if(position<10){
				valid = true;
			}
		}
		if(!valid){
			return null;
		}
		else{
			if(position>1){
				Player player;
				boolean found = false;
				
				while(!found && (player = readOne()) != null){
					if(player.getPrimary() >1){
						Hitter hitter = (Hitter) player;
						if(hitter.canPlay(position)){
								found = true;
								return player;
						}
					}
				}
				reset();
				return null;
			}
			else{
				Pitcher player;
				boolean found = false;
				
				while(!found && (player = (Pitcher) readOne()) != null){
					if(player.getPrimary() == position){
							found = true;
							return player;
					}
				}
				reset();
				return null;
			}
		}
	}
	
	public Player readByPosition(int position){
		setupReader(filepath);
		boolean valid = false;
		if(position>=0){
			if(position<10){
				valid = true;
			}
		}
		if(!valid){
			return null;
		}
		else{
			Player player;
			boolean found = false;
			
			while(!found && (player = readOne()) != null){
				if(player.getPrimary() == position){
						found = true;
						return player;
				}
				else{
					if(player.getPrimary() > 1 && position != 0){
						if(((Hitter) player).getSecondary() == position){
						found = true;
						return player;
						}
					}
				}
			}
			reset();
			return null;
		}
	}
	
	public Player readByTeam(String team){
		setupReader(filepath);
		Player player;
		boolean found = false;
		
		while(!found && (player = readOne()) != null){
			if(player.getTeam().equalsIgnoreCase(team)){
					found = true;
					return player;
			}
		}
		reset();
		return null;
	}
	
	
	//Utilities
	public static int atoi(String string){
		int i = 0;
		if(string != ""){
			try{
				i = Integer.parseInt(string);
				return i;
			}catch(Exception e){
				return -1;
			}
		}
		else{
			return -1;
		}
	}

	public void close() {
		try {
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
