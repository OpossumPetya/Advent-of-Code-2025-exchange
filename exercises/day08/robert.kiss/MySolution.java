import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class MySolution extends MySolutionBase {


	private char[] insturctions;
	private final Map<String, Node> nodes = new TreeMap<>();

	public MySolution(String inputFilename) {
        super(inputFilename);
		this.insturctions = getInputLinesAsList().get(0).toCharArray();
		for (String line:getInputLinesAsList().subList(2,getInputLinesAsList().size())) {
			var node = Node.createNode(line);
			if (node!=null) {
				this.nodes.put(node.nodeid, node);
			}
		}
    }

    private MySolution play1() {
		int step = 0;
		var myNode = this.nodes.get("AAA");
		while (!myNode.nodeid.equals("ZZZ")) {
			var myInsturction = insturctions[step % insturctions.length];
			//System.out.println(myNode +"  "+myInsturction);
			myNode = nodes.get(myNode.getNextNodeId(myInsturction));
			step +=1;
		}
		//System.out.println(myNode);
		System.out.println(step);
		System.out.println("---");
        return this;
	}



	Map<JumpPos,Jump> jumps = new TreeMap<>();

	private Ghost ghoststep(Ghost myGhost) {
		var jumpStart = new JumpPos(myGhost.nodeid, (int)(myGhost.step % this.insturctions.length));
		Jump jump = jumps.get(jumpStart);
		if (jump != null){
			return new Ghost(myGhost.step + jump.dist, jump.nodeid);
		}
		var jumpPos = jumpStart;
		long dist = 0;
		while (dist==0 || !jumpPos.nodeid.endsWith("Z")) {
			jumpPos = new JumpPos(this.nodes.get(jumpPos.nodeid).getNextNodeId(this.insturctions[jumpPos.ipos]), (jumpPos.ipos + 1) % this.insturctions.length);
			dist += 1;
		}
		jump = new Jump(dist, jumpPos.nodeid);
		jumps.put(jumpStart,jump);
		return new Ghost(myGhost.step + jump.dist, jump.nodeid);
	}

	private MySolution play2() {
		long step = 0L;
		var myGhosts = this.nodes.values().stream().filter(node->node.nodeid.endsWith("A")).map(node->new Ghost(0L,node.nodeid)).collect(Collectors.toList()).toArray(new Ghost[0]);
		//System.out.println(Arrays.stream(myGhosts).map(Ghost::toString).collect(Collectors.toList()));
		long minStep = 0L;
		long maxStep = 1L;
		while (minStep<maxStep) {
			minStep = maxStep;
			for (int i=0;i<myGhosts.length;i++) {
				while (myGhosts[i].step<maxStep){
					myGhosts[i] = ghoststep(myGhosts[i]);
				}
				maxStep = myGhosts[i].step;
			}
		}
		System.out.println(maxStep);
		System.out.println("---");
		return this;
	}

	public static void main(String args[]) {
		try {
			Instant start = Instant.now();
            new MySolution("sample.txt").play1().play2();
			new MySolution("sample2.txt").play2();
            new MySolution("input.txt").play1().play2();
			Instant end = Instant.now();
			var duration = Duration.between(start, end);
			long HH = duration.toHours();
			long MM = duration.toMinutesPart();
			long SS = duration.toSecondsPart();
			String timeString = String.format("Time elapsed (hh:mm:ss) %02d:%02d:%02d", HH, MM, SS);
			System.out.println(timeString);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
/*
========== Processing sample.txt ==========
2
---
2
---
========== Processing sample2.txt ==========
6
---
========== Processing input.txt ==========
18727
---
18024643846273
---
00:01:33

 */
