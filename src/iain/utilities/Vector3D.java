package iain.utilities;

public class Vector3D extends Vector {
	
	private static final int DIMENSION = 4;
	
	public Vector3D() {
		super(DIMENSION);
	}
	
	public void setValues(double x, double y, double z, double w) {
		values[0] = x;
		values[1] = y;
		values[2] = z;
		values[3] = w;
	}
	
	public Vector3D addVector(Vector3D v) {
		Vector3D result = new Vector3D();
		for (int i = 0; i < DIMENSION; i++) {
			result.values[i] = this.values[i] + v.values[i];
		}
		return result;
	}
	
	public Vector3D subtractVector(Vector3D v) {
		Vector3D result = new Vector3D();
		for (int i = 0; i < DIMENSION; i++) {
			result.values[i] = this.values[i] - v.values[i];
		}
		return result;
	}
	
	public Vector3D multiply(double d) {
		Vector3D result = new Vector3D();
		for (int i = 0; i < DIMENSION; i++) {
			result.values[i] = this.values[i] * d;
		}
		return result;
	}
	
	public Vector3D vectorMultiply(Vector3D v) {
		Vector3D result = new Vector3D();
		for (int i = 0; i < DIMENSION; i++) {
			result.values[i] = this.values[i] * v.values[i];
		}
		return result;
	}
	
	public String toString() {
		return "(" + values[0] + ", " + values[1] + ", " + values[2] + ", " + values[3] + ")";
	}

}
