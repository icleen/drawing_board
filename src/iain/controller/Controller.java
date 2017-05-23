package iain.controller;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gson.Gson;

import cs355.GUIFunctions;
import cs355.controller.CS355Controller;
import cs355.model.drawing.Circle;
import cs355.model.drawing.Ellipse;
import cs355.model.drawing.Line;
import cs355.model.drawing.Rectangle;
import cs355.model.drawing.Shape;
import cs355.model.drawing.Square;
import cs355.model.drawing.Triangle;
import iain.model.Model;
import iain.model.SaveStructure;
import iain.utilities.Transformer;

public class Controller implements CS355Controller {
	
	private static Gson gson = new Gson();
	
	private static final int INIT_SIZE = 0;
	
	private static enum STATES {
			circle, ellipse, line, rectangle, square, triangle, select, zoomIn, zoomOut
	};
	private STATES currentState;
	private Color currentColor;
	private int currentIndex;

	private List<Point2D.Double> trianglePoints;
	private Shape currentShape;
	private Point2D.Double start;
	
	public Controller() {
		currentState = STATES.select;
		currentColor = Color.WHITE;
//		GUIFunctions.changeSelectedColor(currentColor);
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		if (currentState == STATES.triangle) {
			Point2D.Double p = new Point2D.Double(arg0.getX(), arg0.getY());
			trianglePoints.add(p);
			if (trianglePoints.size() == Model.TOTAL_TRIANGLE_POINTS) {
				Triangle triangle = Model.SINGLETON.setTriangle(currentColor, trianglePoints);
				Model.SINGLETON.addShape(triangle);
				trianglePoints.clear();
				System.out.println("adding triangle");
			}
		}else if (currentState == STATES.select) {
			
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		start = new Point2D.Double();
		start.setLocation(arg0.getX(), arg0.getY());
		switch (currentState) {
		case circle:
			currentShape = new Circle(currentColor, start, INIT_SIZE);
			break;
		case ellipse:
			currentShape = new Ellipse(currentColor, start, INIT_SIZE, INIT_SIZE);
			break;
		case line:
			currentShape = new Line(currentColor, start, start);
			break;
		case rectangle:
			currentShape = new Rectangle(currentColor, start, INIT_SIZE, INIT_SIZE);
			break;
		case square:
			currentShape = new Square(currentColor, start, INIT_SIZE);
			break;
		case select:
			if (currentShape != null && Model.SINGLETON.pointInShape(start, currentShape)) {
				System.out.println("selected currentShape");
				break;
			}else {
				System.out.println("didn't selected currentShape: " + (currentShape == null));
				currentShape = Model.SINGLETON.selectShape(start);
				if (currentShape != null) {
					currentIndex = currentShape.getIndex();
					currentColor = currentShape.getColor();
					GUIFunctions.changeSelectedColor(currentColor);
					
				}else {
					currentIndex = -1;
				}
			}
			break;
		default:
			break;
		}
		if (currentShape != null && this.currentState != STATES.select) {
			currentIndex = Model.SINGLETON.addShape(currentShape);
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		if (currentState != STATES.select) {
			start = null;
			currentShape = null;
			currentIndex = -1;
		}
	}

	@Override
	public void mouseDragged(MouseEvent end) {
		Point2D.Double point = new Point2D.Double(end.getX(), end.getY());
//		System.out.println("start: (" + start.x + ", " + start.y + ")");
//		System.out.println("end: (" + end.getX() + ", " + end.getY() + ")");
		if (currentState != STATES.select && currentState != STATES.triangle) {
			currentShape.resetShape(start, point);
		}
		else if (currentState == STATES.select && currentShape != null) {
			if (!currentShape.isRotating()) {
				double x = point.getX() - start.getX(), y = point.getY() - start.getY();
				x += currentShape.getCenter().getX();
				y += currentShape.getCenter().getY();
				Point2D.Double center = new Point2D.Double(x, y);
				currentShape.setCenter(center);
				start = point;
			}else if (currentShape.getShapeType() != Shape.SHAPE_TYPE.line) {
				Point2D.Double s = new Point2D.Double(0, 0), e = new Point2D.Double(0, 0);
				AffineTransform worldToObj = Transformer.inst().viewToObj(currentShape);
				worldToObj.transform(start, s);
				worldToObj.transform(point, e);
//				System.out.println("start: (" + s.x + ", " + s.y + ")");
//				System.out.println("point: (" + e.x + ", " + e.y + ")");
//				System.out.println("center: (" + currentShape.getCenter().x + ", " + currentShape.getCenter().y + ")");
				double pheta = Math.atan2(e.getY(), e.getX());
				double phi = Math.atan2(s.getY(), s.getX());
				double angle = pheta - phi;
//				System.out.println("pheta: " + pheta + ", phi: " + phi + ", angle: " + angle);
				currentShape.setRotation(currentShape.getRotation() + angle);
//				System.out.println("rotation: " + currentShape.getRotation());
				start = point;
			}else {
				Circle handle = currentShape.getHandle();
				if (handle.shapeSelected()) {
//					System.out.println("handle selected");
					Point2D.Double endPt = ((Line) currentShape).getEnd();
					AffineTransform objToWorld = new AffineTransform();
					objToWorld.translate(currentShape.getCenter().x, currentShape.getCenter().y);
					objToWorld.rotate(currentShape.getRotation());
//					System.out.println(endPt);
					objToWorld.transform(endPt, endPt);
//					System.out.println(endPt);
					currentShape.resetShape(point, endPt);
				}else {
//					System.out.println("end handle selected");
					((Line) currentShape).setEnd(point);
				}
			}
		}
		if (currentShape != null) {
			Model.SINGLETON.setShape(currentIndex, currentShape);
		}
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
	}

	@Override
	public void colorButtonHit(Color c) {
		currentColor = c;
		GUIFunctions.changeSelectedColor(c);
		if (currentShape != null) {
			currentShape.setColor(currentColor);
			Model.SINGLETON.setShape(currentIndex, currentShape);
		}
	}

	@Override
	public void lineButtonHit() {
		currentState = STATES.line;
		start = null;
		currentShape = null;
		currentIndex = -1;
		Model.SINGLETON.deselect();
	}

	@Override
	public void squareButtonHit() {
		currentState = STATES.square;
		start = null;
		currentShape = null;
		currentIndex = -1;
		Model.SINGLETON.deselect();
	}

	@Override
	public void rectangleButtonHit() {
		currentState = STATES.rectangle;
		start = null;
		currentShape = null;
		currentIndex = -1;
		Model.SINGLETON.deselect();
	}

	@Override
	public void circleButtonHit() {
		currentState = STATES.circle;
		start = null;
		currentShape = null;
		currentIndex = -1;
		Model.SINGLETON.deselect();
	}

	@Override
	public void ellipseButtonHit() {
		currentState = STATES.ellipse;
		start = null;
		currentShape = null;
		currentIndex = -1;
		Model.SINGLETON.deselect();
	}

	@Override
	public void triangleButtonHit() {
		currentState = STATES.triangle;
		trianglePoints = new ArrayList<>();
		start = null;
		currentShape = null;
		currentIndex = -1;
		Model.SINGLETON.deselect();
	}

	@Override
	public void selectButtonHit() {
		currentState = STATES.select;
		start = null;
		currentShape = null;
		currentIndex = -1;
		Model.SINGLETON.deselect();
	}

	@Override
	public void zoomInButtonHit() {
		Transformer.inst().zoomIn();
		GUIFunctions.setZoomText(Transformer.inst().getZoom());
	}

	@Override
	public void zoomOutButtonHit() {
		Transformer.inst().zoomOut();
		GUIFunctions.setZoomText(Transformer.inst().getZoom());
	}

	@Override
	public void hScrollbarChanged(int value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void vScrollbarChanged(int value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void openScene(File file) {
		// TODO Auto-generated method stub

	}

	@Override
	public void toggle3DModelDisplay() {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(Iterator<Integer> iterator) {
		// TODO Auto-generated method stub

	}

	@Override
	public void openImage(File file) {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveImage(File file) {
		// TODO Auto-generated method stub

	}

	@Override
	public void toggleBackgroundDisplay() {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveDrawing(File file) {
		SaveStructure save = new SaveStructure();
		save.fromModel();
		try {
			Writer writer = new BufferedWriter( new FileWriter( file ) );
			gson.toJson(save, writer);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void openDrawing(File file) {
		Reader reader = null;
		try {
			reader = new BufferedReader( new FileReader( file ) );
			SaveStructure save = (SaveStructure) gson.fromJson(reader, SaveStructure.class);
			save.toModel();
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void doDeleteShape() {
		if (currentShape != null) {
			Model.SINGLETON.deleteShape(currentIndex);
			currentShape = null;
			currentIndex = -1;
			Model.SINGLETON.deselect();
		}
	}

	@Override
	public void doEdgeDetection() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doSharpen() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doMedianBlur() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doUniformBlur() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doGrayscale() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doChangeContrast(int contrastAmountNum) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doChangeBrightness(int brightnessAmountNum) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doMoveForward() {
		if (currentShape != null) {
			Model.SINGLETON.moveForward(currentIndex);
			currentIndex = currentShape.getIndex();
		}
	}

	@Override
	public void doMoveBackward() {
		if (currentShape != null) {
			Model.SINGLETON.moveBackward(currentIndex);
			currentIndex = currentShape.getIndex();
		}
	}

	@Override
	public void doSendToFront() {
		if (currentShape != null) {
			Model.SINGLETON.moveToFront(currentIndex);
			currentIndex = currentShape.getIndex();
		}
	}

	@Override
	public void doSendtoBack() {
		if (currentShape != null) {
			Model.SINGLETON.movetoBack(currentIndex);
			currentIndex = currentShape.getIndex();
		}
	}

}
