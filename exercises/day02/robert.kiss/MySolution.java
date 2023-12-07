import java.util.ArrayList;
import java.util.List;

public class MySolution extends MySolutionBase {

    private List<Game> games = new ArrayList<>();

	public MySolution(String inputFilename) {
        super(inputFilename);
		getInputLinesAsStream().forEach(line->this.games.add(new Game(line)));
		
    }

    private MySolution play1() {
		int sumOfIds = this.games.stream().filter(game->game.isPossibleWith(12,13,14)).mapToInt(game->game.gameID).sum();
		System.out.println(sumOfIds);
        return this;
	}

	private MySolution play2() {
		var sumOfPower = this.games.stream().mapToInt(game->game.getPower()).sum();
		System.out.println(sumOfPower);
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
