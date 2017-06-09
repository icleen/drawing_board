package iain.utilities;

public class Matrix {
	
	protected int[][] values;
	protected int rows;
	protected int columns;
	
	public Matrix(int row, int column) {
		values = new int[row][column];
		rows = row;
		columns = column;
		loadIdentity();
	}
	
	public void loadIdentity() {
		assert(rows == columns);
		for(int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				if (i == j) {
					values[i][j] = 1;
				}else {
					values[i][j] = 0;
				}
			}
		}
	}
	
	public void translate(int[] val) {
		int index = columns - 1;
		for (int i = 0; i < rows - 1; i++) {
			values[i][index] = val[i];
		}
	}
	
	public Vector transform(Vector v) {
		assert(columns == v.getDimension());
		Vector result = new Vector(values.length);
		double value;
		for (int i = 0; i < rows; i++) {
			value = 0;
			for (int j = 0; j < columns; j++) {
				value += values[i][j] * v.getValue(j);
			}
			result.setValue(i, value);
		}
		return result;
	}
	
	
	public Matrix join(Matrix B) {
		assert(this.columns == B.rows);
		Matrix A = new Matrix(this.rows, B.columns);
		int value;
		for (int i = 0; i < this.rows; i++) {
			for (int j = 0; j < B.columns; j++) {
				value = 0;
				for (int k = 0; k < this.columns; k++) {
					value += this.values[i][k] * B.values[k][j];
				}
				A.values[i][j] = value;
			}
		}
		return A;
	}

}
