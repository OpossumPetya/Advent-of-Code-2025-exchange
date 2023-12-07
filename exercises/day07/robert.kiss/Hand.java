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
        Map<Integer,Integer> cc = new TreeMap<>();
        this.cards.chars().forEach(c->cc.put(c, 0));
        this.cards.chars().forEach(c->cc.put(c, 1+cc.get(c)));

        // get the counts and sort them
        List<Integer> ccc = new ArrayList<>(cc.values());
        Collections.sort(ccc,Collections.reverseOrder());
        
        // join the counts, this will te the type
        String type = ccc.stream().map(i->String.valueOf(i)).collect(Collectors.joining(""));

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
        Map<Integer,Integer> cc = new TreeMap<>();
        this.cards.chars().forEach(c->cc.put(c, 0));
        this.cards.chars().forEach(c->cc.put(c, 1+cc.get(c)));

        // get out the jokers
        Integer ccj = cc.keySet().contains((int)'J') ? cc.remove((int)'J') : 0;

        // get the counts and sort them
        List<Integer> ccc = new ArrayList<>(cc.values());
        Collections.sort(ccc,Collections.reverseOrder());

        // add the jokers to the most valuable group of cards
        if (ccc.size()>0) {
            ccc.set(0,ccc.get(0)+ccj);
        } else {
            ccc.add(ccj);
        }

        // join the counts, this will te the type
        String type = ccc.stream().map(i->String.valueOf(i)).collect(Collectors.joining(""));

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
