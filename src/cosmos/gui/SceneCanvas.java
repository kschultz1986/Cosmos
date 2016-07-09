package cosmos.gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import cosmos.math.Point;
import cosmos.math.Vector;
import cosmos.math.Vertex;
import cosmos.models.Color;
import cosmos.models.LaserBeam;
import cosmos.models.LocalizedModel;
import cosmos.models.Planet;
import cosmos.models.Ship;
import cosmos.models.Ship.ShipStatus;
import cosmos.models.Star;
import cosmos.models.Universe;
import cosmos.util.Timer;

import static com.jogamp.opengl.GL.*; // GL constants
import static com.jogamp.opengl.GL2.*; // GL2 constants

/**
 * JOGL 2.0 Example 2: Rotating 3D Shapes (GLCanvas)
 */
@SuppressWarnings("serial")
public class SceneCanvas extends GLCanvas implements GLEventListener {

	// Setup OpenGL Graphics Renderer
	private GLU glu; // for the GL Utility
	private GLUT glut;
	private Camera _cam;
	private GL2 gl;
	private int _mouseDownX;
	private int _mouseDownY;
	private int _mouseClickX = -1;
	private int _mouseClickY = -1;
	private int _mouseClickButton = MouseEvent.NOBUTTON;
	private LocalizedModel _leftClickedObject = null;
	private LocalizedModel _rightClickedObject = null;
	private Universe u;
	private CosmosFrame _frame;
	private Boolean _doDrawPlanetParentLines = false;
	private SBCMouseListener _sbcMouseListener = null;
	private SBCMouseMotionListener _sbcMouseMotionListener = null;
	private boolean _autoSwapBuffers = false;
	private boolean _disableManualBufferSwapping = false;
	private boolean _backBufferRenderingOnly = false;
	private Timer _timer;

	/** Constructor to setup the GUI for this Component */
	public SceneCanvas(CosmosFrame frame, Universe u) {
		
		_frame = frame;
		
		this.u = u;
		
//		_ship = new Ship(this.u, new Point(0.0, 0.0, 0.0), 10.0, 0);
//		_ship.setSpeed(0.1);
//		_ship.setDestination(u.getObject(0));
//		_ship = (Ship) u.getObject(u.getNumObjects()-1);

		
		_sbcMouseListener = new SBCMouseListener();
		this.addMouseListener(_sbcMouseListener);
		
		_sbcMouseMotionListener = new SBCMouseMotionListener();
		this.addMouseMotionListener(_sbcMouseMotionListener);

		this.addGLEventListener(this);
		
		setAutoSwapBufferMode(_autoSwapBuffers);
		
	}

	// ------ Implement methods declared in GLEventListener ------

	/**
	 * Called back immediately after the OpenGL context is initialized. Can be
	 * used to perform one-time initialization. Run only once.
	 */
	@Override
	public void init(GLAutoDrawable drawable) {
		gl = drawable.getGL().getGL2(); // get the OpenGL graphics context
		glu = new GLU(); // get GL Utilities
		glut = new GLUT();

		float aspect = ((float) this.getWidth()) / this.getHeight();
		gl.glViewport(0, 0, this.getWidth(), this.getHeight());

		gl.glMatrixMode(GL_PROJECTION); // Select The Projection Matrix
		gl.glLoadIdentity(); // Reset The Projection Matrix

		// Calculate The Aspect Ratio Of The Window
		glu.gluPerspective(45.0f, aspect, 0.1f, 100000.0f);

		gl.glMatrixMode(GL_MODELVIEW); // Select The Modelview Matrix
		gl.glLoadIdentity(); // Reset The Modelview Matrix

		gl.glEnable(GL_LINE_SMOOTH);
		gl.glShadeModel(GL_SMOOTH); // Enable Smooth Shading
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		gl.glClearDepth(10.0f); // Depth Buffer Setup
		gl.glEnable(GL_DEPTH_TEST);
		gl.glDepthFunc(GL_LEQUAL); // The Type Of Depth Testing To Do
		gl.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
		
		_cam = new Camera(0.0f, 0.0f, 0.0f);
		_cam.setMaxForwardVelocity(50.0f);
		_cam.setMaxPitchRate(5.0f);
		_cam.setMaxHeadingRate(5.0f);
		_cam.setMaxRollRate(5.0f);
		
		_timer = new Timer();
		
	}
	
