package iain.utility_tests;

import static org.junit.Assert.*;

import org.junit.Test;

import iain.utilities.Matrix3x3;
import iain.utilities.Vector;

public class Matrix3x3Test {

	@Test
	public void test() {
		Matrix3x3 A = new Matrix3x3();
		Vector v = new Vector(3);
		double[] val = {2, 5, 1};
		v.setValues(val);
		A.screen(512, 512);
		v = A.transform(v);
		System.out.println(v.toString());
		Vector check = new Vector(3);
		double[] values = {768, -1024, 1};
		check.setValues(values);
		assertTrue(check.equals(v));
	}

}
