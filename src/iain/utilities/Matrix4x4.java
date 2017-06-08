package iain.utilities;

public class Matrix4x4 extends Matrix {
	
	private static final int SIZE = 4;

	public Matrix4x4() {
		super(SIZE, SIZE);
	}
	
	public void translate(int x, int y, int z) {
		values[3][0] = x;
		values[3][1] = y;
		values[3][2] = z;
	}
	
	/**
	 * Rotates around the Y axis
	 * @param rotation how much to rotate in degrees
	 */
	public void rotateY(double rotation) {
		double rad = (rotation * (Math.PI/180));
		values[0][0] = (int) Math.cos(rad);
		values[0][2] = (int) Math.sin(rad);
		values[2][0] = -1 * (int) Math.sin(rad);
		values[2][2] = (int) Math.cos(rad);
	}
	
	/**
	 * Rotates around the X axis
	 * @param rotation how much to rotate in degrees
	 */
	public void rotateX(double rotation) {
		double rad = (rotation * (Math.PI/180));
		values[1][1] = (int) Math.cos(rad);
		values[1][2] = (int) Math.sin(rad);
		values[2][1] = -1 * (int) Math.sin(rad);
		values[2][2] = (int) Math.cos(rad);
	}
	
	/**
	 * Rotates around the Z axis
	 * @param rotation how much to rotate in degrees
	 */
	public void rotateZ(double rotation) {
		double rad = (rotation * (Math.PI/180));
		values[0][0] = (int) Math.cos(rad);
		values[1][0] = (int) Math.sin(rad);
		values[0][1] = -1 * (int) Math.sin(rad);
		values[1][1] = (int) Math.cos(rad);
	}
	
	public void projectPerspective(double fov, double aspect, double near, double far) {
		double rad = (fov * (Math.PI/180))/2;
		double zoomy = 1 / Math.tan(rad);
		double zoomx = zoomy / aspect;
		double c = (far + near) / (far - near);
		double d = (-2 * far * near) / (far - near);
		
		values[0][0] = (int) zoomx;
		values[1][1] = (int) zoomy;
		values[2][2] = (int) c;
		values[2][3] = (int) d;
		values[3][2] = 1;
	}
	
	public void projectOrtho(double left, double right, double bottom, double top, double near, double far) {
		double a = 2 / (right - left);
		double b = -1 * (right + left) / (right - left);
		double c = 2 / (top - bottom);
		double d = -1 * (top + bottom) / (top - bottom);
		double e = -1 * (far + near) / (far - near);
		double f = -2 / (far - near);
		values[0][0] = (int) a;
		values[0][3] = (int) b;
		values[1][1] = (int) c;
		values[1][3] = (int) d;
		values[2][2] = (int) e;
		values[2][3] = (int) f;
	}
	
	public Vector3D transform(Vector3D v) {
		assert(values[0].length == v.getDimension());
		Vector3D result = new Vector3D();
		double value;
		for (int i = 0; i < rows; i++) {
			value = 0;
			for (int j = 0; j < columns; j++) {
				value += values[i][j] * v.getValue(j);
			}
			result.setValue(i, value);
		}
		return result;
	}
	
	
	public Matrix4x4 join(Matrix4x4 B) {
		assert(this.columns == B.rows);
		Matrix4x4 A = new Matrix4x4();
		int value;
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				value = 0;
				for (int k = 0; k < SIZE; k++) {
					value += this.values[i][k] * B.values[k][j];
				}
				A.values[i][j] = value;
			}
		}
		return A;
	}

}
