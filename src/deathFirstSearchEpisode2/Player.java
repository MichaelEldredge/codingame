package deathFirstSearchEpisode2;

import java.util.Arrays;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

class Player {

	int N;
	int L;
	int E;
	int [][] edges;
	Set<Integer> gateways = new TreeSet<Integer>();
	int bob;
	TreeSet<Integer> degreeTwoNodes = new TreeSet<Integer>();
	
    public static void main(String args[]) {
        Player mge = new Player();
        @SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);
        mge.N = in.nextInt(); // the total number of nodes in the level, including the gateways
        mge.L = in.nextInt(); // the number of links
        mge.E = in.nextInt(); // the number of exit gateways
        System.err.println(mge.N + " " + mge.L + " " + mge.E);
        mge.edges = new int [mge.N][mge.N]; 
        for (int i = 0; i < mge.L; i++) {
            int N1 = in.nextInt(); // N1 and N2 defines a link between these nodes
            int N2 = in.nextInt();
            System.err.println(N1 + " " + N2);
            mge.edges[N1][N2] = 1;
            mge.edges[N2][N1] = 1;
        }
        System.err.println();
        for (int i = 0; i < mge.E; i++) {
            int EI = in.nextInt(); // the index of a gateway node
            System.err.print(EI + " ");
            mge.gateways.add(EI);
        }
        System.err.println();
        System.err.println();
        mge.findDegreeTwoNodes();
        // game loop
        while (true) {
            mge.bob = in.nextInt(); // The index of the node on which the Bobnet agent is positioned this turn
            System.err.println(mge.bob);
            int currentPriority = mge.priorityNode();
            int gatewayNode = mge.adjacentGateway(currentPriority);
            mge.sever(currentPriority, gatewayNode);
        }
    }

	public void sever(int node1, int node2) {
		System.out.print(node1 + " " + node2 + "\n");
		this.edges[node1][node2] = 0;
		this.edges[node2][node1] = 0;
		
		this.degreeTwoNodes.remove(node1);
		this.degreeTwoNodes.remove(node2);
	}

	public int adjacentGateway(int node) {
		for(int i = 0; i < this.N; i++) {
			if(this.edges[node][i] == 1 && this.gateways.contains(i)) {
				return i;
			}
		}
		return -1;
	}

	public void findDegreeTwoNodes() {
		for(int i = 0; i < this.N; i++) {
			int degree = 0;
			for(int j = 0; j < this.N; j++) {
				if(this.edges[i][j] == 1 && this.gateways.contains(j) ) {
					degree++;
					if(degree == 2)
						this.degreeTwoNodes.add(i);
				}
			}
		}
		
	}

	public int priorityNode() {
		if(this.adjacentGateway(this.bob)!= -1 ) {
			return this.bob;			
		}
		if(!this.degreeTwoNodes.isEmpty()) {
			int[] distances = this.distancesToBob();
			int closestDegreeTwoNode = this.degreeTwoNodes.first();
			int closestDistance = distances[closestDegreeTwoNode];
			for(int node : this.degreeTwoNodes) {
				if(distances[node] < closestDistance) {
					closestDegreeTwoNode = node;
					closestDistance = distances[node];
				}
			}
			return closestDegreeTwoNode;
		}
		for(int i = 0; i < this.N; i++) {
			if(this.adjacentGateway(i) != -1) {
				return i;
			}
		}
		return -1;
	}

	int edgeDistance(int start, int end) {
		if(edges[start][end] == 0)
			return N;
		return (this.adjacentGateway(end) == -1)?1:0;
	}

	int[] distancesToBob() {
		// This has worst case time complexity of O(N^3).
		int[] distances = new int[this.N];
		Arrays.fill(distances, this.N);
		distances[this.bob] = 0;
		boolean changes = true;
		while(changes) {
			changes = false;
			for(int i = 0; i < this.N; i++) {
				for(int j = 0; j < this.N; j++) {
					if(distances[i] > distances[j] + this.edgeDistance(j, i)) {
						distances[i] = distances[j] + this.edgeDistance(j, i);
						changes = true;
					}
				}
			}
		}
		return distances;
	}

}
