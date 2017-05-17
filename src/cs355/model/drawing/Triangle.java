package cs355.model.drawing;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

import iain.linear.Vector2D;
import iain.model.Model;

/**
 * Add your triangle code here. You can add fields, but you cannot
 * change the ones that already exist. This includes the names!
 */
public class Triangle extends Shape {

	// The three points of the triangle.
	private Point2D.Double a;
	private Point2D.Double b;
	private Point2D.Double c;

	/**
	 * Basic constructor that sets all fields.
	 * @param color the color for the new shape.
	 * @param center the center of the new shape.
	 * @param a the first point, relative to the center.
	 * @param b the second point, relative to the center.
	 * @param c the third point, relative to the center.
	 */
	public Triangle(Color color, Point2D.Double center, Point2D.Double a,
					Point2D.Double b, Point2D.Double c)
	{

		// Initialize the superclass.
		super(color, center);
		this.type = Shape.SHAPE_TYPE.triangle;
		// Set fields.
		this.a = a;
		this.b = b;
		this.c = c;
		this.resetHandle();
	}

	/**
	 * Getter for the first point.
	 * @return the first point as a Java point.
	 */
	public Point2D.Double getA() {
		return a;
	}

	/**
	 * Setter for the first point.
	 * @param a the new first point.
	 */
	public void setA(Point2D.Double a) {
		this.a = a;
	}

	/**
	 * Getter for the second point.
	 * @return the second point as a Java point.
	 */
	public Point2D.Double getB() {
		return b;
	}

	/**
	 * Setter for the second point.
	 * @param b the new second point.
	 */
	public void setB(Point2D.Double b) {
		this.b = b;
	}

	/**
	 * Getter for the third point.
	 * @return the third point as a Java point.
	 */
	public Point2D.Double getC() {
		return c;
	}

	/**
	 * Setter for the third point.
	 * @param c the new third point.
	 */
	public void setC(Point2D.Double c) {
		this.c = c;
	}

	/**
	 * Add your code to do an intersection test
	 * here. You shouldn't need the tolerance.
	 * @param pt = the point to test against.
	 * @param tolerance = the allowable tolerance.
	 * @return true if pt is in the shape,
	 *		   false otherwise.
	 */
	@Override
	public boolean pointInShape(Point2D.Double point, double tolerance) {
		Vector2D q = new Vector2D(point);
		Vector2D p0 = new Vector2D(a);
		Vector2D p1 = new Vector2D(b);
		Vector2D p2 = new Vector2D(c);
		
		double result1 = q.subtractVector(p0).dotProduct(p1.subtractVector(p0).perpendicular());
		double result2 = q.subtractVector(p1).dotProduct(p2.subtractVector(p1).perpendicular());;
		double result3 = q.subtractVector(p2).dotProduct(p0.subtractVector(p2).perpendicular());;
		if (result1 < 0 && result2 < 0 && result3 < 0) {
			this.isSelected = true;
		}else if (result1 > 0 && result2 > 0 && result3 > 0) {
			this.isSelected = true;
			this.rotating = false;
		}else if (handle != null && this.isSelected) {
			this.isSelected = this.pointInHandle(point, tolerance);
		}else {
			this.isSelected = false;
			this.rotating = false;
		}
		return this.isSelected;
	}

	@Override
	public void resetShape(Double start, Double end) {
		this.resetHandle();
	}

	@Override
	public double getWidth() {
		return 0;
	}

	@Override
	public double getHeight() {
		if (a.y < b.y && a.y < c.y) {
			return a.y * -2;
		}else if (b.y < a.y && b.y < c.y) {
			return b.y * -2;
		}else {
			return c.y * -2;
		}
	}
	
	public int[] getXCoordinates() {
		int[] points = new int[Model.TOTAL_TRIANGLE_POINTS];
		points[0] = (int) (a.x);
		points[1] = (int) (b.x);
		points[2] = (int) (c.x);
		return points;
	}
	
	public int[] getYCoordinates() {
		int[] points = new int[Model.TOTAL_TRIANGLE_POINTS];
		points[0] = (int) (a.y);
		points[1] = (int) (b.y);
		points[2] = (int) (c.y);
		return points;
	}

}
