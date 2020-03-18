import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class TeamWriter {
	static String filepath;
	static BufferedWriter writer;
	
	public static void setupWriter(String newFilepath){
		filepath = newFilepath;
		try{
			File file = new File(filepath);
			BufferedWriter bwriter = new BufferedWriter(new FileWriter(file));
			writer = bwriter;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public TeamWriter(String newFilepath){
		filepath = newFilepath;
	}
	
	public static void writeOne(Team team){
		team.overallSetter();
		setupWriter(filepath);
		ArrayList<Player> players = team.getRoster();
		Analyzer.writeList(filepath, players);
		close();
	}
	
	public static void close(){
		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
}
