import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * see: https://adventofcode.com/2023/day/01
 */
public class Y23Day01Visualization {
 
	
	private static Y23GUIOutput01 output;
	
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


	static final String[] NUMBER_STRINGS = {"zero","one","two","three","four","five","six","seven","eight","nine"};  
	static Map<String, Integer> NUMBER_WORD_MAPPING;
	
	public static void initStringNumberWords() {
		NUMBER_WORD_MAPPING = new HashMap<>();
		for (int i=0; i<NUMBER_STRINGS.length; i++) {
			NUMBER_WORD_MAPPING.put(NUMBER_STRINGS[i], i);
			NUMBER_WORD_MAPPING.put(Integer.toString(i), i);
		}
		
	}
	public static void initNumericNumberWords() {
		NUMBER_WORD_MAPPING = new HashMap<>();
		for (int i=0; i<10; i++) {
			NUMBER_WORD_MAPPING.put(NUMBER_STRINGS[i], i);
			NUMBER_WORD_MAPPING.put(Integer.toString(i), i);
		}
	}
	
	
	
	static record NumberWordInfo(String word, int value, int index) {};
			
	public static NumberWordInfo checkNumberWord(String line, int index) {
		for (String numberWord:NUMBER_WORD_MAPPING.keySet()) {
			if (line.substring(index).startsWith(numberWord)) {
				return new NumberWordInfo(numberWord, NUMBER_WORD_MAPPING.get(numberWord), index);
			}
		}
		return null;
	}
	
	public static NumberWordInfo findFirstNumberWord(String line) {
		for (int i=0; i<line.length(); i++) {
			NumberWordInfo numberWordInfo = checkNumberWord(line, i);
			if (numberWordInfo != null) {
				return numberWordInfo;
			}
		}
		return null;
	}
	
	public static NumberWordInfo findLastNumberWord(String line) {
		for (int i=line.length()-1; i>=0; i--) {
			NumberWordInfo numberWordInfo = checkNumberWord(line, i);
			if (numberWordInfo != null) {
				return numberWordInfo;
			}
		}
		return null;
	}

	private static String show(List<String> lines) {
		StringBuilder result = new StringBuilder();
		for (String line:lines) {
			result.append(line).append('\n');
		}
		return result.toString();
	}
	
	private static String replicate(String text, int repetitions) {
		StringBuilder result = new StringBuilder();
		for (int i=0; i<repetitions; i++) {
			result.append(text);
		}
		return result.toString();
	}
	

	public static void mainPartB(String inputFile) {
		List<String> lines = new ArrayList<>();
		int maxLineLength = 0;
		for (InputData data:new InputProcessor(inputFile)) {
			lines.add(data.line);
			maxLineLength = Math.max(maxLineLength, data.line.length());
		}
		output.addStep(show(lines));
		int sumCalibration = 0;
		for (int i=0; i<lines.size(); i++) {
			String line = lines.get(i);
			System.out.println(line);
			NumberWordInfo firstNumber = findFirstNumberWord(line);
			String result = replicate(".", firstNumber.index) + firstNumber.word; 
			NumberWordInfo lastNumber = findLastNumberWord(line);
			if (firstNumber.index != lastNumber.index) {
				result = result + replicate(".", lastNumber.index-result.length()) + lastNumber.word;
			}
			result = result + replicate(".", line.length()-result.length());
			sumCalibration = sumCalibration + 10*firstNumber.value+lastNumber.value;
			result = result + replicate(" ", maxLineLength+3-result.length());
			result = result + firstNumber.value+lastNumber.value+"  "+sumCalibration;
			lines.set(i, result);
			System.out.println(result);
			output.addStep(show(lines));
		}
		System.out.println("SUM CALIBRATION: "+sumCalibration);
	}

	public static void mainPart1b(String inputFile) {
		initNumericNumberWords();
		output = new Y23GUIOutput01("Y23 Day 01 Part I", true);
		mainPartB(inputFile);
	}

	public static void mainPart2b(String inputFile) {
		initStringNumberWords();
		output = new Y23GUIOutput01("Y23 Day 01 Part II", true);
		mainPartB(inputFile);
	}


	public static void main(String[] args) throws FileNotFoundException {
		System.out.println("--- PART Ib ---");
		mainPart1b("exercises/day01/Feri/input-example.txt");
//		mainPart1b("exercises/day01/Feri/input.txt");
		System.out.println("---------------");
		System.out.println("--- PART IIb ---");
		mainPart2b("exercises/day01/Feri/input-example-2.txt");     
//		mainPart2b("exercises/day01/Feri/input.txt");     
		System.out.println("---------------");    // 
	}
	
}
