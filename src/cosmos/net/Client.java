package cosmos.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

public class Client {
	
	private DatagramSocket _socket = null;
	private byte[] _rxBuffer;
	private byte[] _txBuffer;
	private InetAddress _destAddress = InetAddress.getByName("127.0.0.1");
	//private InetAddress _destAddress = InetAddress.getByName("45.79.128.130");
	private int _destPort = 30000;
	private String _programName = "Cosmos";
	private int _protocolId;
	private int _sequence;
	private int _ack;
	private int _ackBitfield;
	public final int MAX_SEQUENCE = 255;
	public ConnectionState _connectionState;
	
	public Client() throws IOException {
		
		_protocolId = 1;
		_sequence = 0;
		_ack = 0;
		_ackBitfield = 0;
		_socket = new DatagramSocket();
		long startTime = System.currentTimeMillis();
		
		_connectionState = ConnectionState.CONNECTED;
		
		while (true) {
			
			ByteBuffer b = ByteBuffer.allocate(256);
			
			// Put protocolId
			b.put(_programName.getBytes(Charset.forName("UTF-8")));
			b.putInt(_protocolId);

			// Put sequence number
			b.putInt(_sequence);
			
			// Put ack
			b.putInt(_ack);
			
			// Put ackBitfield
			b.putInt(_ackBitfield);
			
			// Put packet data
			String messageStr = "Message";
			b.put(messageStr.getBytes(Charset.forName("UTF-8")));
		
			// Send packet
			_txBuffer = new byte[256];
			System.arraycopy(b.array(), 0, _txBuffer, 0, b.array().length);
			DatagramPacket packet = new DatagramPacket(_txBuffer, _txBuffer.length, _destAddress, _destPort);
			_socket.send(packet);
			System.out.println("TX " + System.currentTimeMillis() + " " + _sequence);
			//System.out.println("Tx " + System.currentTimeMillis() + " " + new String(_txBuffer, "UTF-8"));
			
			// Get response
			_rxBuffer = new byte[256];
			packet = new DatagramPacket(_rxBuffer, _rxBuffer.length);
			_socket.receive(packet);
			
			// Display response
			String received = new String(packet.getData(), 0, packet.getLength());
			//System.out.println("Rx " + System.currentTimeMillis() + " " + new String(packet.getData(), "UTF-8"));
			
			_sequence++;
			if (_sequence > MAX_SEQUENCE) { 
				System.out.println("Loop time: " + ((double) (System.currentTimeMillis() - startTime))/256.0);
				_sequence = 0;
			}
			
		}
		
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
	
	public boolean isSequenceMoreRecent(int s1, int s2, int max) {
		return (s1 > s2) && (s1 - s2 <= max/2) || (s2 > s1) && (s2 - s1  > max/2);
	}

	public static void main(String[] args) throws IOException {
		new Client();
	}
	
}
