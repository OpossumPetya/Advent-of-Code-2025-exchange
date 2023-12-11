public class Route {
    public long step;
    public Position position;

    public Route(long step, Position position) {
        this.step = step;
        this.position = position;
    }

    @Override
    public int hashCode() {
        return position.nodeid.hashCode()+Long.hashCode(this.step);
    }

    @Override
    public String toString() {
        return "Route{"+ step + ',' +position.nodeid +'}';
    }

}
