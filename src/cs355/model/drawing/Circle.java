package cs355.model.drawing;

import java.awt.Color;
import java.awt.geom.Point2D;

import iain.utilities.Vector2D;

/**
 * Add your circle code here. You can add fields, but you cannot
 * change the ones that already exist. This includes the names!
 */
public class Circle extends Shape {

	// The radius.
	private double radius;

	/**
	 * Basic constructor that sets all fields.
	 * @param color the color for the new shape.
	 * @param center the center of the new shape.
	 * @param radius the radius of the new shape.
	 */
	public Circle(Color color, Point2D.Double center, double radius) {

		// Initialize the superclass.
		super(color, center);
		this.type = Shape.SHAPE_TYPE.circle;
		// Set the field.
		this.radius = radius;
	}

	/**
	 * Getter for this Circle's radius.
	 * @return the radius of this Circle as a double.
	 */
	public double getRadius() {
		return radius;
	}

	/**
	 * Setter for this Circle's radius.
	 * @param radius the new radius of this Circle.
	 */
	public void setRadius(double radius) {
		this.radius = radius;
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
		Vector2D p = new Vector2D(point);
		if(p.length() <= radius) {
//			System.out.println("length: " + p.toString() + ", radius: " + radius);
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
	public void resetShape(Point2D.Double start, Point2D.Double end) {
		if (end == null) {
			throw new IllegalArgumentException("the end point is null!");
		}
		double width = start.getX() - end.getX();
		if (width < 0) width *= -1;
		double height = start.getY() - end.getY();
		if (height < 0) height *= -1;
		
//		System.out.println("width: " + width + ", height: " + height);
		
		double radius = 0;
		double x = 0, y = 0;
		// check to see which one is smaller and make the radius the same size as that one
		// then set the center a radius length away from the start point
		if (height < width) {
			radius =  height / 2;
			y = (start.getY() + end.getY()) / 2;
			if (start.getX() < end.getX()) {
				x = start.getX() + radius;
			}else {
				x = start.getX() - radius;
			}
		}else {
			radius =  width / 2;
			x = (start.getX() + end.getX()) / 2;
			if (start.getY() < end.getY()) {
				y = start.getY() + radius;
			}else {
				y = start.getY() - radius;
			}
		}
//		System.out.println("radius: " + radius);
//		System.out.println("xy: (" + x + ", " + y + ")");
		Point2D.Double center = new Point2D.Double(x, y);
		this.setRadius(radius);
		this.setCenter(center);
		this.resetHandle();
	}

	@Override
	public double getWidth() {
		return radius * 2;
	}

	@Override
	public double getHeight() {
		return radius * 2;
	}

}
