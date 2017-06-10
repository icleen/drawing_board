package iain.view;

import java.util.List;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Observable;

import cs355.model.scene.Instance;
import cs355.model.scene.Line3D;
import cs355.model.scene.Point3D;
import cs355.model.scene.WireFrame;
import iain.controller.Controller;
import iain.utilities.Transformer3D;
import iain.utilities.RandomColor;

public class Draw3D extends Observable {
	
	private boolean drawMode;
	
	private ArrayList<RandomColor> colors;
	
	public static final Draw3D SINGLETON = new Draw3D();
	
	private Draw3D() {
		drawMode = false;
		colors = new ArrayList<>();
	}
	
	public void setDrawMode(boolean b) {
		drawMode = b;
		this.setChanged();
		this.notifyObservers();
	}
	
	public boolean getDrawMode() {
		return drawMode;
	}

	public void draw(Graphics2D g2d) {
		if (!drawMode) return;
		
		ArrayList<Instance> instances = Controller.scene.instances();
		WireFrame wireframe;
		List<Line3D> lines;
		int[] pt;
		Instance inst;
		for (int i = 0; i < instances.size(); i++) {
			if (colors.size() < i + 1) {
				colors.add(new RandomColor());
			}
			g2d.setColor(colors.get(i).getColor());
			inst = instances.get(i);
			Transformer3D.SINGLETON.pushTranslate(i * 10, 0, 0);
			Transformer3D.SINGLETON.combineMatrices();
			wireframe = inst.getModel();
			lines = wireframe.getLines();
			for (Line3D line : lines) {
				pt = Transformer3D.SINGLETON.getPoints(line.start, line.end);
				if (pt != null) {
					g2d.drawLine(pt[0], pt[1], pt[2], pt[3]);	
				}
			}
			Transformer3D.SINGLETON.popMatrix();
			
			Transformer3D.SINGLETON.pushRotate(180);
			Transformer3D.SINGLETON.pushTranslate(i * -10, 0, -30);
			Transformer3D.SINGLETON.combineMatrices();
			wireframe = inst.getModel();
			lines = wireframe.getLines();
			for (Line3D line : lines) {
				pt = Transformer3D.SINGLETON.getPoints(line.start, line.end);
				if (pt != null) {
					g2d.drawLine(pt[0], pt[1], pt[2], pt[3]);	
				}
			}
			Transformer3D.SINGLETON.popMatrix();
			Transformer3D.SINGLETON.popMatrix();
		}
	}
	
}
