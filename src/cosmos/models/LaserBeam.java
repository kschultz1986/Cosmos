package cosmos.models;

import com.jogamp.opengl.GL2;

import cosmos.math.Point;
import cosmos.math.Quaternion;
import cosmos.math.Vector;
import cosmos.models.Ship.ShipStatus;

public class LaserBeam extends Projectile {

	LocalizedModel _source;
	private double _length;
	private float _thickness;
	private Color _color;
	
	public LaserBeam(LocalizedModel source, Point origin, Vector direction, double speed, double length, float thickness, double range, Color c) {
		super(source.getUniverse(), speed, range, origin, direction);
		_source = source;
		_length = length;
		_color = c;
	}
	
	public LocalizedModel getSource() {
		return _source;
	}
	
	public double getLength() {
		return _length;
	}
	
	public float getThickness() {
		return _thickness;
	}
	
	public Color getColor() {
		return _color;
	}
	
	public void point(GL2 gl) {
		
		Vector v = _direction;
		
		double[] matrix = new double[16];
		Quaternion q = new Quaternion();
		
		//Current normal = normalize(cross(c-a, d-b)) for a quad abcd
		// A = (0, 2, 2)
		// B = (0, 2, -2)
		// C = (0, -2, -2)
		// D = (0, -2, 2)
		
		Vector currentNormal = Vector.getCrossProduct(Vector.getVectorBetween(new Point(0, -2, -2), new Point(0, 2, 2)),
				Vector.getVectorBetween(new Point(0, -2, 2), new Point(0, 2, -2))).getNormalizedVector();
		
		Vector rotationAxis = Vector.getCrossProduct(currentNormal, v).getNormalizedVector();
		
		float rotationAngle = (float) Math.acos(Vector.getDotProduct(currentNormal.getNormalizedVector(), v.getNormalizedVector()));
		
		q.createFromAxisAngle(rotationAxis.x, rotationAxis.y, rotationAxis.z, (float) Math.toDegrees(rotationAngle));
		q.createMatrix(matrix);
		gl.glMultMatrixd(matrix, 0);
		
	}
	
	public void update(double time) {
		x += _speed*time*_direction.x;
		y += _speed*time*_direction.y;
		z += _speed*time*_direction.z;
	}
	
	public double getDistanceFromOrigin() {
		double dx = _origin.x - x;
		double dy = _origin.y - y;
		double dz = _origin.z - z;
		return Math.sqrt(dx*dx + dy*dy + dz*dz);
	}
	
	public boolean atMaxRange() {
		return (getDistanceFromOrigin() >= _range);
	}
	
	
	
}
