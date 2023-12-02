import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Game {
    public Integer gameID;
    public List<Grab> grabs = new ArrayList<>();

    private static Pattern pattern1 = Pattern.compile("Game (\\d+):(.*)", Pattern.CASE_INSENSITIVE);

    public Game(String line) {
        var matcher = pattern1.matcher(line);
        if (matcher.find()) {
            this.gameID = Integer.valueOf(matcher.group(1));
            for (String grab:matcher.group(2).split(";")){
                this.grabs.add(new Grab(grab));
            }
        }
    }

    public boolean isPossibleWith(int r, int g, int b) {
        for (Grab grab:grabs) {
            if (grab.r>r || grab.g>g || grab.b>b ) {
                return false;
            }
        }
        return true;
    }
    
    public Integer getPower() {
        var rmin = grabs.stream().mapToInt(g->g.r).max().orElse(0);
        var gmin = grabs.stream().mapToInt(g->g.g).max().orElse(0);
        var bmin = grabs.stream().mapToInt(g->g.b).max().orElse(0);
        return rmin*gmin*bmin;
    }

}
