package oneDSpreadsheet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.spy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class SolutionTest {

	Solution solution;

	@BeforeEach
	void setup() {
		solution = new Solution();
		solution.N = 4;

		solution.operations = new String[solution.N];
		solution.arg1 = new String[solution.N];
		solution.arg2 = new String[solution.N];
		
		solution.operations[0] = "VALUE";
		solution.arg1[0] = "3";
		solution.arg2[0] = "_";

		solution.operations[1] = "ADD";
		solution.arg1[1] = "$0";
		solution.arg2[1] = "4";

		solution.operations[2] = "SUB";
		solution.arg1[2] = "10";
		solution.arg2[2] = "5";
		
		solution.operations[3] = "MULT";
		solution.arg1[3] = "$2";
		solution.arg2[3] = "5";
	}
	
	@Test
	void shouldReportBasicValue() {
		assertEquals(3,solution.calculateValueOfCell(0));
	}
	
	@Test
	void shouldGetArgumentFromPlainText() {
		assertEquals(4,solution.getArgument("4"));
	}

	@Test
	void shouldGetArgumentFromField() {
		assertEquals(3,solution.getArgument("$0"));
	}
	
	@Test
	void shouldAdd() {
		assertEquals(7,solution.calculateValueOfCell(1));
	}

	@Test
	void shouldSubtract() {
		assertEquals(5,solution.calculateValueOfCell(2));
	}

	@Test
	void shouldMultiply() {
		assertEquals(25,solution.calculateValueOfCell(3));
	}
	
	@Test
	void perfomance() {
		System.out.println(Solution.class);
		Solution spySolution = spy(solution);
		spySolution.getValueOfCell(0);
		spySolution.getValueOfCell(1);	
		verify(spySolution, times(1)).calculateValueOfCell(0);
	}

}
