package cosmos.models;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Player {

	private Species _species;
	private InetAddress _ipAddress = null;
	private int _port = -1;
	
	public Player(String ipAddress, int port) throws UnknownHostException {
		_ipAddress = InetAddress.getByName(ipAddress);
		_port = port;
	}
	
	public Player() {
		
	}

	public Species getSpecies() {
		return _species;
	}
	
	public InetAddress getIPAddress() {
		return _ipAddress;
	}
	
	public int getPort() {
		return _port;
	}

	public void setSpecies(Species species) {
		_species = species;
	}
	
}
