import java.util.Objects;

public class Position implements Comparable<Position>{
    public String nodeid;
    public int ipos;
    public Position(String nodeid, int ipos) {
        this.nodeid = nodeid;
        this.ipos = ipos;
    }

    @Override
    public int hashCode() {
        return nodeid.hashCode()+Long.hashCode(this.ipos);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position oJumpPos = (Position) o;
        return ipos == oJumpPos.ipos && Objects.equals(nodeid, oJumpPos.nodeid);
    }

    @Override
    public int compareTo(Position o) {
        return this.ipos!=o.ipos ? Integer.compare(this.ipos,o.ipos) : this.nodeid.compareTo(o.nodeid);
    }

    @Override
    public String toString() {
        return "Position{" +
                "nodeid='" + nodeid + '\'' +
                ", ipos=" + ipos +
                '}';
    }
}
