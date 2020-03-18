import java.util.Hashtable;

public class Pitcher extends Player{
	protected int top_spd;
	protected int ctrl;
	protected int stam;
	protected int straight;
	protected int slide;
	protected int curve;
	protected int fork;
	protected int sink;
	protected int sink_fsb;
	
	protected boolean isReliever = false; 
	private Hashtable<String, Integer> attributeHash = new Hashtable<String, Integer>();
	
	public Pitcher(int newId, String newLastName){
		super(newId, newLastName);
		isPitcher = true;
	}
	
	public void setRole(int role){
		primary = role;
		if(role==0){
			isReliever = true;
			positionString = "RP";
		}
		else{
			positionString = "SP";
		}
	}
	
	public void setAttributes(int newTop_spd, int newCtrl, int newStam, int newStraight, int newSlide, int newCurve, int newFork, int newSink, int newSink_fsb){
		top_spd = newTop_spd;
		ctrl = newCtrl;
		stam = newStam;
		straight = newStraight;
		slide = newSlide;
		curve = newCurve;
		fork = newFork;
		sink = newSink;
		sink_fsb = newSink_fsb;
		
		double spd_points;
		if(top_spd < 80){
			spd_points = 0;
		}
		else{
			if(top_spd < 95){
				spd_points = (top_spd - 80)*0.6;
			}
			else{
				if(top_spd < 100){
					spd_points = 9+(top_spd - 95)*.5;
				}
				else{
					spd_points = (11.5)+(top_spd - 100)*.4;
				}
			}
		}
		totalRating = 
				(spd_points*.6)+
				(ctrl*.058824*.16)+
				(stam*.058824*.1)+
				(straight*5*.04)+
				(slide*2.1429*.04)+
				(curve*2.1429*.005)+
				(fork*2.1429*.02)+
				(sink*2.1429*.005)+
				(sink_fsb*2.1429*.03);
		totalRating = totalRating * (100/9.3);
		overall = totalRating.intValue();
		
		attributeHash.put("VELO", top_spd);
		attributeHash.put("CTRL", ctrl);
		attributeHash.put("STAM", stam);
		attributeHash.put("STRT", straight);
		attributeHash.put("SLD", slide);
		attributeHash.put("CRV", curve);
		attributeHash.put("FORK", fork);
		attributeHash.put("SINK", sink);
		attributeHash.put("SKFB", sink_fsb);
	}
	
	public Integer getAttribute(String key){
		return attributeHash.get(key);
	}
	
	public void printAttributes(){
		print();
		System.out.println("VELO: "+top_spd);
		System.out.println("CTRL: "+ctrl);
		System.out.println("STAM: "+stam);
		System.out.println("STRT: "+straight);
		System.out.println("SLD : "+slide);
		System.out.println("CRV : "+curve);
		System.out.println("FORK: "+fork);
		System.out.println("SINK: "+sink);
		System.out.println("SKFB: "+sink_fsb);
	}
}
