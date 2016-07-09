package cosmos.math;

public class Quaternion {
	
	public double w;
	public double z;
	public double y;
	public double x;

	public Quaternion() {
		x = y = z = 0.0;
		w = 1.0;
	}
	
	public Quaternion(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = 1.0;
	}
	
	public Quaternion(double x, double y, double z, double w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	/**
	 * Normalizing a quaternion works similar to a vector. This method will not do anything
	 * if the quaternion is close enough to being unit-length. define TOLERANCE as something
	 * small like 0.00001f to get accurate results
	 */
	public void normalize() {
		double mag2 = w*w + x*x + y*y + z*z;
		if (Math.abs(mag2) > Double.MIN_VALUE && Math.abs(mag2 - 1.0f) > Double.MIN_VALUE) {
			double mag = Math.sqrt(mag2);
			w /= mag;
			x /= mag;
			y /= mag;
			z /= mag;
		}
	}
	
	/**
	 * We need to get the inverse of a quaternion to properly apply a quaternion-rotation to a vector.
	 * The conjugate of a quaternion is the same as the inverse, as long as the quaternion is unit-length
	 */
	public Quaternion getConjugate()
	{
		return new Quaternion(-x, -y, -z, w);
	}
	
	/**
	 * Multiplying a quaternion q with a vector v applies the q-rotation to v
	 * @param v
	 * @return
	 */
	public Vector multiply(Vector v) {
		
		Vector vn = new Vector(v.x, v.y, v.z);
		vn.normalize();
		
		Quaternion vecQuat = new Quaternion(vn.x, vn.y, vn.z, 0.0);
		
		Quaternion resQuat = vecQuat.multiply(getConjugate());
		resQuat = this.multiply(resQuat);
		
		return new Vector(resQuat.x, resQuat.y, resQuat.z);
		
	}

	public void createFromAxisAngle(double x, double y, double z, float degrees) {
		// First we want to convert the degrees to radians
		// since the angle is assumed to be in radians
		double angle = (degrees / 180.0f) * Math.PI;

		// Here we calculate the sin( theta / 2) once for optimization
		double result = Math.sin(angle / 2.0f);

		// Calculate the w value by cos( theta / 2 )
		this.w = Math.cos(angle / 2.0f);

		// Calculate the x, y and z of the quaternion
		this.x = x * result;
		this.y = y * result;
		this.z = z * result;
	}

	public void createMatrix(double matrix[]) {
		// Make sure the matrix has allocated memory to store the rotation data
		if (matrix.length == 0)
			return;

		// First row
		matrix[0] = 1.0f - 2.0f * (y*y + z*z);
		matrix[1] = 2.0f * (x*y + z*w);
		matrix[2] = 2.0f * (x*z - y*w);
		matrix[3] = 0.0f;

		// Second row
		matrix[4] = 2.0f * (x*y - z*w);
		matrix[5] = 1.0f - 2.0f * (x*x + z*z);
		matrix[6] = 2.0f * (z*y + x*w);
		matrix[7] = 0.0f;

		// Third row
		matrix[8] = 2.0f * (x*z + y*w);
		matrix[9] = 2.0f * (y*z - x*w);
		matrix[10] = 1.0f - 2.0f * (x*x + y*y);
		matrix[11] = 0.0f;

		// Fourth row
		matrix[12] = 0;
		matrix[13] = 0;
		matrix[14] = 0;
		matrix[15] = 1.0f;

		// Now pMatrix[] is a 4x4 homogeneous matrix that can be applied to an
		// OpenGL Matrix
	}

	/**
	 * Multiplying q1 with q2 applies the rotation q2 to q1
	 * @param q
	 * @return
	 */
	public Quaternion multiply(Quaternion q) {
		
		return new Quaternion(
			w*q.x + x*q.w + y*q.z - z*q.y,
			w*q.y + y*q.w + z*q.x - x*q.z,
			w*q.z + z*q.w + x*q.y - y*q.x,
			w*q.w - x*q.x - y*q.y - z*q.z);
		
	}
	
	public static Quaternion RotateTowards(Quaternion q1, Quaternion q2, double maxAngle){
		 
	    if (maxAngle < 0.001) {
	        // No rotation allowed. Prevent dividing by 0 later.
	        return q1;
	    }
	 
	    double cosTheta = Quaternion.getDotProduct(q1, q2);
	 
	    // q1 and q2 are already equal.
	    // Force q2 just to be sure
	    if (cosTheta > 0.9999) {
	        return q2;
	    }
	 
	    // Avoid taking the long path around the sphere
	    if (cosTheta < 0) {
	        q1 = q1.scale(-1.0);
	        cosTheta *= -1.0;
	    }
	 
	    double angle = Math.acos(cosTheta);
	 
	    // If there is only a 2° difference, and we are allowed 5°,
	    // then we arrived.
	    if (angle < maxAngle) {
	        return q2;
	    }
	 
	    double fT = maxAngle/angle;
	    angle = maxAngle;
	 
	    Quaternion res = Quaternion.add(q1.scale(angle*Math.sin((1.0 - fT))), q2.scale(Math.sin(fT*angle))).scale(1.0/Math.sin(angle));
	    res.normalize();
	    return res;
	 
	}
	
	/**
	 * Add two quaternions q1 and q2 and return result
	 * @param q1
	 * @param q2
	 * @return
	 */
	public static Quaternion add(Quaternion q1, Quaternion q2) {
		return new Quaternion(q1.x + q2.x, q1.y + q2.y, q1.z + q2.z, q1.w + q2.w);
	}
	
	/**
	 * This is the same as cos theta (angular distance between for two unit-norm quaternions q1 and q2)
	 * @param q1
	 * @param q2
	 * @return
	 */
	public static double getDotProduct(Quaternion q1, Quaternion q2) {
		return q1.x*q2.x + q1.y*q2.y + q1.z*q2.z + q1.w*q2.w;
	}
	
	public Quaternion scale(double s) {
		return new Quaternion(x*s, y*s, z*s, w*s);
	}

}
