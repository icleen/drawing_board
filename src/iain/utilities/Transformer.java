package iain.utilities;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Observable;

import cs355.model.drawing.Shape;

public class Transformer extends Observable {
	
	
	private static Transformer SINGLETON = new Transformer();
	private static final double DEFAULT_ZOOM = 1;
	
	private Point2D.Double screen_center;
	private double zoom;
	
	private Transformer() {
		screen_center = new Point2D.Double(0, 0);
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
		AffineTransform worldToView = new AffineTransform(zoom, 0, 0, zoom, 0, 0);
		worldToView.concatenate(new AffineTransform(1, 0, 0, 1, -1 * screen_center.x, -1 * screen_center.y));
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
		worldToObj.translate(s.getCenter().getX() * -1, s.getCenter().getY() * -1);
		return worldToObj;
	}
	
	public AffineTransform viewToWorld() {
		AffineTransform viewToWorld = new AffineTransform(1, 0, 0, 1, screen_center.x, screen_center.y);
		viewToWorld.concatenate(new AffineTransform(1/zoom, 0, 0, 1/zoom, 0, 0));
		return viewToWorld;
	}
	
	public void zoomIn() {
		if (zoom < 4) zoom *= 2;
		this.setChanged();
		this.notifyObservers();
	}
	
	public void zoomOut() {
		if (zoom > 0.25) zoom *= 0.5;
		this.setChanged();
		this.notifyObservers();
	}
	
	public double getZoom() {
		return zoom;
	}

}
