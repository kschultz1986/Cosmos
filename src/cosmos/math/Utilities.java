package cosmos.math;

import java.util.Random;

import cosmos.models.LocalizedModel;

public final class Utilities {
	
	private static Random _rand = new Random();
	
	private Utilities() {
	}

	public static double randDouble(double min, double max) {
		return min + (max - min) * _rand.nextDouble();
	}
	
	public static int randInt(int min, int max) {
		return _rand.nextInt(max - min) + min;
	}
	
	public static float randFloat(float min, float max) {
		return min + (max - min) * _rand.nextFloat();
	}
	
	public static Point getRandomPositionAround(Point center, double distance) {
		double az = Utilities.randDouble(0.0, 2*Math.PI);
		double el = Utilities.randDouble(-0.5*Math.PI, 0.5*Math.PI);
		return new Point(
			center.x + distance*Math.sin(el)*Math.cos(az),
			center.y + distance*Math.sin(el)*Math.sin(az),
			center.z + distance*Math.cos(el));
	}
	
}
