package cosmos.math;

public class Vertex {

	private double x;
	private double y;
	private double z;

	public Vertex() {
		this(0.0, 0.0, 0.0);
	}

	public Vertex(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public double getZ() {
		return z;
	}
	
	public void setX(double d) {
		this.x = d;
	}
	
	public void setY(double d) {
		this.y = d;
	}
	
	public void setZ(double d) {
		this.z = d;
	}
	
	public Boolean equals(Vertex v) {
		return (x == v.x && y == v.y && z == v.z);
	}
	
	public double calculateDistance(Vertex v) {
		return Math.sqrt(Math.pow(x - v.x, 2) + Math.pow(y - v.y, 2) + Math.pow(z - v.z, 2));
	}
	
	public static double calculateDistance(Vertex v1, Vertex v2) {
		return Math.sqrt(Math.pow(v1.x - v2.x, 2) + Math.pow(v1.y - v2.y, 2) + Math.pow(v1.z - v2.z, 2));
	}
	
	public String getString() {
		return new String(String.format("(%.2f,%.2f,%.2f)", x, y, z));
	}

}
