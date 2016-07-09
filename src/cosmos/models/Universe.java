package cosmos.models;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;

import cosmos.math.Point;
import cosmos.math.Utilities;
import cosmos.math.Vector;
import cosmos.models.Planet;
import cosmos.models.Ship.ShipStatus;

public class Universe extends Model {
	
	private double _size;
	private int _numStars;
	private int _numPlanets;
	private int _numShips;
	private float _minStarRadius;
	private float _maxStarRadius;
	private float _minPlanetRadius;
	private float _maxPlanetRadius;
	private double _minPlanetDistanceFromStar;
	private double _maxPlanetDistanceFromStar;
	private String _planetNamesFile;
	private ArrayList<String> _planetNames;
	private ArrayList<LocalizedModel> _objects;
	private ArrayList<Player> _players;
	private ArrayList<Species> _species;
	private ArrayList<ShipClass> _shipClasses;
	//private static Random _rand;
	private static int _lastId;
	
	public Universe() throws IOException {
		
		Properties prop = new Properties();
		
		try {
			prop.load(getClass().getResourceAsStream("/resources/Universe.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		_lastId = -1;
		_size = Double.parseDouble(prop.getProperty("size"));
		_numStars = Integer.parseInt(prop.getProperty("numStars"));
		_numPlanets = Integer.parseInt(prop.getProperty("numPlanets"));
		_minStarRadius = Float.parseFloat(prop.getProperty("minStarRadius"));
		_maxStarRadius = Float.parseFloat(prop.getProperty("maxStarRadius"));
		_minPlanetRadius = Float.parseFloat(prop.getProperty("minPlanetRadius"));
		_maxPlanetRadius = Float.parseFloat(prop.getProperty("maxPlanetRadius"));
		_minPlanetDistanceFromStar = Double.parseDouble(prop.getProperty("minPlanetDistanceFromStar"));
		_maxPlanetDistanceFromStar = Double.parseDouble(prop.getProperty("maxPlanetDistanceFromStar"));
		_planetNamesFile = prop.getProperty("planetNamesFile");
		
		_species = new ArrayList<Species>();
		_shipClasses = new ArrayList<ShipClass>();
		_numShips = 0;
		
		// Load Species .properties files
		try {

			// TODO: Dynamically load arbitrary Species .properties files
			prop.load(getClass().getResourceAsStream("/resources/Species/Human.properties"));
			Species speciesHuman = new Species();
			speciesHuman.setNameSingular(prop.getProperty("nameSingular"));
			speciesHuman.setNamePlural(prop.getProperty("namePlural"));
			speciesHuman.setMinTemperature(Double.parseDouble(prop.getProperty("minTemperature")));
			speciesHuman.setMaxTemperature(Double.parseDouble(prop.getProperty("maxTemperature")));
			speciesHuman.setPreferredTemperature(Double.parseDouble(prop.getProperty("preferredTemperature")));
			_species.add(speciesHuman);
			
			prop.load(getClass().getResourceAsStream("/resources/Species/Bliger.properties"));
			Species speciesBliger = new Species();
			speciesBliger.setNameSingular(prop.getProperty("nameSingular"));
			speciesBliger.setNamePlural(prop.getProperty("namePlural"));
			speciesBliger.setMinTemperature(Double.parseDouble(prop.getProperty("minTemperature")));
			speciesBliger.setMaxTemperature(Double.parseDouble(prop.getProperty("maxTemperature")));
			speciesBliger.setPreferredTemperature(Double.parseDouble(prop.getProperty("preferredTemperature")));
			_species.add(speciesBliger);
 
		} catch (IOException e) {
			System.out.println("Exception: " + e);
		}
		
		// Load Planet Names file
		loadPlanetNames();
		
		//_rand = new Random();
		
		_objects = new ArrayList<LocalizedModel>();
		
		// Create stars
		for (int i = 0; i < _numStars; i++) {
			_objects.add(createRandomStar(i));
		}
		
		// Create planets
		for (int i = 0; i < _numPlanets; i++) {
			int starIndex = Utilities.randInt(0, _numStars);
			Planet p = createRandomPlanet((Star) _objects.get(starIndex), _numStars + i);
			p.setSpecies(_species.get(i % _species.size()));
			_objects.add(p);
			((Star) _objects.get(starIndex)).addPlanet(p);
			p.addToStar((Star) _objects.get(starIndex));
		}
		
		// Create ship
		
		for (int i = 0; i < getNumObjects(); i++) {
			printObject(i);
		}
		
		_lastId = getNumObjects();
		
		// Create a ship class with length 10
		// TODO: Add more detail; load this from another properties file
		_shipClasses.add(new ShipClass(0.5, 20, 1, 0.1, 3.0, 0.5));
		
		// Create players
//		_players = new ArrayList<Player>();
//		Player p1 = new Player();
//		p1.setSpecies(_species.get(0));
//		_players.add(p1);

	}
	
	public Star createRandomStar(int id) {
		
		// Get random name out of the list, and remove it so it can't be reused later
		int nameIndex = Utilities.randInt(0, _planetNames.size());
		String name = _planetNames.get(nameIndex);
		_planetNames.remove(nameIndex);
		
		return new Star(this, 
			new Point(
				Utilities.randDouble(-_size, _size),
				Utilities.randDouble(-_size, _size),
				Utilities.randDouble(-_size, _size)),
			Utilities.randFloat(_minStarRadius, _maxStarRadius),
			id+1,
			name);
	}
	
	public Planet createRandomPlanet(Star s, int id) {
		
		double az = Utilities.randDouble(0.0, 2*Math.PI);
		double el = Utilities.randDouble(-0.5*Math.PI, 0.5*Math.PI);
		float radius = Utilities.randFloat(_minPlanetRadius, _maxPlanetRadius);
		double minDistance = _minPlanetDistanceFromStar + s.getRadius() + radius;
		double maxDistance = _maxPlanetDistanceFromStar + s.getRadius() + radius;
		double dist = Utilities.randDouble(minDistance, maxDistance);
		
		// Get random name out of the list, and remove it so it can't be reused later
		int nameIndex = Utilities.randInt(0, _planetNames.size());
		String name = _planetNames.get(nameIndex);
		_planetNames.remove(nameIndex);
		
		return new Planet(this, 
			new Point(
				s.x + dist*Math.sin(el)*Math.cos(az),
				s.y + dist*Math.sin(el)*Math.sin(az),
				s.z + dist*Math.cos(el)),
			radius,
			id+1,
			name);
	}
	
	/**
	 * @return the _size
	 */
	public double getSize() {
		return _size;
	}
	
	/**
	 * @return the _numStars
	 */
	public int getNumStars() {
		return _numStars;
	}
	
	/**
	 * @param _numStars the _numStars to set
	 */
	public void setNumStars(int _numStars) {
		this._numStars = _numStars;
	}
	
	public LocalizedModel getObject(int i) {
		return _objects.get(i);
	}
	
	/**
	 * @return the _numPlanets
	 */
	public int getNumPlanets() {
		return _numPlanets;
	}
	
	/**
	 * @param _numPlanets the _numPlanets to set
	 */
	public void setNumPlanets(int _numPlanets) {
		this._numPlanets = _numPlanets;
	}
	
	public int getNumShips() {
		return _numShips;
	}
	
//	public static double randDouble(double min, double max) {
//		return min + (max - min) * _rand.nextDouble();
//	}
//	
//	public static int randInt(int min, int max) {
//		return _rand.nextInt(max - min) + min;
//	}
//	
//	public static float randFloat(float min, float max) {
//		return min + (max - min) * _rand.nextFloat();
//	}
	
	public Planet getClosestPlanet(LocalizedModel m) {
		
		Planet closestPlanet = null;
		double closestPlanetDistance = Double.MAX_VALUE;
		
		for (int i = 0; i < _objects.size(); i++) {
			
			LocalizedModel obj = getObject(i);
			
			if (obj instanceof Planet) {
				
				Planet p = (Planet) obj;
			
				if (p != m) {
					
					double distance = Planet.GetDistance(m, p);
					
					if (distance < closestPlanetDistance) {
						closestPlanetDistance = distance;
						closestPlanet = p;
					}
				}
			}
		}
		
		return closestPlanet;
		
	}
	
	public Player getPlayer(int i) {
		return _players.get(i);
	}
	
	public int getID(LocalizedModel m) {
		return _objects.indexOf(m);
	}
	
	public void loadPlanetNames() {
		_planetNames = new ArrayList<String>();
		Scanner s = new Scanner(getClass().getResourceAsStream("/resources/" + _planetNamesFile));
		while (s.hasNextLine()){
		    _planetNames.add(s.nextLine());
		}
		s.close();
	}
	
	public int getNumObjects() {
		return _objects.size();
	}
	
	public int getNumPlayers() {
		return _players.size();
	}
	
	public int getNewId() {
		_lastId++;
		return _lastId;
	}
	
	
	
	public void addShip(Point position, LocalizedModel source, ShipClass sClass, ShipStatus status, Species species) {
		Ship s = new Ship(this, position, sClass.getLength(), getNewId(), sClass, status, species);
		s.setSource(source);
		s.setPointingPosition(source.getPosition());
		_objects.add(s);
		_numShips++;
	}
	
//	public Ship createShip(Point position, double length, int id) {
//		Ship s = new Ship(this, position, length, id);
//		s.setSpeed(0.1);
//		return s;
//	}
	
//	public void updateObjects(float updatePeriod) {
//		
//		for (int i = 0; i < getNumObjects(); i++) {
//			
//			LocalizedModel m = getObject(i);
//			
//			if (m.isMoving()) {
//				if (m instanceof Ship) {
//					Ship s = (Ship) m;
//					LocalizedModel dest = s.getDestination();
//					Vector v;
//					if (dest == null) {
//						Point p = s.getSource().getPosition();
//						v = new Vector(s.x - p.x, s.y - p.y, s.z - p.z);
//					} else {
//						v = new Vector(dest.x - s.x, dest.y - s.y, dest.z - s.z);
//					}
//					s.moveToward(v, updatePeriod);
//				}
//			}
//		}
//		
//	}
	
	public void addShip(Ship s) {
		_objects.add(s);
	}
	
	public void addPlayer(String namePlural, String ipAddress, int port) throws UnknownHostException {
		Player p = new Player(ipAddress, port);
		p.setSpecies(getSpeciesByNamePlural(namePlural));
		_players.add(p);
	}
	
	public Species getSpeciesByNamePlural(String namePlural) {
		Species ret = null;
		for (int i = 0; i < _species.size(); i++) {
			if (_species.get(i).getNamePlural().equals(namePlural)) {
				ret = _species.get(i);
			}
		}
		return ret;
	}
	
	public ShipClass getShipClass(int i) {
		return _shipClasses.get(i);
	}
	
	public void printObject(int index) {
		System.out.println(String.format("ID: %d Name: %s ColorID: %s", getObject(index).getID(), getObject(index).getName(), getObject(index).getColorIDString()));
	}
	
}
