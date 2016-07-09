package cosmos.net;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import cosmos.math.Point;
import cosmos.models.Planet;
import cosmos.models.Player;
import cosmos.models.Ship;
import cosmos.models.Universe;

public class ServerThread extends Thread {

	private DatagramSocket _socket = null;
	private int _rxPort = 30000;
	private byte[] _rxBuffer;
	private byte[] _txBuffer;
	private ConnectionState _connectionState;
	private final float UPDATE_PERIOD = 0.1f;
	private Universe _u;
	
	public ServerThread() throws IOException {
		super();
		_socket = new DatagramSocket(_rxPort);
		_rxBuffer = new byte[256];
		_txBuffer = new byte[256];
	}
	
	public void initializeGame() throws IOException {
		_u = new Universe();
		_u.addPlayer("Humans", "127.0.0.1", 30000);
		
		// Create ship for testing
		Ship sh = new Ship(new Point(0.0, 0.0, 0.0), 10.0, _u.getNumObjects() + 1);
		Planet closest = _u.getClosestPlanet(sh);
		closest.setPopulation(9000000000L);
		sh.setSpeed(0.1);
		sh.setDestination(closest);
		_u.addShip(sh);
	}
	
	public void receivePlayerInputs() {
//		DatagramPacket packet = new DatagramPacket(_rxBuffer, _rxBuffer.length);
//		try {
//			_socket.receive(packet);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		InetAddress address = packet.getAddress();
//		int port = packet.getPort();
//		try {
//			System.out.println("Rx " + System.currentTimeMillis() + " " + new String(_rxBuffer, "UTF-8"));
//		} catch (UnsupportedEncodingException e2) {
//			// TODO Auto-generated catch block
//			e2.printStackTrace();
//		}
	}
	
	public void updateGameState() {
		_u.updateObjects(UPDATE_PERIOD);
	}
	
	public void transmitInitialGameState() {
		
		// Loop through players
		for (int i = 0; i < _u.getNumPlayers(); i++) {
			
			Player p = _u.getPlayer(i);
			ByteBuffer b = ByteBuffer.allocate
			
			// Loop through objects
			for (int j = 0; j < _u.getNumObjects(); j++) {
				
				
				
				DatagramPacket packet = new DatagramPacket(_txBuffer, _txBuffer.length, p.getIPAddress(), p.getPort());
				
				
			}
			
		}
		
	}
	
	public void transmitGameState() {
		
		// Loop through players
		for (int i = 0; i < _u.getNumPlayers(); i++) {
			
			Player p = _u.getPlayer(i);
			
			// Get objects affecting player - assume for now this is only the test ship
		}
		
		// Figure out response
		String response = null;
		try {
			response = new String (_rxBuffer, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//response = response.substring(0, 255);
		_txBuffer = response.getBytes();
		
		// Send the response to the client at "address" and "port"
		packet = new DatagramPacket(_txBuffer, _txBuffer.length, address, port);
		try {
			_socket.send(packet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void run() {
		
		// Initialize the game
		
		try {
			initializeGame();
		} catch (IOException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		
		// Transmit initial game state
		transmitInitialGameState();
		
		// Start listening
		
		boolean done = false;
		_connectionState = ConnectionState.LISTENING;
		
		
		// If time step has elapsed
		while (!done) {
			
			// Update game state
			updateGameState();
			
			// Transmit game state to players
			transmitGameState();
			
			// Receive player inputs
			receivePlayerInputs();
			
		}
		
		_socket.close();
		
	}
	
	boolean isConnecting() {
		return _connectionState == ConnectionState.CONNECTING;
	}
	
	boolean connectFailed() {
		return _connectionState == ConnectionState.CONNECT_FAIL;
	}
	
	boolean isConnected() {
		return _connectionState == ConnectionState.CONNECTED;
	}
	
	boolean isListening() {
		return _connectionState == ConnectionState.LISTENING;
	}
	
}
