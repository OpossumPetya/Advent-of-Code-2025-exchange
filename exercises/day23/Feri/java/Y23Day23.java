import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

/**
 * see: https://adventofcode.com/2023/day/23
 */
public class Y23Day23 {

	static Y23GUIOutput23 output;

	/*
	 * Example:
	 * 
	 * #.#####################
	 * #.......#########...###
	 * #######.#########.#.###
	 * ###.....#.>.>.###.#.###
	 * ###v#####.#v#.###.#.###
	 * ###.>...#.#.#.....#...#
	 * ###v###.#.#.#########.#
	 * ###...#.#.#.......#...#
	 * #####.#.#.#######.#.###
	 * #.....#.#.#.......#...#
	 * #.#####.#.#.#########v#
	 * #.#...#...#...###...>.#
	 * #.#.#v#######v###.###v#
	 * #...#.>.#...>.>.#.###.#
	 * #####v#.#.###v#.#.###.#
	 * #.....#...#...#.#.#...#
	 * #.#########.###.#.#.###
	 * #...###...#...#...#.###
	 * ###.###.#.###v#####v###
	 * #...#...#.#.>.>.#.>.###
	 * #.###.###.#.###.#.#v###
	 * #.....###...###...#...#
	 * #####################.#
	 * 
	 */

	private static final String INPUT_RX = "^([.#<>^v]+)$";
	
