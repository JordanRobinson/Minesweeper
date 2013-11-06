/**
 *  CellTextView Class
 *  A text based view class to illustrate how the cellModel conforms to MVC
 * 
 * @package textView
 * @author Jordan Robinson
 * @version v1.0.2.4 11 Dec 2011
 */


package textView;

import model.CellModel;

import java.util.Observable;
import java.util.Observer;

public class CellTextView implements Observer {
	
	private CellModel cellModel;
	private char representation;

	public CellTextView(CellModel cellModel) 
	{
		this.cellModel = cellModel;
		cellModel.addObserver(this);
	}
	
	public String toString()
	{
		return representation+"";
	}
	
	public void update(Observable o, Object arg) 
	{

		if (cellModel.getExploded()) { //means we draw an exploded mine, should only be true once per game
			representation = 'X';
		}
		else {
			
			if (!cellModel.getRevealed()) {
				if (cellModel.getFlagged()) {
					representation = 'F'; //draw a flagged cell (note that it has have not been revealed)
				}	
				else {
					representation = ' '; //draw a non-revealed non-flagged cell
				}
			}
			else if (cellModel.getRevealed() && !cellModel.getMined()) { //the cell has been revealed and is not mined, so we show it and draw the number of adjacent mines
				representation = (cellModel.getAdjacentMines()+"").charAt(0);
			}
			else {
				representation = 'M'; //if we reach here, it means that the cell is mined and revealed. This should only occur on game over, when all mines are shown, since if it was clicked, the explode property would be true
				//draw mined cell/if selected draw explosion
			}
		}
	}
}
