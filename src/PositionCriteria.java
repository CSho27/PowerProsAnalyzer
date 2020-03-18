
public class PositionCriteria extends Criteria{
	String[] positions = {"RP", "SP", "C", "1B", "2B", "3B", "SS", "LF", "CF", "RF"};
	
	public PositionCriteria(double newStartingProb) {
		super("Position", newStartingProb);
	}

	@Override
	public void generate() {
		int num = (int) (Math.random()*10);
		key = ""+num;
	}
	
	@Override
	public String toString(){
		if(key.length()>0)
			return name+": "+positions[atoi(key)];
		else{
			return name;
		}
	}
	
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

}
