import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class RatingsAnalysis {
	public static void printPlayerAttributes(String firstName, String lastName, int year){
		long id = getPlayerID(firstName, lastName);
		if(id > 0){
			HittingStats hStats = getPlayerStats(id, year);
			FieldingStats fStats = getFieldingStats(id, year);
			if(hStats != null && fStats != null){
				if(hStats.ab<100 || fStats.tc<100){
					System.out.println("**WARNING** Not enough appearances to be statistically significant");
				}
				int pos = fStats.position;
				double avg = hStats.getAVG();
				double slg = hStats.getSLG();
				int hr = hStats.hr;
				double go_ao = hStats.getGoAo();
				int sb = hStats.sb;
				int cs = hStats.cs;
				int r = hStats.r;
				double fpct = fStats.getFPCT();
				int tc = fStats.tc;
				System.out.println(lastName+", "+firstName);
				int calculatedOVR = overallCalculator(pos, avg, slg, hr, go_ao, sb, cs, r, fpct, tc);
			} else {
				if(hStats == null)
					System.out.println("hitting");
				if(fStats == null)
					System.out.println("fielding");
				System.out.println("ERROR: player stats null");
			}
		} else {
			System.out.println("ERROR: player could not be found");
		}
	}
	
	public static void overallAnalysis(String filename){
		ArrayList<Double> diff = new ArrayList<Double>();
		ArrayList<Double> ovr = new ArrayList<Double>();
		ArrayList<Double> calcOVR = new ArrayList<Double>();
		ArrayList<Player> players = Analyzer.readAll(filename);
		int count = 0;
		for(int i=0; i<players.size(); i++){
			String first = players.get(i).firstName;
			String last = players.get(i).lastName;
			long id = getPlayerID(first, last);
			if(id > 0){
				HittingStats hStats = getPlayerStats(id, 2006);
				FieldingStats fStats = getFieldingStats(id, 2006);
				if(hStats != null && fStats != null){
					fStats.setInnings();
					if(hStats.ab > 100 && fStats.innings>100){
						count++;
						Player player = players.get(i);
						int listedOVR = player.getOverall();
						int pos = player.getPrimary();
						double avg = hStats.getAVG();
						double slg = hStats.getSLG();
						int hr = hStats.hr;
						double go_ao = hStats.getGoAo();
						int sb = hStats.sb;
						int cs = hStats.cs;
						int r = hStats.r;
						double fpct = fStats.getFPCT();
						int tc = fStats.tc;
						System.out.println(player.toString());
						player.printAttributes();
						int calculatedOVR = overallCalculator(pos, avg, slg, hr, go_ao, sb, cs, r, fpct, tc);
						int calcPWR = powerCalculator(slg, hr);
						ovr.add((double) player.getOverall());
						calcOVR.add((double) calculatedOVR);
						diff.add((double) calculatedOVR- player.getOverall());
					}
				}
			}
		}
		System.out.println("\nOVR");
		printStats(ovr);
		System.out.println("\nCALC OVR");
		printStats(calcOVR);
		System.out.println("\nDIFF");
		printStats(diff);
	}
	
	public static int overallCalculator(int pos, double avg, double slg, int hr, double go_ao, int sb, int cs, int r, double fpct, int tc){
		int con = contactCalculator(avg);
		int pwr = powerCalculator(slg, hr);
		int trj = trajectoryCalculator(go_ao);
		int run_spd = runSpdCalculator(sb, cs, r);
		int arm_str = armCalculator(tc, pos);
		int fld = fldCalculator(fpct, tc);
		int e_res = eResCalculator(fpct, tc);
		double totalRating = (7.75186)*((con*0.3)+(pwr*(.0588235)*(0.5))+(run_spd*0.1)+(arm_str*0.05)+(fld*0.025)+(e_res*0.025));
		totalRating = (-.01010101010101)*((totalRating-99.0)*(totalRating-99.0))+99;
		double pRat = (.0001)*(totalRating*totalRating);
		totalRating = (totalRating-((100-totalRating)*pRat));
		totalRating = totalRating*(100.0/99);
		int overall = (int) totalRating;
		System.out.println("CON: "+con);
		System.out.println("POW: "+pwr);
		System.out.println("SPD: "+run_spd);
		System.out.println("ARM: "+arm_str);
		System.out.println("FLD: "+fld);
		System.out.println("TRJ: "+trj);
		System.out.println("eRES: "+e_res);
		System.out.println("OVR: "+overall);
		System.out.println();
		return overall;
	}
	
	public static int contactCalculator(double avg){
		if(between(avg, 0.0, 0.150)){
			return 1;
		} else if(between(avg, 0.150, 0.175)){
			return 2;
		} else if(between(avg, 0.175, 0.200)){
			return 3;
		} else if(between(avg, .200, .230)){
			return 4;
		} else if(between(avg, .230, .240)){
			return 5;
		} else if(between(avg, .240, .250)){
			return 6;
		} else if(between(avg, .250, .268)){
			return 7;
		} else if(between(avg, .268, .282)){
			return 8;
		} else if(between(avg, .282, .295)){
			return 9;
		} else if(between(avg, .295, .310)){
			return 10;
		} else if(between(avg, .310, .320)){
			return 11;
		} else if(between(avg, .320, .335)){
			return 12;
		} else if(between(avg, .335, .345)){
			return 13;
		} else if(between(avg, .345, .350)){
			return 14;
		} else {
			return 15;
		}
	}
	
	public static int ratingToAttribute(double rating){
		int attribute = (int) Math.round(rating);
		if(attribute < 1){
			return 1;
		}else{
			return attribute;
		}
	}
	
	public static int powerCalculator(double slg, double hr){
		//double slg_pwr = 375*slg-10;\
		double slg_pwr = 373*slg-15;
		double hr_pwr = 2.68*hr+100;
		double pwr = slg_pwr*.7+hr_pwr*.3;
		return (int) ratingToAttribute(pwr);
	}
	
	public static int trajectoryCalculator(double go_ao){
		if(between(go_ao, 0.0, 1.0)){
			return 4;
		} else if(between(go_ao, 1.0, 1.5)){
			return 3;
		} else if(between(go_ao, 1.5, 2.0)){
			return 2;
		} else {
			return 1;
		}
	}
	
	public static int runSpdCalculator(int sb, int cs, int r){
		double corr = .5*(sb/64.0)+.25*(cs/20.0)+.25*(r/130.0);
		double spd;
		if(corr < 0.3249){
			spd = 20.8*corr+4.25;
		} else {
			spd = 4.882*corr+9.4;
		}
		return (int) ratingToAttribute(spd);
	}
	
	public static int eResCalculator(double fpct, double tc){
		double tc_eRES;
		double fpct_eRES;

		if(tc<305){
			tc_eRES = tc*.0147 + 6.518;
		} else {
			tc_eRES = tc*.00173 + 10.47;
		}
		
		fpct_eRES = fpct*159.1 - 145.1;
		
		return (int) ratingToAttribute(tc_eRES*.8 + fpct_eRES*.2);
	}
	
	public static int armCalculator(int tc, int pos){
		double[] armAvg = {0.0, 0.0, 8.94, 9.4, 9.14, 9.5, 9.75, 8.82, 9.57, 8.63};
		double tc_arm;

		if(tc<379){
			tc_arm = tc*.0199 + 4.42;
		} else {
			tc_arm = tc*.00182 + 11.31;
		}
		
		return (int) ratingToAttribute(tc_arm*.5 + armAvg[pos]*.5);
	}
	
	public static int fldCalculator(double fpct, double tc){
		double tc_eRES;
		double fpct_eRES;

		if(tc<305){
			tc_eRES = tc*.03017 + 1.798;
		} else {
			tc_eRES = tc*.001887 + 10.4;
		}
		
		fpct_eRES = fpct*171.16 - 158.6;
		
		return (int) ratingToAttribute(tc_eRES*.25 + fpct_eRES*.75);
	}
	
	public static void armAnalysis(String filename){
		ArrayList<Integer> pos = new ArrayList<Integer>();
		for(int j=2; j<=9; j++){
			ArrayList<Double> arm = new ArrayList<Double>();
			ArrayList<Player> players = Analyzer.readAll(filename);
			int armSum = 0;
			int count = 0;
			//players = Analyzer.narrowByAttribute(players, "FLD", false, j, 2);
			players = Analyzer.NarrowByPrimary(players, j);
			for(int i=0; i<players.size(); i++){
				String first = players.get(i).firstName;
				String last = players.get(i).lastName;
				long id = getPlayerID(first, last);
				if(id > 0){
					FieldingStats stats = getFieldingStats(id, 2006);
					if(stats != null){
						count++;
						armSum += stats.a;
						arm.add((double) stats.a);
					}
				}
			}
			double average = (double) armSum / count;
			System.out.println("Arm Stats: "+Analyzer.posNumToString(j));
			printStats(arm);
			System.out.println("avg: "+average+"\n");
		}
	}
	
	public static void statisticAnalysis(String filename){
		ArrayList<Integer> pos = new ArrayList<Integer>();
		ArrayList<Integer> arm = new ArrayList<Integer>();
		ArrayList<Double> ass = new ArrayList<Double>();
		int count = 0;
		for(int j=1; j<=15; j++){
			ArrayList<Player> players = Analyzer.readAll(filename);
			players = Analyzer.narrowByAttribute(players, "ARM", false, j, 2);
			for(int i=0; i<players.size(); i++){
				String first = players.get(i).firstName;
				String last = players.get(i).lastName;
				long id = getPlayerID(first, last);
				if(id > 0){
					FieldingStats stats = getFieldingStats(id, 2006);
					if(stats != null){
						stats.setInnings();
						if(stats.innings>100){
							count++;
							double a_inn = stats.a / stats.innings;
							System.out.println(count);
							pos.add(players.get(i).getPrimary());
							arm.add(j);
							ass.add((double) stats.tc);
						}
					}
				}
			}
		}
		System.out.println("\nPOS");
		for(int i=0; i<pos.size(); i++){
			System.out.println(pos.get(i));
		}
		
		System.out.println("\nARM");
		for(int i=0; i<arm.size(); i++){
			System.out.println(arm.get(i));
		}
		
		System.out.println("\nAssists/Inning");
		for(int i=0; i<ass.size(); i++){
			System.out.println(ass.get(i));
		}
	}
	
	public static void fldAnalysis(String filename){
		ArrayList<Integer> fld = new ArrayList<Integer>();
		ArrayList<Double> fpct   = new ArrayList<Double>();
		ArrayList<Double> rf = new ArrayList<Double>();
		ArrayList<Integer> tc = new ArrayList<Integer>();
		int count = 0;
		for(int j=1; j<=15; j++){
			ArrayList<Player> players = Analyzer.readAll(filename);
			players = Analyzer.narrowByAttribute(players, "FLD", false, j, 2);
			double slgSum = 0;
			double hr_abSum = 0;
			double tb_hSum = 0;
			/*
			ArrayList<Double> slg = new ArrayList<Double>();
			ArrayList<Double> hr_ab = new ArrayList<Double>();
			ArrayList<Double> tb_h = new ArrayList<Double>();
			*/
			for(int i=0; i<players.size(); i++){
				String first = players.get(i).firstName;
				String last = players.get(i).lastName;
				long id = getPlayerID(first, last);
				if(id > 0){
					FieldingStats stats = getFieldingStats(id, 2006);
					if(stats != null){
						stats.setInnings();
						if(stats.innings>100){
							count++;
							System.out.println(count);
							fld.add(j);
							fpct.add(stats.getFPCT());
							rf.add(stats.getRF());
							tc.add(stats.tc);
						}
					}
				}
			}
			/*
			double slgAverage = slgSum/count;
			double hr_abAverage = hr_abSum/count;
			double tb_hAverage = tb_hSum/count;
			if(count > 0){
				System.out.println("POW: "+j);
				System.out.println("SLG");
				printStats(slg);
				System.out.println("avg: "+slgAverage+"\n");
				
				System.out.println("HR/AB");
				printStats(hr_ab);
				System.out.println("avg: "+slgAverage+"\n");
				
				System.out.println("TB/H");
				printStats(tb_h);
				System.out.println("avg: "+slgAverage+"\n");
			} else {
				System.out.println("\nPOW: "+j);
			}
			*/
		}
		System.out.println("\neRES");
		for(int i=0; i<fld.size(); i++){
			System.out.println(fld.get(i));
		}
		System.out.println("\nFPCT");
		for(int i=0; i<fpct.size(); i++){
			System.out.println(fpct.get(i));
		}
		System.out.println("\nRF");
		for(int i=0; i<rf.size(); i++){
			System.out.println(rf.get(i));
		}
		System.out.println("\nTC");
		for(int i=0; i<tc.size(); i++){
			System.out.println(tc.get(i));
		}
	}
	
	public static void eResAnalysis(String filename){
		ArrayList<Integer> eRes = new ArrayList<Integer>();
		ArrayList<Double> fpct   = new ArrayList<Double>();
		ArrayList<Double> rf = new ArrayList<Double>();
		ArrayList<Integer> tc = new ArrayList<Integer>();
		int count = 0;
		for(int j=1; j<=15; j++){
			ArrayList<Player> players = Analyzer.readAll(filename);
			players = Analyzer.narrowByAttribute(players, "eRES", false, j, 2);
			double slgSum = 0;
			double hr_abSum = 0;
			double tb_hSum = 0;
			/*
			ArrayList<Double> slg = new ArrayList<Double>();
			ArrayList<Double> hr_ab = new ArrayList<Double>();
			ArrayList<Double> tb_h = new ArrayList<Double>();
			*/
			for(int i=0; i<players.size(); i++){
				String first = players.get(i).firstName;
				String last = players.get(i).lastName;
				long id = getPlayerID(first, last);
				if(id > 0){
					FieldingStats stats = getFieldingStats(id, 2006);
					if(stats != null){
						stats.setInnings();
						if(stats.innings>100){
							count++;
							System.out.println(count);
							eRes.add(j);
							fpct.add(stats.getFPCT());
							rf.add(stats.getRF());
							tc.add(stats.tc);
						}
					}
				}
			}
			/*
			double slgAverage = slgSum/count;
			double hr_abAverage = hr_abSum/count;
			double tb_hAverage = tb_hSum/count;
			if(count > 0){
				System.out.println("POW: "+j);
				System.out.println("SLG");
				printStats(slg);
				System.out.println("avg: "+slgAverage+"\n");
				
				System.out.println("HR/AB");
				printStats(hr_ab);
				System.out.println("avg: "+slgAverage+"\n");
				
				System.out.println("TB/H");
				printStats(tb_h);
				System.out.println("avg: "+slgAverage+"\n");
			} else {
				System.out.println("\nPOW: "+j);
			}
			*/
		}
		System.out.println("\neRES");
		for(int i=0; i<eRes.size(); i++){
			System.out.println(eRes.get(i));
		}
		System.out.println("\nFPCT");
		for(int i=0; i<fpct.size(); i++){
			System.out.println(fpct.get(i));
		}
		System.out.println("\nRF");
		for(int i=0; i<rf.size(); i++){
			System.out.println(rf.get(i));
		}
		System.out.println("\nTC");
		for(int i=0; i<tc.size(); i++){
			System.out.println(tc.get(i));
		}
	}
	
	public static void trjAnalysis(String filename){
		ArrayList<Integer> trj = new ArrayList<Integer>();
		ArrayList<Double> go_ao   = new ArrayList<Double>();
		int count = 0;
		for(int j=1; j<=4; j++){
			ArrayList<Player> players = Analyzer.readAll(filename);
			players = Analyzer.narrowByAttribute(players, "TRJ", false, j, 2);
			double slgSum = 0;
			double hr_abSum = 0;
			double tb_hSum = 0;
			/*
			ArrayList<Double> slg = new ArrayList<Double>();
			ArrayList<Double> hr_ab = new ArrayList<Double>();
			ArrayList<Double> tb_h = new ArrayList<Double>();
			*/
			for(int i=0; i<players.size(); i++){
				String first = players.get(i).firstName;
				String last = players.get(i).lastName;
				long id = getPlayerID(first, last);
				if(id > 0){
					HittingStats stats = getPlayerStats(id, 2006);
					if(stats != null){
						if(stats.ab>100){
							count++;
							System.out.println(count);
							trj.add(j);
							go_ao.add(stats.getSLG());
						}
					}
				}
			}
			/*
			double slgAverage = slgSum/count;
			double hr_abAverage = hr_abSum/count;
			double tb_hAverage = tb_hSum/count;
			if(count > 0){
				System.out.println("POW: "+j);
				System.out.println("SLG");
				printStats(slg);
				System.out.println("avg: "+slgAverage+"\n");
				
				System.out.println("HR/AB");
				printStats(hr_ab);
				System.out.println("avg: "+slgAverage+"\n");
				
				System.out.println("TB/H");
				printStats(tb_h);
				System.out.println("avg: "+slgAverage+"\n");
			} else {
				System.out.println("\nPOW: "+j);
			}
			*/
		}
		System.out.println("\nTRJ");
		for(int i=0; i<trj.size(); i++){
			System.out.println(trj.get(i));
		}
		System.out.println("\nGO/AO");
		for(int i=0; i<go_ao.size(); i++){
			System.out.println(go_ao.get(i));
		}
	}
	
	
	
	public static void runSpeedAnalysis(String filename){
		ArrayList<Integer> spd = new ArrayList<Integer>();
		ArrayList<Integer> sb  = new ArrayList<Integer>();
		ArrayList<Integer> cs = new ArrayList<Integer>();
		ArrayList<Integer> r = new ArrayList<Integer>();
		int count = 0;
		for(int j=1; j<=15; j++){
			ArrayList<Player> players = Analyzer.readAll(filename);
			players = Analyzer.narrowByAttribute(players, "SPD", false, j, 2);
			double slgSum = 0;
			double hr_abSum = 0;
			double tb_hSum = 0;
			/*
			ArrayList<Double> slg = new ArrayList<Double>();
			ArrayList<Double> hr_ab = new ArrayList<Double>();
			ArrayList<Double> tb_h = new ArrayList<Double>();
			*/
			for(int i=0; i<players.size(); i++){
				String first = players.get(i).firstName;
				String last = players.get(i).lastName;
				long id = getPlayerID(first, last);
				if(id > 0){
					HittingStats stats = getPlayerStats(id, 2006);
					if(stats != null){
						if(stats.ab>100){
							count++;
							System.out.println(count);
							spd.add(j);
							sb.add(stats.sb);
							cs.add(stats.cs);
							r.add(stats.r);
						}
					}
				}
			}
			/*
			double slgAverage = slgSum/count;
			double hr_abAverage = hr_abSum/count;
			double tb_hAverage = tb_hSum/count;
			if(count > 0){
				System.out.println("POW: "+j);
				System.out.println("SLG");
				printStats(slg);
				System.out.println("avg: "+slgAverage+"\n");
				
				System.out.println("HR/AB");
				printStats(hr_ab);
				System.out.println("avg: "+slgAverage+"\n");
				
				System.out.println("TB/H");
				printStats(tb_h);
				System.out.println("avg: "+slgAverage+"\n");
			} else {
				System.out.println("\nPOW: "+j);
			}
			*/
		}
		System.out.println("\nSPD");
		for(int i=0; i<spd.size(); i++){
			System.out.println(spd.get(i));
		}
		System.out.println("\nSB");
		for(int i=0; i<sb.size(); i++){
			System.out.println(sb.get(i));
		}
		System.out.println("\nCS");
		for(int i=0; i<cs.size(); i++){
			System.out.println(cs.get(i));
		}
		System.out.println("\nR");
		for(int i=0; i<r.size(); i++){
			System.out.println(r.get(i));
		}
	}
	
	public static void printStats(ArrayList<Double> list){
		System.out.println("min: "+list.get(0));
		System.out.println("10th: "+percentile(list, .1));
		System.out.println("med: "+percentile(list, .5));
		System.out.println("90th: "+percentile(list, .9));
		System.out.println("max: "+list.get(list.size()-1));
	}
	
	public static double percentile(ArrayList<Double> list, double p){
		if(list.size()>0){
			Collections.sort(list);
			int percentile = (int) (list.size() * p);
			return list.get(percentile);
		} else {
			return -1;
		}
	}
	
	public static void powerAnalysis(String filename){
		ArrayList<Integer> pwr = new ArrayList<Integer>();
		ArrayList<Double> slg = new ArrayList<Double>();
		ArrayList<Double> hr_ab = new ArrayList<Double>();
		ArrayList<Double> tb_h = new ArrayList<Double>();
		int count = 0;
		for(int j=1; j<=255; j++){
			ArrayList<Player> players = Analyzer.readAll(filename);
			players = Analyzer.narrowByAttribute(players, "PWR", false, j, 2);
			double slgSum = 0;
			double hr_abSum = 0;
			double tb_hSum = 0;
			/*
			ArrayList<Double> slg = new ArrayList<Double>();
			ArrayList<Double> hr_ab = new ArrayList<Double>();
			ArrayList<Double> tb_h = new ArrayList<Double>();
			*/
			for(int i=0; i<players.size(); i++){
				String first = players.get(i).firstName;
				String last = players.get(i).lastName;
				long id = getPlayerID(first, last);
				if(id > 0){
					HittingStats stats = getPlayerStats(id, 2006);
					if(stats != null){
						if(stats.ab > 100){
							count++;
							System.out.println(count);
							Double hr_abVal = (double) stats.hr/stats.ab;
							Double tb_hVal = (double) stats.tb/stats.h;
							
							pwr.add(j);
							slg.add(stats.getSLG());
							hr_ab.add((double) stats.hr);
							tb_h.add(tb_hVal);
							
							slgSum += stats.getSLG();
							hr_abSum += hr_abVal;
							tb_hSum +=  tb_hVal;
						}
					}
				}
			}
			/*
			double slgAverage = slgSum/count;
			double hr_abAverage = hr_abSum/count;
			double tb_hAverage = tb_hSum/count;
			if(count > 0){
				System.out.println("POW: "+j);
				System.out.println("SLG");
				printStats(slg);
				System.out.println("avg: "+slgAverage+"\n");
				
				System.out.println("HR/AB");
				printStats(hr_ab);
				System.out.println("avg: "+slgAverage+"\n");
				
				System.out.println("TB/H");
				printStats(tb_h);
				System.out.println("avg: "+slgAverage+"\n");
			} else {
				System.out.println("\nPOW: "+j);
			}
			*/
		}
		System.out.println("\nPWR");
		for(int i=0; i<pwr.size(); i++){
			System.out.println(pwr.get(i));
		}
		System.out.println("\nSLG");
		for(int i=0; i<slg.size(); i++){
			System.out.println(slg.get(i));
		}
		System.out.println("\nHR/AB");
		for(int i=0; i<hr_ab.size(); i++){
			System.out.println(hr_ab.get(i));
		}
		System.out.println("\nTB/H");
		for(int i=0; i<tb_h.size(); i++){
			System.out.println(tb_h.get(i));
		}
	}
	
	public static void contactDiffTest(String filename){
		double sum = 0;
		int count = 0;
		for(int j=1; j<=15; j++){
			ArrayList<Player> players = Analyzer.readAll(filename);
			players = Analyzer.narrowByAttribute(players, "CON", false, j, 2);
			for(int i=0; i<players.size(); i++){
				Player player = players.get(i);
				String first = player.firstName;
				String last = player.lastName;
				long id = getPlayerID(first, last);
				if(id > 0){
					HittingStats stats = getPlayerStats(id, 2006);
					if(stats != null){
						if(stats.ab>100){
							count++;
							int diff = Math.abs(j-contactCalculator(stats.getAVG()));
							//if(diff>5)
								//diff = 5;
							sum += diff;
							System.out.println("con: "+j+", avg: "+stats.getAVG()+", diff: "+diff);
						}
					}
				}
			}
		}
		double average = sum/count;
		System.out.println("Avg Diff: "+average);
	}
	
	public static void contactAnalysis(String filename){
		for(int j=1; j<=15; j++){
			ArrayList<Player> players = Analyzer.readAll(filename);
			players = Analyzer.narrowByAttribute(players, "CON", false, j, 2);
			double min = 1;
			double max = 0;
			double sum = 0;
			int count = 0;
			ArrayList<Double> values = new ArrayList<Double>();
			for(int i=0; i<players.size(); i++){
				String first = players.get(i).firstName;
				String last = players.get(i).lastName;
				long id = getPlayerID(first, last);
				if(id > 0){
					HittingStats stats = getPlayerStats(id, 2006);
					if(stats != null){
						count++;
						System.out.print(count+",");
						values.add(stats.getAVG());
						sum += stats.getAVG();
						if(stats.getAVG()<min && stats.getAVG() > 0){
							min = stats.getAVG();
						}
						if(stats.getAVG()>max && stats.getAVG() < .5){
							max = stats.getAVG();
						}
					}
				}
			}
			double average = sum/count;
			Collections.sort(values);
			int ninetieth = (int) (count * 0.9);
			int tenth = (int) (count * 0.1);
			int median = (int) (count * 0.5);
			if(values.size() > 0)
				System.out.print("\nCON: "+j+"\nmin: "+min+"\n10th: "+values.get(tenth)+"\nmed: "+values.get(median)+"\n90th: "+values.get(ninetieth)+"\nmax: "+max+"\navg: "+average+"\n\n");
			else
				System.out.print("\nCON: "+j+"\nmin: "+min+"\n10th: 0\nmed: 0\n90th: 0\nmax: "+max+"\navg: "+average+"\n\n");
		}
	}
	
	public static boolean between(double num, double lower, double upper){
		return num>=lower && num<upper;
	}
	
	
	
	public static String createSearchRequest(String first, String last){
		return "http://lookup-service-prod.mlb.com/json/named.search_player_all.bam?sport_code='mlb'&name_part=%27"+first+"%20"+last+"%27";
	}
	
	public static String createStatsRequest(long playerID, int year){
		return "http://lookup-service-prod.mlb.com/json/named.sport_hitting_tm.bam?league_list_id='mlb'&game_type='R'&season='"+year+"'&player_id='"+playerID+"'";
	}
	
	public static String createFieldingStatsRequest(long playerID, int year){
		return "http://lookup-service-prod.mlb.com/json/named.sport_fielding_tm.bam?league_list_id='mlb'&game_type='R'&season='"+year+"'&player_id='"+playerID+"'";
	}
	
	public static FieldingStats getFieldingStats(long playerID, int year){
		try {
			URL url = new URL(createFieldingStatsRequest(playerID, year));
			//System.out.println(url.toString());
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer content = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				content.append(inputLine);
			}
			in.close();
			//System.out.println(content);
			Gson gson = new Gson();
			try{
				StatsResults sr = gson.fromJson(content.toString(), StatsResults.class);
				return sr.sport_fielding_tm.queryResults.row;
			}
			catch (JsonSyntaxException jsE){
				StatsResults_arr sr = gson.fromJson(content.toString(),StatsResults_arr.class);
				return sr.sport_fielding_tm.queryResults.getCompiled();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public static HittingStats getPlayerStats(long playerID, int year){
		try {
			URL url = new URL(createStatsRequest(playerID, year));
			System.out.println(url.toString());
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer content = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				content.append(inputLine);
			}
			in.close();
			//System.out.println(content);
			Gson gson = new Gson();
			try{
				StatsResults sr = gson.fromJson(content.toString(), StatsResults.class);
				return sr.sport_hitting_tm.queryResults.row;
			}
			catch (JsonSyntaxException jsE){
				jsE.printStackTrace();
				StatsResults_arr sr = gson.fromJson(content.toString(),StatsResults_arr.class);
				return sr.sport_hitting_tm.queryResults.getCompiled();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public class StatsResults{
		 HittingStatsQuery sport_hitting_tm;
		 FieldingStatsQuery sport_fielding_tm;
	}
	
	public class StatsResults_arr{
		 HittingStatsQuery_arr sport_hitting_tm;
		 FieldingStatsQuery_arr sport_fielding_tm;
	}
	
	public class HittingStatsQuery {
		String copyRight;
		HSQueryResults queryResults;
		
	}
	
	public class FieldingStatsQuery {
		String copyRight;
		FSQueryResults queryResults;
		
	}
	
	public class HittingStatsQuery_arr {
		String copyRight;
		HSQueryResults_arr queryResults;
		
	}
	
	public class FieldingStatsQuery_arr {
		String copyRight;
		FSQueryResults_arr queryResults;
	}
	
	public class HSQueryResults {
		HittingStats row;
	}
	
	public class FSQueryResults {
		FieldingStats row;
	}
	
	public class HSQueryResults_arr {
		HittingStats[] row;
		
		public HittingStats getCompiled(){
			int at_bats = 0;
			int hits = 0;
			int hr = 0;
			int sb = 0;
			int cs = 0;
			int rbi = 0;
			int r = 0;
			int totalBases = 0;
			int go = 0;
			int ao = 0;
			
			for(int i=0; i<row.length; i++){
				at_bats += row[i].ab;
				hits += row[i].h;
				hr += row[i].hr;
				sb += row[i].sb;
				cs += row[i].cs;
				r += row[i].r;
				totalBases += row[i].tb;
				if(row[i].go.length() > 0 && row[i].ao.length() > 0){
					go += Integer.parseInt(row[i].go);
					ao += Integer.parseInt(row[i].ao);
				}	
			}
			
			double avg = hits / (double) at_bats;
			double slg = totalBases / (double) at_bats;
			
			return new HittingStats(hr, sb, at_bats, hits, totalBases, cs, r, go, ao);
		}
	}
	
	public class FSQueryResults_arr {
		FieldingStats[] row;
		
		public FieldingStats getCompiled(){
			double inn = 0;
			int a = 0;
			int po = 0;
			int g = 0;
			int gs = 0;
			int tc = 0;
			int e = 0;
			
			for(int i=0; i<row.length; i++){
				row[i].setInnings();
				inn += row[i].innings;
				a += row[i].a;
				po += row[i].po;
				g += row[i].g;
				gs += row[i].gs;
				tc += row[i].tc;
				e += row[i].e;
			}
			return new FieldingStats(row[0].position, inn, a, po, g, gs, tc, e);
		}
	}
	
	
	
	public class FieldingStats {
		int position;
		String inn;
		double innings = 0;
		int a;
		int po;
		int g;
		int gs;
		int tc;
		int e;
		
		public FieldingStats(int position, double inn, int a, int po, int g, int gs, int tc, int e){
			this.position = position;
			this.innings = inn;
			this.a = a;
			this.po = po;
			this.g = g;
			this.gs = gs;
			this.tc = tc;
			this.e = e;
		}
		
		public void setInnings(){
			try{
				if(inn != null){
					innings = Double.parseDouble(inn); 
				}
			} catch (NullPointerException e){
				e.printStackTrace();
			} catch (NumberFormatException ne){
				ne.printStackTrace();
			}
		}
		
		public double getRF(){
			setInnings();
			return (po+a)/innings;
		}
		
		public double getFPCT(){
			return (tc-e) / (double) tc;
		}
	}
	
	public class HittingStats {
		int hr;
		int sb;
		int cs;
		int ab;
		int h;
		int tb;
		int r;
		String go;
		String ao;
		
		public HittingStats(int hr, int sb, int ab, int h, int tb, int cs, int r, int go, int ao){
			this.hr = hr;
			this.sb = sb;
			this.ab = ab;
			this.h = h;
			this.tb = tb;
			this.cs = cs;
			this.r = r;
			this.go = ""+go;
			this.ao = ""+ao;
		}
		
		public double getGoAo(){
			if(go.length() > 0 && ao.length() > 0){
				double dubGo = Double.parseDouble(go);
				double dubAo = Double.parseDouble(ao);
				if(dubGo > 0 && dubAo > 0){
					return dubGo / dubAo; 
				} else{
					return 1.2;
				}
			}else{
				return 1.2;
			}
		}
		
		public double getAVG(){
			return (double) h / ab;
		}
		
		public double getSLG(){
			return (double) tb / ab;
		}
	}
	
	public static long getPlayerID(String first, String last){
		try {
			URL url = new URL(createSearchRequest(first, last));
			//System.out.println(url.toString());
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer content = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				content.append(inputLine);
			}
			in.close();
			//System.out.println(content);
			Gson gson = new Gson();
			try{
				SearchResults_Arr sr = gson.fromJson(content.toString(), SearchResults_Arr.class);
				if(sr.getFirst() != null){
					return sr.getFirst().player_id;
				} else {
					return -1;
				}
			}
			catch (JsonSyntaxException jsE){
				SearchResults_Obj sr = gson.fromJson(content.toString(), SearchResults_Obj.class);
				return sr.getFirst().player_id;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}
	
	public class SearchResults_Arr {
		Search_player_all search_player_all;
		
		public void print(){
			search_player_all.print();
		}
		
		public SearchPlayer getFirst(){
			if(search_player_all.queryResults.row != null){
				return search_player_all.queryResults.row[0];
			} else {
				return null;
			}
		}
	}
	
	
	
	public class Search_player_all {
		String copyRight;
		QueryResults queryResults;
		
		public void print(){
			queryResults.print();
		}
	}
	
	public class QueryResults {
		int total_size;
		SearchPlayer[] row;
		
		public void print(){
			for(int i=0; i<row.length; i++){
				System.out.println(row[i].toString());
			}
		}
	}
	
	public class SearchResults_Obj {
		Search_player_obj search_player_all;
		
		public void print(){
			search_player_all.print();
		}
		
		public SearchPlayer getFirst(){
			if(search_player_all.queryResults.row != null){
				return search_player_all.queryResults.row;
			} else {
				return null;
			}
		}
	}
	
	public class Search_player_obj {
		String copyRight;
		QueryResults_obj queryResults;
		
		public void print(){
			queryResults.print();
		}
	}
	
	public class QueryResults_obj {
		int total_size;
		SearchPlayer row;
		
		public void print(){
			System.out.println(row.toString());		}
	}
	
	public class SearchPlayer {
		String name_display_first_last;
		long player_id;
		
		public String toString(){
			return name_display_first_last+" : "+player_id;
		}
		
	}
}
