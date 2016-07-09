package cosmos.models;

import java.nio.ByteBuffer;

import cosmos.math.Point;

public abstract class LocalizedModel extends Model {
	
	public double x = 0.0;
	public double y = 0.0;
	public double z = 0.0;
	private int _id = -1;
	public boolean _isMoving;
	public Universe _u;
	
	public LocalizedModel(Universe u, Point p, int id) {
		x = p.x;
		y = p.y;
		z = p.z;
		_id = id;
		_isMoving = false;
		_u = u;
	}
	
	public String getString() {
		return String.format("(%.2f,%.2f,%.2f)", x, y, z);
	}
	
	public static double GetDistance(LocalizedModel p1, LocalizedModel p2) {
		return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2) + Math.pow(p1.z - p2.z, 2));
	}
	
	public double getDistanceTo(LocalizedModel obj) {
		double dx = obj.x - x;
		double dy = obj.y - y;
		double dz = obj.z - z;
		return Math.sqrt(dx*dx + dy*dy + dz*dz);
	}
	
	public double getDistanceTo(Point p) {
		double dx = p.x - x;
		double dy = p.y - y;
		double dz = p.z - z;
		return Math.sqrt(dx*dx + dy*dy + dz*dz);
	}
	
	public double getAzimuthTo(LocalizedModel m) {
		return Math.atan2(z - m.z, m.y - y);
	}
	
	public double getInclinationTo(LocalizedModel m) {
		double slantRange = Math.sqrt(Math.pow(m.x - x, 2) + Math.pow(m.z - z, 2));
		return Math.atan2(m.y - y, slantRange);
	}
	
	public double getXRotationTo(LocalizedModel m) {
		return Math.atan2(y - m.y, m.z - z);
	}
	
	public double getYRotationTo(LocalizedModel m) {
		return Math.atan2(m.z - z, x - m.x);
	}
	
	public double getZRotationTo(LocalizedModel m) {
		return Math.atan2(y - m.y, x - m.x);
	}
	
	public int getID() {
		return _id;
	}
	
	public Color getColorID() {
		
		if (_id == -1)
			return new Color(0, 0, 0);
		
		ByteBuffer b = ByteBuffer.allocate(4);
		b.putInt(_id);
		byte[] arr = b.array();
		return new Color(Byte.toUnsignedInt(arr[1]), Byte.toUnsignedInt(arr[2]), Byte.toUnsignedInt(arr[3]));
		
	}
	
	public String getColorIDString() {
		Color c = getColorID();
		return String.format("%d %d %d", c.r, c.g, c.b);
	}
	
	public String getName() {
		if (this instanceof Star) {
			return ((Star) this).getName();
		}
		else if (this instanceof Planet){
			return ((Planet) this).getName();
		}
		else {
			return "";
		}
	}
	
	public Point getPosition() {
		return new Point(x, y, z);
	}
	
	public boolean isMoving() {
		return _isMoving;
	}
	
	public Universe getUniverse() {
		return _u;
	}
	
}
