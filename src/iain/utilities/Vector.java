package iain.utilities;

public class Vector {
	
	protected double[] values;
	private int dimension;
	
	public Vector(int dimension) {
		this.dimension = dimension;
		values = new double[dimension];
	}
	
	public Vector(int dimension, double[] values) {
		this.dimension = dimension;
		this.values = values;
	}
	
	public void setValue(int index, double value) {
		values[index] = value;
	}
	
	public double getValue(int index) {
		return values[index];
	}

	public double[] getValues() {
		return values;
	}
	
	public void setValues(double[] values) {
		this.values = values;
	}
	
	public Vector addVector(Vector v) {
		assert(v.dimension == this.dimension);
		Vector result = new Vector(this.dimension);
		for (int i = 0; i < dimension; i++) {
			result.values[i] = values[i] + v.values[i];
		}
		return result;
	}
	
	public Vector subtractVector(Vector v) {
		assert(v.dimension == this.dimension);
		Vector result = new Vector(this.dimension);
		for (int i = 0; i < dimension; i++) {
			result.values[i] = values[i] - v.values[i];
		}
		return result;
	}
	
	public Vector multiply(double d) {
		Vector result = new Vector(this.dimension);
		for (int i = 0; i < dimension; i++) {
			result.values[i] = values[i] * d;
		}
		return result;
	}
	
	public Vector vectorMultiply(Vector v) {
		Vector result = new Vector(this.dimension);
		for (int i = 0; i < v.length(); i++) {
			result.values[i] = this.values[i] * v.values[i];
		}
		return result;
	}
	
	public double dotProduct(Vector v) {
		int total = 0;
		for (int i = 0; i < dimension; i++) {
			total += values[i] * v.values[i];
		}
		return total;
	}
	
	public double length() {
		int total = 0;
		for (int i = 0; i < dimension; i++) {
			total += values[i] * values[i];
		}
		return Math.sqrt(total);
	}
	
	public void normalize() {
		double length = length();
		for (int i = 0; i < dimension; i++) {
			values[i] = values[i] / length;
		}
	}
	
	public int getDimension() {
		return dimension;
	}
	
	public boolean equals(Vector v) {
		if (this.dimension != v.dimension) {
			return false;
		}
		for (int i = 0; i < this.dimension; i++) {
			if (this.values[i] != v.values[i]) {
				return false;
			}
		}
		return true;
	}
	
	public String toString() {
		StringBuilder ss = new StringBuilder();
		ss.append("(");
		for (int i = 0; i < this.dimension; i++) {
			ss.append(values[i]);
			if (i < this.dimension - 1) {
				ss.append(", ");
			}
		}
		ss.append(")");
		return ss.toString();
	}
	
	public static double getSquaredLength(int[] a) {
		int total = 0;
		for (int i = 0; i < a.length; i++) {
			total += a[i] * a[i];
		}
		return total;
	}

}
