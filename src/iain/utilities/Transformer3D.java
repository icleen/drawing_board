package iain.utilities;

import java.util.ArrayList;
import java.util.Observable;

import cs355.model.scene.Point3D;

public class Transformer3D  extends Observable {
	
	private Matrix4x4 rotation;
	private Matrix4x4 translation;
	private Matrix4x4 projection;
	private Matrix3x3 screen;
	private Matrix4x4 combined;
	
	private ArrayList<Matrix4x4> stack;
	
	public static final Transformer3D SINGLETON = new Transformer3D();
	
	private Transformer3D() {
		rotation = new Matrix4x4();
		translation = new Matrix4x4();
		projection = new Matrix4x4();
		screen = new Matrix3x3();
		screen(Transformer.LARGEST_SCREEN_SIZE, Transformer.LARGEST_SCREEN_SIZE);
		stack = new ArrayList<>();
		combineMatrices();
	}
	
	public void translate(double x, double y, double z) {
		translation.loadIdentity();
		translation.translate(-x, -y, -z);
	}
	
	public void rotate(double rotation) {
		this.rotation.loadIdentity();
		this.rotation.rotateY(rotation);
	}
	
	public void perspective(double fov, double aspect, double near, double far) {
		this.projection.loadIdentity();
		this.projection.projectPerspective(fov, aspect, near, far);
	}
	
	public void orthographic(double left, double right, double bottom, double top, double near, double far) {
		this.projection.loadIdentity();
		this.projection.projectOrtho(left, right, bottom, top, near, far);
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
		double w2 = e.getValue(3);
		double[] a = {s.getValue(0)/w, s.getValue(1)/w, 1};
		double[] b = {e.getValue(0)/w2, e.getValue(1)/w2, 1};
		Vector one = new Vector(3, a), two = new Vector(3, b);
		one = screen.transform(one);
		two = screen.transform(two);
		int[] result = {(int) (one.getValue(0) + 0.5),(int) (one.getValue(1) + 0.5),(int) (two.getValue(0) + 0.5),(int) (two.getValue(1) + 0.5)};
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
	
	public void combineMatrices() {
		Matrix4x4 temp = new Matrix4x4();
		for (int i = 0; i < stack.size(); i++) {
			temp = temp.join(stack.get(i));
		}
		combined = projection.join(rotation.join(translation.join(temp)));
	}
	
	public void pushTranslate(double x, double y, double z) {
		Matrix4x4 trans = new Matrix4x4();
		trans.translate(x, y, z);
		stack.add(trans);
	}
	
	public void pushRotate(double rotation) {
		Matrix4x4 rot = new Matrix4x4();
		rot.rotateY(rotation);
		stack.add(rot);
	}
	
	public void popMatrix() {
		if (stack.size() == 0) 
			return;
		stack.remove(stack.size() - 1);
	}

}
