package codeALaMode;

import java.util.Scanner;

class Player {

	final static int rows = 7;
	final static int cols = 11; 

	public static Scanner in;
	public static int numAllCustomers;
	public static String[] board;
	public static int playerX;
	public static int playerY;
	public static String playerItem;
	public static String[] orders;
	public static String ovenContents;
	public static int ovenTimer;
	public static int[] tablesX;
	public static int[] tablesY;
	public static String[] tablesContents;

	
    public static void main(String args[]) {
        in = new Scanner(System.in);
        setUpGame();
        while (true) {
        	fetchTurnInfoWithLogging();
        	Task nextTask = pickTask();
        	String nextInstruction = createInstruction(nextTask);
            System.out.println(nextInstruction);
        }
    }
    
	private static String createInstruction(Task nextTask) {
		switch (nextTask) {
		case BLUEBERRIESPICKUP:
			return howToGet("BLUEBERRIES");
		case CHOPPEDSTRAWBERRYPICKUP:
			return pickUpChoppedStrawberries();
		case CHOPPSTRAWBERRIES:
			return howToGet("CHOPPING_BLOCK");
		case DISHEMPTY: // Intentionally empty
		case DISHPICKUP:
			return howToGet("DISH");
		case ICECREAMPICKUP:
			return howToGet("ICE_CREAM");
		case ITEMSETDOWN:
			return howToPutDown();
		case STRAWBERRYPICKUP:
			return howToGet("STRAWBERRIES");
		case WAIT:
			return "WAIT";
		case WINDOWDELIVER:
			return howToGet("WINDOW");
		case CROISSANTPICKUP:
			return howToGet("OVEN");
		case DOUGHPICKUP:
			return howToGet("H");
		case DOUGHBAKE:
			return howToGet("OVEN");
		}
		throw new RuntimeException("Unexpected task passed to createInstruction");
	}

	public static void setUpGame() {
        numAllCustomers = in.nextInt();
        for (int i = 0; i < numAllCustomers; i++) {
        	in.next();
        	in.nextInt();
        }
        in.nextLine();
        board = new String[rows];
        for (int i = 0; i < rows; i++) {
            board[i] = in.nextLine();
        }
	}
	
	public static void fetchTurnInfoWithLogging() {
        int turnsRemaining = in.nextInt();
        System.err.println(turnsRemaining);
        Player.playerX = in.nextInt();
        Player.playerY = in.nextInt();
        Player.playerItem = in.next();
        System.err.println(playerX + " " + playerY + " " + playerItem);
        int partnerX = in.nextInt();
        int partnerY = in.nextInt();
        String partnerItem = in.next();
        System.err.println(partnerX + " " + partnerY + " " + partnerItem);
        int numTablesWithItems = in.nextInt(); // the number of tables in the kitchen that currently hold an item
        System.err.println(numTablesWithItems);
        tablesX = new int[numTablesWithItems];
        tablesY = new int[numTablesWithItems];
        tablesContents = new String[numTablesWithItems];
        for (int i = 0; i < numTablesWithItems; i++) {
            int tableX = in.nextInt();
            int tableY = in.nextInt();
            String item = in.next();
            tablesX[i] = tableX;
            tablesY[i] = tableY;
            tablesContents[i] = item;
            System.err.println(tableX + " " + tableY + " " + item);
        }
        ovenContents = in.next(); // ignore until wood 1 league
        ovenTimer = in.nextInt();
        System.err.println(ovenContents + " " + ovenTimer);
        int numCustomers = in.nextInt(); // the number of customers currently waiting for food
        System.err.println(numCustomers);
        orders = new String[3];
        for (int i = 0; i < numCustomers; i++) {
            String customerItem = in.next();
            int customerAward = in.nextInt();
            orders[i] = customerItem;
            System.err.println(customerItem + " " + customerAward);
        }
	}
	
	public static boolean playerCarrying(String item) {
		return Player.playerItem.contains(item);
	}
	
	public static String howToGet(String item) {
		int[] coordinates = fetchLocation(item);
		if(coordinates != null) {
			return "USE " + coordinates[0] + " " + coordinates[1];
		}
		else
			return "WAIT";
	}

	public static int[] fetchLocation(String item) {
		for(int i = 0; i < rows; i++) {
			int indexOfItem = board[i].indexOf(item.charAt(0));
			if(indexOfItem != -1) {
				int[] coordinates = {indexOfItem, i};
				return coordinates;
			}
		}
		return null;
	}
	
