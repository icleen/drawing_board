package iain.utilities;

import java.util.Observable;

import cs355.model.scene.Point3D;

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
	
	public int[] getPoints(Point3D start, Point3D end) {
		Vector3D s = new Vector3D(), e = new Vector3D();
		s.setValues(start.x, start.y, start.z, 1);
		e.setValues(end.x, end.y, end.z, 1);
		s = this.transform(s);
		e = this.transform(e);
		if (!inBounds(s, e)) {
			return null;
		}
		double w = s.getValue(3);
		double[] a = {s.getValue(0)/w, s.getValue(1)/w, s.getValue(2)/w};
		double[] b = {e.getValue(0)/w, e.getValue(1)/w, e.getValue(2)/w};
		Vector one = new Vector(3, a), two = new Vector(3, b);
		one = screen.transform(one);
		two = screen.transform(two);
		int[] result = {(int) one.getValue(0),(int) one.getValue(1),(int) two.getValue(0),(int) two.getValue(1)};
		return result;
	}
	
	public boolean inBounds(Vector3D start, Vector3D end) {
		double w = start.getValue(3), w2 = end.getValue(3);
		double x = start.getValue(0), x2 = end.getValue(0);
		double y = start.getValue(1), y2 = end.getValue(1);
		double z = start.getValue(2), z2 = end.getValue(2);
		boolean fails = x < -w && x2 < -w2;
		boolean faile = x > w && x2 > w2;
		if (fails || faile) {
			return false;
		}
		fails = y < -w && y2 < -w2;
		faile = y > w && y2 > w2;
		if (fails || faile) {
			return false;
		}
		fails = z < -w || z2 < -w2;
		faile = z > w && z2 > w2;
		if (fails || faile) {
			return false;
		}
		return true;
	}
	
	private void combineMatrices() {
		combined = projection.join(rotation.join(translation));
	}

}
