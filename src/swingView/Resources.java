/**
 *  Resources Class
 *  Collection of images relating to the Swing View associated with the Minesweeper application
 * 
 * @package swingView
 * @author Jordan Robinson
 * @version v2.0.0.0 13 Nov 2011
 */

package swingView;

import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;

public class Resources {

	static Image mine; //static since there ever only need to be one instance of any images used
	static Image flag;
	static Image explosion;
	static Image newCell;
	static Image revealedCell;
	static Image logo;
	static Image icon;
	static Image highScoresImage;

	public Resources() {

	}

	static {
		URL url = Resources.class.getResource("resources/mine.png");
		mine = Toolkit.getDefaultToolkit().getImage(url);
		url = Resources.class.getResource("resources/explosion.png");
		explosion = Toolkit.getDefaultToolkit().getImage(url);			
		url = Resources.class.getResource("resources/flag.png");
		flag = Toolkit.getDefaultToolkit().getImage(url);
		url = Resources.class.getResource("resources/newCell.png");
		newCell = Toolkit.getDefaultToolkit().getImage(url);
		url = Resources.class.getResource("resources/revealedCell.png");
		revealedCell = Toolkit.getDefaultToolkit().getImage(url);
		url = Resources.class.getResource("resources/logo.png");
		logo = Toolkit.getDefaultToolkit().getImage(url);
		url = Resources.class.getResource("resources/icon.png");
		icon = Toolkit.getDefaultToolkit().getImage(url);
		url = Resources.class.getResource("resources/highscores.png");
		highScoresImage = Toolkit.getDefaultToolkit().getImage(url);
	}
}
