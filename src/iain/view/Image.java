package iain.view;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;

import cs355.model.image.CS355Image;
import iain.utilities.Vector;

public class Image extends CS355Image {
	
	private boolean changed;
	private boolean drawMode;
	
	private final double BLUR_FACTOR = 0.5;
	
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
		System.out.println("start edge");
		
		int[][] kernel = {{-1, 0, 1}, {-2, 0, 2}, {-1, 0, 1}};
		int[][] kernel2 = {{-1, -2, -1}, {0, 0, 0}, {1, 2, 1}};
		
		int width = this.getWidth(), height = this.getHeight();
		Image replacement = new Image(width, height);
		double r_tot, g_tot, b_tot;
		double r_tot2, g_tot2, b_tot2;
		int[] rgb = new int[3];
		float[] hsb = new float[3];
		int a, b;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				r_tot = 0;
				g_tot = 0;
				b_tot = 0;
				r_tot2 = 0;
				g_tot2 = 0;
				b_tot2 = 0;
				
				for (int i = -1; i <= 1; i++) {
					for (int j = -1; j <= 1; j++) {
						a = x + i;
						b = y + j;
						if (a < 0) {
							a = 0;
						}if (b < 0) {
							b = 0;
						}if (a >= width) {
							a = width - 1;
						}if (b >= height) {
							b = height - 1;
						}
						r_tot += this.getRed(a, b) * kernel[i + 1][j + 1];
						g_tot += this.getGreen(a, b) * kernel[i + 1][j + 1];
						b_tot += this.getBlue(a, b) * kernel[i + 1][j + 1];
						r_tot2 += this.getRed(a, b) * kernel2[i + 1][j + 1];
						g_tot2 += this.getGreen(a, b) * kernel2[i + 1][j + 1];
						b_tot2 += this.getBlue(a, b) * kernel2[i + 1][j + 1];
					}
				}
				r_tot += r_tot2;
				g_tot += g_tot2;
				b_tot += b_tot2;
				r_tot /= 8;
				g_tot /= 8;
				b_tot /= 8;
				
				
				
