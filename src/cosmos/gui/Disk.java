package cosmos.gui;

public class Disk {
	private double innerRadius;
	private double outerRadius;
	private int slices;
	private int loops;
	private float rotationX;
	private float rotationY;
	private float rotationZ;
	private float positionX;
	private float positionY;
	private float positionZ;
	
	Disk(double innerRadius, double outerRadius, int slices, int loops) {
		setInnerRadius(innerRadius);
		setOuterRadius(outerRadius);
		setSlices(slices);
		setLoops(loops);
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

	public double getInnerRadius() {
		return innerRadius;
	}

	public void setInnerRadius(double innerRadius) {
		this.innerRadius = innerRadius;
	}

	public double getOuterRadius() {
		return outerRadius;
	}

	public void setOuterRadius(double outerRadius) {
		this.outerRadius = outerRadius;
	}

	public int getSlices() {
		return slices;
	}

	public void setSlices(int slices) {
		this.slices = slices;
	}

	public int getLoops() {
		return loops;
	}

	public void setLoops(int loops) {
		this.loops = loops;
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