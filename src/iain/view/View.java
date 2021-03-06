package iain.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Observable;

import cs355.GUIFunctions;
import cs355.model.drawing.Circle;
import cs355.model.drawing.Line;
import cs355.model.drawing.Shape;
import cs355.model.drawing.Triangle;
import cs355.view.ViewRefresher;
import iain.controller.Controller;
import iain.model.Model;
import iain.utilities.Transformer;
import iain.utilities.Transformer3D;

public class View implements ViewRefresher {
	
	private static final int BORDER_SIZE = 1;
	private static final int LINE_SIZE = 1;
	private static final int ORIGIN = 0;
	
	private static final int CENTER_X = 2048/2;
	private static final int CENTER_Y = 2048/2;
	
	private Shape selected;
	
	private BufferedImage img;
	
	public View() {
		Model.SINGLETON.addObserver(this);
		Image.SINGLETON.addObserver(this);
		Transformer.inst().addObserver(this);
		Transformer3D.SINGLETON.addObserver(this);
		Draw3D.SINGLETON.addObserver(this);
		Controller.scene.addObserver(this);
	}

	@Override
	public void update(Observable o, Object arg) {
		GUIFunctions.refresh();
	}

	@Override
	public void refreshView(Graphics2D g2d) {
		System.out.println("refresh");
		if (Image.SINGLETON.hasChanged()) {
			img = Image.SINGLETON.getImage();
//			System.out.println("got image");
		}
		if (img != null && Image.SINGLETON.getDrawMode()) {
			g2d.setTransform(Transformer.inst().worldToView());
			g2d.drawImage(img, null, CENTER_X - Image.SINGLETON.getWidth()/2, CENTER_Y - Image.SINGLETON.getHeight()/2);
		}
		
		List<Shape> shapes = Model.SINGLETON.getShapes();
		Shape.SHAPE_TYPE type = null;
		AffineTransform objToView = null;
		for (Shape s : shapes) {
			type = s.getShapeType();
			int width = (int) s.getWidth(), height = (int) s.getHeight();
			objToView = Transformer.inst().objToView(s);
			g2d.setTransform(objToView);
			g2d.setColor(s.getColor());
			switch (type) {
			case circle:
				g2d.fillOval(ORIGIN - (width/2), ORIGIN - (height/2), width, height);
				break;
			case ellipse:
				g2d.fillOval(ORIGIN - (width/2), ORIGIN - (height/2), width, height);
				break;
			case line:
				g2d.setStroke(new BasicStroke(LINE_SIZE));
				Point2D.Double end = ((Line) s).getEnd();
				g2d.drawLine(ORIGIN, ORIGIN, (int) end.x, (int) end.y);
				break;
			case rectangle:
				g2d.fillRect(ORIGIN - (width/2), ORIGIN - (height/2), width, height);
				break;
			case square:
				g2d.fillRect(ORIGIN - (width/2), ORIGIN - (height/2), width, height);
				break;
			case triangle:
				int[] xPoints = ((Triangle) s).getXCoordinates();
				int[] yPoints = ((Triangle) s).getYCoordinates();
				g2d.fillPolygon(xPoints, yPoints, Model.TOTAL_TRIANGLE_POINTS);
				break;
			default:
				break;
			}
			if (s.shapeSelected()) {
				selected = s;
			}
		}
		if (selected != null) {
			objToView = Transformer.inst().objToView(selected);
			g2d.setTransform(objToView);
			g2d.setColor(Color.red);
			g2d.setStroke(new BasicStroke(BORDER_SIZE));
			if (selected.getShapeType() == Shape.SHAPE_TYPE.line) {
				Circle startHandle = selected.getHandle();
				Circle endHandle = ((Line) selected).getEndHandle();
				Point2D.Double end = ((Line) selected).getEnd();
				g2d.drawOval(ORIGIN - (int) startHandle.getRadius(), ORIGIN - (int) startHandle.getRadius(),
						(int) startHandle.getWidth(), (int) startHandle.getHeight());
				g2d.drawOval((int) (end.x - endHandle.getRadius()), (int) (end.y - endHandle.getRadius()),
						(int) endHandle.getWidth(), (int) endHandle.getHeight());
			}
			else {
				int width = (int) selected.getWidth(), height = (int) selected.getHeight();
				if (selected.getShapeType() == Shape.SHAPE_TYPE.triangle) {
					g2d.drawPolygon(((Triangle) selected).getXCoordinates(), 
							((Triangle) selected).getYCoordinates(), Model.TOTAL_TRIANGLE_POINTS);
				}
				else if (selected.getShapeType() != Shape.SHAPE_TYPE.line) {
					g2d.drawRect(ORIGIN - (width/2), ORIGIN - (height/2), width, height);
				}
				Circle handle = selected.getHandle();
				g2d.drawOval((int) (handle.getCenter().x - handle.getRadius()), (int) (handle.getCenter().y - handle.getRadius()), 
						(int) handle.getWidth(), (int) handle.getHeight());
			}
		}
		selected = null;
		g2d.setTransform(Transformer.inst().worldToView());
		Draw3D.SINGLETON.draw(g2d);
	}

}
