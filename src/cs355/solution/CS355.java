package cs355.solution;

import java.awt.Color;

import cs355.GUIFunctions;
import iain.controller.Controller;
import iain.utilities.Transformer;
import iain.view.View;

/**
 * This is the main class. The program starts here.
 * Make you add code below to initialize your model,
 * view, and controller and give them to the app.
 */
public class CS355 {

	/**
	 * This is where it starts.
	 * @param args = the command line arguments
	 */
	public static void main(String[] args) {

		// Fill in the parameters below with your controller and view.
		GUIFunctions.createCS355Frame(new Controller(), new View());
		GUIFunctions.changeSelectedColor(Color.white);
		GUIFunctions.setZoomText(Transformer.inst().getZoom());
		GUIFunctions.setHScrollBarKnob((int) (1/Transformer.inst().getZoom() * Transformer.DEFAULT_SCREEN_SIZE));
		GUIFunctions.setVScrollBarKnob((int) (1/Transformer.inst().getZoom() * Transformer.DEFAULT_SCREEN_SIZE));
//		System.out.println(Transformer.inst().getCenter().x + ", " + Transformer.inst().getCenter().y);
		GUIFunctions.setHScrollBarPosit((int) (Transformer.LARGEST_SCREEN_SIZE/2 - Transformer.inst().getScreenSize()/2));
		GUIFunctions.setVScrollBarPosit((int) (Transformer.LARGEST_SCREEN_SIZE/2 - Transformer.inst().getScreenSize()/2));
		GUIFunctions.refresh();
	}
}