	public static Texture loadTexture(String file) throws GLException, IOException
	{
	    ByteArrayOutputStream os = new ByteArrayOutputStream();
	    ImageIO.write(ImageIO.read(new File(file)), "png", os);
	    InputStream fis = new ByteArrayInputStream(os.toByteArray());
	    return TextureIO.newTexture(fis, true, TextureIO.PNG);
	}

	/**
	 * Call-back handler for window re-size event. Also called when the drawable
	 * is first set to visible.
	 */
	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		if (height == 0)
			height = 1; // prevent divide by zero
		float aspect = ((float) this.getWidth()) / this.getHeight();

		// Set the view port (display area) to cover the entire window
		gl.glViewport(0, 0, this.getWidth(), this.getHeight());

		// Setup perspective projection, with aspect ratio matches viewport
		gl.glMatrixMode(GL_PROJECTION); // choose projection matrix
		gl.glLoadIdentity(); // reset projection matrix
		glu.gluPerspective(45.0, aspect, 0.1, 100000.0); // fovy, aspect, zNear,
															// zFar

		// Enable the model-view transform
		gl.glMatrixMode(GL_MODELVIEW);
		gl.glLoadIdentity(); // reset
	}
	
	void drawQuad(Quad q) {
		Color c = q.getColor();
		if (c != null) {
			gl.glColor3f(c.r, c.g, c.b);
		}
		for (int i = 0; i < 4; i++) {
			gl.glVertex3d(q.getPoint(i).getX(), q.getPoint(i).getY(), q.getPoint(i).getZ());
		}
	}
	
	void drawTriangle(Triangle t) {
		for (int i = 0; i < 3; i++) {
			gl.glVertex3d(t.getPoint(i).getX(), t.getPoint(i).getY(), t.getPoint(i).getZ());
		}
	}
	
	void drawCylinder(Cylinder c) {
		GLUquadric quadric = glu.gluNewQuadric();
		gl.glTranslatef(c.getPositionX(), c.getPositionY(), c.getPositionZ());
		gl.glRotatef(c.getRotationX(), 1.0f, 0.0f, 0.0f);
		gl.glRotatef(c.getRotationY(), 0.0f, 1.0f, 0.0f);
		gl.glRotatef(c.getRotationZ(), 0.0f, 0.0f, 1.0f);
		gl.glColor3f(0.4f, 0.4f, 0.4f);
		glu.gluQuadricDrawStyle(quadric, GLU.GLU_FILL);
		glu.gluCylinder(quadric,
			c.getBaseRadius(),
			c.getTopRadius(),
			c.getHeight(),
			c.getSlices(),
			c.getStacks());
		gl.glRotatef(-c.getRotationX(), 1.0f, 0.0f, 0.0f);
		gl.glRotatef(-c.getRotationY(), 0.0f, 1.0f, 0.0f);
		gl.glRotatef(-c.getRotationZ(), 0.0f, 0.0f, 1.0f);
		gl.glTranslatef(-c.getPositionX(), -c.getPositionY(), -c.getPositionZ());
		glu.gluDeleteQuadric(quadric);
	}
	
	void drawDisk(Disk d) {
		GLUquadric quadric = glu.gluNewQuadric();
		gl.glTranslatef(d.getPositionX(), d.getPositionY(), d.getPositionZ());
		gl.glRotatef(d.getRotationX(), 1.0f, 0.0f, 0.0f);
		gl.glRotatef(d.getRotationY(), 0.0f, 1.0f, 0.0f);
		gl.glRotatef(d.getRotationZ(), 0.0f, 0.0f, 1.0f);
		gl.glColor3f(0.2f, 0.2f, 0.2f);
		glu.gluQuadricDrawStyle(quadric, GLU.GLU_FILL);
		glu.gluDisk(quadric,
			d.getInnerRadius(),
			d.getOuterRadius(),
			d.getSlices(),
			d.getLoops());
		gl.glRotatef(-d.getRotationX(), 1.0f, 0.0f, 0.0f);
		gl.glRotatef(-d.getRotationY(), 0.0f, 1.0f, 0.0f);
		gl.glRotatef(-d.getRotationZ(), 0.0f, 0.0f, 1.0f);
		gl.glTranslatef(-d.getPositionX(), -d.getPositionY(), -d.getPositionZ());
		glu.gluDeleteQuadric(quadric);
	}
	
	void drawSphere(Color c, double radius, double x, double y, double z) {
		
		// First calculate the distance between the camera and the sphere
		double distance = Vertex.calculateDistance(new Vertex(_cam.getPosition().getX(), _cam.getPosition().getY(), -_cam.getPosition().getZ()), new Vertex(x, y, z));
		
		gl.glPushMatrix();
		
		gl.glTranslated(x, y, z);
		
		gl.glColor4f(c.r/255.0f, c.g/255.0f, c.b/255.0f, 1.0f);
		
		if (distance > 2000) {
			glut.glutSolidSphere(radius, 5, 5);
		} else if (distance > 1000) {
			glut.glutSolidSphere(radius, 8, 8);
		} else if (distance > 500) {
			glut.glutSolidSphere(radius, 12, 12);
		} else if (distance > 100) {
			glut.glutSolidSphere(radius, 16, 16);
		} else {
			glut.glutSolidSphere(radius, 20, 20);
		}
		
		gl.glPopMatrix();
		
	}
	
	void drawTranslucentSphere(Color c, double radius, double x, double y, double z, float alpha) {
		
		// First calculate the distance between the camera and the sphere
		double distance = Vertex.calculateDistance(new Vertex(_cam.getPosition().getX(), _cam.getPosition().getY(), -_cam.getPosition().getZ()), new Vertex(x, y, z));
		
		gl.glPushMatrix();
		
		gl.glEnable(GL_BLEND);
		gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		gl.glTranslated(x, y, z);
		
		gl.glColor4f(c.r/255.0f, c.g/255.0f, c.b/255.0f, alpha);
		
		if (distance > 2000) {
			glut.glutSolidSphere(radius, 5, 5);
		} else if (distance > 1000) {
			glut.glutSolidSphere(radius, 8, 8);
		} else if (distance > 500) {
			glut.glutSolidSphere(radius, 12, 12);
		} else if (distance > 100) {
			glut.glutSolidSphere(radius, 16, 16);
		} else {
			glut.glutSolidSphere(radius, 20, 20);
		}
		
		gl.glDisable(GL_BLEND);
		
		gl.glPopMatrix();
		
		
		
	}
	
	void drawLaserBeam(LaserBeam laser) {
		double distance = laser.getDistanceFromOrigin();
		double length = laser.getLength();
		Color c = laser.getColor();
		gl.glPushMatrix();
		gl.glTranslated(laser.x, laser.y, laser.z);
		laser.point(gl);
		gl.glBegin(GL.GL_LINES);
		gl.glLineWidth(laser.getThickness());
		gl.glColor4f(c.r/255.0f, c.g/255.0f, c.b/255.0f, 1.0f);
		gl.glVertex3d(-distance, 0.0, 0.0);
		gl.glVertex3d(-distance - length, 0.0, 0.0);
		gl.glEnd();
		gl.glPopMatrix();
		
	}
	
	void drawShip(Ship s, boolean select) {
		
		if (getFrame().getShipPanel() != null) {
			getFrame().getShipPanel().setStatus(s.getStatus());
		}
		
		Color c = s.getColorID();
		
		gl.glPushMatrix();
		
		s.move();
		
		if (Double.isNaN(s.x) || Double.isNaN(s.y) || Double.isNaN(s.z)) {
			System.out.print(s.getString());
			System.out.println();
		}
		
		gl.glTranslated(s.x, s.y, s.z);
		
		s.point(gl);
		
		gl.glBegin(GL2.GL_QUADS);
		if (!select) { gl.glColor4f(1.0f, 1.0f, 0.0f, 0.0f); }
		else { gl.glColor4f(c.r/255.0f, c.g/255.0f, c.b/255.0f, 1.0f); }
		gl.glVertex3d(s.length/2, 0.1, 0.1);
		gl.glVertex3d(s.length/2, 0.1, -0.1);
		gl.glVertex3d(s.length/2, -0.1, -0.1);
		gl.glVertex3d(s.length/2, -0.1, 0.1);
		gl.glEnd();
		
		gl.glBegin(GL.GL_TRIANGLES);
		if (!select) { gl.glColor4f(0.0f, 1.0f, 0.0f, 0.0f); }
		else { gl.glColor4f(c.r/255.0f, c.g/255.0f, c.b/255.0f, 1.0f); }
		gl.glVertex3d(s.length/2, 0.1, 0.1);
		gl.glVertex3d(s.length/2, -0.1, 0.1);
		gl.glVertex3d(-s.length/2, 0, 0);
		gl.glVertex3d(s.length/2, 0.1, -0.1);
		gl.glVertex3d(s.length/2, -0.1, -0.1);
		gl.glVertex3d(-s.length/2, 0, 0);
		if (!select) { gl.glColor4f(0.0f, 0.0f, 1.0f, 0.0f); }
		else { gl.glColor4f(c.r/255.0f, c.g/255.0f, c.b/255.0f, 1.0f); }
		gl.glVertex3d(s.length/2, 0.1, 0.1);
		gl.glVertex3d(s.length/2, 0.1, -0.1);
		gl.glVertex3d(-s.length/2, 0, 0);
		gl.glVertex3d(s.length/2, -0.1, 0.1);
		gl.glVertex3d(s.length/2, -0.1, -0.1);
		gl.glVertex3d(-s.length/2, 0, 0);
		gl.glEnd();
		
		gl.glPopMatrix();

	}
	
	public void drawCartesianAxes(Point p) {
		
		gl.glLineWidth(5.0f);
	
		// Draw X Axis Line
		drawLine(new Color(255, 0, 0), p, new Point(p.x + 20.0, p.y, p.z));
		
		// Draw Y Axis Line
		drawLine(new Color(0, 255, 0), p, new Point(p.x, p.y + 20.0, p.z));
		
		// Draw Z Axis Line
		drawLine(new Color(0, 0, 255), p, new Point(p.x, p.y, p.z + 20.0));

	}
	
	public void drawOrientationAxes(Point p) {
		
		gl.glLineWidth(1.0f);
		
		// Draw X Axis Line
		drawLine(new Color(255, 0, 0), p, new Point(p.x + 20.0, p.y, p.z));
		
		// Draw Y Axis Line
		drawLine(new Color(0, 255, 0), p, new Point(p.x, p.y + 20.0, p.z));
		
		// Draw Z Axis Line
		drawLine(new Color(0, 0, 255), p, new Point(p.x, p.y, p.z + 20.0));
		
	}
	
	public void drawLine(Color c, Point p1, Point p2) {
		gl.glColor4f(c.r/255.0f, c.g/255.0f, c.b/255.0f, 1.0f);
	    gl.glBegin(GL.GL_LINES);
			gl.glVertex3d(p1.x, p1.y, p1.z);
			gl.glVertex3d(p2.x, p2.y, p2.z);
		gl.glEnd();
	}
	
	public void drawScene() {
	    
		// Draw Planets
		for (int i = 0; i < u.getNumObjects(); i++) {
			
			LocalizedModel obj = u.getObject(i);
			
			if (obj instanceof Planet) {
				
				Planet p = (Planet) obj;
				Star s = p.getStar();
				
				if (_doDrawPlanetParentLines) {
					gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
				    gl.glBegin(GL.GL_LINES);
						gl.glVertex3d(s.x, s.y, s.z);
						gl.glVertex3d(p.x, p.y, p.z);
					gl.glEnd();
				}
				
				drawSphere(p.color, p.getRadius(), p.x, p.y, p.z);
			
			}
			
		}
		
		// Draw Stars
		for (int i = 0; i < u.getNumObjects(); i++) {
			
			LocalizedModel obj = u.getObject(i);
			
			if (obj instanceof Star) {
				Star s = (Star) obj;
				drawSphere(s.color, s.getRadius(), s.x, s.y, s.z);
			}
			
		}
		
		for (int i = 0; i < u.getNumObjects(); i++) {
			LocalizedModel model = u.getObject(i);
			if (model instanceof Ship) {
				drawShip((Ship) model, false);
			}
		}
		
		for (int i = 0; i < u.getNumObjects(); i++) {
			LocalizedModel model = u.getObject(i);
			if (model instanceof Ship) {
				Ship s = (Ship) model;
				if (((s.getStatus() == ShipStatus.ATTACKING) && s.isInRange()) || (s.getNumLasers() > 0)) {
					s.updateLasers(_timer.getTimeSinceReset());
					for (int j = 0; j < s.getNumLasers(); j++) {
						drawLaserBeam(s.getLaser(j));
					}
				}
			}
		}
		
		if ((_leftClickedObject != null) && (_leftClickedObject instanceof Ship)) {
			Ship s = (Ship) _leftClickedObject;
			gl.glEnable(GL_CULL_FACE);
			drawTranslucentSphere(new Color(128, 128, 255), 0.5, s.x, s.y, s.z, 0.2f);
			gl.glDisable(GL_CULL_FACE);
		}
		
	}
	
	public void selectScene() {
		
		//
		//  Rendering into the back buffer for color picking
		//
		_backBufferRenderingOnly = false;
		FloatBuffer pixels = FloatBuffer.allocate(4);

		//  Select the back buffer, once for all
		gl.glDrawBuffer(GL2.GL_BACK);

		if (u.getNumObjects() > 0) {
			
			/*
			 * Rendering all objects.
			 */
			for (int i = 0; i < u.getNumObjects(); i++) {

				LocalizedModel obj = u.getObject(i);
				
				if (obj instanceof Star) {
					Star s = (Star) obj;
					drawSphere(s.getColorID(), s.getRadius(), s.x, s.y, s.z);
				}
				else if (obj instanceof Planet) {
					Planet p = (Planet) obj;
					drawSphere(p.getColorID(), p.getRadius(), p.x, p.y, p.z);
				}
				else if (obj instanceof Ship) {
					Ship s = (Ship) obj;
					drawSphere(s.getColorID(), 0.5, s.x, s.y, s.z);
				}
				
			}

			int[] viewport = new int[4];

			gl.glGetIntegerv(GL2.GL_VIEWPORT, viewport, 0);

			// Read the back buffer pixel value
			gl.glReadBuffer(GL2.GL_BACK);
			gl.glReadPixels(_mouseClickX, viewport[3] - _mouseClickY, 1, 1, GL2.GL_RGB, GL2.GL_FLOAT, pixels);

			// Calculate the object ID from the pixel value
			float[] pixelarray = pixels.array();	
			byte[] bytearray = { (byte) 0, (byte) (pixelarray[0]*255), (byte) (pixelarray[1]*255), (byte) (pixelarray[2]*255)};
			ByteBuffer b = ByteBuffer.wrap(bytearray);
			int id = b.getInt();

			if (id > 0) {
				if (_mouseClickButton == MouseEvent.BUTTON1) {
					
					getFrame().removeAllPanels();
					
					_leftClickedObject = u.getObject(id-1);
					_rightClickedObject = null;
					
					if (_leftClickedObject instanceof Ship) {
						getFrame().setLeftClickedObject("Ship");
						getFrame().addShipPanel((Ship) _leftClickedObject);
					} else if (_leftClickedObject instanceof Planet) {
						getFrame().setLeftClickedObject(_leftClickedObject.getName());
						getFrame().addPlanetPanel((Planet) _leftClickedObject);
					} else if (_leftClickedObject instanceof Star) {
						getFrame().setLeftClickedObject(_leftClickedObject.getName());
					}
					
					getFrame().setRightClickedObject("");
					
				}
				else if (_mouseClickButton == MouseEvent.BUTTON3) {
					
					_rightClickedObject = u.getObject(id-1);
					
					if (_rightClickedObject instanceof Ship) {
						
						getFrame().setRightClickedObject("Ship");
						
						if (_leftClickedObject instanceof Ship) {
							
							Ship leftShip = ((Ship) _leftClickedObject);
							Ship rightShip = ((Ship) _rightClickedObject);
							
							if (leftShip.getSpecies() != rightShip.getSpecies()) {
								
								leftShip.setStatus(ShipStatus.ATTACKING);
								leftShip.setSpeed(leftShip.getShipClass().getSpeed());
								leftShip.setDestination(rightShip);
								leftShip.setObjectToAttack(rightShip);
								leftShip.setPointingPosition(rightShip.getPosition());
								leftShip.setDesiredRange(leftShip.getRange());
								
							}
							
						}
						
						
					} else if (_rightClickedObject instanceof Planet) {
						
						getFrame().setRightClickedObject(_rightClickedObject.getName());
						
						if (_leftClickedObject instanceof Ship) {
							
							Ship leftShip = (Ship) _leftClickedObject;
							Planet rightPlanet = (Planet) _rightClickedObject;
							double desiredRange = rightPlanet.getRadius() + leftShip.getRange();
							leftShip.setPointingPosition(rightPlanet.getPosition());
							
							if (leftShip.getDistanceTo(rightPlanet) > desiredRange) {
								
								leftShip.setStatus(ShipStatus.MOVING);
								leftShip.setSpeed(leftShip.getShipClass().getSpeed());
								leftShip.setDestination(rightPlanet);
								leftShip.setDesiredRange(desiredRange);
								
							} else {
								
								leftShip.setStatus(ShipStatus.IDLE);
								leftShip.setSpeed(0.0);
								
							}
							
						}
						
					} else if (_rightClickedObject instanceof Star) {
						
						getFrame().setRightClickedObject(_rightClickedObject.getName());
						
						if (_leftClickedObject instanceof Ship) {
							
							Ship leftShip = (Ship) _leftClickedObject;
							Star rightStar = (Star) _rightClickedObject;
							double desiredRange = rightStar.getRadius() + leftShip.getRange();
							leftShip.setPointingPosition(rightStar.getPosition());
							
							if (leftShip.getDistanceTo(rightStar) > desiredRange) {
								
								leftShip.setStatus(ShipStatus.MOVING);
								leftShip.setSpeed(leftShip.getShipClass().getSpeed());
								leftShip.setDestination(rightStar);
								leftShip.setDesiredRange(desiredRange);
								
							} else {
								
								leftShip.setStatus(ShipStatus.IDLE);
								leftShip.setSpeed(0.0);
								
							}
							
						}
						
					}
					
				}
			}
			else {
				if (_mouseClickButton == MouseEvent.BUTTON1) {
					
					getFrame().removeAllPanels();
					
					_leftClickedObject = null;
					_rightClickedObject = null;
					getFrame().setLeftClickedObject("");
					getFrame().setRightClickedObject("");
					
				}
				else if (_mouseClickButton == MouseEvent.BUTTON3) {
					
					_rightClickedObject = null;
					getFrame().setRightClickedObject("");
					
				}
			}
			
			//  Disable manual buffer swapping for one time
			_disableManualBufferSwapping = true;
		}
			   
	}

	/**
	 * Called back by the animator to perform rendering.
	 */
	@Override
	public void display(GLAutoDrawable drawable) {
		
//		getFrame().setStatus(String.format("Planet: %s, Species: %s, Population: %d",
//				dest.getName(),
//				u.getPlayer(0).getSpecies().getNamePlural(),
//				9000000000L));
		getFrame().setStatus(String.format("Num Objs = %d", u.getNumObjects()));
		getFrame().setCameraPositionString(_cam.getPosition().getString());
		
		if (_backBufferRenderingOnly) {
			gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			gl.glMatrixMode(GL2.GL_MODELVIEW);
			gl.glLoadIdentity();
			_cam.setPrespective(gl);
			selectScene();
		} else {
			gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			gl.glMatrixMode(GL2.GL_MODELVIEW);
			gl.glLoadIdentity();
			_cam.setPrespective(gl);
			drawScene();
		}
		gl.glFlush();
		
		if (!_autoSwapBuffers) {
		    if (_disableManualBufferSwapping) {
		        _disableManualBufferSwapping = false;
		    } else {
		        swapBuffers();
		    }
		}
		
		_timer.resetTime();
	
	}

	/**
	 * Called back before the OpenGL context is destroyed. Release resource such
	 * as buffers.
	 */
	@Override
	public void dispose(GLAutoDrawable drawable) {
	}
	
	public void setDrawPlanetParentLines(Boolean b) {
		_doDrawPlanetParentLines = b;
	}
	
	public CosmosFrame getFrame() {
		return _frame;
	}
	
	public Camera getCamera() {
		return _cam;
	}
	
