import java.util.Objects;
import java.util.regex.Pattern;

public class Node {
    public String nodeid;
    public String next_l;
    public String next_r;

    public Node(String pos, String next_l, String next_r) {
        this.nodeid = pos;
        this.next_l = next_l;
        this.next_r = next_r;
    }

    public String getNextNodeId(char direction) {
        return direction == 'L' ? this.next_l : this.next_r;
    }

    private static Pattern p = Pattern.compile("([0-9A-Z]+) = \\(([0-9A-Z]+), ([0-9A-Z]+)\\)");
    public static Node createNode(String nodestr) {
        var m = p.matcher(nodestr);
        if (m.find()) {
            return new Node(m.group(1),m.group(2),m.group(3));
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        return "Node{" +
                "id='" + nodeid + '\'' +
                ", next_l='" + next_l + '\'' +
                ", next_r='" + next_r + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return this.nodeid.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Objects.equals(nodeid, node.nodeid);
    }
}
