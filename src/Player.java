
public abstract class Player {
	protected int id;
	protected String firstName;
	protected String lastName;
	protected String team;
	protected String positionString;
	protected boolean isPitcher = false;
	protected int primary;
	protected int row;
	protected int column;
	
	protected Double totalRating;
	protected int overall;
	
	public Player(int newId, String newLastName){
		id = newId;
		lastName = newLastName;
	}
	
	
	//Setters
	public void setFirstName(String newFirstName){
		firstName = newFirstName;
	}
	
	public void setTeam(String newTeam){
		team = newTeam;
	}
	
	public void setGrid(int newColumn, int newRow){
		row = newRow;
		column = newColumn;
	}
	
	
	//Getters
	public int getId(){
		return id;
	}
	
	public String getFirstName(){
		return firstName;
	}
	
	public String getLastName(){
		return lastName;
	}
	
	public String getTeam(){
		return team;
	}
	
	public Integer getPrimary(){
		return primary;
	}
	
	public int getRow(){
		return row;
	}
	
	public int getColumn(){
		return column;
	}
	
	public Double getTotalRating(){
		return totalRating;
	}
	
	public int getOverall(){
		return overall;
	}
	
	public abstract void printAttributes();
	
	public abstract Integer getAttribute(String key);
	
	
	//Utilities
	public String posNumToString(int posNum)
	{
		String position;
		String[] posList = {"RP", "SP", "C ", "1B", "2B", "3B", "SS", "LF", "CF", "RF", "DH"};
		position = posList[posNum];
		return position;				
	}
	
	public String toString(){
		String playerString = lastName;
		if(firstName!=null)
			playerString +=", "+firstName;
		if(overall!=0){
			playerString +=" "+totalRating+"\n";
		}
		else
			playerString += "\n";
		
		if(team!=null)
			playerString += team;
		
		if(positionString!=null)
			playerString += "  "+positionString+"\n";
		else
			playerString += "\n";
		return playerString;
	}
	
	public String toStringLine(){
		String playerString = "";
		if(overall!=0)
			playerString +=" "+overall;
		if(positionString!=null){
			if(positionString.length()>2)
				playerString += " "+positionString;
			else
				playerString += " "+positionString+"   ";
		}
			
		playerString += " "+lastName;
		if(firstName!=null)
			playerString +=", "+firstName;
		
		return playerString;
	}
	
	public String toAttributeLine(String attributeKey){
		String playerString = "";
		if(getAttribute(attributeKey) != null)
			playerString +="   "+attributeKey+": "+getAttribute(attributeKey);
		if(overall!=0)
			playerString +=" overall: "+overall;
		if(positionString!=null){
			if(positionString.length()>2)
				playerString += " "+positionString;
			else
				playerString += " "+positionString+"   ";
		}
			
		playerString += " "+lastName;
		if(firstName!=null)
			playerString +=", "+firstName;
		
		return playerString;
	}
	
	
	public void print(){
		System.out.print(toString());
	}
}
