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
import cs355.model.image.CS355Image;
import cs355.model.scene.CS355Scene;
import iain.model.Model;
import iain.model.SaveStructure;
import iain.utilities.Transformer;
import iain.utilities.Transformer3D;
import iain.view.Draw3D;
import iain.view.Image;

public class Controller implements CS355Controller {
	
	private double rotationX;
	private double rotationY;
	private double rotationZ;
	private double translationX;
	private double translationY;
	private double translationZ;
	
	private final int FAR = 500;
	private final int NEAR = 1;
	
	private final int KEY_A = 65;
	private final int KEY_S = 83;
	private final int KEY_D = 68;
	private final int KEY_Q = 81;
	private final int KEY_W = 87;
	private final int KEY_E = 69;
	private final int KEY_R = 82;
	private final int KEY_F = 70;
	private final int KEY_H = 72;
	private final int KEY_O = 79;
	private final int KEY_P = 80;
	
	private final int CHANGE_AMOUNT = 2;
	
	private static Gson gson = new Gson();
	
	private static final int INIT_SIZE = 0;
	
	private static enum STATES {
			circle, ellipse, line, rectangle, square, triangle, select
	};
	
	
	public static CS355Scene scene = new CS355Scene();
	private boolean drawMode3D;
	private boolean imageMode;
	
	private STATES currentState;
	private Color currentColor;
	private int currentIndex;

	private List<Point2D.Double> trianglePoints;
	private Shape currentShape;
	private Point2D.Double start;
	
