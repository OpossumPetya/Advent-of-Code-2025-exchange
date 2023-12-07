import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Hand implements Comparable<Hand> {

    public String cards;
    public Long bid;
    public String compareString;

    public Hand(String line) {
        var lineparts = line.split(" ");
        this.cards=lineparts[0];
        this.bid = Long.valueOf(lineparts[1]);
    }

    public void prepareForPart1() {
        // counting identical cards
        Map<Character,Integer> card2count = new TreeMap<>();
        for (char c:this.cards.toCharArray()) {
            card2count.put(c,card2count.getOrDefault(c, 0)+1);
        }

        // get the counts and sort them
        List<Integer> counts = new ArrayList<>(card2count.values());
        Collections.sort(counts,Collections.reverseOrder());
        
        // join the counts, this will te the type
        String type = counts.stream().map(i->String.valueOf(i)).collect(Collectors.joining(""));

        // create the string for comparing cards
        this.compareString = type + "_" +
                             this.cards
                               .replace("T", "B")
                               .replace("J", "C")
                               .replace("Q", "D")
                               .replace("K", "E")
                               .replace("A", "F");
    }


    public void prepareForPart2() {
        // counting identical cards
        Map<Character,Integer> card2count = new TreeMap<>();
        for (char c:this.cards.toCharArray()) {
            card2count.put(c,card2count.getOrDefault(c, 0)+1);
        }

        // get out the jokers
        Integer joker = card2count.getOrDefault('J',0);
        card2count.remove('J');

        // get the counts and sort them
        List<Integer> counts = new ArrayList<>(card2count.values());
        Collections.sort(counts,Collections.reverseOrder());

        // add the jokers to the most valuable group of cards
        if (counts.size()>0) {
            counts.set(0,counts.get(0)+joker);
        } else {
            counts.add(joker);
        }

        // join the counts, this will te the type
        String type = counts.stream().map(i->String.valueOf(i)).collect(Collectors.joining(""));

        // create the string for comparing cards
        this.compareString = type + "_" +
                             this.cards
                               .replace("T", "B")
                               .replace("J", "0")  // this has changed
                               .replace("Q", "D")
                               .replace("K", "E")
                               .replace("A", "F");
    }

    @Override
    public int compareTo(Hand o) {
        return this.compareString.compareTo(o.compareString);
    }

}
