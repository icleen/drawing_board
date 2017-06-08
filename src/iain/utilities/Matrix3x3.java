package iain.utilities;

public class Matrix3x3 extends Matrix {
	
	private static final int SIZE = 3;

	public Matrix3x3() {
		super(SIZE, SIZE);
	}
	
	public void translate(int x, int y) {
		values[2][0] = x;
		values[2][1] = y;
	}
	
	/**
	 * Sets up a rotation matrix
	 * @param rotation how much to rotate in degrees
	 */
	public void rotate(double rotation) {
		double rad = (rotation * (Math.PI/180));
		values[0][0] = (int) Math.cos(rad);
		values[0][1] = (int) Math.sin(rad);
		values[1][0] = -1 * (int) Math.sin(rad);
		values[1][1] = (int) Math.cos(rad);
	}
	
	public void screen(int width, int height) {
		double w = width / 2, h = height / 2;
		values[0][0] = (int) w;
		values[1][1] = (int) h * -1;
		values[0][2] = (int) w;
		values[1][2] = (int) h;
	}

}
