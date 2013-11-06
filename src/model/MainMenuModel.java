/**
 *  MainMenuModel Class
 *  Handles loading new or saved boards, and maintains the scores of users
 * 
 * @package model
 * @author Jordan Robinson
 * @version v2.0.0.4 26 Nov 2011
 */

package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Observable;
import java.util.Scanner;

public class MainMenuModel extends Observable implements Serializable {
	
	private static final long serialVersionUID = 1L;

	//variable assignment here
	private int difficulty; //this and boardsize are here as well as in board so that the board can check them when it starts up, to see if the user has changed them
	private int boardSize;
	private BoardModel board;
	private Score[] scores; //working copy of the scores 
	private static transient final int DEFAULT_DIFFICULTY = 8;
	private static transient final int DEFAULT_BOARDSIZE = 8;
	private static File scoresFile; //text based file containing the scores saved from previous games
	
	//constructors here
	public MainMenuModel()
	{	
		difficulty = DEFAULT_DIFFICULTY;
		boardSize = DEFAULT_BOARDSIZE;
		
		scoresFile = new File("src/resources/scores");
		
		scores = new Score[5];
		setupScores();
		
		board = new BoardModel(this);
	}

	//gets here
	public int getDifficulty()
	{
		return difficulty;
	}
	public int getBoardSize()
	{
		return boardSize;
	}
	public BoardModel getBoardModel()
	{
		return board;
	}
	public Score[] getScores()
	{
		return scores;
	}

	//sets here
	public void setDifficulty(int difficulty)
	{
		this.difficulty = difficulty;
	}
	public void setBoardSize(int boardSize)
	{
		this.boardSize = boardSize;
	}
	public void setBoardModel(BoardModel board)
	{
		this.board = board;
	}
	public void setScores(Score[] scores)
	{
		this.scores = scores;
	}

	//methods here
	/**
	 * load a board that has previously been saved into the field within this class, also tells observers to draw a visual representation of the loaded board
	 */
	public void loadBoard()
	{
		FileInputStream fis = null;
		ObjectInputStream in = null;
		BoardModel savedBoard = null;
		try {
			//loading in the board
			fis = new FileInputStream("src/resources/savedBoard.ser");
			in = new ObjectInputStream(fis);

			savedBoard = (BoardModel)in.readObject();
			board = savedBoard;

			in.close();
		} catch (IOException e) {
			setChanged();
			notifyObservers("IOError"); //notify observers of the error, so it can tell the user
		} catch (ClassNotFoundException e) {
			setChanged();
			notifyObservers("classError");
		}
		setChanged();
		notifyObservers("loadBoard"); //draw the loaded in board
	}
	
	/**
	 * Loads in the scores file to set up the scores
	 */
	public void setupScores()
	{
		Scanner textScanner = null;
		try {
			textScanner = new Scanner(scoresFile);
		} catch (FileNotFoundException e) { //likely the scores file is damaged or not present
			setChanged();
			notifyObservers("scoresLoadFNFError"); //notify the observer so the user can be told of the error (since it will result in lost scores)
			
			int defaultScore = 50; //then recover, by generating a set of scores
			for (int i = 0; i<scores.length;i++) {
				scores[i] = new Score("AAA",defaultScore);
				defaultScore -= 10;
			}
			
		}
		//convert the scores file to a variable copy
		int i = 0;
		Scanner lineScanner = null;
		while(textScanner.hasNextLine()) {
			lineScanner = new Scanner(textScanner.nextLine()).useDelimiter(","); //scan the file line by line
						
			scores[i] = new Score(lineScanner.next(),lineScanner.nextInt());
			i++;
		}
		lineScanner.close();
		textScanner.close();
	}
	
	/**
	 * Saves the high scores to the text-based scores file, so it can be used again after the program has closed
	 */
	public void saveScores()
	{
		try {
			PrintWriter out = new PrintWriter(scoresFile);
			for (int i = 0; i< scores.length; i++) {
				System.out.println(scores[i].toString());
				out.println(scores[i].toString());
			}
			out.close();
		} catch (FileNotFoundException e) {
			setChanged();
			notifyObservers("scoresSaveFNFError"); //notify the observer so the user can be told of the error (since it will result in lost scores)
		}
	}
}
