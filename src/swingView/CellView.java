/**
 *  CellView Class
 *  The graphical representation of a cell
 * 
 * @package swingView
 * @author Jordan Robinson
 * @version v2.0.0.4 26 Nov 2011
 */

package swingView;

import java.awt.Color;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;


import model.CellModel;

public class CellView extends JLabel implements Observer{

	private static final long serialVersionUID = 1L;
	//variable assignment here
	private CellModel cellModel;

	//constructors here
	public CellView(CellModel cellModel) 
	{
		//variables passed in
		this.cellModel = cellModel;
		cellModel.addObserver(this);

		//variables not passed in
	}

	//gets here
	//sets here
	//methods here
	@Override
	public void update(Observable o, Object arg) 
	{
		if (arg != null && arg.equals("hint")) { //the user has pressed the 'hint' button
			this.setBorder(BorderFactory.createLineBorder(Color.green)); //sets the border to green to display that there is no mine in that square
		}
		else if (cellModel.getExploded()) { //means we draw an exploded mine, should only be true once per game
			this.setBorder(BorderFactory.createLineBorder(Color.red));
			this.setIcon(new ImageIcon(Resources.explosion));
		}
		else {

			this.setBorder(BorderFactory.createLineBorder(Color.black)); 
			if (!cellModel.getRevealed()) {
				if (cellModel.getFlagged()) {
					this.setIcon(new ImageIcon(Resources.flag)); //draw a flagged cell (note that it has have not been revealed)
				}	
				else {
					this.setIcon(new ImageIcon(Resources.newCell)); //draw a non-revealed non-flagged cell
				}
			}
			else if (cellModel.getRevealed() && !cellModel.getMined()) { //the cell has been revealed and is not mined, so we show it and draw the number of adjacent mines
				this.setIcon(new ImageIcon(Resources.revealedCell));
				this.setText(cellModel.getAdjacentMines()+"");
				this.setVerticalTextPosition(JLabel.CENTER); //this and the below sets it so that there can be text on top of an image, in this case the number of adjacent mines
				this.setHorizontalTextPosition(JLabel.CENTER);
				this.setHorizontalAlignment(JLabel.CENTER);
			}
			else {
				this.setIcon(new ImageIcon(Resources.mine)); //if we reach here, it means that the cell is mined and revealed. This should only occur on game over, when all mines are shown, since if it was clicked, the explode property would be true
				//draw mined cell/if selected draw explosion
			}
			this.setVisible(true);
		}

	}
}
