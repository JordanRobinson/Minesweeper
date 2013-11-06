/**
 *  BoardTextView Class
 *  A text based view class to illustrate how the BoardModel conforms to MVC
 * 
 * @package textView
 * @author Jordan Robinson
 * @version v1.0.2.4 11 Dec 2011
 */


package textView;

import java.util.Observable;
import java.util.Observer;


import model.BoardModel;
import viewInterfaces.BoardViewInterface;

public class BoardTextView implements Observer, BoardViewInterface {

	//variable assignment here
	private BoardModel boardModel;
	private MainMenuTextView menuView;
	private CellTextView[][] cellViews; 

	//constructors here
	public BoardTextView(BoardModel boardModel, MainMenuTextView menuView)
	{
		this.menuView = menuView;
		this.boardModel = boardModel;
		boardModel.addObserver(this);
		cellViews = new CellTextView[boardModel.getBoardSize()][boardModel.getBoardSize()];
	}

	//variables passed in

	//variables not passed in

	//gets here

	//sets here

	//methods here

	@Override
	public void setBoardModel(BoardModel boardModel) 
	{
		this.boardModel = boardModel;
	}
	
	public String boardToString()
	{
		int boardSize  = boardModel.getBoardSize();
		String boardAsString = "  ";


		for (int i = 0; i < boardSize; i++) {
			boardAsString += (i+1)+" ";
		}
		boardAsString += "\n";
		
		for (int x = 0; x<boardSize; x++) {
			boardAsString += (x+1) + "|";
			for (int y = 0; y<boardSize; y++) {
				cellViews[x][y] = new CellTextView(boardModel.getCells()[x][y]);
				boardModel.getCells()[x][y].update();
				boardAsString += cellViews[x][y].toString() + "|";
			}
			boardAsString += "\n";
		}
		return boardAsString;
	}

	@Override
	public void drawBoard() {

		int boardSize  = boardModel.getBoardSize();
		String boardAsString = "  ";


		for (int i = 0; i < boardSize; i++) {
			boardAsString += (i+1)+" ";
		}
		boardAsString += "\n";
		
		for (int x = 0; x<boardSize; x++) {
			boardAsString += (x+1) + "|";
			for (int y = 0; y<boardSize; y++) {
				cellViews[x][y] = new CellTextView(boardModel.getCells()[x][y]);
				boardModel.getCells()[x][y].update();
				boardAsString += cellViews[x][y].toString() + "|";
			}
			boardAsString += "\n";
		}
		System.out.println(boardAsString);
	}

	@Override
	public void update(Observable o, Object arg) {
		System.out.println(boardToString());
		
		if (boardModel.getGameWon() || boardModel.getGameLost()) { //check win and loss conditions

			//set up a window with message for each possible game outcome
			if (boardModel.getGameLost()) {
				System.out.println("You Lose :/ \ntry again?\n(y,n)");
			}
			else if (boardModel.getGameWon() && boardModel.checkHighScorePosition() > -1) {
				
				System.out.println("High Score! Please enter your name:"); //for capturing the name of the player, for high score screen
				String inputString = menuView.getInput().next().trim().toLowerCase();
				boardModel.setName(inputString);
				boardModel.setHighScore();				
				System.out.println("Congratulations! You Win!\nYou got a high score!\nPlay again?\n(y,n)");
			}
			else if (boardModel.getGameWon()) {
				System.out.println("Congratulations! You Win!\nYou didn't get a high score :(\ntry again?\n(y,n)");
			}
			String inputString = menuView.getInput().next().trim().toLowerCase();

			boardModel.generateNewBoard(); //here rather than later so that it resets the game won/lost conditions before anything relying on them is run
			
			if (inputString.equals("y")) {
				menuView.drawGameBoard(); //new board using the current settings
			}
			else if (inputString.equals("n")) {
				menuView.drawMainMenu(); //return to main menu
			}
			boardModel.generateNewBoard(); //here rather than later so that it resets the game won/lost conditions before anything relying on them is run
		}
	}

}
