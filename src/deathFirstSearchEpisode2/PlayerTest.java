package deathFirstSearchEpisode2;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


class PlayerTest {

	@Test
	public void whenNotUseMockAnnotation_thenCorrect() {
	    @SuppressWarnings("unchecked")
		List<String> mockList = Mockito.mock(ArrayList.class);
	    
	    assertFalse(mockList.add("one"));
	    Mockito.verify(mockList).add("one");
	    assertEquals(0, mockList.size());

	    Mockito.when(mockList.size()).thenReturn(100);
	    assertEquals(100, mockList.size());
	}
	
	@Test
	public void whenNotUseSpyAnnotation_thenCorrect() {
	    List<String> spyList = Mockito.spy(new ArrayList<String>());
	    
	    spyList.add("one");
	    spyList.add("two");

	    Mockito.verify(spyList).add("one");
	    Mockito.verify(spyList).add("two");

	    assertEquals(2, spyList.size());

	    Mockito.doReturn(100).when(spyList).size();
	    assertEquals(100, spyList.size());
	}
	
	private Player loadState(String filename) {
		Player tester = new Player();
		File setup = new File("C:\\trg_demos\\CodingGame\\src\\theLabyrinth\\" + filename);
		try (Scanner in = new Scanner (setup)) {
			tester.N = in.nextInt(); // the total number of nodes in the level, including the gateways
			tester.L = in.nextInt(); // the number of links
			tester.E = in.nextInt(); // the number of exit gateways
			tester.edges = new int [tester.N][tester.N]; 
			for (int i = 0; i < tester.L; i++) {
			    int N1 = in.nextInt(); // N1 and N2 defines a link between these nodes
			    int N2 = in.nextInt();
			    tester.edges[N1][N2] = 1;
			    tester.edges[N2][N1] = 1;
			}
			for (int i = 0; i < tester.E; i++) {
			    int EI = in.nextInt(); // the index of a gateway node
			    tester.gateways.add(EI);
			}
			tester.bob = in.nextInt();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return tester;
	}

	@Test
	public void shouldPrintSeveredLink() {
		Player tester = loadState("twoNodes.txt");
		ByteArrayOutputStream testOut = new ByteArrayOutputStream();
		System.setOut(new PrintStream(testOut));
		
		tester.sever(0,1);
		
		assertEquals("0 1\n",testOut.toString());
	}
	
	@Test
	public void shouldUpdateLinksAfterSever() {
		Player tester = loadState("twoNodes.txt");
		assertEquals(1,tester.edges[0][1]);
		
		tester.sever(0,1);
		
		assertEquals(0,tester.edges[0][1]);
	}

	@Test
	public void shouldGetGatewayNodeConnectedToGivenNode() {
		Player tester = loadState("threeNodes.txt");
		
		assertEquals(2,tester.adjacentGateway(0));
	}
	
	@Test
	public void shouldGetDegreeTwoToGatewayNode() {
		Player tester = loadState("doubleGateway.txt");
		tester.findDegreeTwoNodes();
		
		assertTrue(tester.degreeTwoNodes.contains(1));
		assertEquals(1,tester.degreeTwoNodes.size());
	}
	
	@Test
	public void shouldRemoveNodeFromDegreeTwoNodes() {
		Player tester = loadState("doubleGateway.txt");
		tester.findDegreeTwoNodes();
				
		tester.sever(1, 2);
		
		assertTrue(tester.degreeTwoNodes.isEmpty());
	}
	
	@Test
	public void shouldPrioritizeBobnetNodeWhenNextToGateway() {
		Player tester = loadState("threeNodes.txt");
		tester.findDegreeTwoNodes();
		
		assertEquals(0,tester.priorityNode());
	}

	@Test
	public void shouldPrioritizeDegreeTwoNodeWhenNotNextToGateway() {
		Player tester = loadState("doubleGateway.txt");
		tester.findDegreeTwoNodes();
		
		assertEquals(1,tester.priorityNode());
	}
	
	@Test
	public void shouldPrioritizeNodeAdjacentToGateway() {
		Player tester = loadState("threeNodes.txt");
		tester.bob = 1;
		tester.findDegreeTwoNodes();
		
		assertEquals(0,tester.priorityNode());
	}
}
