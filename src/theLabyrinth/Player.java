package theLabyrinth;

import java.util.Scanner;


class Player {
	
	public static final int maxDistance = 2000;
	
	char[][] currentMap;
	int distances [][][][];
	int R = -1;
	int C = -1;
	int controllerR = -1;
	int controllerC = -1;
	int transporterR = -1;
	int transporterC = -1;
		
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
        int Rows = in.nextInt();
        int Cols = in.nextInt();
        
        in.nextInt();
        boolean countDownTriggered = false;
        while(true) {
        	int KR = in.nextInt();
        	int KC = in.nextInt();
        	in.nextLine();
        	Player currentPlayer = new Player(in,Rows,Cols);
        	currentPlayer.calculateDistances();
        	currentPlayer.findControler();
        	currentPlayer.findTransporter();
        	
        	if(KR == currentPlayer.controllerR && KC == currentPlayer.controllerC) {
        		countDownTriggered = true;
        	}
        	
        	if(currentPlayer.controllerC == -1) {
        		int [] destination = currentPlayer.closeExplorationPoint(KR,KC,'?');
        		System.out.println(currentPlayer.getDirection(KR, KC, destination[0], destination[1]));
        		continue;
        	}
        	
        	if(countDownTriggered) {
        		System.out.println(currentPlayer.getDirection(KR, KC, currentPlayer.transporterR, currentPlayer.transporterC));
        		continue;
        	}
        	
        	currentPlayer.markPath();
        	int [] explorePath = currentPlayer.closeExplorationPoint(KR, KC, '*');
        	// go to the controller
        	if(explorePath[0] == -1) {
        		System.out.println(currentPlayer.getDirection(KR, KC, currentPlayer.controllerR, currentPlayer.controllerC));
        		continue;
        	}
        	currentPlayer.replaceCells('C', '#');
        	currentPlayer.initializeDistances();
        	currentPlayer.calculateDistances();

        	int[] direction = currentPlayer.closeExplorationPoint(KR, KC, '*');
        	System.out.println(currentPlayer.getDirection(KR, KC, direction[0], direction[1]));
        }
	}
	
	public Player(Scanner in, int R, int C) {
		this.R = R;
		this.C = C;
		currentMap = new char[R][C];
		for(int r = 0; r < R; r++) {
			String mazeLine = in.nextLine();
			for(int c = 0; c < C; c++)
				currentMap[r][c] = mazeLine.charAt(c);
		}
		distances = new int[R][C][R][C];
		initializeDistances();
	}
	
	public Player(Player oldMaze) {
		this.R = oldMaze.R;
		this.C = oldMaze.C;
		this.controllerC = oldMaze.controllerC;
		this.controllerR = oldMaze.controllerR;
		this.transporterC = oldMaze.transporterC;
		this.currentMap = new char[R][C];
		for(int row = 0; row < R; row++)
			for(int col = 0; col < C; col++)
				this.currentMap[row][col] = oldMaze.currentMap[row][col];
		distances = new int[R][C][R][C];
		initializeDistances();
	}
	
	void calculateDistances() {
		boolean changes = true;
		while (changes) {
			changes = false;
			for(int row = 0; row < R; row++)
				for(int col = 0; col < C; col++)
					if(currentMap[row][col] != '#')
						changes = takeShortcut(row, col) || changes;
		}
	}

	void replaceCells(char from, char to) {
		for(int row = 0; row < R; row++)
			for(int col = 0; col < C; col++)
				if(currentMap[row][col] == from)
					currentMap[row][col] = to;
	}

	String getDirection(int startRow, int startCol, int endRow, int endCol) {
		if(startCol != C - 1 && distances[startRow][startCol][endRow][endCol] == 1+ distances[startRow][startCol+1][endRow][endCol])
			return "RIGHT";
		if(startCol != 0 && distances[startRow][startCol][endRow][endCol] == 1+ distances[startRow][startCol-1][endRow][endCol])
			return "LEFT";
		if(startRow != 0 && distances[startRow][startCol][endRow][endCol] == 1+ distances[startRow-1][startCol][endRow][endCol])
			return "UP";
		if(startRow != R - 1 && distances[startRow][startCol][endRow][endCol] == 1+ distances[startRow+1][startCol][endRow][endCol])
			return "DOWN";
		return "NONE";
	}

	void findControler() {
		for(int row = 0; row < R; row++) {
			for(int col = 0; col < C; col++) {
				if(currentMap[row][col] == 'C') {
					this.controllerR = row;
					this.controllerC = col;
					return;
				}
			}
		}
	}

	void findTransporter() {
		for(int row = 0; row < R; row++) {
			for(int col = 0; col < C; col++) {
				if(currentMap[row][col] == 'T') {
					this.transporterR = row;
					this.transporterC = col;
					return;
				}
			}
		}	
	}

	int[] closeExplorationPoint(int startRow, int startCol, char target) {
		int[] explorationPoint = new int[] {-1,-1};
		int shortestDistance = Player.maxDistance;
		for(int row = 0; row < R; row++) {
			for(int col = 0; col < C; col++) {
				if(isExplorationPoint(row,col,target) 
						&& distances[startRow][startCol][row][col] < shortestDistance) {
					explorationPoint[0] = row;
					explorationPoint[1] = col;
					shortestDistance = distances[startRow][startCol][row][col];
				}
			}
		}
		return explorationPoint;
	}

	void markPath() {
		int pointerRow = controllerR;
		int pointerCol = controllerC;
		while(pointerRow != transporterR || pointerCol != transporterC) {
			String direction = getDirection(pointerRow,pointerCol,transporterR,transporterC);
			switch(direction) {
				case "LEFT": pointerCol--;
					break;
				case "RIGHT": pointerCol++;
					break;
				case "UP": pointerRow--;
					break;
				case "DOWN": pointerRow++;
					break;
				default: throw new RuntimeException("Invalid direction");
			}
			if(currentMap[pointerRow][pointerCol] == '?') {
				currentMap[pointerRow][pointerCol] = '*';
			}
		}
		
	}

	boolean takeShortcut(int row, int col) {
		boolean returnme = false;
		if (row != 0)
			returnme = returnme || takeShortcutAux(row, col, -1, 0);
		if (row != R - 1)
			returnme = returnme || takeShortcutAux(row, col, 1, 0);
		if (col != 0)
			returnme = returnme || takeShortcutAux(row, col, 0, -1);			
		if (col != C - 1)
			returnme = returnme || takeShortcutAux(row, col, 0, 1);
		return returnme;
	}
	
	private boolean takeShortcutAux(int row, int col, int rowChange, int colChange) {
		boolean returnme = false;
		if (currentMap[row+rowChange][col+colChange] != '#')
			for(int i = 0; i < R; i++)
				for(int j = 0; j < C; j++) {
					int newPath = 1+distances[row+rowChange][col+colChange][i][j];
					if(distances[row][col][i][j]> newPath) {
						distances[row][col][i][j] = newPath;
						distances[i][j][row][col] = newPath;
						returnme = true;
					}
				}
		return returnme;
	}

	private void initializeDistances() {
		for(int i = 0; i < R; i++)
			for(int j = 0; j < C; j++)
				for(int k = 0; k < R; k++)
					for(int l = 0; l < C; l++)
						distances[i][j][k][l] = maxDistance;

		for(int i = 0; i < R; i++)
			for (int j = 0; j < C; j++)
				distances[i][j][i][j] = 0;
	}

	boolean isExplorationPoint(int row, int col, char target) {
		if(currentMap[row][col] == '.')
			for(int rowChange = -2; rowChange <= 2; rowChange++)
				if(row + rowChange >=0 && row + rowChange < R)
					for(int colChange = -2; colChange <= 2; colChange++)
						if(col + colChange >= 0 && col + colChange < C)
							if(currentMap[row+rowChange][col+colChange] == target)
								return true;
		return false;
	}

}
