public class Jump {
    public long dist;
    Position position;
    public Jump(long dist, Position position) {
        this.dist = dist;
        this.position = position;
    }

    @Override
    public String toString() {
        return "Jump{" +
                "dist=" + dist +
                ", dest='" + position.toString() + '\'' +
                '}';
    }
}
