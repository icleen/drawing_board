package iain.view;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import cs355.model.image.CS355Image;

public class Image extends CS355Image {
	
	private boolean changed;
	private boolean drawMode;
	
	public static Image SINGLETON = new Image();
	
	private Image() {
		super();
		changed = false;
		drawMode = false;
	}
	
	private Image(int w, int h) {
		super(w, h);
		changed = false;
		drawMode = false;
	}

	@Override
	public BufferedImage getImage() {
		if (this.getWidth() == 0) {
			return null;
		}
//		System.out.println("start get");
		BufferedImage img = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
		WritableRaster r = img.getRaster();
		int[] rgb = new int[3];
		for (int x = 0; x < this.getWidth(); x++) {
			for (int y = 0; y < this.getHeight(); y++) {
				this.getPixel(x, y, rgb);
				r.setPixel(x, y, rgb);
			}
		}
		img.setData(r);
//		System.out.println("end get");
		return img;
	}

	@Override
	public void edgeDetection() {
		Image replacement = new Image();
		int[] rgb = new int[3];
		float[] hsb = new float[3];
		int width = this.getWidth(), height = this.getHeight();
		Color color;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				this.getPixel(x, y, rgb);
				Color.RGBtoHSB(rgb[0], rgb[1], rgb[2], hsb);
				// yaru
				color = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
				rgb[0] = color.getRed();
				rgb[1] = color.getGreen();
				rgb[2] = color.getBlue();
				replacement.setPixel(x, y, rgb);
			}
		}
	}

	@Override
	public void sharpen() {
		int[] rgb = new int[3];
		float[] hsb = new float[3];
		int width = this.getWidth(), height = this.getHeight();
		Color color;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				this.getPixel(x, y, rgb);
				Color.RGBtoHSB(rgb[0], rgb[1], rgb[2], hsb);
				// yaru
				color = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
				rgb[0] = color.getRed();
				rgb[1] = color.getGreen();
				rgb[2] = color.getBlue();
				this.setPixel(x, y, rgb);
			}
		}
	}

	@Override
	public void medianBlur() {
		int[] rgb = new int[3];
		float[] hsb = new float[3];
		int width = this.getWidth(), height = this.getHeight();
		Color color;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				this.getPixel(x, y, rgb);
				Color.RGBtoHSB(rgb[0], rgb[1], rgb[2], hsb);
				// yaru
				color = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
				rgb[0] = color.getRed();
				rgb[1] = color.getGreen();
				rgb[2] = color.getBlue();
				this.setPixel(x, y, rgb);
			}
		}
	}

	@Override
	public void uniformBlur() {
		int[] rgb = new int[3];
		float[] hsb = new float[3];
		int width = this.getWidth(), height = this.getHeight();
		Color color;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				this.getPixel(x, y, rgb);
				Color.RGBtoHSB(rgb[0], rgb[1], rgb[2], hsb);
				// yaru
				color = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
				rgb[0] = color.getRed();
				rgb[1] = color.getGreen();
				rgb[2] = color.getBlue();
				this.setPixel(x, y, rgb);
			}
		}
	}

	@Override
	public void grayscale() {
		int[] rgb = new int[3];
		float[] hsb = new float[3];
		int width = this.getWidth(), height = this.getHeight();
		Color color;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				this.getPixel(x, y, rgb);
				Color.RGBtoHSB(rgb[0], rgb[1], rgb[2], hsb);
				// yaru
				color = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
				rgb[0] = color.getRed();
				rgb[1] = color.getGreen();
				rgb[2] = color.getBlue();
				this.setPixel(x, y, rgb);
			}
		}
	}

	@Override
	public void contrast(int amount) {
		System.out.println("start contrast");
		int[] rgb = new int[3];
		float[] hsb = new float[3];
		int width = this.getWidth(), height = this.getHeight();
		Image replacement = new Image(width, height);
		Color color;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				rgb = this.getPixel(x, y, rgb);
				Color.RGBtoHSB(rgb[0], rgb[1], rgb[2], hsb);
				
				hsb[2] = (float) Math.pow((amount + 100)/100, 4) * (hsb[2] - 128) + 128;
				
				color = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
				rgb[0] = color.getRed();
				rgb[1] = color.getGreen();
				rgb[2] = color.getBlue();
				replacement.setPixel(x, y, rgb);
			}
		}
		this.setPixels(replacement);
		changed = true;
		System.out.println("end contrast");
		this.setChanged();
		this.notifyObservers();
	}

	@Override
	public void brightness(int amount) {
		float brightness = amount / 100;
		System.out.println("start_bright");
		int[] rgb = new int[3];
		float[] hsb = new float[3];
		int width = this.getWidth(), height = this.getHeight();
		Image replacement = new Image(width, height);
		Color color;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				rgb = this.getPixel(x, y, rgb);
				Color.RGBtoHSB(rgb[0], rgb[1], rgb[2], hsb);
				
				hsb[2] += brightness;
				if (hsb[2] > 1) {
					hsb[2] = 1;
				}if (hsb[2] < -1) {
					hsb[2] = -1;
				}
//				hsb[2] += amount;
				
				color = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
				rgb[0] = color.getRed();
				rgb[1] = color.getGreen();
				rgb[2] = color.getBlue();
				replacement.setPixel(x, y, rgb);
			}
		}
		this.setPixels(replacement);
		changed = true;
		System.out.println("end_bright");
		this.setChanged();
		this.notifyObservers();
	}
	
	public void operate(int amount) {
		int[] rgb = new int[3];
		float[] hsb = new float[3];
		int width = this.getWidth(), height = this.getHeight();
		Image replacement = new Image(width, height);
		Color color;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				this.getPixel(x, y, rgb);
				Color.RGBtoHSB(rgb[0], rgb[1], rgb[2], hsb);
				// yaru
				color = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
				rgb[0] = color.getRed();
				rgb[1] = color.getGreen();
				rgb[2] = color.getBlue();
				replacement.setPixel(x, y, rgb);
			}
		}
		this.setPixels(replacement);
		changed = true;
	}
	
	public boolean hasChanged() {
		if (changed) {
			changed = false;
			return true;
		}
		return false;
	}
	
	public void setChanged() {
		changed = true;
	}
	
	public boolean getDrawMode() {
		return drawMode;
	}
	
	public void setDrawMode(boolean draw) {
		drawMode = draw;
		this.setChanged();
		this.notifyObservers();
	}

}
