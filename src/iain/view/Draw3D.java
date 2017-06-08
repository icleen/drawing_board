package iain.view;

import java.util.Observable;

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

	public void draw() {
		if (!drawMode) return;
		
		
	}
	
}
