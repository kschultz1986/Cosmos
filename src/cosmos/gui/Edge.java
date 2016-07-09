package cosmos.gui;

import java.util.ArrayList;

import cosmos.math.Vertex;


public class Edge {
	private Vertex v1;
	private Vertex v2;
	private ArrayList<Face> faces;
	
	Edge(Vertex v1, Vertex v2) {
		this.v1 = v1;
		this.v2 = v2;
		faces = new ArrayList<Face>();
	}
	
	public Vertex getVertex1() {
		return v1;
	}
	
	public void setVertex1(Vertex v) {
		v1 = v;
	}
	
	public Vertex getVertex2() {
		return v2;
	}
	
	public void setVertex2(Vertex v) {
		v2 = v;
	}
	
	public Boolean equals(Edge e) {
		return (v1 == e.getVertex1() && v2 == e.getVertex2());
	}
	
	public void addToFace(Face f) {
		faces.add(f);
	}
	
	public Face getFace(int i) {
		return faces.get(i);
	}
	
	public int getNumFaces() {
		return faces.size();
	}
	
	public Boolean hasVertex(Vertex v) {
		return (v1.equals(v) || v2.equals(v));
	}
}
