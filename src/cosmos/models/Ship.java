package cosmos.models;

import java.util.ArrayList;

import com.jogamp.opengl.GL2;

import cosmos.math.Point;
import cosmos.math.Quaternion;
import cosmos.math.Vector;
import cosmos.models.Ship.ShipStatus;
import cosmos.util.Timer;

public class Ship extends LocalizedModel {
	
	public enum ShipStatus {
		SPAWNING,
		IDLE,
		MOVING,
		ATTACKING,
		DIEING
	};
	
	public double length;
	private double _speed;
	private int _hitPoints;
	private int _attack;
	private double _range;
	private boolean _isRotating;
	private boolean _isShooting;
	private Quaternion _pitchRotation;
	private Quaternion _rollRotation;
	private Quaternion _headingRotation;
	private float _pitchDegrees;
	private float _rollDegrees;
	private float _headingDegrees;
	private float _pitchVelocity;
	private float _rollVelocity;
	private float _headingVelocity;
	private float _rotationDegrees;
	private float _rotationVelocity;
	private LocalizedModel _destination;
	private LocalizedModel _source;
	private Point _pointingPosition;
	private ShipStatus _status;
	private ShipClass _shipClass;
	private Species _species;
	private LocalizedModel _objectToAttack;
	private ArrayList<LaserBeam> _lasers;
	private Timer _fireTimer;
	private double _desiredRange;
	
	public Ship(Universe u, Point p, double l, int id, ShipClass sClass, ShipStatus status, Species species) {
		super(u, p, id);
		length = l;
		_isRotating = false;
		_isShooting = false;
		_pitchRotation = new Quaternion();
		_rollRotation = new Quaternion();
		_headingRotation = new Quaternion();
		_pitchDegrees = 0.0f;
		_rollDegrees = 0.0f;
		_headingDegrees = 0.0f;
		_rotationDegrees = 0.0f;
		_pitchVelocity = 0.0f;
		_rollVelocity = 0.0f;
		_headingVelocity = 0.0f;
		_rotationVelocity = 0.0f;
		_isMoving = true;
		_lasers = new ArrayList<LaserBeam>();
		_status = status;
		_hitPoints = sClass.getHitPoints();
		_attack = sClass.getAttack();
		_range = sClass.getRange();
		_shipClass = sClass;
		_species = species;
		_objectToAttack = null;
	}
	
	public void setDestination(LocalizedModel m) {
		_destination = m;
		if (_destination instanceof Planet) {
			Planet p = (Planet) _destination;
			_desiredRange = 2.0*p.getRadius();
		} else if (_destination instanceof Star) {
			Star s = (Star) _destination;
			_desiredRange = 2.0*s.getRadius();
		}
	}
	
	public LocalizedModel getDestination() {
		return _destination;
	}
	
	public void setSource(LocalizedModel m) {
		_source = m;
	}
	
	public LocalizedModel getSource() {
		return _source;
	}
	
	public void setSpeed(double speed) {
		_speed = speed;
	}
	
	public double getSpeed() {
		return _speed;
	}
	
	public void setPitchVelocity(float value) {
		_pitchVelocity = value;
	}
	
	public void setRollVelocity(float value) {
		_rollVelocity = value;
	}
	
	public void setHeadingVelocity(float value) {
		_headingVelocity = value;
	}
	
	public void setRotateVelocity(float value) {
		_rotationVelocity = value;
	}
	
	public void point(GL2 gl) {
		
		if (_status == ShipStatus.MOVING) {
			_pointingPosition = _destination.getPosition();
		} else if (_status == ShipStatus.ATTACKING) {
			_pointingPosition = _destination.getPosition();
		}
		
		Vector v = new Vector(_pointingPosition.x - x, _pointingPosition.y - y, _pointingPosition.z - z);
		
		double[] matrix = new double[16];
		Quaternion q = new Quaternion();
		
		//Current normal = normalize(cross(c-a, d-b)) for a quad abcd
		// A = (0, 2, 2)
		// B = (0, 2, -2)
		// C = (0, -2, -2)
		// D = (0, -2, 2)
		
		Vector currentNormal = Vector.getCrossProduct(Vector.getVectorBetween(new Point(0, -2, -2), new Point(0, 2, 2)),
				Vector.getVectorBetween(new Point(0, -2, 2), new Point(0, 2, -2))).getNormalizedVector();
		
		//Rotation axis = normalize(crossproduct(currentNormal, desiredNormal))
		
		Vector rotationAxis = Vector.getCrossProduct(currentNormal, v).getNormalizedVector();
		
		//Rotation angle = acos(dotproduct(normalize(currentNormal), normalize(desiredNormal))
		
		float rotationAngle = (float) Math.acos(Vector.getDotProduct(currentNormal.getNormalizedVector(), v.getNormalizedVector()));
		
		q.createFromAxisAngle(rotationAxis.x, rotationAxis.y, rotationAxis.z, (float) Math.toDegrees(rotationAngle));
		q.createMatrix(matrix);
		gl.glMultMatrixd(matrix, 0);
	}
	
