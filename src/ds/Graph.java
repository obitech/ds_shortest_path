package ds;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


/**
 * Graph is stored as HashMap with key = id, value = vertex
 * Vertex contains ArrayList of specific edges
 * Edge stores src, dest, dist
 */

public class Graph {
	
	private HashMap<Integer, Vertex> vertices;

	public Graph() {
		vertices = new HashMap<Integer, Vertex>();
	}
	
	public class Vertex {
		private final int 		id;
		private String 			mod;
		private int 			timesVisited;
		private double	 		speed;
		private double 			time;
		private ArrayList<Edge> edge;
		
		public ArrayList<Edge> getEdge() {
			return edge;
		}

		public Vertex(int id) {
			this.id = id;
			this.edge = new ArrayList<Edge>();
		}
		
		public String getMod() {
			return mod;
		}
		
		public int getId() {
			return id;
		}
		
		public void setMod(String mod) {
			this.mod = mod;
		}

		public int getTimesVisited() {
			return timesVisited;
		}

		public void setTimesVisited(int timesVisited) {
			this.timesVisited = timesVisited;
		}

		public double getSpeed() {
			return speed;
		}

		public void setSpeed(double speed) {
			this.speed = speed;
		}

		public double getTime() {
			return time;
		}

		public void setTime(double time) {
			this.time = time;
		}
	}

	public class Edge {
		private Vertex src;
		private Vertex dest;
		private int dist;
		
		private Edge(Vertex src, Vertex dest, int dist) {
			this.src = src;
			this.dest = dest;
			this.dist = dist;
		}

		public Vertex getSrc() {
			return src;
		}

		public Vertex getDest() {
			return dest;
		}

		public int getDist() {
			return dist;
		}
	}

	public HashMap<Integer,Vertex> getVertices() {
		return this.vertices;
	}
	
	public Vertex getVertex(int data) {
		return vertices.get(data);
	}
	
	// Read vertices from file
	public boolean readVertices() {
		try {
			InputStream in = getClass().getResourceAsStream("/util/nodes.csv");
			Scanner scanner = new Scanner(new InputStreamReader(in));
			scanner.useDelimiter(";|\\n");
			scanner.nextLine();
			
			while(scanner.hasNext()) {
				int id = Integer.parseInt(scanner.next());
				Vertex vert = new Vertex(id);
				vert.mod = scanner.next();
				vertices.put(vert.id, vert);
			}

			vertices.get(0).mod = "=1";
			vertices.get(21).mod = "*1";
			
			scanner.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	// Read edges from file
	public boolean readEdges() {
		try {
			InputStream in = getClass().getResourceAsStream("/util/arces.csv");
			Scanner scanner = new Scanner(new InputStreamReader(in));
			scanner.useDelimiter(";|\\n");
			scanner.nextLine();

			while(scanner.hasNext()) {
				Vertex src = vertices.get(Integer.parseInt(scanner.next()));
				Vertex dest = vertices.get(Integer.parseInt(scanner.next()));
				int dist = Integer.parseInt(scanner.next());
				src.edge.add(new Edge(src, dest, dist));
			}
			
			scanner.close();
			return true;
		} catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	public void printVertices() {
		for (Map.Entry<Integer, Vertex> entry : vertices.entrySet())
			System.out.println("ID: " + entry.getKey() + ", mod: " + entry.getValue().mod);
	}
	
	public void printEdge(int id) {
		vertices.get(id).edge.forEach((x) -> System.out.println(x.src.id +" -> " + x.dest.id + ", dist: " + x.dist));
	}
}