	public static Task pickTask() {
		
		if(howManyMoreStrawberriesNeeded() > 0) {
			if(playerCarrying("NONE")) {
				return Task.STRAWBERRYPICKUP;
			}
			else if(playerCarrying("CHOPPED_STRAWBERRIES")) {
				return Task.ITEMSETDOWN;
			}
			else if(playerCarrying("STRAWBERRIES")) {
				return Task.CHOPPSTRAWBERRIES;
			}
			// else do nothing
		}
		

		if(ovenContents.equals("NONE") && !playerCarrying("DISH")) {
			if(playerCarrying("DOUGH")) {
				return Task.DOUGHBAKE;
			}
			else {
				return Task.DISHPICKUP;
			}
		}
		
		if(needCroissant() && !playerCarrying("CROISSANT")) {
			return Task.CROISSANTPICKUP;
		}
		
		if(needStrawberries() && !playerCarrying("CHOPPED_STRAWBERRIES")) {
			return Task.CHOPPEDSTRAWBERRYPICKUP;
		}
		
		if(!playerCarrying("DISH")) {
			return Task.DISHPICKUP;
		}
		
		if(needIceCream() && !playerCarrying("ICE_CREAM")) {
			return Task.ICECREAMPICKUP;
		}
		
		if(needBlueBerries() && !playerCarrying("BLUEBERRIES")) {
			return Task.BLUEBERRIESPICKUP;
		}
		
		return Task.WINDOWDELIVER;
	}
	
	private static boolean needCroissant() {
		return orders[0].contains("CROISSANT");
	}

	private static boolean needBlueBerries() {
		return orders[0].contains("BLUEBERRIES");
	}

	private static boolean needIceCream() {
		return orders[0].contains("ICE_CREAM");
	}

	enum Task {
		DISHPICKUP,
		DISHEMPTY,
		ICECREAMPICKUP,
		BLUEBERRIESPICKUP,
		STRAWBERRYPICKUP,
		CHOPPEDSTRAWBERRYPICKUP,
		CHOPPSTRAWBERRIES,
		WINDOWDELIVER,
		ITEMSETDOWN,
		WAIT,
		DOUGHPICKUP,
		CROISSANTPICKUP,
		DOUGHBAKE
	}
	
	public static boolean needStrawberries() {
		return orders[0].contains("STRAWBERRIES");
	}

	public static int howManyMoreStrawberriesNeeded() {
		int count = 0;
		for(String order:orders) {
			if(order.contains("STRAWBERRIES")) {
				count++;
			}
		}
		for(String tableContent:tablesContents) {
			if(tableContent.contains("CHOPPED_STRAWBERRIES")) {
				count--;
			}
		}
		return count;
	}

	public static String pickUpChoppedStrawberries() {
		int tableToPickFrom = 0;
		while(!tablesContents[tableToPickFrom].contains("CHOPPED_STRAWBERRIES")) {
			tableToPickFrom++;
		}
		return "USE " + tablesX[tableToPickFrom] + " " + tablesY[tableToPickFrom];
	}

	public static boolean isEmptyTable(int x, int y) {
		for(int i = 0; i < tablesX.length; i++) {
			if(tablesX[i] == x && tablesY[i] == y) {
				return false;
			}
		}
		return board[y].charAt(x) == '#';
	}

	public static String howToPutDown() {
		for(int i = -1; i <= 1; i++) {
			for(int j = -1; j <= 1; j++) {
				if(isEmptyTable(playerX + i, playerY + j)) {
					return "USE " + (playerX + i) + " " + (playerY + j);
				}
			}
		}
		return null;
	}

	public static int howManyMoreCroissantsNeeded() {
		int count = 0;
		for(String order:orders) {
			if(order.contains("CROISSANT")) {
				count++;
			}
		}
		for(String tableContent:tablesContents) {
			if(tableContent.contains("CROISSANT")) {
				count--;
			}
		}
		return count;
	}


	public static boolean isPartialDish(String dish, int i) {
		if(!dish.contains("DISH")) {
			return false;
		}
		String orderStr = orders[i]; 
		for(FinishedDeserts desert:FinishedDeserts.values()) {
			String desertStr = desert.toString();
			if(dish.contains(desertStr) && !orderStr.contains(desertStr)) {
				return false;
			}
		}
		return true;
	}
	
	enum FinishedDeserts {
		CHOPPED_STRAWBERRIES,
		BLUEBERRIES,
		ICE_CREAM,
		CROISSANT
	}


}