	public void pointToward(GL2 gl, Vector p) {
		double[] matrix = new double[16];
		Quaternion q = new Quaternion();
		
		//Current normal = normalize(cross(c-a, d-b)) for a quad abcd
		// A = (0, 2, 2)
		// B = (0, 2, -2)
		// C = (0, -2, -2)
		// D = (0, -2, 2)
		
		Vector currentNormal = Vector.getCrossProduct(Vector.getVectorBetween(new Point(0, -2, -2), new Point(0, 2, 2)),
				Vector.getVectorBetween(new Point(0, -2, 2), new Point(0, 2, -2))).getNormalizedVector();
		
		//Rotation axis = normalize(crossproduct(currentNormal, desiredNormal))
		
		Vector rotationAxis = Vector.getCrossProduct(currentNormal, p).getNormalizedVector();
		
		//Rotation angle = acos(dotproduct(normalize(currentNormal), normalize(desiredNormal))
		
		float rotationAngle = (float) Math.acos(Vector.getDotProduct(currentNormal.getNormalizedVector(), p.getNormalizedVector()));
		
		q.createFromAxisAngle(rotationAxis.x, rotationAxis.y, rotationAxis.z, (float) Math.toDegrees(rotationAngle));
		q.createMatrix(matrix);
		gl.glMultMatrixd(matrix, 0);
	}
	
	public void rotate(GL2 gl, Vector axis) {
		double[] matrix = new double[16];
		Quaternion q = new Quaternion();
		
		_rotationDegrees += _rotationVelocity;
		
		q.createFromAxisAngle(axis.x, axis.y, axis.z, _rotationDegrees);
		q.createMatrix(matrix);
		gl.glMultMatrixd(matrix, 0);
	}
	
	public void rotate(GL2 gl) {
		
		double[] matrix = new double[16];
		Quaternion q;
		
		_pitchDegrees += _pitchVelocity;
		_headingDegrees += _headingVelocity;
		_rollDegrees += _rollVelocity;

		// Make the Quaternions that will represent our rotations
		_pitchRotation.createFromAxisAngle(1.0, 0.0, 0.0, _pitchDegrees);
		_headingRotation.createFromAxisAngle(0.0, 1.0, 0.0, _headingDegrees);
		_rollRotation.createFromAxisAngle(0.0, 0.0, 1.0, _rollDegrees);
		
		// Combine the pitch and heading rotations and store the results in q
		q = _pitchRotation.multiply(_headingRotation);
		q = q.multiply(_rollRotation);
		q.createMatrix(matrix);

		// Let OpenGL set our new prespective on the world!
		gl.glMultMatrixd(matrix, 0);
		
	}
	
	public boolean isRotating() {
		return _isRotating;
	}
	
	public void setRotating(boolean value) {
		_isRotating = value;
	}
	
	public void move() {
		
		if ((_status != ShipStatus.MOVING) && (_status != ShipStatus.ATTACKING)) {
			return;
		}
		
		if (this.getDistanceTo(_destination) <= _desiredRange) {
			if (_status == ShipStatus.MOVING) {
				_status = ShipStatus.IDLE;
			}
			return;
		}
		
		_pointingPosition = _destination.getPosition();
		Vector v = new Vector(_destination.x - x, _destination.y - y, _destination.z - z);
		Vector normalizedDirection = v.getNormalizedVector();
		
		double testx = x + _speed*normalizedDirection.x;
		double testy = y + _speed*normalizedDirection.y;
		double testz = z + _speed*normalizedDirection.z;
		
		double dx = _destination.x - testx;
		double dy = _destination.y - testy;
		double dz = _destination.z - testz;
		double distance = Math.sqrt(dx*dx + dy*dy + dz*dz);
		
		if (distance < _desiredRange) {
			
			x = _destination.x - _desiredRange*normalizedDirection.x;
			y = _destination.y - _desiredRange*normalizedDirection.y;
			z = _destination.z - _desiredRange*normalizedDirection.z;
			_speed = 0.0;
			
			if (_status == ShipStatus.MOVING) {
				_status = ShipStatus.IDLE;
			}
			
		} else {
			
			x = testx;
			y = testy;
			z = testz;
			
		}
		
	}
	
