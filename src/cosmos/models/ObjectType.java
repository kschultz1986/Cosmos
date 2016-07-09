package cosmos.models;

public enum ObjectType {
	
	STAR(0),
	PLANET(1),
	SHIP(2);
	
	private int _value;
	
	private ObjectType(int value) {
		_value = value;
	}
	
	public int getValue() {
		return _value;
	}
	
}
