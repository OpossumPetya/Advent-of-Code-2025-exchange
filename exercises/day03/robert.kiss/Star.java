import java.util.ArrayList;
import java.util.List;

public class Star {
    public int x;
    public int y;
    private List<MyNumber> adjacentNumbers = new ArrayList<>();

    public Star(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void addAdjacentNumber(MyNumber n) {
        this.adjacentNumbers.add(n);
    }

    public long calcGearRatio() {
        return this.adjacentNumbers.size()==2 ? this.adjacentNumbers.get(0).value() * this.adjacentNumbers.get(1).value() : 0;
    }

}