	public void moveToward(Vector v) {
		Vector normalizedDirection = v.getNormalizedVector();

		if (_status == ShipStatus.ATTACKING) {

			LocalizedModel obj = _objectToAttack;
			
			double testx = x + _speed*normalizedDirection.x;
			double testy = y + _speed*normalizedDirection.y;
			double testz = z + _speed*normalizedDirection.z;
			
			double dx = obj.x - testx;
			double dy = obj.y - testy;
			double dz = obj.z - testz;
			double newDist = Math.sqrt(dx*dx + dy*dy + dz*dz);
			
			if (newDist < _range) {
				
				x = _objectToAttack.x - _range*normalizedDirection.x;
				y = _objectToAttack.y - _range*normalizedDirection.y;
				z = _objectToAttack.z - _range*normalizedDirection.z;
				_speed = 0.0;
				
			} else {
				x = testx;
				y = testy;
				z = testz;
			}
		} else {
			
			if ((_destination instanceof Planet) || (_destination instanceof Star)) {
				
				double testx = x + _speed*normalizedDirection.x;
				double testy = y + _speed*normalizedDirection.y;
				double testz = z + _speed*normalizedDirection.z;
				
				double dx = _destination.x - testx;
				double dy = _destination.y - testy;
				double dz = _destination.z - testz;
				double newDist = Math.sqrt(dx*dx + dy*dy + dz*dz);
				
				if (newDist < _desiredRange) {
					x = _destination.x - _desiredRange*normalizedDirection.x;
					y = _destination.y - _desiredRange*normalizedDirection.y;
					z = _destination.z - _desiredRange*normalizedDirection.z;
					_speed = 0.0;
					_status = ShipStatus.IDLE;
				} else {
					x = testx;
					y = testy;
					z = testz;
				}
				
			} else {
				x += _speed*normalizedDirection.x;
				y += _speed*normalizedDirection.y;
				z += _speed*normalizedDirection.z;
			}
		}
		
	}
	
	public void setPointingPosition(Point p) {
		_pointingPosition = p;
	}
	
	public Point getPointingPosition() {
		return _pointingPosition;
	}
	
	public void updateLasers(double time) {
		
		if ((_status == ShipStatus.ATTACKING) && isInRange()) {
			if (_fireTimer == null) {
				Vector shootingDirection = new Vector(_pointingPosition.x - x, _pointingPosition.y - y, _pointingPosition.z - z);
				_lasers.add(new LaserBeam(this, this.getPosition(), shootingDirection.getNormalizedVector(), 4.0, 0.05, 4.0f, _range, new Color(255, 255, 255)));
				_fireTimer = new Timer();
			} else if (_fireTimer.getTimeSinceReset() >= _shipClass.getRateOfFire()){
				Vector shootingDirection = new Vector(_pointingPosition.x - x, _pointingPosition.y - y, _pointingPosition.z - z);
				_lasers.add(new LaserBeam(this, this.getPosition(), shootingDirection.getNormalizedVector(), 4.0, 0.05, 4.0f, _range, new Color(255, 255, 255)));
				_fireTimer.resetTime();
			}
		} else {
			_fireTimer = null;
		}
		
		for (int i = 0; i < _lasers.size(); i++) {
			_lasers.get(i).update(time);
			if (_lasers.get(i).atMaxRange()) {
				_lasers.remove(i);
				i--;
			}
		}
		
	}
	
	public void setShooting(boolean value) {
		_isShooting = value;
	}
	
	public boolean isShooting() {
		return _isShooting;
	}
	
	public ShipStatus getStatus() {
		return _status;
	}
	
	public void setStatus(ShipStatus status) {
		_status = status;
	}
	
	public ShipClass getShipClass() {
		return _shipClass;
	}
	
	public void setHitPoints(int hitPoints) {
		_hitPoints = hitPoints;
	}
	
	public int getHitPoints() {
		return _hitPoints;
	}
	
	public void setAttack(int attack) {
		_attack = attack;
	}
	
	public int getAttack() {
		return _attack;
	}
	
	public Species getSpecies() {
		return _species;
	}
	
	public double getRange() {
		return _range;
	}
	
	public void setRange(double range) {
		_range = range;
	}
	
	public LocalizedModel getObjectToAttack() {
		return _objectToAttack;
	}
	
	public void setObjectToAttack(LocalizedModel obj) {
		_objectToAttack = obj;
	}
	
	public int getNumLasers() {
		return _lasers.size();
	}
	
	public LaserBeam getLaser(int i) {
		return _lasers.get(i);
	}
	
	public double getDesiredRange() {
		return _desiredRange;
	}
	
	public void setDesiredRange(double desiredRange) {
		_desiredRange = desiredRange;
	}
	
	public double getDistanceToObjectToAttack() {
		double dx = _objectToAttack.x - x;
		double dy = _objectToAttack.y - y;
		double dz = _objectToAttack.z - z;
		return Math.sqrt(dx*dx + dy*dy + dz*dz);
	}
	
	public boolean isInRange() {
		return (getDistanceToObjectToAttack() <= (_range + 1e-6));
	}

}
