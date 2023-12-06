import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * see: https://adventofcode.com/2023/day/04
 */
public class Y23Day06 {
 
	/*
	 * 
	 * Time:      7  15   30
	 * Distance:  9  40  200
	 * 
	 */

	private static final String INPUT_RX_TIME     = "^Time: ([ 0-9]+)$";
	private static final String INPUT_RX_DISTANCE = "^Distance: ([ 0-9]+)$";
	
	public static record InputData(
			List<Integer> times, 
			List<Integer> distances,
			long bigTime, 
			long bigDistance 
	) {}
	
	public static class InputProcessor implements Iterable<InputData>, Iterator<InputData> {
		private Scanner scanner;
		public InputProcessor(String inputFile) {
			try {
				scanner = new Scanner(new File(inputFile));
			} catch (FileNotFoundException e) {
				throw new RuntimeException(e);
			}
		}
		@Override public Iterator<InputData> iterator() { return this; }
		@Override public boolean hasNext() { return scanner.hasNext(); }
		@Override public InputData next() {
			String line = scanner.nextLine().trim();
			while (line.length() == 0) {
				line = scanner.nextLine();
			}
			if (!line.matches(INPUT_RX_TIME)) {
				throw new RuntimeException("invalid line '"+line+"'");
			}
			long bigTime = Long.parseLong(line.replaceAll("[^0-9]+", ""));
			String[] timesArray = line.replaceFirst(INPUT_RX_TIME, "$1").trim().split(" +");
			List<Integer> times = new ArrayList<>();
			for (String time:timesArray) {
				times.add(Integer.parseInt(time));
			}
			line = scanner.nextLine().trim();
			if (!line.matches(INPUT_RX_DISTANCE)) {
				throw new RuntimeException("invalid line '"+line+"'");
			}
			long bigDistance = Long.parseLong(line.replaceAll("[^0-9]+", ""));
			String[] distancesArray = line.replaceFirst(INPUT_RX_DISTANCE, "$1").trim().split(" +");
			List<Integer> distances = new ArrayList<>();
			for (String distance:distancesArray) {
				distances.add(Integer.parseInt(distance));
			}
			return new InputData(times, distances, bigTime, bigDistance);
		}
	}
	
	public static void mainPart1(String inputFile) {
		for (InputData data:new InputProcessor(inputFile)) {
			int prodSol = 1;
			System.out.println(data);
			for (int i=0; i<data.times.size(); i++) {
				double time = data.times.get(i);
				double distance = data.distances.get(i);
				// time*n - n*n > distance
				// n*n - time*n + distance > 0
				// x*x + p*x + q = 0
				// -p/2 +/- SQRT((p/2)*(p/2)-q)
				// p = -time
				// q = distance
				// dk = -time/2
				// nMIN > -dk - SQRT(dk*dk-distance)  
				// nMAX > -dk + SQRT(dk*dk-distance)
				double dk = -time/2;
				double sq = Math.sqrt(dk*dk-distance);
				double nMin = -dk - sq;  
				double nMax = -dk + sq;
				nMin = nMin + 0.0000001;    // avoid rounding issues
				nMax = nMax - 0.0000001;    // avoid rounding issues
				int iMin = (int)Math.floor(nMin)+1;
				int iMax = (int)Math.ceil(nMax)-1;
				int sol = iMax-iMin+1;
				System.out.println("time: "+time+" distance: "+distance+" nMin: "+nMin+" nMax: "+nMax+" iMin: "+iMin+" iMax: "+iMax+"  solutions: "+sol);
				prodSol = prodSol * sol;
			}
			System.out.println("prodSol: "+prodSol);
		}
	}
	
	
	public static void mainPart2(String inputFile) {
		for (InputData data:new InputProcessor(inputFile)) {
			int prodSol = 1;
			System.out.println(data);
			double time = data.bigTime;
			double distance = data.bigDistance;
			// time*n - n*n > distance
			// n*n - time*n + distance > 0
			// x*x + p*x + q = 0
			// -p/2 +/- SQRT((p/2)*(p/2)-q)
			// p = -time
			// q = distance
			// dk = -time/2
			// nMIN > -dk - SQRT(dk*dk-distance)  
			// nMAX > -dk + SQRT(dk*dk-distance)
			double dk = -time/2;
			double sq = Math.sqrt(dk*dk-distance);
			double nMin = -dk - sq;  
			double nMax = -dk + sq;
			nMin = nMin + 0.0000001;    // avoid rounding issues
			nMax = nMax - 0.0000001;    // avoid rounding issues
			int iMin = (int)Math.floor(nMin)+1;
			int iMax = (int)Math.ceil(nMax)-1;
			int sol = iMax-iMin+1;
			System.out.println("time: "+time+" distance: "+distance+" nMin: "+nMin+" nMax: "+nMax+" iMin: "+iMin+" iMax: "+iMax+"  solutions: "+sol);
			prodSol = prodSol * sol;
			System.out.println("prodSol: "+prodSol);
		}
	}

	

	public static void main(String[] args) throws FileNotFoundException {
		System.out.println("--- PART I ---");
//		mainPart1("exercises/day06/Feri/input-example.txt");
		mainPart1("exercises/day06/Feri/input.txt");             
		System.out.println("---------------");
		System.out.println("--- PART II ---");
//		mainPart2("exercises/day06/Feri/input-example.txt");
		mainPart2("exercises/day06/Feri/input.txt");     
		System.out.println("---------------");    // 
	}
	
}
