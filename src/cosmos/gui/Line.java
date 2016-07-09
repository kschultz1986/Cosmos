package cosmos.gui;

import cosmos.math.Vertex;
import cosmos.models.Color;

public class Line {
	private Vertex p1;
	private Vertex p2;
	private Color color;
	
	Line(Vertex p1, Vertex p2, Color color) {
		this.p1 = p1;
		this.p2 = p2;
		this.setColor(color);
	}

	public Vertex getPoint1() {
		return p1;
	}
	
	public Vertex getPoint2() {
		return p2;
	}

	public void setPoint1(Vertex p) {
		p1 = p;
	}
	
	public void setPoint2(Vertex p) {
		p2 = p;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

}