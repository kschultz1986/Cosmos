package cosmos.gui;

import cosmos.math.Vertex;
import cosmos.models.Color;

public class Quad {
	private Vertex[] points = new Vertex[4];
	private Color c = null;
	
	Quad(Vertex p1, Vertex p2, Vertex p3, Vertex p4) {
		points[0] = p1;
		points[1] = p2;
		points[2] = p3;
		points[3] = p4;
	}

	public Vertex getPoint(int i) {
		return points[i];
	}

	public void setPoint(int i, Vertex p) {
		points[i] = p;
	}
	
	public Color getColor() {
		return c;
	}
	
	public void setColor(Color c) {
		this.c = c;
	}

}
