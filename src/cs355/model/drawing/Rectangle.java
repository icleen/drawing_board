package cs355.model.drawing;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

/**
 * Add your rectangle code here. You can add fields, but you cannot
 * change the ones that already exist. This includes the names!
 */
public class Rectangle extends Shape {

	// The width of this shape.
	private double width;

	// The height of this shape.
	private double height;

	/**
	 * Basic constructor that sets all fields.
	 * @param color the color for the new shape.
	 * @param center the center of the new shape.
	 * @param width the width of the new shape.
	 * @param height the height of the new shape.
	 */
	public Rectangle(Color color, Point2D.Double center, double width, double height) {

		// Initialize the superclass.
		super(color, center);
		this.type = Shape.SHAPE_TYPE.rectangle;
		// Set fields.
		this.width = width;
		this.height = height;
	}

	/**
	 * Getter for this shape's width.
	 * @return this shape's width as a double.
	 */
	public double getWidth() {
		return width;
	}

	/**
	 * Setter for this shape's width.
	 * @param width the new width.
	 */
	public void setWidth(double width) {
		this.width = width;
	}

	/**
	 * Getter for this shape's height.
	 * @return this shape's height as a double.
	 */
	public double getHeight() {
		return height;
	}

	/**
	 * Setter for this shape's height.
	 * @param height the new height.
	 */
	public void setHeight(double height) {
		this.height = height;
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
		double x = point.x;
		double y = point.y;
		if (x < 0) x *= -1;
		if (y < 0) y *= -1;
		double w = width/2, h = height/2;
		if (x <= w && y <= h) {
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
	public void resetShape(Point2D.Double start, Double end) {
		if (end == null) {
			throw new IllegalArgumentException("the end point is null!");
		}
		double width = 0, height = 0;
		width = start.getX() - end.getX();
		height = start.getY() - end.getY();
		if (width < 0) width *= -1;
		if (height < 0) height *= -1;
		double x = 0, y = 0;
		if (start.getX() < end.getX()) {
			x = start.getX() + width/2;
		}else {
			x = start.getX() - width/2;
		}
		if (start.getY() < end.getY()) {
			y = start.getY() + height/2;
		}else {
			y = start.getY() - height/2;
		}
		Point2D.Double point = new Point2D.Double(x, y);
		this.setCenter(point);
		this.setHeight(height);
		this.setWidth(width);
		this.resetHandle();
	}

}
