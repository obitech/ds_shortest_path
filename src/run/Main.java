package run;

import ds.Algo;
import ds.Graph;


public class Main {

	public Main() {
	}

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
	
		Graph graph = new Graph();
		graph.readVertices();
		graph.readEdges();

		Algo algo = new Algo(graph);
		algo.run();
	
		long endTime = System.currentTimeMillis();
		System.out.println("\nProgram finished after " + (endTime - startTime) + "ms.");

	}

}
