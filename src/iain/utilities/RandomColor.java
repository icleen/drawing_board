package iain.utilities;

import java.awt.Color;

public class RandomColor {
	
	private float red;
	private float green;
	private float blue;
	private Color color;
	
	public RandomColor() {
		red = (float) Math.random();
    	green = (float) Math.random();
    	blue = (float) Math.random();
    	color = new Color(red, green, blue);
	}

	public float getRed() {
		return red;
	}

	public float getGreen() {
		return green;
	}

	public float getBlue() {
		return blue;
	}
	
	public Color getColor() {
		return color;
	}

}
