package ds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import ds.Graph.Edge;
import ds.Graph.Vertex;

@SuppressWarnings("unused")
public class Algo {

	private HashMap<Integer, Vertex> vertices;
	// Maps the optimal path for any given vertex at the end of an iteration
	private HashMap<Integer, ArrayList<Waypoint>> pathMap;
	// Holds a list of all pathMaps							
	private ArrayList<HashMap<Integer, ArrayList<Waypoint>>> results;							 
	private int rounds;
	private double endTime;
	static boolean loggingEnabled = false;
	final private double MAX = Double.POSITIVE_INFINITY; 
	
	// This class keeps track of each step the algorithm takes along the graph
	private class Waypoint {
		private int src;
		private int dest;
		private double time;
		private double sectionTime;
		private double startSpeed;
		private double endSpeed;
		
		private Waypoint(int src, int dest, double sectionTime, double time, double startSpeed, double endSpeed) {
			this.src = src;
			this.dest = dest;
			this.sectionTime = sectionTime;
			this.time = time;
			this.startSpeed = startSpeed;
			this.endSpeed = endSpeed;
		}
	}
	
	public Algo(Graph graph) {
		this.rounds = 0;
		this.endTime = MAX;
		this.vertices = graph.getVertices();
		this.results = new ArrayList<HashMap<Integer, ArrayList<Waypoint>>>();
	}
	
	// Sets the initial values for all nodes
	private void initNodes() {		
		// time[1:end] = inf, speed[1:end] = 0
		for (Map.Entry<Integer, Vertex> en : vertices.entrySet()) {
			en.getValue().setTime(MAX);
			en.getValue().setSpeed(0);
			en.getValue().setTimesVisited(0);
		}
		
		// time[0] = 0, speed[0] = 1
		vertices.get(0).setTime(0);
		vertices.get(0).setSpeed(1);
		vertices.get(0).setTimesVisited(0);
	}
	
	public void shortestPath() {
		int nMax = 1;
		Queue<Vertex> queue = new LinkedList<Vertex>();
		boolean running = true;
		
		while (running) {
			initNodes();
			running = false;
			
			this.pathMap = new HashMap<Integer, ArrayList<Waypoint>>();
			
			// Initially queueing 0
			queue.offer(vertices.get(0));
			pathMap.put(0, new ArrayList<Waypoint>());
			
			while (!queue.isEmpty()) {
				// Retrieving top vertex from queue
				Vertex vertexTop = queue.poll();
				
				// Mark as visited
				vertexTop.setTimesVisited(vertexTop.getTimesVisited() + 1);
				
				// Check all connected vertices ..
				l("========== Checking " + vertexTop.getId() + " ...");
				l("Current time: " + vertexTop.getTime() + "\nCurrent speed: " + vertexTop.getSpeed());
				l("Times visited: " + vertexTop.getTimesVisited());
				
				for(Edge edge : vertexTop.getEdge()) {
					Vertex src = edge.getSrc();
					Vertex dest = edge.getDest();

					l("Checking edge "+src.getId() +" -> " + dest.getId() );
					
					if (dest.getId() != 21) {
						// ... if (either speed OR time improves) AND timesVisited would be <= nMax ...	
						if ((adjustedSpeed(edge) > dest.getSpeed() || adjustedTime(edge) < dest.getTime()) && 
							(dest.getTimesVisited() + 1 <= nMax) && 
							adjustedSpeed(edge) > 0) {
						
							// adjust speed for dest
							vertices.get(dest.getId()).setSpeed(adjustedSpeed(edge));
							
							// adjust time for dest
							vertices.get(dest.getId()).setTime(adjustedTime(edge));
						
							// Only queue vertex if it's not already in queue
							if (!queue.contains(dest)) {
								queue.offer(dest);
								
								l("  Queued " + dest.getId());
							}
							
							l("  Adjusted "+ dest.getId());
							
							// Take the path from src and set it as new path for dest ...
							pathMap.put(dest.getId(), new ArrayList<>(pathMap.get(src.getId())));
							// ... and add src to new path of dest
							pathMap.get(dest.getId()).add(new Waypoint(src.getId(), dest.getId(), travelTime(edge),
									dest.getTime(), src.getSpeed(), dest.getSpeed()));
							
							l("Dest waypoints:");
							pathMap.get(dest.getId()).forEach((x) -> l(x.src +" -> " + x.dest + ", sectionTime = "+ x.sectionTime +", t = " + x.time +", endSpeed = " + x.endSpeed));
				
						}
					} else {
						// On vertex 21 only optimize for time
						if ((adjustedTime(edge) < dest.getTime()) && (dest.getTimesVisited() + 1 <= nMax)) {
							vertices.get(dest.getId()).setSpeed(src.getSpeed());
							vertices.get(dest.getId()).setTime(adjustedTime(edge));
							
							l("  Adjusted "+ dest.getId());
							
							pathMap.put(dest.getId(), pathMap.get(src.getId()));
							pathMap.get(dest.getId()).add(new Waypoint(src.getId(), dest.getId(), travelTime(edge), 
									dest.getTime(), src.getSpeed(), dest.getSpeed()));
							
							l("Dest waypoints:");
							pathMap.get(dest.getId()).forEach((x) -> l(x.src +" -> " + x.dest + ", sectionTime = "+ x.sectionTime +", t = " + x.time +", endSpeed = " + x.endSpeed));
						}
					}
				
					l("  new speed for " + dest.getId() + ": " + dest.getSpeed());
					l("  new time for " + dest.getId() + ": " + dest.getTime());
					
				} // end edge loop
			} // end while (queue is not empty) loop
			
			l("===================== End of Round " + nMax + "==========================================\n");
			
			// If time has improved, do another run where you can visit vertices one more time
			if (vertices.get(21).getTime() < this.endTime) {
				l("Prev time: " + endTime);
				l("Final time: " + vertices.get(21).getTime());
				l("Final path:");
				pathMap.get(21).forEach((x) -> l(x.src +" -> " + x.dest + ", sectionTime = "+ x.sectionTime +", t = " + x.time +", endSpeed = " + x.endSpeed));
				
				this.endTime = vertices.get(21).getTime();
				nMax++;
				running = true;
			}
			results.add(pathMap);
		} // end while(running)
		
		l("Final time: " + vertices.get(21).getTime() + "\nRounds: " + (nMax - 1) + "\n");
		l("Final path:");
		results.get(results.size() - 2).get(21).forEach((x) -> l(x.src +" -> " + x.dest + ", sectionTime = "+ x.sectionTime +", t = " + x.time +", endSpeed = " + x.endSpeed));
		
	}
	
