package cosmos.models;

public class Species {
	
	private String _nameSingular;
	private String _namePlural;
	private double _minTemperature;
	private double _maxTemperature;
	private double _preferredTemperature;
	
	public Species() {
		
	}
	
	public Species(String nameSingular, String namePlural) {
		_nameSingular = nameSingular;
		_namePlural = namePlural;
	}
	
	public String getNameSingular() {
		return _nameSingular;
	}

	public void setNameSingular(String nameSingular) {
		_nameSingular = nameSingular;
	}

	public String getNamePlural() {
		return _namePlural;
	}

	public void setNamePlural(String namePlural) {
		_namePlural = namePlural;
	}

	public double getMinTemperature() {
		return _minTemperature;
	}

	public void setMinTemperature(double minTemperature) {
		_minTemperature = minTemperature;
	}

	public double getMaxTemperature() {
		return _maxTemperature;
	}

	public void setMaxTemperature(double maxTemperature) {
		_maxTemperature = maxTemperature;
	}

	public double getPreferredTemperature() {
		return _preferredTemperature;
	}

	public void setPreferredTemperature(double preferredTemperature) {
		_preferredTemperature = preferredTemperature;
	}

}
