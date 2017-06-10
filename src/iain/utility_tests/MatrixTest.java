package iain.utility_tests;

import static org.junit.Assert.*;

import org.junit.Test;

import iain.utilities.Matrix;
import iain.utilities.Matrix4x4;

public class MatrixTest {

	@Test
	public void testJoin() {
		Matrix A = new Matrix(4, 4);
		Matrix B = new Matrix(4, 4);
		double[] val = {1, 2, 3};
		double[] val2 = {4, 3, 2};
		A.translate(val);
		B.translate(val2);
		Matrix C = A.join(B);
		System.out.println(C.toString());
		
		double[] val3 = {5, 5, 5};
		Matrix4x4 D = new Matrix4x4();
		D.translate(val3);
		assertTrue(D.equals(C));
	}

}
