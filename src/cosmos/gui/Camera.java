package cosmos.gui;

import com.jogamp.opengl.GL2;

import cosmos.math.Quaternion;
import cosmos.math.Vector;
import cosmos.math.Vertex;

public class Camera {

	private float _maxPitchRate;
	private float _maxHeadingRate;
	private float _maxRollRate;
	private float _headingDegrees;
	private float _pitchDegrees;
	private float _rollDegrees;
	private float _maxForwardVelocity;
	private float _forwardVelocity;
	private Quaternion _qHeading;
	private Quaternion _qPitch;
	private Quaternion _qRoll;
	private Vertex _position;
	private Vector _directionVector;

	Camera(float x, float y, float z) {
		_position = new Vertex();
		_qHeading = new Quaternion();
		_qPitch = new Quaternion();
		_qRoll = new Quaternion();
		_directionVector = new Vector();
		
		// Initalize all our member varibles.
		_maxPitchRate = 0.0f;
		_maxHeadingRate = 0.0f;
		_maxRollRate = 0.0f;
		_headingDegrees = 0.0f;
		_pitchDegrees = 45.0f;
		_rollDegrees = 0.0f;
		_maxForwardVelocity = 0.0f;
		_forwardVelocity = 0.0f;
		_position.setX(x);
		_position.setY(y);
		_position.setZ(z);
	}

	void setPrespective(GL2 gl)
	{
		double[] matrix = new double[16];
		Quaternion q;

		// Make the Quaternions that will represent our rotations
		_qPitch.createFromAxisAngle(1.0f, 0.0f, 0.0f, _pitchDegrees);
		_qHeading.createFromAxisAngle(0.0f, 1.0f, 0.0f, _headingDegrees);
		
		// Combine the pitch and heading rotations and store the results in q
		q = _qPitch.multiply(_qHeading);
		q.createMatrix(matrix);

		// Let OpenGL set our new prespective on the world!
		gl.glMultMatrixd(matrix, 0);
		
		// Create a matrix from the pitch Quaternion and get the j vector 
		// for our direction.
		_qPitch.createMatrix(matrix);
		_directionVector.y = matrix[9];

		// Combine the heading and pitch rotations and make a matrix to get
		// the i and j vectors for our direction.
		q = _qHeading.multiply(_qPitch);
		q.createMatrix(matrix);
		_directionVector.x = matrix[8];
		_directionVector.z = matrix[10];

		// Scale the direction by our speed.
		_directionVector = _directionVector.scale(_forwardVelocity);

		// Increment our position by the vector
		_position.setX(_position.getX() + _directionVector.x);
		_position.setY(_position.getY() + _directionVector.y);
		_position.setZ(_position.getZ() + _directionVector.z);

		// Translate to our new position.
		gl.glTranslated(-_position.getX(), -_position.getY(), _position.getZ());
	}

	void changePitch(float degrees) {
		if (Math.abs(degrees) < Math.abs(_maxPitchRate)) {
			// Our pitch is less than the max pitch rate that we
			// defined so lets increment it.
			_pitchDegrees += degrees;
		} else {
			// Our pitch is greater than the max pitch rate that
			// we defined so we can only increment our pitch by the
			// maximum allowed value.
			if (degrees < 0) {
				// We are pitching down so decrement
				_pitchDegrees -= _maxPitchRate;
			} else {
				// We are pitching up so increment
				_pitchDegrees += _maxPitchRate;
			}
		}

		// We don't want our pitch to run away from us. Although it
		// really doesn't matter I prefer to have my pitch degrees
		// within the range of -360.0f to 360.0f
		if (_pitchDegrees > 360.0f) {
			_pitchDegrees -= 360.0f;
		} else if (_pitchDegrees < -360.0f) {
			_pitchDegrees += 360.0f;
		}
	}

	void changeRoll(float degrees) {
		if (Math.abs(degrees) < Math.abs(_maxRollRate)) {
			// Our pitch is less than the max pitch rate that we
			// defined so lets increment it.
			_rollDegrees += degrees;
		} else {
			// Our pitch is greater than the max pitch rate that
			// we defined so we can only increment our pitch by the
			// maximum allowed value.
			if (degrees < 0) {
				// We are pitching down so decrement
				_rollDegrees -= _maxRollRate;
			} else {
				// We are pitching up so increment
				_rollDegrees += _maxRollRate;
			}
		}

		// We don't want our pitch to run away from us. Although it
		// really doesn't matter I prefer to have my pitch degrees
		// within the range of -360.0f to 360.0f
		if (_rollDegrees > 360.0f) {
			_rollDegrees -= 360.0f;
		} else if (_rollDegrees < -360.0f) {
			_rollDegrees += 360.0f;
		}
	}

