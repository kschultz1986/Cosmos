package cosmos.gui;

import cosmos.math.Vertex;

public class Triangle {
	private Vertex[] points = new Vertex[3];
	
	Triangle(Vertex p1, Vertex p2, Vertex p3) {
		points[0] = p1;
		points[1] = p2;
		points[2] = p3;
	}

	public Vertex getPoint(int i) {
		return points[i];
	}

	public void setPoint(int i, Vertex p) {
		points[i] = p;
	}

}
