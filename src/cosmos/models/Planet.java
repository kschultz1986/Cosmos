package cosmos.models;

import cosmos.math.Point;

public class Planet extends LocalizedModel {

	private double _radius;
	public Color color;
	private Star _star;
	private long _population;
	private String _name;
	private Species _species;
	
	public Planet(Universe u, Point p, float radius, int id, String name) {
		super(u, p, id);
		_star = null;
		_radius = radius;
		color = Color.getRandom();
		_name = name;
	}
	
	public void addToStar(Star s) {
		_star = s;
	}
	
	public Star getStar() {
		return _star;
	}
	
	public void setName(String name) {
		_name = name;
	}
	
	public String getName() {
		return _name;
	}
	
	public void setPopulation(long population) {
		_population = population;
	}
	
	public long getPopulation() {
		return _population;
	}
	
	public double getRadius() {
		return _radius;
	}
	
	public Species getSpecies() {
		return _species;
	}
	
	public void setSpecies(Species s) {
		_species = s;
	}

}
