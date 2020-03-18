
public abstract class Criteria {
	protected String name;
	protected String key = "";
	protected double startingProb;
	protected double prob;
	protected boolean isCriteria = true;
	
	public Criteria(String newName, double newStartingProb){
		name = newName;
		startingProb = newStartingProb;
		prob = startingProb;
	}
	
	public abstract void generate();
	
	public void decay(int playersRemaining){
		prob = (startingProb/970.0)*playersRemaining;
	}
	
	public void reset(){
		prob = startingProb;
	}
	
	public double getProb(){
		return prob;
	}
	
	public String getName(){
		return name;
	}
	
	public String getKey(){
		return key;
	}
	
	public String toString(){
		if(key.length()>0)
			return name+": "+key;
		else{
			return name;
		}
	}
	
	public boolean isCriteria(){
		return isCriteria;
	}
}
