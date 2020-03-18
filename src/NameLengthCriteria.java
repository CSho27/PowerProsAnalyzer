
public class NameLengthCriteria extends Criteria {

	public NameLengthCriteria(double newStartingProb) {
		super("Name Length", newStartingProb);
	}

	@Override
	public void generate() {
		int num = (int) (Math.random()*9+3);
		key = ""+num;
	}
}
