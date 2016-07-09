package cosmos.gui;

public class Cylinder {
	private double baseRadius;
	private double topRadius;
	private double height;
	private int slices;
	private int stacks;
	private float rotationX;
	private float rotationY;
	private float rotationZ;
	private float positionX;
	private float positionY;
	private float positionZ;
	
	Cylinder(double baseRadius, double topRadius, double height, int slices, int stacks) {
		setBaseRadius(baseRadius);
		setTopRadius(topRadius);
		setHeight(height);
		setSlices(slices);
		setStacks(stacks);
		setRotationX(0.0f);
		setRotationY(0.0f);
		setRotationZ(0.0f);
		setPositionX(0.0f);
		setPositionY(0.0f);
		setPositionZ(0.0f);
	}
	
	public void setRotation(float x, float y, float z) {
		setRotationX(x);
		setRotationY(y);
		setRotationZ(z);
	}
	
	public void setPosition(float x, float y, float z) {
		setPositionX(x);
		setPositionY(y);
		setPositionZ(z);
	}

	public double getBaseRadius() {
		return baseRadius;
	}

	public void setBaseRadius(double baseRadius) {
		this.baseRadius = baseRadius;
	}

	public double getTopRadius() {
		return topRadius;
	}

	public void setTopRadius(double topRadius) {
		this.topRadius = topRadius;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public int getSlices() {
		return slices;
	}

	public void setSlices(int slices) {
		this.slices = slices;
	}

	public int getStacks() {
		return stacks;
	}

	public void setStacks(int stacks) {
		this.stacks = stacks;
	}

	public float getRotationX() {
		return rotationX;
	}

	public void setRotationX(float rotationX) {
		this.rotationX = rotationX;
	}

	public float getRotationY() {
		return rotationY;
	}

	public void setRotationY(float rotationY) {
		this.rotationY = rotationY;
	}

	public float getRotationZ() {
		return rotationZ;
	}

	public void setRotationZ(float rotationZ) {
		this.rotationZ = rotationZ;
	}

	public float getPositionX() {
		return positionX;
	}

	public void setPositionX(float positionX) {
		this.positionX = positionX;
	}

	public float getPositionY() {
		return positionY;
	}

	public void setPositionY(float positionY) {
		this.positionY = positionY;
	}

	public float getPositionZ() {
		return positionZ;
	}

	public void setPositionZ(float positionZ) {
		this.positionZ = positionZ;
	}

}