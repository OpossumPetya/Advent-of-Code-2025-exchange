import java.util.ArrayList;
import java.util.List;

public class MySolution extends MySolutionBase {


	private String[] lines;
	private int map_size_x;
	private int map_size_y;
	private char[][] map;
	private List<MyNumber> numbers = new ArrayList<>();
	private List<Star> stars = new ArrayList<>();

	public MySolution(String inputFilename) {
        super(inputFilename);
		this.lines = getInputLinesAsList().toArray(new String[0]);
		this.map_size_x = this.lines[0].length();
		this.map_size_y = this.lines.length;
		this.map = new char[this.map_size_x][this.map_size_y];
		
		for (int y=0;y<this.lines.length;y++) {
			MyNumber myNumber = null;
			for (int x=0;x<this.lines[0].length();x++) {
				var c = this.lines[y].charAt(x);
				if (Character.isDigit(c)){
					if (myNumber == null){
						myNumber = new MyNumber(x,y);
						this.numbers.add(myNumber);
					}
					myNumber.s += c;
				} else {
					myNumber = null;
				}
				if (c=='*') {
					this.stars.add(new Star(x,y));
				}
				this.map[x][y] = c;
			}
		}
    }

    private MySolution play1() {
		int result = this.numbers.stream().filter(n->this.isAdjacentToSmybol(n)).mapToInt(n->Integer.parseInt(n.s)).sum();
		System.out.println(result);
        return this;
	}

	private boolean isAdjacentToSmybol(MyNumber n) {
		for (int x = n.x - 1;x<n.x+n.s.length()+1;x++)
			for (int y = n.y - 1;y<n.y+2;y++)
				if (x>=0 && x<this.map_size_x && y>=0 && y<this.map_size_y)
					if (!Character.isDigit(this.map[x][y]) && (this.map[x][y]!='.'))
						return true;

		return false;
	}

	private MySolution play2() {
		this.numbers.stream().forEach(n->{
			this.stars.forEach(s->{
				if (s.x>=n.x-1 && s.x<=n.x+n.s.length() && s.y>=n.y-1 && s.y<=n.y+1)
					s.addAdjacentNumber(n);
			});
		});
		long result = this.stars.stream().mapToLong(s->s.calcGearRatio()).sum();
		System.out.println(result);
        return this;
	}

	public static void main(String args[]) {
		try {
            new MySolution("sample.txt").play1().play2();
            new MySolution("input.txt").play1().play2();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
