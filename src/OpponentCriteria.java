
public class OpponentCriteria extends Criteria {

	public OpponentCriteria(double newStartingProb) {
		super("Opponent Picks", newStartingProb);
	}

	@Override
	public void generate() {
	}
	
	@Override
	public String toString(){
		return "***Opponent Picks***";
	}

}
