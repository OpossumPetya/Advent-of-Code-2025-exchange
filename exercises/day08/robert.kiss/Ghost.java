import java.util.Objects;

public class Ghost {
    public long step;
    public String nodeid;

    public Ghost(long step, String nodeid) {
        this.step = step;
        this.nodeid = nodeid;
    }

    @Override
    public int hashCode() {
        return nodeid.hashCode()+Long.hashCode(this.step);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ghost oGhost = (Ghost) o;
        return step == oGhost.step && Objects.equals(nodeid, oGhost.nodeid);
    }

    @Override
    public String toString() {
        return "{"+ step + ',' +nodeid +'}';
    }

}
