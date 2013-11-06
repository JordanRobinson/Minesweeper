/**
 *  Controller Class
 *  Controller for the Minesweeper application
 * 
 * @package controller
 * @author Jordan Robinson
 * @version v2.0.2.4 30 Nov 2011
 */

package controller;

import swingView.MainMenuView;
import textView.MainMenuTextView;
import model.MainMenuModel;

@SuppressWarnings("unused")
public class Controller {

	public static void main(String[] args) {

		MainMenuModel menuModel = new MainMenuModel();
		MainMenuView menuView = new MainMenuView(menuModel);
//		MainMenuTextView menuView = new MainMenuTextView(menuModel);

	}
}