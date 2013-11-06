/**
 *  BoardModel Class
 *  Contains the logic and variables needed to play a game of minesweeper
 * 
 * @package model
 * @author Jordan Robinson
 * @version v5.0.0.4 13 Nov 2011
 */

package model;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;


public class BoardModel extends Observable implements Serializable {

	private static final long serialVersionUID = 1L;
	//variable assignment here
	private CellModel[][] cells;
	private int noOfMines;
	private int flagsRemaining;
	private int boardSize;
	private int score;
	private boolean gameWon;
	private boolean gameLost;
	private String name;
	private static transient final int INITIAL_SCORE = 100;

	private MainMenuModel menuModel;

	//constructors here
	public BoardModel(MainMenuModel menuModel)
	{
		//variables passed in
		this.menuModel = menuModel;

		//variables not passed in
		name = "AAA"; //arbitrary value, set here to avoid any null pointer exceptions in future

		generateNewBoard();
	}

	//gets here
	public int getScore()
	{
		return score;
	}
	public int getBoardSize()
	{
		return boardSize;
	}
	public int getFlagsRemaining()
	{
		return flagsRemaining;
	}
	public CellModel[][] getCells()
	{
		return cells;
	}
	public boolean getGameWon()
	{
		return gameWon;
	}
	public boolean getGameLost()
	{
		return gameLost;
	}
	public int getNoOfMines()
	{
		return noOfMines;
	}

	//sets here
	public void setFlagsRemaining(int flagsRemaining)
	{
		this.flagsRemaining = flagsRemaining;
	}
	public void setScore(int score)
	{
		this.score = score;
	}
	public void setName(String name)
	{
		this.name = name;
	}

	//methods here
	/**
	 * For accessing a cell, either flags or reveals the cell.
	 * 
	 * @param accessType can be 1 for flag, or 0 for reveal
	 * @param x the X coordinate of the cell
	 * @param y the Y coordinate of the cell
	 */
	public void accessCell(int accessType,int x,int y)
	{		
		if (accessType == 0) { //reveal

			if (!cells[x][y].getRevealed() && !cells[x][y].getFlagged()) { //note if a cell is flagged in cannot be revealed until unflagged
				score--;

				cells[x][y].setRevealed(true);

				if (cells[x][y].getMined()) { //this is the game over condition, a cell containing a mine is clicked
					cells[x][y].setExploded(true);
					gameOver(); //as such it triggers the gameOver method
				}
				else {
					revealAdjacentSquares(x,y);
				}
			}
		}
		else if (accessType == 1) { //flag
			if (!cells[x][y].getRevealed() && !cells[x][y].getFlagged() && getFlagsRemaining() > 0) { //flag the cell
				score--; //note that score is in both the if and else if rather than simply executing before the if statement, this is because if the if or else if are not true, the score should not be decremented, as the user has not taken an action
				cells[x][y].setFlagged(true);
				flagsRemaining--;
			}
			else if(cells[x][y].getFlagged()) { //unflag the cell
				score--;
				cells[x][y].setFlagged(false);
				flagsRemaining++;
			}
		}
		
		checkWinCondition();

		setChanged();
		notifyObservers(); //to update the view of the board
	}


	/**
	 * Randomly places mines according to the number of mines set by the difficulty
	 */
	private void setMines()
	{
		int randomX = 0;
		int randomY = 0;
		Random random = new Random();

		for (int i = 0;i<noOfMines;i++) {
			randomX = random.nextInt(boardSize);
			randomY = random.nextInt(boardSize);
			if (cells[randomX][randomY].getMined()) { //accounting for duplicate mines
				i--;
				continue; //note that this should never be an infinite loop, since the board cannot be set small enough that there are only mines and still mines to add
			}
			cells[randomX][randomY].setMined(true); //set the mine
		}
	}

	/**
	 * calculates and sets the number of adjacent mines that each cell has, for example a cell with a mine to it's north and a mine to it's south east will have a number of two
	 */
	private void setAdjacentNumbers()
	{
		//top two for loops iterate through the board and pass each cell into the second two for loops, which check each cell adjacent to the cell for a mine
		for (int x = 0; x<boardSize; x++) { //this is to iterate through each cell
			for (int y = 0; y<boardSize; y++) {
				
				for (int t = x-1;t<x+2;t++) { //3x3 grid of original cell, checks for mines in this grid and if so increments counter for original cell
					for (int b = y-1; b<y+2; b++) {
						if (t<0 || b < 0 || t>boardSize-1 || b>boardSize-1 ) { //so that out of bounds cannot occur
							continue;
						}
						if (cells[t][b].getMined()) { //if there is a mine in one of the adjacent cells
							cells[x][y].incrementAdjacentMines(); //then increment the number of adjacent mines
						}
					}
				}
			}
		}
	}

	/**
	 * checks for an unmined cell, and updates that cell to show a hint
	 */
	public void displayHint()
	{
		int randomX = 0;
		int randomY = 0;
		int loopCounter = (boardSize*boardSize); //loopCounter is used to stop an infinite loop occurring
		Random random = new Random();

		randomX = random.nextInt(boardSize);
		randomY = random.nextInt(boardSize);
		
		while ((cells[randomX][randomY].getRevealed() || cells[randomX][randomY].getMined())) { //look for an unmined and unrevealed cell to show a hint for
			randomX = random.nextInt(boardSize);
			randomY = random.nextInt(boardSize);
			loopCounter--;
			if (loopCounter == 0) { //if it has reached this far it's unlikely (but possible, due to randomness) there are no unmined cells available
				setChanged();
				notifyObservers("noHint"); //note that this notifies boardView, so that a JOptionPane can be showed
				return;
			}
		}

		score--;

		cells[randomX][randomY].displayHint(); //whereas this calls CellModel's displayHint, which updates cellView, to update the cell to show a hint
	}

