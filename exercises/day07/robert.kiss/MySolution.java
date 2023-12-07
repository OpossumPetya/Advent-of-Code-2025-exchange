import java.util.ArrayList;
import java.util.List;


public class MySolution extends MySolutionBase {

	private List<Hand> hands = new ArrayList<>();

	public MySolution(String inputFilename) {
        super(inputFilename);
		for (String line:getInputLinesAsList()) {
			this.hands.add(new Hand(line));
		}

    }

    private MySolution play1() {	
		this.hands.forEach(h->h.prepareForPart1());
		//this.hands.forEach(h->pln("%s -> %s",h.cards,h.compareString));
		this.hands.sort(null);
		long totalWinning = 0;
		for (int rank=1;rank<=this.hands.size();rank++) {
			totalWinning += rank * this.hands.get(rank-1).bid;
		}
		System.out.println(totalWinning);
        return this;
	}

	private MySolution play2() {
		this.hands.forEach(h->h.prepareForPart2());
		//this.hands.forEach(h->pln("%s -> %s",h.cards,h.compareString));
		this.hands.sort(null);
		long totalWinning = 0;
		for (int rank=1;rank<=this.hands.size();rank++) {
			totalWinning += rank * this.hands.get(rank-1).bid;
		}
		System.out.println(totalWinning);
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
