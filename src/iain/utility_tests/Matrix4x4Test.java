package iain.utility_tests;

import static org.junit.Assert.*;

import org.junit.Test;

import iain.utilities.Matrix4x4;
import iain.utilities.Vector3D;

public class Matrix4x4Test {

	@Test
	public void testJoin() {
		Matrix4x4 A = new Matrix4x4();
		Matrix4x4 B = new Matrix4x4();
		double[] val = {1, 2, 3};
		double[] val2 = {4, 3, 2};
		A.translate(val);
		B.translate(val2);
		Matrix4x4 C = A.join(B);
//		System.out.println(C.toString());
		
		double[] val3 = {5, 5, 5};
		Matrix4x4 D = new Matrix4x4();
		D.translate(val3);
		assertTrue(D.equals(C));
		
		A.loadIdentity();
		B.loadIdentity();
		A.rotateY(0);
		B.rotateY(90);
		C = A.join(B);
//		System.out.println(C.toString());
		
//		D.loadIdentity();
//		assertTrue(D.equals(C));
	}
	
	@Test
	public void testTransform() {
		Matrix4x4 A = new Matrix4x4();
		double[] val = {1, 2, 3};
		A.translate(val);
		Vector3D v = new Vector3D(), v2 = new Vector3D();
		v.setValues(1, 1, 1, 1);
		v2.setValues(2, 3, 4, 1);
		v = A.transform(v);
//		System.out.println(v.toString());
		assertTrue(v.equals(v2));
		
		Matrix4x4 B = new Matrix4x4();
		B.rotateY(90);
		v = new Vector3D();
		v2 = new Vector3D();
		v.setValues(1, 1, 1, 1);
		v2.setValues(1, 1, -0.9999999999999999, 1);
		v = B.transform(v);
//		System.out.println(v.toString());
		assertTrue(v.equals(v2));
		
		Matrix4x4 C = new Matrix4x4();
		C.projectPerspective(25, 1, 1, 500);
		v = new Vector3D();
		v2 = new Vector3D();
		v.setValues(1, 1, 1, 1);
		double rad = 12.5 * Math.PI / 180;
		double zoom = 1 / Math.tan(rad);
		v2.setValues(zoom, zoom, -0.9999999999999998, 1);
		v = C.transform(v);
//		System.out.println(v.toString());
//		System.out.println(v2.toString());
		assertTrue(v.equals(v2));
		
		v = new Vector3D();
		v2 = new Vector3D();
		v.setValues(1, 1, 1, 1);
		v2.setValues(1, 1, 1, 1);
		Matrix4x4 D = C.join(B.join(A));
		v2 = D.transform(v2);
		v = C.transform(B.transform(A.transform(v)));
		System.out.println(v.toString());
		System.out.println(v2.toString());
//		assertTrue(v.equals(v2));
	}

}
