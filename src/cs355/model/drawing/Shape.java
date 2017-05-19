package cs355.model.drawing;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

/**
 * This is the base class for all of your shapes.
 * Make sure they all extend this class.
 */
public abstract class Shape {
	
	private static final int ORIGIN = 0;
	protected static final double HANDLE_RADIUS = 5;
	
	public enum SHAPE_TYPE { 
		circle, ellipse, line, rectangle, square, triangle
	}
	
	protected SHAPE_TYPE type;
	protected boolean isSelected = false;
	protected boolean rotating = false;
	
	private int index;
	
	protected Circle handle;

	// The color of this shape.
	protected Color color;

	// The center of this shape.
	protected Point2D.Double center;

	// The rotation of this shape.
	protected double rotation;

	/**
	 * Basic constructor that sets fields.
	 * It initializes rotation to 0.
	 * @param color the color for the new shape.
	 * @param center the center point of the new shape.
	 */
	public Shape(Color color, Point2D.Double center) {
		this.color = color;
		this.center = center;
		rotation = 0.0;
	}

	/**
	 * Getter for this shape's color.
	 * @return the color of this shape.
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Setter for this shape's color
	 * @param color the new color for the shape.
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * Getter for this shape's center.
	 * @return this shape's center as a Java point.
	 */
	public Point2D.Double getCenter() {
		return center;
	}

	/**
	 * Setter for this shape's center.
	 * @param center the new center as a Java point.
	 */
	public void setCenter(Point2D.Double center) {
		this.center = center;
		resetHandle();
	}

	/**
	 * Getter for this shape's rotation.
	 * @return the rotation as a double.
	 */
	public double getRotation() {
		return rotation;
	}

	/**
	 * Setter for this shape's rotation.
	 * @param rotation the new rotation.
	 */
	public void setRotation(double rotation) {
		this.rotation = rotation;
	}

	/**
	 * Used to test for whether the user clicked inside a shape or not.
	 * @param pt = the point to test whether it's in the shape or not.
	 * @param tolerance = the tolerance for testing. Mostly used for lines.
	 * @return true if pt is in the shape, false otherwise.
	 */
	public abstract boolean pointInShape(Point2D.Double pt, double tolerance);
	
	public boolean pointInHandle(Point2D.Double point, double tolerance) {
		Point2D.Double objCoord = new Point2D.Double(0, 0);
		AffineTransform worldToObj = new AffineTransform();
		worldToObj.rotate(this.rotation * -1);
		worldToObj.translate(handle.getCenter().x * -1, handle.getCenter().y * -1);
		worldToObj.transform(point, objCoord);
//		System.out.println("point: (" + point.x + ", " + point.y + ")");
//		System.out.println("objCoord: (" + objCoord.x + ", " + objCoord.y + ")");
		rotating = handle.pointInShape(objCoord, tolerance);
		return rotating;
	}
	
	public abstract void resetShape(Point2D.Double start, Point2D.Double end);
	
	public SHAPE_TYPE getShapeType() { return type; }
	
	public int getIndex() {
		return index;
	}
	
	public void setIndex(int index) {
		this.index = index;
	}
	
	public boolean shapeSelected() {
		return isSelected;
	}
	
	public void setShapeSelected(boolean setting) {
		isSelected = setting;
	}
	
	public Circle getHandle() {
		return handle;
	}
	
	public void resetHandle() {
		Point2D.Double handleCoor = new Point2D.Double(ORIGIN, ORIGIN - (getHeight()/2 + HANDLE_RADIUS*3));
		handle = new Circle(Color.RED, handleCoor, HANDLE_RADIUS);
//		System.out.println("handle: (" + handleCoor.getX() + ", " + handleCoor.getY() + "), " + handle.getRadius());
	}
	
	public boolean isRotating() {
		return rotating;
	}
	
	public void setRotating(boolean set) {
		this.rotating = set;
	}
	
	public abstract double getWidth();
	public abstract double getHeight();
	
	public String toString() {
		return "(" + center.x + ", " + center.y + ")"; 
	}
	
}
