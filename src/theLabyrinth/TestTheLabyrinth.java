package theLabyrinth;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

class TestTheLabyrinth {
	
	private Maze loadMazeFromFile(String filename) {
		Maze newMaze = null;
		File boardFile = new File("C:\\trg_demos\\CodingGame\\src\\theLabyrinth\\" + filename);
		try (Scanner fileIn = new Scanner (boardFile)) {
			int localR = fileIn.nextInt();
			int localC = fileIn.nextInt();
			fileIn.nextLine();
			newMaze = new Maze(fileIn,localR,localC);
		} catch (FileNotFoundException e) {
			fail(e.toString());
		}
		return newMaze;
		
	}

	@Test
	void shouldLoadState() {
		Maze testMaze = loadMazeFromFile("basicMaze.txt");
		assertEquals(5, testMaze.R );
		assertEquals(6, testMaze.C );
		assertEquals('.', testMaze.currentMap[3][3]);
		assertEquals('#', testMaze.currentMap[2][3]);
	}

	@Test
	void singleNodeSelfDistanceZero() {
		Maze testMaze = loadMazeFromFile("basicMaze.txt");
		assertEquals(0,testMaze.distances[1][2][1][2]);
	}
	
	@Test
	void adjacentNodeDistanceOne() {
		Maze testMaze = loadMazeFromFile("basicMaze.txt");
		assertEquals(Maze.maxDistance,testMaze.distances[1][2][1][3]);
		testMaze.calculateDistances();
		assertEquals(1,testMaze.distances[1][2][1][3]);
	}

	@Test
	void distantNodeDistance() {
		Maze testMaze = loadMazeFromFile("basicMaze.txt");
		assertEquals(Maze.maxDistance,testMaze.distances[1][2][4][2]);
		testMaze.calculateDistances();
		assertEquals(8,testMaze.distances[1][1][3][1]);
	}
	
	@Test
	void takeShortcutShouldReportChange() {
		Maze testMaze = loadMazeFromFile("basicMaze.txt");
		assertTrue(testMaze.takeShortcut(1,2));
	}
	
	@Test
	void replaceCShouldBlockEndpoint() {
		Maze testMaze = loadMazeFromFile("basicMaze.txt");;
		testMaze.replaceCells('C','#');
		testMaze.calculateDistances();
		assertEquals(Maze.maxDistance,testMaze.distances[1][1][3][1]);
	}
	
	@Test
	void shouldCopyMaze() {
		Maze testMaze = loadMazeFromFile("basicMaze.txt");;
		Maze copiedMaze = new Maze(testMaze);
		copiedMaze.replaceCells('C', '#');
		assertEquals('C', testMaze.currentMap[3][1]);
		assertEquals('#', copiedMaze.currentMap[3][1]);
	}
	
	@Test
	void shouldGiveDirections() {
		Maze testMaze = loadMazeFromFile("basicMaze.txt");;
		testMaze.calculateDistances();
		assertEquals("RIGHT",testMaze.getDirection(1,1,3,1));
		assertEquals("DOWN",testMaze.getDirection(1,4,3,1));
		assertEquals("NONE",testMaze.getDirection(1,1,0,0));
	}
	
	@Test
	void getLocationOfC() {
		Maze testMaze = loadMazeFromFile("basicMaze.txt");;
		assertEquals(-1,testMaze.controllerR);
		testMaze.findControler();
		assertEquals(3,testMaze.controllerR);		
	}

	@Test
	void getLocationOfT() {
		Maze testMaze = loadMazeFromFile("basicMaze.txt");
		assertEquals(-1,testMaze.transporterR);
		testMaze.findTransporter();
		assertEquals(1,testMaze.transporterR);		
	}
	
	@Test
	void shouldDetermineExplorationPoints() {
		Maze testMaze = loadMazeFromFile("fogBoard.txt");
		assertTrue(testMaze.isExplorationPoint(1,2,'?'));
		assertFalse(testMaze.isExplorationPoint(1,1,'?'));
	}

	@Test
	void getClosestExplorationPoint() {
		Maze testMaze = loadMazeFromFile("fogBoard.txt");
		testMaze.calculateDistances();
		int[] closePoint = testMaze.closeExplorationPoint(1,1,'?');
		assertEquals(1,closePoint[0]);
		assertEquals(2,closePoint[1]);		
	}

	@Test
	void shouldMarkPath() {
		Maze testMaze = loadMazeFromFile("hiddenPath.txt");
		assertEquals('?',testMaze.currentMap[4][12]);
		testMaze.findControler();
		testMaze.findTransporter();
		testMaze.calculateDistances();
		testMaze.markPath();
		assertEquals('*',testMaze.currentMap[5][12]);
		assertEquals('*',testMaze.currentMap[4][12]);
		assertEquals('.',testMaze.currentMap[2][12]);
	}
	

}
