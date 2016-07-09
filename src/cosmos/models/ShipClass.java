package cosmos.models;

public class ShipClass {

	private int _hitPoints;
	private int _attack;
	private double _speed;
	private double _length;
	private double _range;
	private double _rateOfFire; // in seconds per shot
	
	public ShipClass(double length, int hitPoints, int attack, double speed, double range, double rateOfFire) {
		_length = length;
		_hitPoints = hitPoints;
		_attack = attack;
		_speed = speed;
		_range = range;
		_rateOfFire = rateOfFire;
	}
	
	public double getLength() {
		return _length;
	}
	
	public int getHitPoints() {
		return _hitPoints;
	}
	
	public int getAttack() {
		return _attack;
	}
	
	public double getSpeed() {
		return _speed;
	}
	
	public double getRange() {
		return _range;
	}
	
	public double getRateOfFire() {
		return _rateOfFire;
	}
	
}