	public Controller() {
		currentState = STATES.select;
		currentColor = Color.WHITE;
		drawMode3D = false;
		imageMode = false;
//		GUIFunctions.changeSelectedColor(currentColor);
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		if (currentState == STATES.triangle) {
			Point2D.Double p = new Point2D.Double(arg0.getX(), arg0.getY());
			AffineTransform viewToWorld = Transformer.inst().viewToWorld();
			viewToWorld.transform(p, p);
			trianglePoints.add(p);
			if (trianglePoints.size() == Model.TOTAL_TRIANGLE_POINTS) {
				Triangle triangle = Model.SINGLETON.setTriangle(currentColor, trianglePoints);
				Model.SINGLETON.addShape(triangle);
				trianglePoints.clear();
//				System.out.println("adding triangle");
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
		AffineTransform viewToWorld = Transformer.inst().viewToWorld();
		viewToWorld.transform(start, start);
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
			if (currentShape == null || !Model.SINGLETON.pointInShape(start, currentShape)) {
				currentShape = Model.SINGLETON.selectShape(start);
				if (currentShape != null) {
					currentIndex = currentShape.getIndex();
					currentColor = currentShape.getColor();
					GUIFunctions.changeSelectedColor(currentColor);	
				}
				else {
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
		AffineTransform viewToWorld = Transformer.inst().viewToWorld();
		viewToWorld.transform(point, point);
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
				AffineTransform worldToObj = Transformer.inst().worldToObj(currentShape);
				worldToObj.transform(start, s);
				worldToObj.transform(point, e);
				double pheta = Math.atan2(e.getY(), e.getX());
				double phi = Math.atan2(s.getY(), s.getX());
				double angle = pheta - phi;
				currentShape.setRotation(currentShape.getRotation() + angle);
				start = point;
			}else {
				Circle handle = currentShape.getHandle();
				if (handle.shapeSelected()) {
					Point2D.Double endPt = ((Line) currentShape).getEnd();
					AffineTransform objToWorld = Transformer.inst().objToWorld(currentShape);
					objToWorld.transform(endPt, endPt);
					currentShape.resetShape(point, endPt);
				}else {
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
		GUIFunctions.setHScrollBarKnob((int) (1/Transformer.inst().getZoom() * Transformer.DEFAULT_SCREEN_SIZE));
		GUIFunctions.setVScrollBarKnob((int) (1/Transformer.inst().getZoom() * Transformer.DEFAULT_SCREEN_SIZE));
	}

	@Override
	public void hScrollbarChanged(int value) {
		Transformer.inst().setHorizontal(value);
	}

	@Override
	public void vScrollbarChanged(int value) {
		Transformer.inst().setVertical(value);
	}

	@Override
	public void openScene(File file) {
		scene.open(file);
		Draw3D.SINGLETON.setDrawMode(drawMode3D);
	}

	@Override
	public void toggle3DModelDisplay() {
		if (drawMode3D) {
			drawMode3D = false;
		}else {
			drawMode3D = true;
			setDefaultPosition();
			Transformer3D.SINGLETON.perspective(25, 1, NEAR, FAR);
			Transformer3D.SINGLETON.rotate(rotationY);
			Transformer3D.SINGLETON.translate(translationX, translationY, translationZ);
            Transformer3D.SINGLETON.combineMatrices();
		}
		Draw3D.SINGLETON.setDrawMode(drawMode3D);
	}

	@Override
	public void keyPressed(Iterator<Integer> iterator) {
		if (drawMode3D) {
			boolean changed = false;
			int key = 0;
			while (iterator.hasNext()) {
				key = iterator.next();
				if(key == KEY_W) {
		        	double value = rotationY * Math.PI / 180;
		            translationZ += Math.cos(value) * CHANGE_AMOUNT;
		            translationX -= Math.sin(value) * CHANGE_AMOUNT;
		            changed = true;
		        }if(key == KEY_S) {
		        	double value = rotationY * Math.PI / 180;
		            translationZ -= Math.cos(value) * CHANGE_AMOUNT;
		            translationX += Math.sin(value) * CHANGE_AMOUNT;
		            changed = true;
		        }if(key == KEY_A) {
		        	double value = rotationY * Math.PI / 180;
		            translationZ -= Math.sin(value) * CHANGE_AMOUNT;
		            translationX -= Math.cos(value) * CHANGE_AMOUNT;
		            changed = true;
		        }if(key == KEY_D) {
		        	double value = rotationY * Math.PI / 180;
		            translationZ += Math.sin(value) * CHANGE_AMOUNT;
		            translationX += Math.cos(value) * CHANGE_AMOUNT;
		            changed = true;
		        }if(key == KEY_R) {
		            translationY += CHANGE_AMOUNT;
		            changed = true;
		        }if(key == KEY_F) {
		            translationY -= CHANGE_AMOUNT;
		            changed = true;
		        }if (key == KEY_Q) {
		        	rotationY += CHANGE_AMOUNT;
		        	rotationY %= 360;
		        	changed = true;
		        }if (key == KEY_E) {
		        	rotationY -= CHANGE_AMOUNT;
		        	rotationY %= 360;
		        	changed = true;
		        }if (key == KEY_H) {
		        	setDefaultPosition();
		        	changed = true;
		        }
			}
	        if(changed) {
//				System.out.println("translation: " + translationX + ", " + translationY + ", " + translationZ + ", rotation: " + rotationY);
	            Transformer3D.SINGLETON.translate(translationX, translationY, translationZ);
	            Transformer3D.SINGLETON.rotate(rotationY);
	            Transformer3D.SINGLETON.combineMatrices();
	            Draw3D.SINGLETON.setDrawMode(drawMode3D);
	    		changed = false;
	        }
		}
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
	public void openImage(File file) {
		Image.SINGLETON.open(file);
		System.out.println("opened");
		Image.SINGLETON.setChanged();
		Image.SINGLETON.setDrawMode(imageMode);
		System.out.println("notified");
	}

	@Override
	public void saveImage(File file) {
		Image.SINGLETON.save(file);
		Image.SINGLETON.setChanged();
		Image.SINGLETON.setDrawMode(imageMode);
	}

	@Override
	public void toggleBackgroundDisplay() {
		if (imageMode) {
			imageMode = false;
		}else {
			imageMode = true;
		}
		Image.SINGLETON.setDrawMode(imageMode);
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
		Image.SINGLETON.brightness(brightnessAmountNum);
//		Image.SINGLETON.setDrawMode(imageMode);
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
	
    private void setDefaultPosition() {
    	rotationX = 0;
		rotationY = 0;
		rotationZ = 0;
		translationX = 0;
		translationY = 3;
		translationZ = -50;
    }

}
