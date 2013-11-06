/**
 *  CellModel Class
 *  The logic of the cells used by the board
 * 
 * @package model
 * @author Jordan Robinson, Workshop Group 3
 * @version v5.0.0.1 12 Nov 2011
 */

package model;

import java.io.Serializable;
import java.util.Observable;

public class CellModel extends Observable implements Serializable{

	//variable assignment here
	private static final long serialVersionUID = 1L;
	private boolean revealed;
	private boolean flagged;
	private boolean mined;
	private boolean exploded;
	private int adjacentMines;

	//constructors here
	public CellModel() 
	{
		//variables passed in

		//variables not passed in
		revealed = false;
		flagged = false;
		mined = false;
		exploded = false;
		adjacentMines = 0;
	}

	//gets here
	public boolean getMined()
	{
		return mined;
	}
	public boolean getFlagged()
	{
		return flagged;
	}
	public boolean getRevealed()
	{
		return revealed;
	}
	public boolean getExploded()
	{
		return exploded;
	}
	public int getAdjacentMines()
	{
		return adjacentMines;
	}
	//sets here
	public void setMined(boolean mined)
	{
		this.mined = mined;
	}
	public void setRevealed(boolean revealed)
	{
		this.revealed = revealed;
	}	
	public void setFlagged(boolean flagged)
	{
		this.flagged = flagged;
	}
	public void setExploded(boolean exploded)
	{
		this.exploded = exploded;
	}

	//methods here
	/**
	 * Notifies observers of this class that an instance of this class has updated
	 */
	public void update()
	{
		setChanged();
		notifyObservers();
	}
	/**
	 * called by boardModel, notifies the observers that a hint should be displayed. args should only ever be "hint" or "noHint" to represent whether there are any available cells to display a hint or not
	 */
	public void displayHint()
	{
		setChanged();
		notifyObservers("hint");
	}
	
	/**
	 * since the number of adjacent mines should only ever need to be incremented and never set, this is in place rather than a set method
	 */
	public void incrementAdjacentMines()
	{
		adjacentMines++;
	}
}