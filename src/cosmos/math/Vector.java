package cosmos.math;

public class Vector {

	public double x;
	public double y;
	public double z;

	public Vector() {
		x = y = z = 0.0;
	}
	
	public Vector(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector(Point p) {
		x = p.x;
		y = p.y;
		z = p.z;
	}
	
	public Vector scale(double s) {
		Vector r = new Vector();
		r.x = x * s;
		r.y = y * s;
		r.z = z * s;
		return (r);
	}
	
	public static Vector getVectorBetween(Point p1, Point p2) {
		return new Vector(p2.x - p1.x, p2.y - p1.y, p2.z - p1.z);
	}
	
	public Vector getVectorTo(Point p) {
		return new Vector(p.x - x, p.y - y, p.z - z);
	}
	
	public Vector getNormalizedVector() {
		double mag = Math.sqrt(x*x + y*y + z*z);
		return new Vector(x/mag, y/mag, z/mag);
	}
	
	public void normalize() {
		double mag = Math.sqrt(x*x + y*y + z*z);
		x /= mag;
		y /= mag;
		z /= mag;
	}
	
	public static double getDotProduct(Vector v1, Vector v2) {
		return v1.x*v2.x + v1.y*v2.y + v1.z*v2.z;
	}
	
	public static Vector getCrossProduct(Vector v1, Vector v2) {
		return new Vector(v1.y*v2.z - v1.z*v2.y, v1.z*v2.x - v1.x*v2.z, v1.x*v2.y - v1.y*v2.x);
	}
	
}
