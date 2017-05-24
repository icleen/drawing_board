package iain.utilities;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Observable;

import cs355.GUIFunctions;
import cs355.model.drawing.Shape;

public class Transformer extends Observable {
	
	
	private static Transformer SINGLETON = new Transformer();
	private static final double DEFAULT_ZOOM = 1;
	public static final int DEFAULT_SCREEN_SIZE = 512;
	public static final int LARGEST_SCREEN_SIZE = 2048;
	
	private Point2D.Double screen_center;
	private double zoom;
	
	private Transformer() {
		screen_center = new Point2D.Double(LARGEST_SCREEN_SIZE/2, LARGEST_SCREEN_SIZE/2);
		zoom = DEFAULT_ZOOM;
	}
	
	public static Transformer inst() {
		return SINGLETON;
	}
	
	public AffineTransform objToView(Shape s) {
		AffineTransform objToView = worldToView();
		objToView.concatenate(objToWorld(s));
		return objToView;
	}
	
	public AffineTransform objToWorld(Shape s) {
		AffineTransform objToWorld = new AffineTransform(1, 0, 0, 1, s.getCenter().x, s.getCenter().y);
		objToWorld.concatenate(new AffineTransform(Math.cos(s.getRotation()), Math.sin(s.getRotation()), 
				-1 * Math.sin(s.getRotation()), Math.cos(s.getRotation()), 
				0, 0));
		return objToWorld;
	}
	
	public AffineTransform worldToView() {
		double x = screen_center.x - (getScreenSize()/2), y = screen_center.y - (getScreenSize()/2);
		AffineTransform worldToView = new AffineTransform(zoom, 0, 0, zoom, 0, 0);
		worldToView.concatenate(new AffineTransform(1, 0, 0, 1, -1 * x, -1 * y));
		return worldToView;
	}
	
//	*********************************************************************************
	
	public AffineTransform viewToObj(Shape s) {
		AffineTransform viewToObj = worldToObj(s);
		viewToObj.concatenate(viewToWorld());
		return viewToObj;
	}
	
	public AffineTransform worldToObj(Shape s) {
		AffineTransform worldToObj = new AffineTransform(Math.cos(s.getRotation()), Math.sin(-1 * s.getRotation()), 
				-1 * Math.sin(-1 * s.getRotation()), Math.cos(s.getRotation()), 
				0, 0);
		worldToObj.concatenate(new AffineTransform(1, 0, 0, 1, s.getCenter().getX() * -1, s.getCenter().getY() * -1));
		return worldToObj;
	}
	
	public AffineTransform viewToWorld() {
		double x = screen_center.x - (getScreenSize()/2), y = screen_center.y - (getScreenSize()/2);
		AffineTransform viewToWorld = new AffineTransform(1, 0, 0, 1, x, y);
		viewToWorld.concatenate(new AffineTransform(1/zoom, 0, 0, 1/zoom, 0, 0));
		return viewToWorld;
	}
	
//	*********************************************************************************
	
	public Point2D.Double getCenter() {
		return screen_center;
	}
	
	public void zoomIn() {
		if (zoom < 4) zoom *= 2;
		System.out.println("center: (" + screen_center.x + ", " + screen_center.y + ")");
		GUIFunctions.setHScrollBarPosit(((int) screen_center.x - getScreenSize()/2));
		System.out.println("screenSize: " + getScreenSize());
		GUIFunctions.setVScrollBarPosit(((int) screen_center.y - getScreenSize()/2));
		System.out.println("new center (y): " + ((int) screen_center.y - getScreenSize()/2));
		this.setChanged();
		this.notifyObservers();
	}
	
	public void zoomOut() {
		if (zoom > 0.25) zoom *= 0.5;
		System.out.println("center: (" + screen_center.x + ", " + screen_center.y + ")");
		System.out.println("screenSize: " + getScreenSize());
		System.out.println("new center (y): " + ((int) screen_center.y - getScreenSize()/2));
		int test = LARGEST_SCREEN_SIZE - getScreenSize();
		if (screen_center.x - getScreenSize()/2 < test) {
			GUIFunctions.setHScrollBarPosit((int) screen_center.x - getScreenSize()/2);
		}else {
			GUIFunctions.setHScrollBarPosit(test);
		}
		if (screen_center.y - getScreenSize()/2 < test) {
			GUIFunctions.setVScrollBarPosit((int) screen_center.y - getScreenSize()/2);
		}else {
			GUIFunctions.setVScrollBarPosit(test);
		}
		this.setChanged();
		this.notifyObservers();
	}
	
	public double getZoom() {
		return zoom;
	}
	
	public int getScreenSize() {
		return (int) (1/zoom * Transformer.DEFAULT_SCREEN_SIZE);
	}
	
	public void setHorizontal(int position) {
		System.out.println("horizontal: " + position);
		System.out.println("point: " + (position + getScreenSize()/2));
//		int test = LARGEST_SCREEN_SIZE - getScreenSize();
//		System.out.println("test: " + test);
//		if (position <= test) {
//			screen_center.x = position;
//		}else {
//			screen_center.x = test;
//			GUIFunctions.setHScrollBarPosit(test);
//		}
		if (position == ((int) screen_center.x - getScreenSize()/2)) {
			screen_center.x = position + getScreenSize()/2;
			System.out.println("center: (" + screen_center.x + ", " + screen_center.y + ")");
			this.setChanged();
			this.notifyObservers();
		}
	}
	
	public void setVertical(int position) {
		System.out.println("point: " + (position + getScreenSize()/2));
		if (position == ((int) screen_center.y - getScreenSize()/2)) {
			screen_center.y = position + getScreenSize()/2;
			System.out.println("center: (" + screen_center.x + ", " + screen_center.y + ")");
			this.setChanged();
			this.notifyObservers();
		}
	}

}
