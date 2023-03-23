package codeALaMode;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

class PlayerTest {
	
	@Test
	void shouldGetNumAllCustomersForWoodLeague()  {
		injectInputFromFile("woodLevelBoard.txt");
		Player.setUpGame();
		assertEquals(20, Player.numAllCustomers);
	}
	
	@Test
	void shouldImportGameBoard() {
		injectInputFromFile("woodLevelBoard.txt");
		Player.setUpGame();
		assertEquals('D',Player.board[0].charAt(5));
	}
	
	@Test
	void shouldImportTurnInfo() {
		injectInputFromFile("basicTurn.txt");
		Player.fetchTurnInfoWithLogging();
		assertEquals(1, Player.playerX);
		assertEquals(3, Player.playerY);
	}
	
	@Test
	void shouldImportPlayerCarrying() {
		injectInputFromFile("basicTurn.txt");
		Player.fetchTurnInfoWithLogging();
		assertEquals("NONE",Player.playerItem);
		
		injectInputFromFile("carryingDishTurn.txt");
		Player.fetchTurnInfoWithLogging();
		assertEquals("DISH",Player.playerItem);
	}
	
	@Test
	void shouldTellIfPlayerIsCarryingIceCream() {
		injectInputFromFile("carryingDishTurn.txt");
		Player.fetchTurnInfoWithLogging();
		assertFalse(Player.playerCarrying("ICE_CREAM"));
		
		injectInputFromFile("carryingIceCream.txt");
		Player.fetchTurnInfoWithLogging();
		assertTrue(Player.playerCarrying("ICE_CREAM"));		
	}
	
	@Test
	void shouldTellIfPlayerIsCarryingBlueberries() {
		injectInputFromFile("carryingDishTurn.txt");
		Player.fetchTurnInfoWithLogging();
		assertFalse(Player.playerCarrying("BLUEBERRIES"));
		
		injectInputFromFile("carryingIceCreamBlueberries.txt");
		Player.fetchTurnInfoWithLogging();
		assertTrue(Player.playerCarrying("BLUEBERRIES"));		
	}
	
	@Test
	void shouldTellIfPlayerIsCarryingDish() {
		injectInputFromFile("basicTurn.txt");
		Player.fetchTurnInfoWithLogging();
		assertFalse(Player.playerCarrying("DISH"));
		
		injectInputFromFile("carryingIceCreamBlueberries.txt");
		Player.fetchTurnInfoWithLogging();
		assertTrue(Player.playerCarrying("DISH"));		
	}
	
	@Test
	void shouldInstructHowToGetDish() {
		injectInputFromFile("woodLevelBoard.txt");
		Player.setUpGame();
		injectInputFromFile("basicTurn.txt");
		Player.fetchTurnInfoWithLogging();
		assertEquals("USE 5 0",Player.howToGet("DISH"));
	}
	
	@Test
	void shouldFindItemLocation() {
		injectInputFromFile("woodLevelBoard.txt");
		Player.setUpGame();
		int[] coordinates = Player.fetchLocation("DISH");
		assertEquals(5,coordinates[0]);
		assertEquals(0,coordinates[1]);
	}
	
	@Test
	void shouldStoreCustomerOrders() {
		injectInputFromFile("turnWithStrawberries.txt");
		Player.fetchTurnInfoWithLogging();
		assertEquals("DISH-CHOPPED_STRAWBERRIES-BLUEBERRIES-ICE_CREAM",Player.orders[0]);
		assertEquals("DISH-CHOPPED_STRAWBERRIES-ICE_CREAM-BLUEBERRIES",Player.orders[1]);
		assertEquals("DISH-ICE_CREAM-CHOPPED_STRAWBERRIES-BLUEBERRIES",Player.orders[2]);
	}
	
	@Test
	void shouldReportStrawberriesNotNeeded() {
		injectInputFromFile("basicTurn.txt");
		Player.fetchTurnInfoWithLogging();
		assertFalse(Player.needStrawberries());
	}
	
	@Test
	void shouldReportStrawberriesNeeded() {
		injectInputFromFile("turnWithStrawberries.txt");
		Player.fetchTurnInfoWithLogging();
		assertTrue(Player.needStrawberries());
	}
	
	@Test
	void shouldNotGetStuckInLoop1() {
		injectInputFromFile("turnDishCycle.txt");
		Player.fetchTurnInfoWithLogging();
		assertNotEquals(Player.Task.DISHEMPTY,Player.pickTask());
		assertNotEquals(Player.Task.DISHPICKUP,Player.pickTask());
	}
	
	@Test
	void shouldCountRequiredStrawberries() {
		injectInputFromFile("turnDishCycle.txt");
		Player.fetchTurnInfoWithLogging();
		assertEquals(2,Player.howManyMoreStrawberriesNeeded());
	}
	
	@Test
	void shouldCountRequiredStrawberriesWithAlreadyChoppedBerries() {
		injectInputFromFile("turnWithStrawberries.txt");
		Player.fetchTurnInfoWithLogging();
		assertEquals(2,Player.howManyMoreStrawberriesNeeded());		
	}
	
	@Test
	void shouldPickUpStrawberries() {
		injectInputFromFile("turnWithStrawberries.txt");
		Player.fetchTurnInfoWithLogging();
		assertEquals("USE 8 3", Player.pickUpChoppedStrawberries());
	}
	
	@Test
	void shouldReportEmptyTable() {
		injectInputFromFile("woodLevelBoard.txt");
		Player.setUpGame();
		injectInputFromFile("turnWithStrawberries.txt");
		Player.fetchTurnInfoWithLogging();
		assertTrue(Player.isEmptyTable(0,3));
		assertFalse(Player.isEmptyTable(8,3));
	}
	
	@Test
	void shouldPickUpChoppedStrawberries() {
		injectInputFromFile("turnWithStrawberries.txt");
		Player.fetchTurnInfoWithLogging();
		assertEquals(Player.Task.CHOPPEDSTRAWBERRYPICKUP,Player.pickTask());
	}
	
	@Test 
	void shouldCountNeededCroissant() {
		injectInputFromFile("turnPickUpStrawberries.txt");
		Player.fetchTurnInfoWithLogging();
		assertEquals(1,Player.howManyMoreCroissantsNeeded());
	}
	
	//@Test
	void shouldPutDownDishWhenWaiting() {
		injectInputFromFile("turnEmptyOvenWithDish.txt");
		Player.fetchTurnInfoWithLogging();
		assertEquals(Player.Task.ITEMSETDOWN,Player.pickTask());
	}
	
	@Test 
	void shouldTellADishCanBeExtendedToCustomer() {
		injectInputFromFile("turnPickUpStrawberries.txt");
		Player.fetchTurnInfoWithLogging();
		assertTrue(Player.isPartialDish("DISH-CROISSANT",0));
		assertFalse(Player.isPartialDish("DISH-CROISSANT",1));
	}
	
	
	void injectInputFromFile(String fileName) {
		String boardPath = "C:\\trg_demos\\CodingGame\\src\\codeALaMode\\" + fileName;
		File initialFile = new File(boardPath);
		try {
			Player.in = new Scanner(initialFile);
		}
		catch(Exception e) {
			fail(e.toString());
		}
	}
}
