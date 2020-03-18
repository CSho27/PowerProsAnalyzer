
public class DisplayLine {
	public static String displaySideBySide(String string1, String string2, int separation){
		String[] lines1 = string1.split("\n");
		String[] lines2 = string2.split("\n");
		String display = "";
		int maxLength = 0;
		for(int i=0; i<lines1.length; i++){
			if(lines1[i].length() > maxLength)
				maxLength = lines1[i].length();
		}
		for(int i=0; i<lines1.length || i<lines2.length; i++){
			if(i<lines1.length){
				display += lines1[i];
				for(int j=0; j<(maxLength-lines1[i].length()+separation); j++)
					display += " ";
			}
			else{
				for(int j=0; j<(maxLength+separation); j++)
					display += " ";
			}
			if(i<lines2.length){
				display += lines2[i];
			}
			display += "\n";
		}
		return display;
	}
	
	public static void main(String[] args){
		System.out.println(displaySideBySide("1\n2.\n3.)\n12)\nasdjaldj", "1\n2.\n3.)\nasldkasldasdlasldkal\n4.\n5.\n6. YADADADA", 8));
	}
}
