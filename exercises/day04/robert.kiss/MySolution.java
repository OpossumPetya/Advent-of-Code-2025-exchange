import java.util.ArrayList;
import java.util.List;

public class MySolution extends MySolutionBase {


	private List<Card> cards = new ArrayList<>();

	public MySolution(String inputFilename) {
        super(inputFilename);
		getInputLinesAsStream().forEach(line->this.cards.add(new Card(line)));

    }

    private MySolution play1() {
		int result = this.cards.stream().mapToInt(c->c.getPoints()).sum();
		System.out.println(result);
        return this;
	}

	private MySolution play2() {
		this.cards.forEach(c->{
			for ( int i=0;i<c.getMatchCount();i++) {
				this.cards.get(c.cardID + i).addCopies(c.copies);
			}
		});
		int result = this.cards.stream().mapToInt(c->c.copies).sum();;
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
