
public class GridCriteria extends Criteria{
	public GridCriteria(String name, double newStartingProb) {
		super(name, newStartingProb);
	}

	@Override
	public void generate() {
		int num;
		if(name.equals("Column")){
			 num = (int) (Math.random()*3+1);
		}
		else{
			if(name.equals("Row")){
				num = (int) (Math.random()*6+1);
			}
			else{
				num = 0;
			}
		}
		key = ""+num;
	}
}