	public static record InputData(String row) {
		@Override public String toString() { return row; }
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
				String row = line.replaceFirst(INPUT_RX, "$1");
				return new InputData(row);
			}
			else {
				throw new RuntimeException("invalid line '"+line+"'");
			}
		}
	}

	static String DIRS            = ">v<^";
	static int[]  DIR_ADD_X 	  = { 1,   0,  -1,   0};
	static int[]  DIR_ADD_Y 	  = { 0,   1,   0,  -1};
	
	static int DIR_EAST = 0;
	static int DIR_SOUTH = 1;
	static int DIR_WEST = 2;
	static int DIR_NORTH = 3;
	
	static int DIR_ROT_RIGHT = 1;
	static int DIR_ROT_180   = 2;
	static int DIR_ROT_LEFT  = 3;

	static int rotRight(int dir) { return rot(dir, DIR_ROT_RIGHT); }
	static int rot180(int dir) { return rot(dir, DIR_ROT_180); }
	static int rotLeft(int dir) { return rot(dir, DIR_ROT_LEFT); }

	static int rot(int dir, int rot) { return (dir+rot+4)%4; }

	static record Pos(int x, int y) {
		Pos move(int dir) {
			return new Pos(x+DIR_ADD_X[dir], y+DIR_ADD_Y[dir]);
		}		
		Pos move(int dir, int steps) {
			return new Pos(x+steps*DIR_ADD_X[dir], y+steps*DIR_ADD_Y[dir]);
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

	
	static record Walker(Pos pos, int dir, int pathLength) {}

	static Map<Pos, String> nodeNames = new HashMap<>();

	static record Path(Pos pos, int pathLength) {
		@Override public String toString() {
			return (nodeNames.get(pos)==null?"":nodeNames.get(pos)+" ")+pos+":"+pathLength;
		}
	}
	
	public static class World {
		List<String> rows;
		char[][] field;
		int maxX;
		int maxY;
		int ticks;
		Pos startPos;
		Pos targetPos;
		Map<Pos, Set<Path>> topology;
		public World() {
			this.rows = new ArrayList<>();
		}
		public void addRow(String row) {
			rows.add(row);
		}
		public void init() {
			ticks = 0;
			maxY = rows.size();
			maxX = rows.get(0).length();
			field = new char[maxY][maxX];
			for (int y=0; y<maxY; y++) {
				for (int x=0; x<maxX; x++) {
					char c = rows.get(y).charAt(x);
					field[y][x] = c;
				}
			}
			startPos = new Pos(1,0);
			targetPos = new Pos(maxX-2, maxY-1);
		}
		private char get(Pos pos) {
			return get(pos.x, pos.y);
		}		
		private char get(int x, int y) {
			if ((x<0)||(y<0)||(x>=maxX)||(y>=maxY)) {
				return '#';
			}
			return field[y][x];
		}		
		public void tick() {
			ticks++;
		}
		@Override public String toString() {
			StringBuilder result = new StringBuilder();
			for (int y=0; y<maxY; y++) {
				for (int x=0; x<maxX; x++) {
					char c = get(x,y);
					result.append(c);
				}
				result.append("\n");
			}
			return result.toString();
		}
		public void show() {
			output.addStep("TICKS: "+ticks+"\n"+toString());
		}
		public void createTopology() {
			topology = new LinkedHashMap<>();
			Set<Pos> visitedPositions = new HashSet<>();
			Queue<Pos> currentPositions = new ArrayDeque<>();
			currentPositions.add(startPos);
			visitedPositions.add(startPos);
			while (!currentPositions.isEmpty()) {
				Pos currentPos = currentPositions.poll();
				Set<Path> nextPaths = follow(currentPos);
				System.out.println("WALKED FROM "+currentPos+" to "+nextPaths);
				if (nextPaths.isEmpty()) {
					continue;
				}
				for (Path nextPath:nextPaths) {
					topology.computeIfAbsent(currentPos, k->new LinkedHashSet<Path>()).add(nextPath);
					if (!visitedPositions.contains(nextPath.pos)) {
						visitedPositions.add(nextPath.pos);
						currentPositions.add(nextPath.pos);
					}
				}
			}
			int nextNodeIndex = 1;
			nodeNames = new HashMap<>();
			for (Pos pos:topology.keySet()) {
				String nodeName = "N"+nextNodeIndex++;
				nodeNames.put(pos, nodeName);
				System.out.println(nodeName+" : "+pos);
			}
			nodeNames.put(targetPos, "t");
			
		}
		private Set<Path> follow(Pos pos) {
			Set<Path> result = new LinkedHashSet<Path>();
			List<Integer> dirs = validNextDirs(pos);
			for (int dir:dirs) {
				Path path = follow(new Walker(pos, dir, 0));
				if (path != null) {
					result.add(path);
				}
			}
			return result;
		}
		private Path follow(Walker walker) {
			int pathLength = 0;
			Pos currentPos = walker.pos;
			int currentDir = walker.dir;
			while (true) {
				Pos nextPos = currentPos.move(currentDir);
				pathLength++;
				if (get(nextPos) != '.') {
					if (get(nextPos) != DIRS.charAt(currentDir)) {
						return null;   // against one way direction 
					}
				}
				if (nextPos.equals(targetPos)) {
					return new Path(nextPos, pathLength);
				}
				List<Integer> nextDirs = validNextDirs(nextPos);
				nextDirs.remove((Object)rot180(currentDir));
				if (nextDirs.size() == 0) {
					return null;   // dead end
				}
				if (nextDirs.size() > 1) {
					return new Path(nextPos, pathLength);
				}
				currentPos = nextPos;
				currentDir = nextDirs.get(0);
			}			
		}
		private List<Integer> validNextDirs(Pos pos) {
			List<Integer> result = new ArrayList<>();
			for (int dir=DIR_EAST; dir<=DIR_NORTH; dir++) {
				if (get(pos.move(dir)) != '#') {
					result.add(dir);
				}
			}
			return result;
		}
		int longestPath = 0;
		
		public int calcLongestPath() {
			return recursiveCalcLongestPath(startPos, new LinkedHashSet<>(), 0, "");
		}
		private int recursiveCalcLongestPath(Pos pos, LinkedHashSet<Pos> visitedPos, int len, String debug) {
			if (pos.equals(targetPos)) {
				if (len > longestPath) {
					System.out.println("new longest path "+len+ " "+ debug);
					longestPath = len;
				}
				return 0; 
			}
			int result = Integer.MIN_VALUE;
			Set<Path> paths = topology.get(pos);
			if (paths != null) {
				for (Path path:paths) {
					if (!visitedPos.contains(path.pos)) {
						String deb = debug+nodeNames.get(path.pos);
						visitedPos.add(pos);
						int targetPathLen = path.pathLength+recursiveCalcLongestPath(path.pos, visitedPos, len+path.pathLength, deb);
						visitedPos.remove(pos);
						result = Math.max(result, targetPathLen);
					}
				}
			}
			return result;
		}
	}

	public static void mainPart1(String inputFile) {
//		output = new Y23GUIOutput23("2023 day 23 Part I", true);
		World world = new World();
		for (InputData data:new InputProcessor(inputFile)) {
			world.addRow(data.row);
		}
		world.init();
		world.createTopology();
		System.out.println(world.topology);
		System.out.println("#"+world.topology.size());
		int longestPath = world.calcLongestPath();
		System.out.println("LONGEST PATH: "+longestPath);
	}

	public static void mainPart2(String inputFile) {
//		output = new Y23GUIOutput23("2023 day 23 Part I", true);
		World world = new World();
		for (InputData data:new InputProcessor(inputFile)) {
			world.addRow(data.row.replaceAll("[<>^v]", "."));
		}
		world.init();
		world.createTopology();
		System.out.println(world.topology);
		System.out.println("#"+world.topology.size());
		int longestPath = world.calcLongestPath();
		System.out.println("LONGEST PATH: "+longestPath);
	}


	public static void main(String[] args) throws FileNotFoundException {
		System.out.println("--- PART I ---");
//		mainPart1("exercises/day23/Feri/input-example.txt");
		mainPart1("exercises/day23/Feri/input.txt");               
		System.out.println("---------------");                           
		System.out.println("--- PART II ---");
//		mainPart2("exercises/day23/Feri/input-example.txt");
		mainPart2("exercises/day23/Feri/input.txt");
	}
	
}
