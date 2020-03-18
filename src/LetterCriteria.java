
public class LetterCriteria extends Criteria{
	char[] letters;
	String displayName;
	
	public LetterCriteria(String name, double newStartingProb) {
		super(name, newStartingProb);
		letters =  new char[26];
		//inserting the alphabet into the letters array
		for(int i = 0; i < letters.length; i++){
			letters[i] = (char)(i+65);
		}
		displayName = name;
		displayName = displayName.replace("Lo", "Last Letter of");
		displayName = displayName.replace("Fo", "First Letter of");
		displayName = displayName.replace("fL", " Last Name");
		displayName = displayName.replace("fF", " First Name");
	}

	@Override
	public void generate() {
		int let = (int) (Math.random()*26);
		char letter = letters[let];
		key = ""+letter;
	}
	
	@Override
	public String toString(){
		if(key.length()>0)
			return displayName+": "+key;
		else{
			return displayName;
		}
	}

}