	void changeHeading(float degrees) {
		if (Math.abs(degrees) < Math.abs(_maxHeadingRate)) {
			// Our Heading is less than the max heading rate that we
			// defined so lets increment it but first we must check
			// to see if we are inverted so that our heading will not
			// become inverted.
			if (_pitchDegrees > 90 && _pitchDegrees < 270
					|| (_pitchDegrees < -90 && _pitchDegrees > -270)) {
				_headingDegrees -= degrees;
			} else {
				_headingDegrees += degrees;
			}
		} else {
			// Our heading is greater than the max heading rate that
			// we defined so we can only increment our heading by the
			// maximum allowed value.
			if (degrees < 0) {
				// Check to see if we are upside down.
				if ((_pitchDegrees > 90 && _pitchDegrees < 270)
						|| (_pitchDegrees < -90 && _pitchDegrees > -270)) {
					// Ok we would normally decrement here but since we are
					// upside
					// down then we need to increment our heading
					_headingDegrees += _maxHeadingRate;
				} else {
					// We are not upside down so decrement as usual
					_headingDegrees -= _maxHeadingRate;
				}
			} else {
				// Check to see if we are upside down.
				if (_pitchDegrees > 90 && _pitchDegrees < 270
						|| (_pitchDegrees < -90 && _pitchDegrees > -270)) {
					// Ok we would normally increment here but since we are
					// upside
					// down then we need to decrement our heading.
					_headingDegrees -= _maxHeadingRate;
				} else {
					// We are not upside down so increment as usual.
					_headingDegrees += _maxHeadingRate;
				}
			}
		}

		// We don't want our heading to run away from us either. Although it
		// really doesn't matter I prefer to have my heading degrees
		// within the range of -360.0f to 360.0f
		if (_headingDegrees > 360.0f) {
			_headingDegrees -= 360.0f;
		} else if (_headingDegrees < -360.0f) {
			_headingDegrees += 360.0f;
		}
	}

	void changeVelocity(float vel) {
		if (Math.abs(vel) < Math.abs(_maxForwardVelocity)) {
			// Our velocity is less than the max velocity increment that we
			// defined so lets increment it.
			_forwardVelocity += vel;
		} else {
			// Our velocity is greater than the max velocity increment that
			// we defined so we can only increment our velocity by the
			// maximum allowed value.
			if (vel < 0) {
				// We are slowing down so decrement
				_forwardVelocity -= -_maxForwardVelocity;
			} else {
				// We are speeding up so increment
				_forwardVelocity += _maxForwardVelocity;
			}
		}
	}
	
	void setPosition(float x, float y, float z) {
		_position.setX(x);
		_position.setY(y);
		_position.setZ(z);
	}
	
	public Vertex getPosition() {
		return _position;
	}
	
	void moveForward(GL2 gl, float angle) {
//		float c_a = (float) Math.cos(angle);
//		float s_a = (float) Math.sin(angle);
//		Quaternion M_x = new Quaternion(s_a, 0, 0, c_a);
//		
//		float[] matrix = new float[16];
//		Quaternion q;
//
//		// Make the Quaternions that will represent our rotations
//		m_qPitch.createFromAxisAngle(1.0f, 0.0f, 0.0f, m_PitchDegrees);
//		m_qHeading.createFromAxisAngle(0.0f, 1.0f, 0.0f, m_HeadingDegrees);
//		m_qRoll.createFromAxisAngle(0.0f, 0.0f, 1.0f, m_RollDegrees);
//		
//		// Combine the pitch and heading rotations and store the results in q
//		q = m_qPitch.multiply(m_qHeading);
//		q = q.multiply(m_qRoll);
//		q = q.multiply(M_x);
//		q.createMatrix(matrix);
//
//		// Let OpenGL set our new prespective on the world!
//		gl.glMultMatrixf(matrix, 0);
		//Quaternion currentDirectionQuat = new Quaternion();
		
	}
	
	void setVelocity(float vel) {
		_forwardVelocity = vel;
	}
	
	void setMaxForwardVelocity(float vel) {
		_maxForwardVelocity = vel;
	}
	
	void setMaxPitchRate(float pitchRate) {
		_maxPitchRate = pitchRate;
	}
	
	void setMaxHeadingRate(float headingRate) {
		_maxHeadingRate = headingRate;
	}
	
	void setMaxRollRate(float rollRate) {
		_maxRollRate = rollRate;
	}

}
