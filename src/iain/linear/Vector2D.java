package iain.linear;

import java.awt.geom.Point2D;

public class Vector2D extends Vector {
	
	private static final int DIMENSION = 2;
	
	public Vector2D() {
		super(DIMENSION);
	}
	
	public Vector2D(double x, double y) {
		super(DIMENSION);
		values[0] = x;
		values[1] = y;
	}
	
	public Vector2D(Point2D.Double pt) {
		super(DIMENSION);
		values[0] = pt.x;
		values[1] = pt.y;
	}

	public void setValues(double x, double y) {
		values[0] = x;
		values[1] = y;
	}
	
	public Vector2D addVector(Vector2D v) {
		Vector2D result = new Vector2D();
		for (int i = 0; i < DIMENSION; i++) {
			result.values[i] = values[i] + v.values[i];
		}
		return result;
	}
	
	public Vector2D subtractVector(Vector2D v) {
		Vector2D result = new Vector2D();
		for (int i = 0; i < DIMENSION; i++) {
			result.values[i] = values[i] - v.values[i];
		}
		return result;
	}
	
	public Vector2D multiply(double d) {
		Vector2D result = new Vector2D();
		for (int i = 0; i < DIMENSION; i++) {
			result.values[i] = values[i] * d;
		}
		return result;
	}
	
	public Vector2D perpendicular() {
		Vector2D result = new Vector2D(values[1], values[0] * -1);
		return result;
	}
	
	public String toString() {
		return "(" + values[0] + ", " + values[1] + ")";
	}
	
}