				replacement.setRed(x, y, (int) r_tot);
				replacement.setGreen(x, y, (int) g_tot);
				replacement.setBlue(x, y, (int) b_tot);
			}
		}
		this.setPixels(replacement);
		changed = true;
		this.setChanged();
		this.notifyObservers();
		System.out.println("end edge");
	}

	@Override
	public void sharpen() {
		System.out.println("start sharpen");
		
		int[][] kernel = {{0, -1, 0}, {-1, 6, -1}, {0, -1, 0}};
		
		int width = this.getWidth(), height = this.getHeight();
		Image replacement = new Image(width, height);
		double r_tot, g_tot, b_tot;
		int a, b;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				r_tot = 0;
				g_tot = 0;
				b_tot = 0;
				
				for (int i = -1; i <= 1; i++) {
					for (int j = -1; j <= 1; j++) {
						a = x + i;
						b = y + j;
						if (a < 0) {
							a = 0;
						}if (b < 0) {
							b = 0;
						}if (a >= width) {
							a = width - 1;
						}if (b >= height) {
							b = height - 1;
						}
						r_tot += this.getRed(a, b) * kernel[i + 1][j + 1];
						g_tot += this.getGreen(a, b) * kernel[i + 1][j + 1];
						b_tot += this.getBlue(a, b) * kernel[i + 1][j + 1];
					}
				}
				r_tot /= 2;
				g_tot /= 2;
				b_tot /= 2;
				
				if (r_tot > 255) {
					r_tot = 255;
				}if (g_tot > 255) {
					g_tot = 255;
				}if (b_tot > 255) {
					b_tot = 255;
				}
				if (r_tot < 0) {
					r_tot = 0;
				}if (g_tot < 0) {
					g_tot = 0;
				}if (b_tot < 0) {
					b_tot = 0;
				}
				
				replacement.setRed(x, y, (int) r_tot);
				replacement.setGreen(x, y, (int) g_tot);
				replacement.setBlue(x, y, (int) b_tot);
			}
		}
		this.setPixels(replacement);
		changed = true;
		this.setChanged();
		this.notifyObservers();
		System.out.println("end sharpen");
	}

	@Override
	public void medianBlur() {
		System.out.println("start median");
		
		int[] current = new int[3], comparator = new int[3];
		int smallest_x = 0, smallest_y = 0;
		double distance = 0, min_distance = 100;
		int width = this.getWidth(), height = this.getHeight();
		Image replacement = new Image(width, height);
		ArrayList<Integer> reds = new ArrayList<>(), greens = new ArrayList<>(), blues = new ArrayList<>();
		int a, b;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				reds.clear();
				greens.clear();
				blues.clear();
				min_distance = 100;
				
				for (int i = -1; i <= 1; i++) {
					for (int j = -1; j <= 1; j++) {
						a = x + i;
						b = y + j;
						if (a < 0) {
							a = 0;
						}if (b < 0) {
							b = 0;
						}if (a >= width) {
							a = width - 1;
						}if (b >= height) {
							b = height - 1;
						}
						reds.add(this.getRed(a, b));
						greens.add(this.getGreen(a, b));
						blues.add(this.getBlue(a, b));
					}
				}
				
				reds.sort(null);
				greens.sort(null);
				blues.sort(null);
				
				current[0] = reds.get(4);
				current[1] = greens.get(4);
				current[2] = blues.get(4);
				
				for (int i = -1; i <= 1; i++) {
					for (int j = -1; j <= 1; j++) {
						a = x + i;
						b = y + j;
						if (a >= 0 && b >= 0 && a < width && b < height) {
							this.getPixel(a, b, comparator);
							distance = Math.abs(Vector.getSquaredLength(current) - Vector.getSquaredLength(comparator));
							if (distance < min_distance) {
								min_distance = distance;
								smallest_x = a;
								smallest_y = b;
							}
						}
					}
				}
				
				replacement.setRed(x, y, this.getRed(smallest_x, smallest_y));
				replacement.setGreen(x, y, this.getGreen(smallest_x, smallest_y));
				replacement.setBlue(x, y, this.getBlue(smallest_x, smallest_y));
			}
		}
		this.setPixels(replacement);
		changed = true;
		this.setChanged();
		this.notifyObservers();
		System.out.println("end median");
	}

	@Override
	public void uniformBlur() {
		System.out.println("start uBlur");

		int width = this.getWidth(), height = this.getHeight();
		Image replacement = new Image(width, height);
		double r_tot, g_tot, b_tot;
		int a, b;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				r_tot = 0;
				g_tot = 0;
				b_tot = 0;
				
				for (int i = -1; i <= 1; i++) {
					for (int j = -1; j <= 1; j++) {
						a = x + i;
						b = y + j;
						if (a < 0) {
							a = 0;
						}if (b < 0) {
							b = 0;
						}if (a >= width) {
							a = width - 1;
						}if (b >= height) {
							b = height - 1;
						}
						r_tot += this.getRed(a, b) * BLUR_FACTOR;
						g_tot += this.getGreen(a, b) * BLUR_FACTOR;
						b_tot += this.getBlue(a, b) * BLUR_FACTOR;
					}
				}
				if (r_tot > 255) {
					r_tot = 255;
				}if (g_tot > 255) {
					g_tot = 255;
				}if (b_tot > 255) {
					b_tot = 255;
				}
				if (r_tot < 0) {
					r_tot = 0;
				}if (g_tot < 0) {
					g_tot = 0;
				}if (b_tot < 0) {
					b_tot = 0;
				}
				
				replacement.setRed(x, y, (int) r_tot);
				replacement.setGreen(x, y, (int) g_tot);
				replacement.setBlue(x, y, (int) b_tot);
			}
		}
		this.setPixels(replacement);
		changed = true;
		this.setChanged();
		this.notifyObservers();
		System.out.println("end uBlur");
	}

	@Override
	public void grayscale() {
		System.out.println("start grayscale");
		int[] rgb = new int[3];
		float[] hsb = new float[3];
		int width = this.getWidth(), height = this.getHeight();
		Image replacement = new Image(width, height);
		Color color;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				rgb = this.getPixel(x, y, rgb);
				Color.RGBtoHSB(rgb[0], rgb[1], rgb[2], hsb);
				
				hsb[1] = 0;
				
				color = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
				rgb[0] = color.getRed();
				rgb[1] = color.getGreen();
				rgb[2] = color.getBlue();
				replacement.setPixel(x, y, rgb);
			}
		}
		this.setPixels(replacement);
		changed = true;
		this.setChanged();
		this.notifyObservers();
		System.out.println("end grayscale");
	}

	@Override
	public void contrast(int amount) {
		float c = (float) (amount * 1.0000);
		double pwr = Math.pow( ((c + 100)/100), 4 );
		System.out.println("start contrast: " + c);
		int[] rgb = new int[3];
		float[] hsb = new float[3];
		int width = this.getWidth(), height = this.getHeight();
		Image replacement = new Image(width, height);
		Color color;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				rgb = this.getPixel(x, y, rgb);
				Color.RGBtoHSB(rgb[0], rgb[1], rgb[2], hsb);
				
				hsb[2] = (float) (pwr * (hsb[2] - 128) + 128);
				
				color = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
				rgb[0] = color.getRed();
				rgb[1] = color.getGreen();
				rgb[2] = color.getBlue();
				replacement.setPixel(x, y, rgb);
			}
		}
		this.setPixels(replacement);
		changed = true;
		this.setChanged();
		this.notifyObservers();
		System.out.println("end contrast");
	}

	@Override
	public void brightness(int amount) {
		System.out.println("start_bright");
		int[] rgb = new int[3];
		float[] hsb = new float[3];
		int width = this.getWidth(), height = this.getHeight();
		Image replacement = new Image(width, height);
		Color color;
		float temp = 0;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				rgb = this.getPixel(x, y, rgb);
				Color.RGBtoHSB(rgb[0], rgb[1], rgb[2], hsb);
				
				temp = hsb[2];
				temp *= 100;
				temp += amount;
				hsb[2] = temp/100;
				if (hsb[2] > 1) {
					hsb[2] = 1;
				}if (hsb[2] < -1) {
					hsb[2] = -1;
				}
				
				color = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
				rgb[0] = color.getRed();
				rgb[1] = color.getGreen();
				rgb[2] = color.getBlue();
				replacement.setPixel(x, y, rgb);
			}
		}
		this.setPixels(replacement);
		changed = true;
		this.setChanged();
		this.notifyObservers();
		System.out.println("end_bright");
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
