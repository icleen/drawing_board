package cs355.model.drawing;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import iain.linear.Vector2D;

/**
 * Add your line code here. You can add fields, but you cannot
 * change the ones that already exist. This includes the names!
 */
public class Line extends Shape {

	// The ending point of the line.
	private Point2D.Double end;

	/**
	 * Basic constructor that sets all fields.
	 * @param color the color for the new shape.
	 * @param start the starting point.
	 * @param end the ending point.
	 */
	public Line(Color color, Point2D.Double start, Point2D.Double end) {

		// Initialize the superclass.
		super(color, start);
		this.type = Shape.SHAPE_TYPE.line;
		// Set the field.
		this.end = new Point2D.Double(end.x - start.x, end.y - start.y);
	}

	/**
	 * Getter for this Line's ending point.
	 * @return the ending point as a Java point.
	 */
	public Point2D.Double getEnd() {
		return end;
	}

	/**
	 * Setter for this Line's ending point.
	 * @param end the new ending point for the Line.
	 */
	public void setEnd(Point2D.Double end) {
		AffineTransform worldToObj = new AffineTransform();
		worldToObj.rotate(this.getRotation() * -1);
		worldToObj.translate(this.getCenter().x * -1, this.getCenter().y * -1);
		this.end = new Point2D.Double(0, 0);
		worldToObj.transform(end, this.end);
	}

	/**
	 * Add your code to do an intersection test
	 * here. You <i>will</i> need the tolerance.
	 * @param pt = the point to test against.
	 * @param tolerance = the allowable tolerance.
	 * @return true if pt is in the shape,
	 *		   false otherwise.
	 */
	@Override
	public boolean pointInShape(Point2D.Double pt, double tolerance) {
		Vector2D p0 = new Vector2D(0, 0);
		Vector2D p1 = new Vector2D(end.getX(), end.getY());
		Vector2D q = new Vector2D(pt);
		Vector2D d = p1.subtractVector(p0);
		double length = d.length();
		d.normalize();
		double t = d.dotProduct(q.subtractVector(p0));
//		System.out.println("t: " + t);
		if (t > length || t < 0) {
			this.isSelected = false;
		}else {
			Vector2D q1 = (Vector2D) p0.addVector( d.multiply( q.subtractVector(p0).dotProduct(d) ) );
			double distance = q.subtractVector(q1).length();
//			System.out.println("distance: " + distance);
			if (distance <= tolerance) {
				this.isSelected = true;
			}else {
				this.isSelected = false;
			}
		}
		return this.isSelected;
	}

	@Override
	public void resetShape(Point2D.Double start, Point2D.Double end) {
		assert(start.getX() == this.center.getX());
		assert(start.getY() == this.center.getY());
		this.setEnd(end);
		this.resetHandle();
	}

	@Override
	public double getWidth() {
		return 0;
	}

	@Override
	public double getHeight() {
		return 0;
	}

}
