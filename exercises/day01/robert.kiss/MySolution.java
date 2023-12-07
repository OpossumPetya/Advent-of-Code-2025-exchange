import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class MySolution extends MySolutionBase {


	public MySolution(String inputFilename) {
        super(inputFilename);
    }

    private MySolution play1() {
		List<Integer> values = new ArrayList<Integer>();
		getInputLinesAsList().forEach(line -> {
			Integer cc[]={null,null};
			line.chars().boxed().forEach(c->{
				if (Character.isDigit(c)) {
					if (cc[0]==null) {
						cc[0]= c;
					}
					cc[1]= c;
				}
			});
			values.add(10*(cc[0]-48)+cc[1]-48);
		});
		System.out.println("play1: " + values.stream().mapToInt(Integer::intValue).sum());
        return this;
	}

	private String replacewords(String line) {
		//System.out.println(line);
		line = line.replaceAll("one", "one1one");
		line = line.replaceAll("two", "two2two");
		line = line.replaceAll("three", "three3three");
		line = line.replaceAll("four", "four4four");
		line = line.replaceAll("five", "five5five");
		line = line.replaceAll("six", "six6six");
		line = line.replaceAll("seven", "seven7seven");
		line = line.replaceAll("eight", "eight8eight");
		line = line.replaceAll("nine", "nine9nine");		
		//System.out.println(line);
		return line;
	}

	private MySolution play2() {
		List<Integer> values = new ArrayList<Integer>();
		getInputLinesAsList().forEach(line -> {
			line = this.replacewords(line);
			Integer cc[]={null,null};
			line.chars().boxed().forEach(c->{
				if (Character.isDigit(c)) {
					if (cc[0]==null) {
						cc[0]= c;
					}
					cc[1]= c;
				}
			});
			values.add(10*(cc[0]-48)+cc[1]-48);
		});
		//System.out.println("play2a: " + values);
		System.out.println("play2a: " + values.stream().mapToInt(Integer::intValue).sum());
        return this;
	}

	Map<String,Integer> myStrToIntMap = Map.of(
			"one", 1,
			"two", 2,
			"three", 3,
			"four", 4,
			"five", 5,
			"six", 6,
			"seven", 7,
			"eight", 8,
			"nine", 9
		);
	private int myStrToInt(String str) {
		return this.myStrToIntMap.containsKey(str) ? this.myStrToIntMap.get(str) : Integer.parseInt(str);

	}
	private Pattern myPattern1 = Pattern.compile("([0-9]|one|two|three|four|five|six|seven|eight|nine).*");
	private Pattern myPattern2 = Pattern.compile(".*([0-9]|one|two|three|four|five|six|seven|eight|nine)");

	private MySolution play2b() {
		List<Integer> values = new ArrayList<Integer>();
		getInputLinesAsList().forEach(line -> {
			var mymatcher1 = myPattern1.matcher(line);
			var mymatcher2 = myPattern2.matcher(line);
			if (mymatcher1.find() && mymatcher2.find()) {
				//System.out.println(mymatcher.group(0) + " -> " + mymatcher.group(1) + "  " + mymatcher.group(2));
				values.add( 10*myStrToInt(mymatcher1.group(1)) + myStrToInt(mymatcher2.group(1) ));
			}
		});
		//System.out.println("play2b: " + values);
		System.out.println("play2b: " + values.stream().mapToInt(Integer::intValue).sum());
        return this;
	}

	public static void main(String args[]) {
		try {
            new MySolution("sample1.txt").play1();
            new MySolution("sample2.txt").play2().play2b();
            new MySolution("input.txt").play1().play2().play2b();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
