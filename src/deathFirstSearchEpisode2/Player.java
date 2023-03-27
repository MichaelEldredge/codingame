package deathFirstSearchEpisode2;

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

        // game loop
        while (true) {
            mge.bob = in.nextInt(); // The index of the node on which the Bobnet agent is positioned this turn
            System.err.println(mge.bob);
            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");


            // Example: 3 4 are the indices of the nodes you wish to sever the link between
            System.out.println("3 4");
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
			return this.degreeTwoNodes.first();
		}
		for(int i = 0; i < this.N; i++) {
			if(this.adjacentGateway(i) != -1) {
				return i;
			}
		}
		return -1;
	}


}
