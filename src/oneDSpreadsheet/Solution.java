package oneDSpreadsheet;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Solution {

	int N;
	String[] operations;
	String[] arg1;
	String[] arg2;
	Map<Integer,Integer> values = new HashMap<Integer,Integer>();
	
	private void init() {
		Scanner in = new Scanner(System.in);
        N = in.nextInt();
        operations = new String[N];
        arg1 = new String[N];
        arg2 = new String[N];
        for (int i = 0; i < N; i++) {
            operations[i] = in.next();
            arg1[i] = in.next();
            arg2[i] = in.next();
        }
        in.close();
	}
	public static void main(String[] args) {
		Solution solution = new Solution();
		solution.init();
        for (int i = 0; i < solution.N; i++) {
        	System.out.println(solution.getValueOfCell(i));

        }
	}
	
	int getValueOfCell(int i) {
		if(!values.containsKey(i))
			values.put(i,calculateValueOfCell(i));
		return values.get(i);
	}

	int calculateValueOfCell(int i) {
		switch(operations[i]) {
			case "VALUE":
				return getArgument(arg1[i]);
			case "ADD":
				return getArgument(arg1[i]) + getArgument(arg2[i]);
			case "SUB":			
				return getArgument(arg1[i]) - getArgument(arg2[i]);
			case "MULT":
				return getArgument(arg1[i]) * getArgument(arg2[i]);
			default:
				throw new RuntimeException("Invalid operation type");				
		}
	}

	int getArgument(String argument) {
		if (argument.contains("$"))
			return getValueOfCell(Integer.parseInt(argument.substring(1)));
		else
			return Integer.parseInt(argument);
	}

}
