package iain.utilities;

public class Matrix4x4 extends Matrix {
	
	private static final int SIZE = 4;

	public Matrix4x4() {
		super(SIZE, SIZE);
	}
	
	public void translate(double x, double y, double z) {
		values[0][3] = x;
		values[1][3] = y;
		values[2][3] = z;
	}
	
	/**
	 * Rotates around the Y axis
	 * @param rotation how much to rotate in degrees
	 */
	public void rotateY(double rotation) {
		double rad = (rotation * (Math.PI/180));
		values[0][0] = Math.cos(rad);
		values[0][2] = Math.sin(rad);
		values[2][0] = -1 * values[0][2];
		values[2][2] = values[0][0];
	}
	
	/**
	 * Rotates around the X axis
	 * @param rotation how much to rotate in degrees
	 */
	public void rotateX(double rotation) {
		double rad = (rotation * (Math.PI/180));
		values[1][1] = Math.cos(rad);
		values[1][2] = Math.sin(rad);
		values[2][1] = -1 * values[1][2];
		values[2][2] = values[1][1];
	}
	
	/**
	 * Rotates around the Z axis
	 * @param rotation how much to rotate in degrees
	 */
	public void rotateZ(double rotation) {
		double rad = (rotation * (Math.PI/180));
		values[0][0] = Math.cos(rad);
		values[1][0] = Math.sin(rad);
		values[0][1] = -1 * values[1][0];
		values[1][1] = values[0][0];
	}
	
	public void projectPerspective(double fov, double aspect, double near, double far) {
		double rad = (fov/2) * (Math.PI/180);
		double zoomy = 1 / Math.tan(rad);
		double zoomx = zoomy / aspect;
		double c = (far + near) / (far - near);
		double d = (-2 * far * near) / (far - near);
		
		values[0][0] = zoomx;
		values[1][1] = zoomy;
		values[2][2] = c;
		values[2][3] = d;
		values[3][2] = 1;
		values[3][3] = 0;
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
		double value;
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
