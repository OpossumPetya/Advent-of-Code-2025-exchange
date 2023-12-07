import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Card {
    private static Pattern pattern1 = Pattern.compile("Card\\s+(\\d+):(.*)", Pattern.CASE_INSENSITIVE);

    public Integer cardID;

    private Set<Integer> winningNumbers;
    private Set<Integer> myNumbers;
    public int copies = 1;

    public Card(String line) {
        var matcher = pattern1.matcher(line);
        if (matcher.find()) {
            this.cardID = Integer.valueOf(matcher.group(1));
            String cardContent = matcher.group(2);
            String[] numbergroups = cardContent.split("\\|");
            this.winningNumbers = Arrays.asList(numbergroups[0].trim().split("\\s+")).stream().map(Integer::valueOf).collect(Collectors.toSet());
            this.myNumbers = Arrays.asList(numbergroups[1].trim().split("\\s+")).stream().map(Integer::valueOf).collect(Collectors.toSet());
        }
    }

    public int getMatchCount() {
        var share = new TreeSet<>(this.myNumbers);
        share.retainAll(winningNumbers);

        return share.size();
    }

    public int getPoints() {
        var matchcount = getMatchCount();
        return matchcount > 0 ? 1 << matchcount-1 : 0;
    }

    public void addCopies ( int n) {
        this.copies += n;
    }

}
