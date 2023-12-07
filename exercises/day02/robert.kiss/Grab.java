
public class Grab {
    public int r=0;
    public int g=0;
    public int b=0;

    public Grab(String grab) {
        for (String grab1:grab.split(",")) {
            String[] grab1split=grab1.trim().split(" ");
            if (grab1split[1].startsWith("red")) {
                this.r = Integer.parseInt(grab1split[0]);
            }
            if (grab1split[1].startsWith("green")) {
                this.g = Integer.parseInt(grab1split[0]);
            }
            if (grab1split[1].startsWith("blue")) {
                this.b = Integer.parseInt(grab1split[0]);
            }
        }
    }

}