	private double adjustedTime(Edge edge) {
		return travelTime(edge) + edge.getSrc().getTime();
	}
	
	private double travelTime(Edge edge) {
		return edge.getDist() / edge.getSrc().getSpeed();
	}
	
	private double adjustedSpeed(Edge edge) {
		final char op = edge.getDest().getMod().charAt(0);
		final double s = edge.getSrc().getSpeed();

		switch(op) {
		case '-':
			return s - Double.parseDouble(edge.getDest().getMod().substring(1));
		case '+':
			return s + Double.parseDouble(edge.getDest().getMod().substring(1));
		case '*':
			switch(edge.getDest().getMod().charAt(1)) {
				case 's':
					return s * s;
				case '0':
					return s * 0.5;
				default:
					return s * 1000;
			}
		default:
			return 1;
		}
	}
	
	public void run() {
		shortestPath();
		showResult(results.size() - 2);
	}
	
	// Will print the optimal path for vertex 21 after n + 1 iterations.
	// e.g. for the iteration where a vertex could be visited 5 times call with showResult(4)
	public void showResult(int n) {
		p("The shortest path from 0 to 21 is: ");
		ArrayList<Waypoint> map = results.get(n).get(21);
		double finalTime = 0;
		for (int i = 0; i < map.size(); i++) {
			Waypoint c = map.get(i);
			p((i+1) + ") " + c.src + " -> " + c.dest + " with speed " + c.startSpeed + " in " + c.sectionTime + " time. Arrival time: " + c.time);
			finalTime = c.time;
		}
		p("\nTotal time travelled from 0 to 21 is " + finalTime);
	}
	
	public void setLogging(boolean arg) {
		Algo.loggingEnabled = arg;
	}
	
	public void l(Object o) {
		if (loggingEnabled)
			System.out.println(o);
	}
	
	public void p(Object o) {
		System.out.println(o);
	}

	public void logStatus() {
		for (Map.Entry<Integer, Vertex> entry : vertices.entrySet()) {
			l(entry.getKey() + ":");
			for (Edge edge : entry.getValue().getEdge()) {
				l("  --> " + edge.getDest().getId());
				l("\tt = " + edge.getDest().getTime());
				l("\ts = " + edge.getDest().getSpeed());
			}
		}
	}
}
