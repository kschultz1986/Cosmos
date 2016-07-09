package cosmos.models;

import java.util.Random;

public class Color {
	
	public int r;
	public int g;
	public int b;
	private static Random _rand = new Random();
	
	public Color(int r, int g, int b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	public static Color getRandom() {
		byte[] b = new byte[3];
		return new Color(
			randInt(0, 255),
			randInt(0, 255),
			randInt(0, 255));
	}
	
	public static float randFloat(float min, float max) {
		return min + (max - min) * _rand.nextFloat();
	}
	
	public static int randInt(int min, int max) {
		return _rand.nextInt(max - min) + min;
	}
	
	public void print() {
		System.out.println(String.format("%d %d %d", r, g, b));
	}
	
}
