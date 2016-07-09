package cosmos.models;

import cosmos.math.Point;
import cosmos.math.Vector;

public class Projectile extends LocalizedModel {
	
	protected double _speed;
	protected double _range;
	protected Point _origin;
	protected Vector _direction;
	
	public Projectile(Universe u, double speed, double range, Point origin, Vector direction) {
		super(u, origin, -1);
		_speed = speed;
		_range = range;
		_origin = origin;
		_direction = direction;
		// TODO Auto-generated constructor stub
	}
	
	public double getSpeed() {
		return _speed;
	}
	
	public double getRange() {
		return _range;
	}
	
	public Point getOrigin() {
		return _origin;
	}
	
	public Vector getDirection() {
		return _direction;
	}

}
