import java.util.ArrayList;

public class CriteriaGenerator {
	ArrayList<Criteria> possibleCriteria = new ArrayList<Criteria>();
	
	public CriteriaGenerator(){
		reset();
	}
	
	public Criteria generateCriteria(int playersRemaining){
		double[] rangeStarts = new double[possibleCriteria.size()];
		double[] probs = new double[possibleCriteria.size()];
		double sum = 0;
		//System.out.println("\nplayers remianing: "+playersRemaining);
		for(int i=0; i<possibleCriteria.size(); i++){
			possibleCriteria.get(i).decay(playersRemaining);
			rangeStarts[i] = sum;
			sum += possibleCriteria.get(i).getProb();
			probs[i] = possibleCriteria.get(i).getProb();
		}
		double num = Math.random()*sum;
		for(int i=0; i<possibleCriteria.size(); i++){
			double percentage = probs[i]/sum;
			//System.out.println(possibleCriteria.get(i).getName()+": "+percentage);
		}
		for(int i=0; i<rangeStarts.length; i++){
			if(num>=rangeStarts[i] && num<rangeStarts[i]+possibleCriteria.get(i).getProb()){
				if(!possibleCriteria.get(i).isCriteria()){
					possibleCriteria.get(i).generate();
					return possibleCriteria.get(i);
				}
				else{
					possibleCriteria.get(i).generate();
					return possibleCriteria.remove(i);
				}
			}
		}
		return null;	
	}
	
	public void reset(){
		possibleCriteria.add(new OpponentCriteria(1));
		possibleCriteria.add(new TeamCriteria(10));
		possibleCriteria.add(new LetterCriteria("FofL", 9));
		possibleCriteria.add(new LetterCriteria("FofF", 9));
		possibleCriteria.add(new LetterCriteria("LofF", 9));
		possibleCriteria.add(new LetterCriteria("LofL", 9));
		possibleCriteria.add(new NameLengthCriteria(15));
		possibleCriteria.add(new PositionCriteria(15));
		possibleCriteria.add(new GridCriteria("Row", 10));
		possibleCriteria.add(new GridCriteria("Column", 10));
		possibleCriteria.add(new NoCriteria(.25));
	}
	

			
	
	
}
