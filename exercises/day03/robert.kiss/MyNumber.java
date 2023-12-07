
public class MyNumber {

    public int x;
    public int y;
    public String s;

    public MyNumber(int x, int y) {
        this.x = x;
        this.y = y;
        this.s = "";
    }

    public long value() {
        return Long.parseLong(this.s);
    }

}
