import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class PlayerWriter {
	static String filepath;
	static BufferedWriter writer;
	
	public static BufferedWriter setupWriter(String newFilepath){
		filepath = newFilepath;
		try{
			File file = new File(filepath);
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			return writer;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public PlayerWriter(String newFilepath){
		filepath = newFilepath;
		writer = setupWriter(filepath);
	}
	
	public static void writeOne(Player player){
		if(player.getPrimary()>1){
			Hitter hitter = (Hitter) player;
			String line = 
					hitter.getId()+","+
					hitter.getFirstName()+","+
					hitter.getLastName()+","+
					hitter.getTeam()+","+
					hitter.getPrimary()+","+
					hitter.getSecondary()+","+
					hitter.getCon()+","+
					hitter.getPwr()+","+
					hitter.getRun_spd()+","+
					hitter.getArm_str()+","+
					hitter.getFld()+","+
					hitter.getTrj()+","+
					hitter.getE_res()+","+
					hitter.getColumn()+","+
					hitter.getRow()+","+
					hitter.getOverall()+","+
					hitter.getTotalRating()+"\n";
			try {
				writer.append(line);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else{
			Pitcher pitcher = (Pitcher) player;
			String line = 
					pitcher.getId()+","+
					pitcher.getFirstName()+","+
					pitcher.getLastName()+","+
					pitcher.getTeam()+","+
					pitcher.getPrimary()+","+
					pitcher.getAttribute("VELO")+","+
					pitcher.getAttribute("CTRL")+","+
					pitcher.getAttribute("STAM")+","+
					pitcher.getAttribute("STRT")+","+
					pitcher.getAttribute("SLD")+","+
					pitcher.getAttribute("CRV")+","+
					pitcher.getAttribute("FORK")+","+
					pitcher.getAttribute("SINK")+","+
					pitcher.getAttribute("SKFB")+","+
					pitcher.getColumn()+","+
					pitcher.getRow()+","+
					pitcher.getOverall()+","+
					pitcher.getTotalRating()+"\n";
			try {
				writer.append(line);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void close(){
		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