	/**
	 * saves the board to a file, to be loaded later by the corresponding method.
	 */
	public void saveBoard() 
	{
		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		try {
			fos = new FileOutputStream("src/resources/savedBoard.ser");
			out = new ObjectOutputStream(fos);
			out.writeObject(this);
			out.close();
		} catch (IOException e) {
			setChanged();
			notifyObservers("IOError"); //something went wrong, so notify the user
		}
		setChanged();
		notifyObservers("saveSuccess"); //saving was a success, so notify the user
	}

	/**
	 * sets the gameLost condition, and sets all mines to revealed, to show the user where the mines were
	 */
	private void gameOver()
	{
		gameLost = true;

		for (int i = 0; i<boardSize; i++) { //iterate through board
			for (int j = 0; j<boardSize; j++) {
				if (cells[i][j].getMined()) { //if a cell is mined
					cells[i][j].setRevealed(true); //set it to reveal
				}
			}
		}
	}

	/**
	 * Reveals cells nearby to the cell at the coordinates input if the cell itself has no adjacent mines, and any cells to be revealed have no adjacent mines
	 */
	private void revealAdjacentSquares(int x, int y)
	{
		if (cells[x][y].getAdjacentMines() > 0) { //if there are adjacent mines to this cell
			return; //we don't want to reveal any, so return
		}

		for (int t = x-1;t<x+2;t++) { //3x3 grid of original cell input, checks the cells in the grid to see if any have no adjacent mines
			for (int b = y-1; b<y+2; b++) {
				if (t<0 || b < 0 || t>boardSize-1 || b>boardSize-1 ) { //to avoid out of bounds
					continue;
				}
				if (cells[t][b].getAdjacentMines() == 0 && !cells[t][b].getRevealed()) { //cell has no adjacent mines and hasn't been revealed
					cells[t][b].setRevealed(true); //so we set it to be revealed
					revealAdjacentSquares(t, b); //and call this method on it, to recursively cycle through all cells
				}
				else if (cells[t][b].getAdjacentMines() >0) { //if there are adjacent mines to one of the cells in the 3x3 grid
					cells[t][b].setRevealed(true); //we just reveal it
				}
			}
		}
		setChanged();
		notifyObservers();
	}

	/**
	 * checks if all mines have been flagged
	 */
	private void checkWinCondition()
	{
		boolean allCellsFlagged = true;

		for (int x = 0; x<boardSize; x++) { //iterate through board
			for (int y = 0; y<boardSize; y++) {
				if (cells[x][y].getMined() && !cells[x][y].getFlagged()) { //if a cell with a mine hasn't been flagged, then the game can't have been won
					allCellsFlagged = false;
				}
			}
		}
		if (allCellsFlagged) {
			gameWon = true; //if we get here, all cells have been flagged
		}
	}

	/**
	 * resets the game board so that a new game can be played
	 */
	public void generateNewBoard()
	{
		this.noOfMines = menuModel.getDifficulty(); //get these attributes from the menuModel, where they could have been set by the user
		this.boardSize = menuModel.getBoardSize();

		 //set everything to its default value
		score = INITIAL_SCORE;
		this.flagsRemaining = noOfMines;
		gameWon = false;
		gameLost = false;

		//recreate and populate the cells array
		cells = new CellModel[boardSize][boardSize];
		for (int x = 0;x<boardSize;x++) {
			for (int y = 0;y<boardSize;y++) {
				CellModel cell = new CellModel();
				cells[x][y] = cell;
			}
		}
		//call the setup methods
		setMines();
		setAdjacentNumbers();
	}
	
	/**
	 * checks if the player has a high score, and if they do, where it is in relation to the other high scores
	 * @returns the high score position of the user, -1 if no high score
	 */
	public int checkHighScorePosition()
	{
		Score[] scores = menuModel.getScores(); //retrieve the scores
		int scorePos = -1;

		for (int i = 0; i < scores.length; i++) { //iterates through the scores in descending order
			if (score > scores[i].getScore()) { //if we find that our score is higher than one of the current scores
				scorePos = i; //we note down the score
				break; //break out of the loop since if we're higher than this score we're guaranteed to be higher than the rest of the scores
			}
		}

		return scorePos;
	}

	/**
	 * Places the current score in it's rightful position in the list of scores
	 */
	public void setHighScore()
	{		
		if (checkHighScorePosition() == -1) { //if score position is -1 this has been called in error
			return;
		}
		
		int scorePos = (checkHighScorePosition()); //get the score position
		Score[] scores = menuModel.getScores(); //get the current scores
		Score[] newScores = new Score[5]; //create a set of new scores
		newScores[scorePos] = new Score(name,score); //insert the new score into the set of new scores

		//this next section works by iterating up to the point before the high score, inserting the high score, 
		//then iterating after the high score, to place the scores lower than the high score entered in position in the list of scores
		for (int i = 0; i < scorePos; i++) {
			newScores[i] = scores[i];
		}
		newScores[scorePos].setScore(score);
		newScores[scorePos].setName(name);
		for (int i = scorePos+1; i < scores.length; i++) {
			newScores[i] = scores[i-1];
		}
		
		menuModel.setScores(newScores); //update the scores in the menu
		menuModel.saveScores(); //save the scores
	}
}