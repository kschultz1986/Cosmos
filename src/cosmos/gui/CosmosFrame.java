package cosmos.gui;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.border.EmptyBorder;

import com.jogamp.nativewindow.ScalableSurface;
import com.jogamp.opengl.util.FPSAnimator;

import cosmos.models.Planet;
import cosmos.models.Ship;
import cosmos.models.Universe;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JCheckBoxMenuItem;

public class CosmosFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private SceneCanvas _canvas;
	private FPSAnimator animator;
	private JLabel lblStatus;
	private JLabel lblCameraPosition;
	private JLabel lblLeftClickedObject;
	private JLabel lblRightClickedObject;
	private JButton btnLeft;
	private JButton btnRight;
	private JButton btnPitchDown;
	private JButton btnPitchUp;
	private Boolean _doDrawOpenGL = true;
	private JPanel _infoPanel;
	private Universe _u;
	private PlanetPanel _planetPanel;
	private ShipPanel _shipPanel;

	/**
	 * Create the frame.
	 */
	public CosmosFrame(Universe u) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new Thread() {
					@Override
					public void run() {
						
						if (_doDrawOpenGL) {
							if (animator.isStarted()) {
								animator.stop();
							}
						}
						System.exit(0);
					}
				}.start();
			}
		});
		mnFile.add(mntmExit);
		
		JMenu mnView = new JMenu("View");
		menuBar.add(mnView);
		
		JCheckBoxMenuItem chckbxmntmDrawPlanetParentLines = new JCheckBoxMenuItem("Draw Planet Parent Lines");
		chckbxmntmDrawPlanetParentLines.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_canvas.setDrawPlanetParentLines(chckbxmntmDrawPlanetParentLines.isSelected());
			}
		});
		mnView.add(chckbxmntmDrawPlanetParentLines);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);

		JMenuItem mntmAbout = new JMenuItem("About...");
		mnHelp.add(mntmAbout);
		
		contentPane = new JPanel();
		contentPane.setPreferredSize(new Dimension(1536, 768));
		
		_u = u;
		_canvas = new SceneCanvas(this, _u);
		
		// Added this line to allow correct display of the canvas on Mac OS X with Retina display
		_canvas.setSurfaceScale(new float[] { ScalableSurface.IDENTITY_PIXELSCALE, ScalableSurface.IDENTITY_PIXELSCALE });

		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));

		contentPane.add(_canvas);
		setContentPane(contentPane);
		
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		contentPane.add(toolBar, BorderLayout.NORTH);

		JButton btnOut = new JButton("Out");
		btnOut.addMouseListener(new MouseListener() {
			
			public void mouseClicked(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				_canvas.getCamera().setVelocity(-1.0f);	
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				_canvas.getCamera().setVelocity(0.0f);
			}
		});
		toolBar.add(btnOut);
		
		JButton btnIn = new JButton("In");
		btnIn.addMouseListener(new MouseListener() {
			
			public void mouseClicked(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				_canvas.getCamera().setVelocity(1.0f);
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				_canvas.getCamera().setVelocity(0.0f);
			}
		});
		toolBar.add(btnIn);
		
		btnLeft = new JButton("Turn Left");
		btnLeft.addMouseListener(new MouseListener() {
			
//			Ship s = _canvas.getShip();
			
			public void mouseClicked(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
//				s.setHeadingVelocity(-0.5f);
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
//				s.setHeadingVelocity(0.0f);
			}
		});
		toolBar.add(btnLeft);
		
		btnRight = new JButton("Turn Right");
		btnRight.addMouseListener(new MouseListener() {
			
//			Ship s = _canvas.getShip();
			
			public void mouseClicked(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
//				s.setHeadingVelocity(+0.5f);
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
//				s.setHeadingVelocity(0.0f);
			}
		});
		toolBar.add(btnRight);
		
		btnPitchDown = new JButton("Pitch Down");
		btnPitchDown.addMouseListener(new MouseListener() {
			
//			Ship s = _canvas.getShip();
			
			public void mouseClicked(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
//				s.setPitchVelocity(-0.5f);
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
//				s.setPitchVelocity(0.0f);
			}
		});
		toolBar.add(btnPitchDown);
		
		btnPitchUp = new JButton("Pitch Up");
		btnPitchUp.addMouseListener(new MouseListener() {
			
//			Ship s = _canvas.getShip();
			
			public void mouseClicked(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
//				s.setPitchVelocity(+0.5f);
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
//				s.setPitchVelocity(0.0f);
			}
		});
		toolBar.add(btnPitchUp);
		
		JButton btnRollLeft = new JButton("Roll Left");
		btnRollLeft.addMouseListener(new MouseListener() {
			
//			Ship s = _canvas.getShip();
			
			public void mouseClicked(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
//				s.setRollVelocity(-0.5f);
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
//				s.setRollVelocity(+0.0f);
			}
		});
		toolBar.add(btnRollLeft);
		
		JButton btnRollRight = new JButton("Roll Right");
		btnRollRight.addMouseListener(new MouseListener() {
			
//			Ship s = _canvas.getShip();
			
			public void mouseClicked(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
//				s.setRollVelocity(+0.5f);
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
//				s.setRollVelocity(0.0f);
			}
		});
		toolBar.add(btnRollRight);
		
		JButton btnRotateLeft = new JButton("Rotate Left");
		btnRotateLeft.addMouseListener(new MouseListener() {
			
//			Ship s = _canvas.getShip();
			
			public void mouseClicked(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
//				s.setRotateVelocity(-0.5f);
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
//				s.setRotateVelocity(+0.0f);
			}
		});
		toolBar.add(btnRotateLeft);
		
		JButton btnRotateRight = new JButton("Rotate Right");
		btnRotateRight.addMouseListener(new MouseListener() {
			
//			Ship s = _canvas.getShip();
			
			public void mouseClicked(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
//				s.setRotateVelocity(+0.5f);
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
//				s.setRotateVelocity(0.0f);
			}
		});
		toolBar.add(btnRotateRight);
		
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getID() == KeyEvent.KEY_TYPED) {
				    switch (e.getKeyCode()) {
				        case KeyEvent.VK_UP:
				            // Handle +Y Rotation
				            break;
				        case KeyEvent.VK_DOWN:
				            // Handle -Y Rotation
				            break;
				        case KeyEvent.VK_RIGHT:
				        	// Handle +X Rotation
				        	break;
				        case KeyEvent.VK_LEFT:
				            // Handle -X Rotation
				            break;
				        case KeyEvent.VK_K:
				            // Handle -Z Rotation
				        	break;
				        case KeyEvent.VK_M:
				        	// Handle +Z Rotation
				            break;
				        default:
				        	break;
				     }
				}
			}
		});
		
		JPanel statusBar = new JPanel();
		FlowLayout fl_statusBar = (FlowLayout) statusBar.getLayout();
		fl_statusBar.setAlignment(FlowLayout.LEFT);
		contentPane.add(statusBar, BorderLayout.SOUTH);
		
		lblStatus = new JLabel("Ready");
		statusBar.add(lblStatus);
		
		_infoPanel = new JPanel();
		_infoPanel.setPreferredSize(new Dimension(300, 500));
		FlowLayout flowLayout = (FlowLayout) _infoPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		_infoPanel.setSize(new Dimension(300, 500));
		_infoPanel.setMinimumSize(new Dimension(300, 500));
		contentPane.add(_infoPanel, BorderLayout.EAST);
		
		lblCameraPosition = new JLabel("Camera Position: (,,)");
		lblCameraPosition.setMinimumSize(new Dimension(300, 16));
		lblCameraPosition.setPreferredSize(new Dimension(300, 16));
		_infoPanel.add(lblCameraPosition);
		
		lblLeftClickedObject = new JLabel("Left Clicked Object:");
		lblLeftClickedObject.setPreferredSize(new Dimension(300, 16));
		lblLeftClickedObject.setMinimumSize(new Dimension(300, 16));
		_infoPanel.add(lblLeftClickedObject);
		
		lblRightClickedObject = new JLabel("Right Clicked Object:");
		lblRightClickedObject.setPreferredSize(new Dimension(300, 16));
		lblRightClickedObject.setMinimumSize(new Dimension(300, 16));
		_infoPanel.add(lblRightClickedObject);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		if (_doDrawOpenGL) {
			animator = new FPSAnimator(_canvas, 60, true);
			animator.start(); // start the animation loop
		}
		
		setTitle("Cosmos");
		pack();
		setVisible(true);
	}
	
	public void setStatus(String s) {
		lblStatus.setText(s);
	}
	
	public void setCameraPositionString(String s) {
		lblCameraPosition.setText("Camera Position: " + s);
	}
	
	public void setLeftClickedObject(String s) {
		lblLeftClickedObject.setText("Left Clicked Object: " + s);
		lblLeftClickedObject.repaint();
	}
	
	public void setRightClickedObject(String s) {
		lblRightClickedObject.setText("Right Clicked Object: " + s);
	}
	
	public void addPlanetPanel(Planet p) {
		_planetPanel = new PlanetPanel(p);
		_infoPanel.add(_planetPanel);
	}
	
	public void removePlanetPanel() {
		if (_planetPanel != null) {
			_infoPanel.remove(_planetPanel);
		}
	}
	
	public void addShipPanel(Ship s) {
		_shipPanel = new ShipPanel(s);
		_infoPanel.add(_shipPanel);
	}
	
	public void removeShipPanel() {
		if (_shipPanel != null) {
			_infoPanel.remove(_shipPanel);
		}
	}
	
	public ShipPanel getShipPanel() {
		return _shipPanel;
	}
	
	public void removeAllPanels() {
		this.removePlanetPanel();
		this.removeShipPanel();
		_infoPanel.revalidate();
		_infoPanel.repaint();
	}

}
