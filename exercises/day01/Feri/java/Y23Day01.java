import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Scanner;

/**
 * see: https://adventofcode.com/2023/day/01
 */
public class Y23Day01 {
 
	/*
	 * example input: 
	 *
	 * 
	 */

	private static final String INPUT_RX   = "^([a-z0-9]+)$";
	
	
	public static class InputData {
		String line;
		@Override
		public String toString() {
			return line;
		}
		
		
	}
	
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
			InputData data = new InputData();
			if (line.matches(INPUT_RX)) {
				data.line = line.replaceFirst(INPUT_RX, "$1");
			}
			else {
				throw new RuntimeException("invalid line '"+line+"'");
			}
			return data;
		}
	}

	
	public static void mainPart1(String inputFile) {
		int sumCalibration = 0;
		for (InputData data:new InputProcessor(inputFile)) {
			System.out.println(data.line);
			String nums = data.line.replaceAll("[a-z]", "");
			String firstlastnum = ""+nums.charAt(0) + nums.charAt(nums.length()-1);
			System.out.println(firstlastnum);
			sumCalibration = sumCalibration + Integer.parseInt(firstlastnum);
		}
		System.out.println("SUM CALIBRATION: "+sumCalibration);
	}

	
	private static String replaceNumberWords(String line) {
		return line.replace("one", "one1one").replace("two", "two2two").replace("three", "three3three")
		.replace("four", "four4four").replace("five", "five5five").replace("six", "six6six")
		.replace("seven", "seven7seven").replace("eight", "eight8eight").replace("nine", "nine9nine");
	}

	public static void mainPart2(String inputFile) {
		int sumCalibration = 0;
		for (InputData data:new InputProcessor(inputFile)) {
			System.out.println(data.line);
			String wordsReplacedLine = replaceNumberWords(data.line);
			System.out.println(wordsReplacedLine);
			String nums = wordsReplacedLine.replaceAll("[a-z]", "");
			String firstlastnum = ""+nums.charAt(0) + nums.charAt(nums.length()-1);
			System.out.println(firstlastnum);
			sumCalibration = sumCalibration + Integer.parseInt(firstlastnum);
		}
		System.out.println("SUM CALIBRATION: "+sumCalibration);
	}

	

	public static void main(String[] args) throws FileNotFoundException {
		System.out.println("--- PART I ---");
//		mainPart1("exercises/day01/Feri/input-example.txt");
		mainPart1("exercises/day01/Feri/input.txt");
		System.out.println("---------------");
		System.out.println("--- PART II ---");
//		mainPart2("exercises/day01/Feri/input-example-2.txt");
		mainPart2("exercises/day01/Feri/input.txt");     
		System.out.println("---------------");    // 
	}
	
}
