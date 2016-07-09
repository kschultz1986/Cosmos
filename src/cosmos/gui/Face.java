package cosmos.gui;

import java.util.ArrayList;

import cosmos.math.Vertex;


public class Face {
	private ArrayList<Edge> edges;
	
	Face() {
		edges = new ArrayList<Edge>();
	}
	
	Face(Edge e1, Edge e2, Edge e3) {
		this();
		edges.add(e1);
		edges.add(e2);
		edges.add(e3);
	}
	
	public void addEdge(Edge e) {
		edges.add(e);
	}
	
	public Edge getEdge(int i) {
		return edges.get(i);
	}
	
	public Boolean equals(Face f) {
		Vertex a1 = getEdge(0).getVertex1();
		Vertex a2 = f.getEdge(0).getVertex1();
		Vertex b1 = getEdge(1).getVertex1();
		Vertex b2 = f.getEdge(1).getVertex1();
		Vertex c1 = getEdge(2).getVertex1();
		Vertex c2 = f.getEdge(2).getVertex1();
		
		if (a1.equals(a2) && b1.equals(b2) && c1.equals(c2)) {
			return true;
		}
		else if (b1.equals(a2) && c1.equals(b2) && a1.equals(c2)) {
			return true;
		}
		else if (c1.equals(a2) && a1.equals(b2) && b1.equals(c2)) {
			return true;
		}
		else if (a1.equals(a2) && c1.equals(b2) && b1.equals(c2)) {
			return true;
		}
		else if (b1.equals(a2) && a1.equals(b2) && c1.equals(c2)) {
			return true;
		}
		else if (c1.equals(a2) && b1.equals(b2) && a1.equals(c2)) {
			return true;
		}
		else {
			return false;
		}
		
	}
	
	public void printVertices() {
		for (int i = 0; i < edges.size(); i++) {
			System.out.print("(" + edges.get(i).getVertex1().getX() + " " + edges.get(i).getVertex1().getY() + " " + edges.get(i).getVertex1().getZ() + ")");
		}
		System.out.println("");
	}
	
}
