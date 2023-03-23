package theLabyrinth;

import java.util.Scanner;

class Maze {
	
	public static final int maxDistance = 2000;
	char[][] currentMap;
	int distances [][][][];
	
	public Maze(Scanner in) {
		currentMap = new char[Player.R][Player.C];
		for(int r = 0; r < Player.R; r++) {
			String mazeLine = in.nextLine();
			for(int c = 0; c < Player.C; c++)
				currentMap[r][c] = mazeLine.charAt(c);
		}
		distances = new int[Player.R][Player.C][Player.R][Player.C];
		initializeDistances();
	}

	public Maze(Maze oldMaze) {
		this.currentMap = new char[Player.R][Player.C];
		for(int row = 0; row < Player.R; row++)
			for(int col = 0; col < Player.C; col++)
				this.currentMap[row][col] = oldMaze.currentMap[row][col];
		distances = new int[Player.R][Player.C][Player.R][Player.C];
		initializeDistances();
	}

	private void initializeDistances() {
		for(int i = 0; i < Player.R; i++)
			for(int j = 0; j < Player.C; j++)
				for(int k = 0; k < Player.R; k++)
					for(int l = 0; l < Player.C; l++)
						distances[i][j][k][l] = maxDistance;

		for(int i = 0; i < Player.R; i++)
			for (int j = 0; j < Player.C; j++)
				distances[i][j][i][j] = 0;
	}
	
	public void calculateDistances() {
		boolean changes = true;
		while (changes) {
			changes = false;
			for(int row = 0; row < Player.R; row++)
				for(int col = 0; col < Player.C; col++)
					if(currentMap[row][col] != '#')
						changes = takeShortcut(row, col) || changes;
		}
	}

	boolean takeShortcut(int row, int col) {
		boolean returnme = false;
		if (row != 0)
			returnme = returnme || takeShortcutAux(row, col, -1, 0);
		if (row != Player.R - 1)
			returnme = returnme || takeShortcutAux(row, col, 1, 0);
		if (col != 0)
			returnme = returnme || takeShortcutAux(row, col, 0, -1);			
		if (col != Player.C - 1)
			returnme = returnme || takeShortcutAux(row, col, 0, 1);
		return returnme;
	}
	
	private boolean takeShortcutAux(int row, int col, int rowChange, int colChange) {
		boolean returnme = false;
		if (currentMap[row+rowChange][col+colChange] != '#')
			for(int i = 0; i < Player.R; i++)
				for(int j = 0; j < Player.C; j++) {
					int newPath = 1+distances[row+rowChange][col+colChange][i][j];
					if(distances[row][col][i][j]> newPath) {
						distances[row][col][i][j] = newPath;
						distances[i][j][row][col] = newPath;
						returnme = true;
					}
				}
		return returnme;
	}

	public void replaceCells(char from, char to) {
		for(int row = 0; row < Player.R; row++)
			for(int col = 0; col < Player.C; col++)
				if(currentMap[row][col] == from)
					currentMap[row][col] = to;
	}

	public String getDirection(int startRow, int startCol, int endRow, int endCol) {
		if(startCol != 0 && distances[startRow][startCol][endRow][endCol] == 1+ distances[startRow][startCol+1][endRow][endCol])
			return "RIGHT";
		if(startCol != Player.C - 1 && distances[startRow][startCol][endRow][endCol] == 1+ distances[startRow][startCol-1][endRow][endCol])
			return "LEFT";
		if(startRow != 0 && distances[startRow][startCol][endRow][endCol] == 1+ distances[startRow-1][startCol][endRow][endCol])
			return "UP";
		if(startRow != Player.R - 1 && distances[startRow][startCol][endRow][endCol] == 1+ distances[startRow+1][startCol][endRow][endCol])
			return "DOWN";
		return "NONE";
	}

	public void findControler() {
		for(int row = 0; row < Player.R; row++) {
			for(int col = 0; col < Player.C; col++) {
				if(currentMap[row][col] == 'C') {
					Player.controllerR = row;
					Player.controllerC = col;
					return;
				}
			}
		}
	}

	public void findTransporter() {
		for(int row = 0; row < Player.R; row++) {
			for(int col = 0; col < Player.C; col++) {
				if(currentMap[row][col] == 'T') {
					Player.transporterR = row;
					Player.transporterC = col;
					return;
				}
			}
		}	
	}

	public boolean isExplorationPoint(int row, int col, char target) {
		if(currentMap[row][col] == '.')
			for(int rowChange = -2; rowChange <= 2; rowChange++)
				if(row + rowChange >=0 && row + rowChange < Player.R)
					for(int colChange = -2; colChange <= 2; colChange++)
						if(col + colChange >= 0 && col + colChange < Player.C)
							if(currentMap[row+rowChange][col+colChange] == target)
								return true;
		return false;
	}
	
	public int[] closeExplorationPoint(int startRow, int startCol, char target) {
		int[] explorationPoint = new int[] {-1,-1};
		int shortestDistance = Maze.maxDistance;
		for(int row = 0; row < Player.R; row++) {
			for(int col = 0; col < Player.C; col++) {
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

	public void markPath() {
		int pointerRow = Player.controllerR;
		int pointerCol = Player.controllerC;
		while(pointerRow != Player.transporterR || pointerCol != Player.transporterC) {
			String direction = getDirection(pointerRow,pointerCol,Player.transporterR,Player.transporterC);
			switch(direction) {
				case "LEFT": pointerCol++;
					break;
				case "RIGHT": pointerCol--;
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


}
