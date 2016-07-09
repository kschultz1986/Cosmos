package cosmos.gui;

import javax.swing.JPanel;

import cosmos.math.Point;
import cosmos.math.Utilities;
import cosmos.models.Planet;
import cosmos.models.Ship;
import cosmos.models.Ship.ShipStatus;
import cosmos.models.Universe;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.BoxLayout;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.FormSpecs;
import javax.swing.JLabel;
import net.miginfocom.swing.MigLayout;

public class ShipPanel extends JPanel {
	
	private Ship _ship;
	private JLabel _lblStatusValue;
	
	public ShipPanel(Ship s) {
		
		_ship = s;
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JButton btnShoot = new JButton("Shoot");
		JButton btnStop = new JButton("Stop");
		
		btnShoot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//_ship.shootLaser();
				//_ship.setShooting(true);
			}
		});
		
		JPanel _statusPanel = new JPanel();
		add(_statusPanel);
		_statusPanel.setLayout(new MigLayout("", "[66px][8px][45px][8px][41px][20px][43px][73px]", "[][][][16px][]"));
		
		JLabel lblSpecies = new JLabel("Species:");
		_statusPanel.add(lblSpecies, "cell 0 0");
		
		JLabel lblSpeciesValue = new JLabel(s.getSpecies().getNamePlural());
		_statusPanel.add(lblSpeciesValue, "cell 2 0");
		
		JLabel lblHitPoints = new JLabel("Hit Points:");
		_statusPanel.add(lblHitPoints, "cell 0 1,alignx left,aligny top");
		
		JLabel lblHitPointsValue = new JLabel(String.valueOf(s.getHitPoints()));
		_statusPanel.add(lblHitPointsValue, "cell 2 1,alignx left,aligny top");
		
		JLabel lblAttack = new JLabel("Attack:");
		_statusPanel.add(lblAttack, "cell 0 2,alignx left,aligny top");
		
		JLabel lblAttackValue = new JLabel(String.valueOf(s.getAttack()));
		_statusPanel.add(lblAttackValue, "cell 2 2,alignx left,aligny top");
		
		JLabel lblSpeed = new JLabel("Speed:");
		_statusPanel.add(lblSpeed, "cell 0 3,alignx left,aligny top");
		
		JLabel lblSpeedValue = new JLabel(String.valueOf(s.getSpeed()));
		_statusPanel.add(lblSpeedValue, "cell 2 3,alignx left,aligny top");
		
		JLabel lblStatus = new JLabel("Status:");
		_statusPanel.add(lblStatus, "cell 0 4,alignx left,aligny top");
		
		_lblStatusValue = new JLabel(s.getStatus().toString());
		_statusPanel.add(_lblStatusValue, "cell 2 4,alignx left,aligny top");
		add(btnShoot);
		
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_ship.setStatus(ShipStatus.IDLE);
			}
		});
		add(btnStop);
		
	}
		
	public void setStatus(ShipStatus status) {
		_lblStatusValue.setText(status.toString());
	}

}
