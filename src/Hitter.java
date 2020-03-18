import java.util.Comparator;
import java.util.Hashtable;

public class Hitter extends Player{
	//Values Read out of File
	private int secondary;
	private int con;
	private int pwr;
	private int run_spd;
	private int arm_str;
	private int fld;
	private int trj;
	private int e_res;
	
	//Values Calculated
	private int[] positionCapabilities = new int[10];
	private Hashtable<String, Integer> attributeHash = new Hashtable<String, Integer>();

	
	//Just implements Constructor from Parent
	public Hitter(int newId, String newLastName){
		super (newId, newLastName);
	}
	
	//Setters
	public void setPositions(int newPrimary, int newSecondary){
		primary = newPrimary;
		secondary = newSecondary;
		
		//setting position string
		if(primary>1){
			positionString = posNumToString(primary);
			if(secondary>1)
				positionString += ","+posNumToString(secondary);
		}	
		capabilitySetter(primary, secondary);
	}
	
	public void capabilitySetter(int primary, int secondary){
		if(primary > 1){
			switch(primary){
				case 2:
					positionCapabilities[2] = 6;
					break;
				case 3:
					positionCapabilities[3] = 6;
					positionCapabilities[4] = 1;
					positionCapabilities[5] = 1;
					positionCapabilities[6] = 1;
					break;
				case 4:
					positionCapabilities[4] = 6;
					positionCapabilities[3] = 1;
					positionCapabilities[5] = 2;
					positionCapabilities[6] = 2;
					break;
				case 5:
					positionCapabilities[5] = 6;
					positionCapabilities[3] = 1;
					positionCapabilities[4] = 2;
					positionCapabilities[6] = 2;
					break;
				case 6:
					positionCapabilities[6] = 6;
					positionCapabilities[3] = 1;
					positionCapabilities[4] = 2;
					positionCapabilities[5] = 2;
					break;
				case 7:
					positionCapabilities[7] = 6;
					positionCapabilities[8] = 2;
					positionCapabilities[9] = 2;
					break;
				case 8:
					positionCapabilities[8] = 6;
					positionCapabilities[7] = 2;
					positionCapabilities[9] = 2;
					break;
				case 9:
					positionCapabilities[9] = 6;
					positionCapabilities[7] = 2;
					positionCapabilities[8] = 2;
					break;
				default:
					break;
			}
			if(secondary > 1){
				positionCapabilities[secondary] = 5;
			}
		}
	}
	
	public void setAttributes(int newCon, int newPwr, int newRun_spd, int newArm_str, int newFld, int newTrj, int newE_res){
		con = newCon;
		pwr = newPwr;
		run_spd = newRun_spd;
		arm_str = newArm_str;
		fld = newFld;
		trj = newTrj;
		e_res = newE_res;
		totalRating = (7.75186)*((con*0.3)+(pwr*(.0588235)*(0.5))+(run_spd*0.1)+(arm_str*0.05)+(fld*0.025)+(e_res*0.025));
		totalRating = (-.01010101010101)*((totalRating-99.0)*(totalRating-99.0))+99;
		double pRat = (.0001)*(totalRating*totalRating);
		totalRating = (totalRating-((100-totalRating)*pRat));
		totalRating = totalRating*(100.0/99);
		overall = (int) totalRating.intValue();
		
		attributeHash.put("CON", con);
		attributeHash.put("PWR", pwr);
		attributeHash.put("SPD", run_spd);
		attributeHash.put("ARM", arm_str);
		attributeHash.put("FLD", fld);
		attributeHash.put("TRJ", trj);
		attributeHash.put("eRES", e_res);
	}
	
	public Integer getAttribute(String key){
		return attributeHash.get(key);
	}
	
	//Getters
	public int getSecondary(){
		return secondary;
	}
	
	public int getCon(){
		return con;
	}
	
	public int getPwr(){
		return pwr;
	}
	
	public int getRun_spd(){
		return run_spd;
	}
	
	public int getArm_str(){
		return arm_str;
	}
	
	public int getFld(){
		return fld;
	}
	
	public int getTrj(){
		return trj;
	}
	
	public int getE_res(){
		return e_res;
	}
	
	
	//Utilities
	public String numToGrade(int num){
		switch(num){
			case 0:
				return "G";
			case 1:
				return "F";
			case 2:
				return "E";
			case 3:
				return "D";
			case 4:
				return "C";
			case 5:
				return "B";
			case 6:
				return "A";
			default:
				return "Error";
		}
	}

	public void printAttributes(){
		print();
		System.out.println("CON: "+con);
		System.out.println("POW: "+pwr);
		System.out.println("SPD: "+run_spd);
		System.out.println("ARM: "+arm_str);
		System.out.println("FLD: "+fld);
		System.out.println("TRJ: "+trj);
		System.out.println("eRES: "+e_res);
	}
	
	public void printCapabilities(){
		print();
		for(int i=2; i<10; i++){
			System.out.println(posNumToString(i)+": "+numToGrade(positionCapabilities[i]));
		}
	}
	
	public boolean canPlay(int position){
		return positionCapabilities[position] >= 2;
	}
	
}
