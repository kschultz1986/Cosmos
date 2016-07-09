package cosmos.util;

import java.awt.EventQueue;
import java.io.IOException;
import cosmos.gui.CosmosFrame;
import cosmos.models.Universe;

public class Cosmos {
	
	private Universe _universe = null;
	private CosmosFrame _frame = null;

	public Cosmos() {
		
		System.out.println("Welcome to Cosmos.");
		try {
			_universe = new Universe();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					_frame = new CosmosFrame(_universe);
					_frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	};
	public static void main(String[] args) {
		new Cosmos();
	}
	
}