//	public Ship getShip() {
//		return _ship;
//	}
	
    private class SBCMouseListener implements MouseListener {
        
        public void mouseClicked(MouseEvent e) {
        	_mouseClickX = e.getX();
        	_mouseClickY = e.getY();
        	_mouseClickButton = e.getButton();
        	if (_mouseClickButton == MouseEvent.BUTTON1) {
	        	_backBufferRenderingOnly = true;
        	}
        	else if (_mouseClickButton == MouseEvent.BUTTON3) {
        		if (_leftClickedObject != null) {
    	        	_backBufferRenderingOnly = true;
        		}
        	}
        }
        
        public void mousePressed(MouseEvent e) { }

        public void mouseReleased(MouseEvent e) { }
        public void mouseEntered(MouseEvent e) { }
        public void mouseExited(MouseEvent e) { }
        
    }
    
    private class SBCMouseMotionListener implements MouseMotionListener {
    	
    	public void mouseDragged(MouseEvent e) {
        	
			int mouseX = e.getX();
			int mouseY = e.getY();
			float deltaMouse;
			
			if (mouseX < _mouseDownX) {
				deltaMouse = _mouseDownX - mouseX;
				_cam.changeHeading(-0.2f * deltaMouse);
			}
			else if (mouseX > _mouseDownX) {
				deltaMouse = mouseX - _mouseDownX;
				_cam.changeHeading(0.2f * deltaMouse);
			}
			
			if (mouseY < _mouseDownY) {
				deltaMouse = _mouseDownY - mouseY;
				_cam.changePitch(-0.2f * deltaMouse);
			}
			else if (mouseY > _mouseDownY) {
				deltaMouse = mouseY - _mouseDownY;
				_cam.changePitch(0.2f * deltaMouse);
			}
			
			_mouseDownX = mouseX;
			_mouseDownY = mouseY;
		}

		@Override
		public void mouseMoved(MouseEvent e) {	}
		
    }

    public LocalizedModel getLeftClickedObject() {
    	return _leftClickedObject;
    }
    
    public LocalizedModel getRightClickedObject() {
    	return _rightClickedObject;
    }

}