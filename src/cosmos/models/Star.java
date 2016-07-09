package cosmos.models;

import java.util.ArrayList;

import cosmos.math.Point;

public class Star extends LocalizedModel {
	
	private double _radius;
	public Color color;
	private String _name = null;
	private ArrayList<Planet> _planets;

	public Star(Universe u, Point p, double radius, int id, String name) {
		super(u, p, id);
		_radius = radius;
		color = Color.getRandom();
		_name = name;
		_planets = new ArrayList<Planet>();
	}
	
	public void addPlanet(Planet p) {
		_planets.add(p);
	}
	
	public int getNumPlanets() {
		return _planets.size();
	}
	
	public Planet getPlanet(int i) {
		return _planets.get(i);
	}
	
	public String getName() {
		return _name;
	}
	
	public double getRadius() {
		return _radius;
	}

}
