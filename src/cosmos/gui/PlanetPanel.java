package cosmos.gui;

import javax.swing.JPanel;

import cosmos.math.Point;
import cosmos.math.Utilities;
import cosmos.models.Planet;
import cosmos.models.Ship.ShipStatus;
import net.miginfocom.swing.MigLayout;
import cosmos.models.Universe;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class PlanetPanel extends JPanel {
	
	private Planet _planet;
	
	public PlanetPanel(Planet p) {
		
		_planet = p;
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JButton btnCreateNewShip = new JButton("Create New Ship");
		
		JPanel _statusPanel = new JPanel();
		add(_statusPanel);
		_statusPanel.setLayout(new MigLayout("", "[66px][8px][45px]", "[][][][16px][]"));
		
		JLabel lblSpecies = new JLabel("Species:");
		_statusPanel.add(lblSpecies, "cell 0 0");
		
		JLabel lblSpeciesValue = new JLabel(_planet.getSpecies().getNamePlural());
		_statusPanel.add(lblSpeciesValue, "cell 2 0");
		
		btnCreateNewShip.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Universe u = _planet.getUniverse();
				Point position = Utilities.getRandomPositionAround(_planet.getPosition(), 2.0*_planet.getRadius());
				u.addShip(position, _planet, u.getShipClass(0), ShipStatus.IDLE, _planet.getSpecies());
				u.printObject(u.getNumObjects()-1);
			}
		});
		add(btnCreateNewShip);
	}

}
