package cs355.solution;

import java.awt.Color;

import cs355.GUIFunctions;
import iain.controller.Controller;
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
		GUIFunctions.refresh();
	}
}
