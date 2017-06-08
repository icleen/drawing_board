package iain.utilities;

import java.util.Observable;

public class Transformer3D  extends Observable {
	
	private Matrix4x4 rotation;
	private Matrix4x4 translation;
	private Matrix4x4 projection;
	private Matrix3x3 screen;
	private Matrix4x4 combined;
	
	public static final Transformer3D SINGLETON = new Transformer3D();
	
	private Transformer3D() {
		rotation = new Matrix4x4();
		translation = new Matrix4x4();
		projection = new Matrix4x4();
		screen = new Matrix3x3();
		screen(Transformer.DEFAULT_SCREEN_SIZE, Transformer.DEFAULT_SCREEN_SIZE);
		combineMatrices();
	}
	
	public void translate(double x, double y, double z) {
		translation.loadIdentity();
		translation.translate((int) x,(int) y,(int) z);
		combineMatrices();
	}
	
	public void rotate(double rotation) {
		this.rotation.loadIdentity();
		this.rotation.rotateY(rotation);
		combineMatrices();
	}
	
	public void perspective(double fov, double aspect, double near, double far) {
		this.projection.loadIdentity();
		this.projection.projectPerspective(fov, aspect, near, far);
		combineMatrices();
	}
	
	public void orthographic(double left, double right, double bottom, double top, double near, double far) {
		this.projection.loadIdentity();
		this.projection.projectOrtho(left, right, bottom, top, near, far);
		combineMatrices();
	}
	
	public void screen(int width, int height) {
		screen.screen(width, height);
	}
	
	public Vector3D transform(Vector3D v) {
		return combined.transform(v);
	}
	
	private void combineMatrices() {
		combined = projection.join(rotation.join(translation));
	}

}
