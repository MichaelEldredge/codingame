package theLabyrinth;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

class TestTheLabyrinth {
	
	private Player loadState(String filename) {
		Player newPlayer = new Player();
		Player.controllerC = -1;
		Player.controllerR = -1;
		Player.transporterC = -1;
		Player.transporterR = -1;
		File boardFile = new File("C:\\trg_demos\\CodingGame\\src\\theLabyrinth\\" + filename);
		try (Scanner fileIn = new Scanner (boardFile)) {
			Player.R = fileIn.nextInt();
			Player.C = fileIn.nextInt();
			fileIn.nextLine();
			newPlayer.currentMaze = new Maze(fileIn);
		} catch (FileNotFoundException e) {
			fail(e.toString());
		}
		return newPlayer;
	}

	@Test
	void shouldLoadState() {
		Player basePlayer = loadState("basicMaze.txt");
		assertEquals(5, Player.R );
		assertEquals(6, Player.C );
		assertEquals('.', basePlayer.currentMaze.currentMap[3][3]);
		assertEquals('#', basePlayer.currentMaze.currentMap[2][3]);
	}

	@Test
	void singleNodeSelfDistanceZero() {
		Player basePlayer = loadState("basicMaze.txt");
		Maze testMaze = basePlayer.currentMaze;
		assertEquals(0,testMaze.distances[1][2][1][2]);
	}
	
	@Test
	void adjacentNodeDistanceOne() {
		Player basePlayer = loadState("basicMaze.txt");
		Maze testMaze = basePlayer.currentMaze;
		assertEquals(Maze.maxDistance,testMaze.distances[1][2][1][3]);
		testMaze.calculateDistances();
		assertEquals(1,testMaze.distances[1][2][1][3]);
	}

	@Test
	void distantNodeDistance() {
		Player basePlayer = loadState("basicMaze.txt");
		Maze testMaze = basePlayer.currentMaze;
		assertEquals(Maze.maxDistance,testMaze.distances[1][2][4][2]);
		testMaze.calculateDistances();
		assertEquals(8,testMaze.distances[1][1][3][1]);
	}
	
	@Test
	void takeShortcutShouldReportChange() {
		Player basePlayer = loadState("basicMaze.txt");
		Maze testMaze = basePlayer.currentMaze;
		assertTrue(testMaze.takeShortcut(1,2));
	}
	
	@Test
	void replaceCShouldBlockEndpoint() {
		Player basePlayer = loadState("basicMaze.txt");
		Maze testMaze = basePlayer.currentMaze;
		testMaze.replaceCells('C','#');
		testMaze.calculateDistances();
		assertEquals(Maze.maxDistance,testMaze.distances[1][1][3][1]);
	}
	
	@Test
	void shouldCopyMaze() {
		Player basePlayer = loadState("basicMaze.txt");
		Maze testMaze = basePlayer.currentMaze;
		Maze copiedMaze = new Maze(testMaze);
		copiedMaze.replaceCells('C', '#');
		assertEquals('C', testMaze.currentMap[3][1]);
		assertEquals('#', copiedMaze.currentMap[3][1]);
	}
	
	@Test
	void shouldGiveDirections() {
		Player basePlayer = loadState("basicMaze.txt");
		Maze testMaze = basePlayer.currentMaze;
		testMaze.calculateDistances();
		assertEquals("RIGHT",testMaze.getDirection(1,1,3,1));
		assertEquals("DOWN",testMaze.getDirection(1,4,3,1));
		assertEquals("NONE",testMaze.getDirection(1,1,0,0));
	}
	
	@Test
	void getLocationOfC() {
		Player basePlayer = loadState("basicMaze.txt");
		Maze testMaze = basePlayer.currentMaze;
		assertEquals(-1,Player.controllerR);
		testMaze.findControler();
		assertEquals(3,Player.controllerR);		
	}

	@Test
	void getLocationOfT() {
		Player basePlayer = loadState("basicMaze.txt");
		Maze testMaze = basePlayer.currentMaze;
		assertEquals(-1,Player.transporterR);
		testMaze.findTransporter();
		assertEquals(1,Player.transporterR);		
	}
	
	@Test
	void shouldDetermineExplorationPoints() {
		Player basePlayer = loadState("fogBoard.txt");
		Maze testMaze = basePlayer.currentMaze;
		assertTrue(testMaze.isExplorationPoint(1,2,'?'));
		assertFalse(testMaze.isExplorationPoint(1,1,'?'));
	}

	@Test
	void getClosestExplorationPoint() {
		Player basePlayer = loadState("fogBoard.txt");
		Maze testMaze = basePlayer.currentMaze;
		testMaze.calculateDistances();
		int[] closePoint = testMaze.closeExplorationPoint(1,1,'?');
		assertEquals(1,closePoint[0]);
		assertEquals(2,closePoint[1]);		
	}

	@Test
	void shouldMarkPath() {
		Player basePlayer = loadState("hiddenPath.txt");
		Maze testMaze = basePlayer.currentMaze;
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
