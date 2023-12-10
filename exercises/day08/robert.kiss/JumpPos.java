import java.util.Objects;

public class JumpPos implements Comparable<JumpPos>{
    public String nodeid;
    public int ipos;
    public JumpPos(String nodeid, int ipos) {
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
        JumpPos oJumpPos = (JumpPos) o;
        return ipos == oJumpPos.ipos && Objects.equals(nodeid, oJumpPos.nodeid);
    }

    @Override
    public int compareTo(JumpPos o) {
        return this.ipos!=o.ipos ? Integer.compare(this.ipos,o.ipos) : this.nodeid.compareTo(o.nodeid);
    }
}
