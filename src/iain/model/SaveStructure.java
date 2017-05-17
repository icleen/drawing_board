package iain.model;

import java.util.ArrayList;
import java.util.List;

import cs355.model.drawing.Circle;
import cs355.model.drawing.Ellipse;
import cs355.model.drawing.Line;
import cs355.model.drawing.Rectangle;
import cs355.model.drawing.Shape;
import cs355.model.drawing.Square;
import cs355.model.drawing.Triangle;

public class SaveStructure {
	
	public Circle[] circles;
	public Ellipse[] ellipses;
	public Line[] lines;
	public Rectangle[] rectangles;
	public Square[] squares;
	public Triangle[] triangles;
	
	public int total;
	
	public SaveStructure() {}
	
	public void fromModel() {
		List<Shape> shapes = Model.SINGLETON.getShapes();
		initArrays(shapes);
		int c = 0, e = 0, l = 0, r = 0, s = 0, t = 0;
		for (int i = 0; i < shapes.size(); i++) {
			shapes.get(i).setIndex(i);
			switch (shapes.get(i).getShapeType()) {
			case circle: circles[c++] = (Circle) shapes.get(i); break;
			case ellipse: ellipses[e++] = (Ellipse) shapes.get(i); break;
			case line: lines[l++] = (Line) shapes.get(i); break;
			case rectangle: rectangles[r++] = (Rectangle) shapes.get(i); break;
			case square: squares[s++] = (Square) shapes.get(i); break;
			case triangle: triangles[t++] = (Triangle) shapes.get(i); break;
			default: break;
			}
		}
		total = shapes.size();
	}
	
	private void initArrays(List<Shape> shapes) {
		int c = 0, e = 0, l = 0, r = 0, s = 0, t = 0;
		for (int i = 0; i < shapes.size(); i++) {
			switch (shapes.get(i).getShapeType()) {
			case circle: c++; break;
			case ellipse: e++; break;
			case line: l++; break;
			case rectangle: r++; break;
			case square: s++; break;
			case triangle: t++; break;
			default: break;
			}
		}
		circles = new Circle[c];
		ellipses = new Ellipse[e];
		lines = new Line[l];
		rectangles = new Rectangle[r];
		squares = new Square[s];
		triangles = new Triangle[t];
	}
	
	public void toModel() {
		Shape[] shapes = new Shape[total];
		assert(shapes.length == total);
		for (int i = 0; i < circles.length; i++) {
			shapes[circles[i].getIndex()] = circles[i];
		}
		for (int i = 0; i < ellipses.length; i++) {
			shapes[ellipses[i].getIndex()] = ellipses[i];
		}
		for (int i = 0; i < lines.length; i++) {
			shapes[lines[i].getIndex()] = lines[i];
		}
		for (int i = 0; i < rectangles.length; i++) {
			shapes[rectangles[i].getIndex()] = rectangles[i];
		}
		for (int i = 0; i < squares.length; i++) {
			shapes[squares[i].getIndex()] = squares[i];
		}
		for (int i = 0; i < triangles.length; i++) {
			shapes[triangles[i].getIndex()] = triangles[i];
		}
		List<Shape> other = new ArrayList<>();
		for (int i = 0; i < shapes.length; i++) { 
			other.add(shapes[i]);
		}
		assert(shapes.length == other.size());
		Model.SINGLETON.setShapes(other);
	}

}
