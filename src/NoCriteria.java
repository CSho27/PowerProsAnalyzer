
public class NoCriteria extends Criteria {

	public NoCriteria(double newStartingProb) {
		super("Nothing", newStartingProb);
		isCriteria = false;
	}
	
	@Override
	public void decay(int num){
		prob = startingProb;
	}
	
	@Override
	public void generate() {
	}
	
}
