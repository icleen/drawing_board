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

public class Draw3D extends Observable {
	
	private boolean drawMode;
	
	public static final Draw3D SINGLETON = new Draw3D();
	
	private Draw3D() {
		drawMode = false;
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
		for (Instance inst : instances) {
			wireframe = inst.getModel();
			lines = wireframe.getLines();
			for (Line3D line : lines) {
				pt = Transformer3D.SINGLETON.getPoints(line.start, line.end);
				if (pt != null) {
					g2d.drawLine(pt[0], pt[1], pt[2], pt[3]);	
				}
			}
		}
	}
	
}
