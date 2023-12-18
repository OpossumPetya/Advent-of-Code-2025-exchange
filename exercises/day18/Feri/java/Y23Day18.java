import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * see: https://adventofcode.com/2023/day/18
 */
public class Y23Day18 {

	static Y23GUIOutput18 output;

	/*
	 * Example:
	 * 
	 * R 6 (#70c710)
	 * D 5 (#0dc571)
	 * L 2 (#5713f0)
	 * D 2 (#d2c081)
	 * R 2 (#59c680)
	 * D 2 (#411b91)
	 * L 5 (#8ceee2)
	 * U 2 (#caa173)
	 * L 1 (#1b58a2)
	 * U 2 (#caa171)
	 * R 2 (#7807d2)
	 * U 3 (#a77fa3)
	 * L 2 (#015232)
	 * U 2 (#7a21e3)
	 * 
	 */

	private static final String INPUT_RX = "^([RDLU]) ([0-9]+) [(]#([0-9a-f]{6})[)]$";
	
	public static record InputData(char dir, int steps, int color) {
		@Override public String toString() { return dir+" "+steps+"(#"+Integer.toHexString(color)+")"; }
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
			if (line.matches(INPUT_RX)) {
				char dir = line.replaceFirst(INPUT_RX, "$1").charAt(0);
				int steps = Integer.parseInt(line.replaceFirst(INPUT_RX, "$2"));
				int color = Integer.parseInt(line.replaceFirst(INPUT_RX, "$3"), 16);
				return new InputData(dir, steps, color);
			}
			else {
				throw new RuntimeException("invalid line '"+line+"'");
			}
		}
	}

	static String DIRS            = ">v<^";
	static String DIR_LETTERS     = "RDLU";
	static int[]  DIR_ADD_X 	  = { 1,   0,  -1,   0};
	static int[]  DIR_ADD_Y 	  = { 0,   1,   0,  -1};
	
	static int DIR_EAST = 0;
	static int DIR_SOUTH = 1;
	static int DIR_WEST = 2;
	static int DIR_NORTH = 3;
	
	static int DIR_ROT_LEFT = 3;
	static int DIR_ROT_RIGHT = 1;
	
	static int rot(int dir, int rot) { return (dir+rot+4)%4; }

	
	static record Pos(int x, int y) {
		Pos move(int dir) {
			return new Pos(x+DIR_ADD_X[dir], y+DIR_ADD_Y[dir]);
		}		
		public Pos min(Pos other) {
			if ((x<=other.x) && (y<=other.y)) {
				return this;
			}
			if ((other.x<=x) && (other.y<=y)) {
				return other;
			}
			return new Pos(Math.min(x,  other.x), Math.min(y,  other.y));
		}
		public Pos max(Pos other) {
			if ((x>=other.x) && (y>=other.y)) {
				return this;
			}
			if ((other.x>=x) && (other.y>=y)) {
				return other;
			}
			return new Pos(Math.max(x,  other.x), Math.max(y,  other.y));
		}
		@Override public String toString() { return "("+x+","+y+")"; }
		public List<Pos> getNeighbours() {
			List<Pos> result = new ArrayList<>();
			result.add(move(DIR_EAST));
			result.add(move(DIR_SOUTH));
			result.add(move(DIR_WEST));
			result.add(move(DIR_NORTH));
			return result;
		}
	}
	
	static record Beam(Pos pos, int dir) {
		public Beam move() {
			return new Beam(pos.move(dir), dir);
		}
		@Override public String toString() { return "B["+pos+"|"+DIRS.charAt(dir)+"]"; }
	}
	
	public static class World {
		Map<Pos, Integer> field;
		Set<Pos> outside;
		int ticks;
		Pos startPos;
		Pos currentPos;
		Pos maxPos;
		Pos minPos;
		public World() {
			this.field = new LinkedHashMap<>();
			this.outside = new LinkedHashSet<>();
			this.ticks = 0;
			this.startPos = new Pos(0,0);
			this.currentPos = startPos;
			this.minPos = currentPos;
			this.maxPos = currentPos;
			this.currentPos = new Pos(0,0);
		}
		public void move(InputData move) {
			int dir = DIR_LETTERS.indexOf(move.dir);
			for (int i=0; i<move.steps; i++) {
				currentPos = currentPos.move(dir);
				field.put(currentPos, move.color);
			}
			minPos = minPos.min(currentPos);
			maxPos = maxPos.max(currentPos);
		}
		@Override public String toString() {
			StringBuilder result = new StringBuilder();
			for (int y=minPos.y; y<=maxPos.y; y++) {
				for (int x=minPos.x; x<=maxPos.x; x++) {
					Integer col = get(x, y);
					char c = (col == null) ? '.' : '#';
					result.append(c);
				}
				result.append("\n");
			}
			return result.toString();
		}
		public String showBorder() {
			StringBuilder result = new StringBuilder();
			for (int y=minPos.y-2; y<=maxPos.y+2; y++) {
				for (int x=minPos.x-2; x<=maxPos.x+2; x++) {
					result.append(getChar(x, y, 1));
				}
				result.append("\n");
			}
			return result.toString();
		}
		public void fillOutside() {
			outside.clear();
			Set<Pos> currentPositions = new LinkedHashSet<>();
			currentPositions.add(new Pos(minPos.x-1, minPos.y-1));
			while (!currentPositions.isEmpty()) {
				Pos pos = currentPositions.iterator().next();
				currentPositions.remove(pos);
				if (getChar(pos.x,  pos.y,  1) != '.') {
					continue;
				}
				if (outside.contains(pos)) {
					continue;
				}
				outside.add(pos);
				currentPositions.addAll(pos.getNeighbours());
			}
		}
		private Integer get(int x, int y) {
			return field.get(new Pos(x,y));
		}
		private char getChar(int x, int y) {
			return getChar(x,y,0);
		}
		private char getChar(int x, int y, int border) {
			if (outside.contains(new Pos(x,y))) {
				return 'o';
			}
			if ((x<minPos.x-border) || (x>maxPos.x+border) || (y<minPos.y-border) || (y>maxPos.y+border)) {
				return '~';
			}
			if ((x<minPos.x) || (x>maxPos.x) || (y<minPos.y) || (y>maxPos.y)) {
				return '.';
			}
			Integer col = field.get(new Pos(x,y));
			return (col == null) ? '.' : '#';
		}
		public int countCubicmeters() {
			int result = 0;
			for (int y=minPos.y; y<=maxPos.y; y++) {
				for (int x=minPos.x; x<=maxPos.x; x++) {
					char c = getChar(x, y);
					if ((c == '.') || (c == '#')) {
						result++;
					}
				}
			}
			return result;
		}
	}

	public static void mainPart1(String inputFile) {
		World world = new World();
		for (InputData data:new InputProcessor(inputFile)) {
			System.out.println(data);
			world.move(data);
		}
		System.out.println(world);
		System.out.println(world.showBorder());
		world.fillOutside();
		System.out.println(world.showBorder());
		System.out.println(world.countCubicmeters());
	}

	


	public static void mainPart2(String inputFile) {
	}


	public static void main(String[] args) throws FileNotFoundException {
		System.out.println("--- PART I ---");
//		mainPart1("exercises/day18/Feri/input-example.txt");
		mainPart1("exercises/day18/Feri/input.txt");               
		System.out.println("---------------");                           
		System.out.println("--- PART II ---");
		mainPart2("exercises/day18/Feri/input-example.txt");
//		mainPart2("exercises/day18/Feri/input.txt");
		System.out.println("---------------");    
	}
	
}
