package cs355.model.drawing;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

/**
 * Add your square code here. You can add fields, but you cannot
 * change the ones that already exist. This includes the names!
 */
public class Square extends Shape {

	// The size of this Square.
	private double size;

	/**
	 * Basic constructor that sets all fields.
	 * @param color the color for the new shape.
	 * @param center the center of the new shape.
	 * @param size the size of the new shape.
	 */
	public Square(Color color, Point2D.Double center, double size) {

		// Initialize the superclass.
		super(color, center);
		this.type = Shape.SHAPE_TYPE.square;
		// Set the field.
		this.size = size;
	}

	/**
	 * Getter for this Square's size.
	 * @return the size as a double.
	 */
	public double getSize() {
		return size;
	}

	/**
	 * Setter for this Square's size.
	 * @param size the new size.
	 */
	public void setSize(double size) {
		this.size = size;
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
		double radius = size/2;
		if (x <= radius && y <= radius) {
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
		if (end == null) {
			throw new IllegalArgumentException("the end point is null!");
		}
		double width = start.getX() - end.getX();
		double height = start.getY() - end.getY();
		if (width < 0) width *= -1;
		if (height < 0) height *= -1;
		double size = 0;
		if (width < height) {
			size = width;
		}else {
			size = height;
		}
		double x = 0, y = 0;
		if (start.getX() < end.getX()) {
			x = start.getX() + size/2;
		}else {
			x = start.getX() - size/2;
		}
		if (start.getY() < end.getY()) {
			y = start.getY() + size/2;
		}else {
			y = start.getY() - size/2;
		}
		Point2D.Double center = new Point2D.Double(x, y);
		this.setCenter(center);
		this.setSize(size);
		this.resetHandle();
	}

	@Override
	public double getWidth() {
		return size;
	}

	@Override
	public double getHeight() {
		return size;
	}

}
