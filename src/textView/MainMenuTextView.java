/**
 *  MainMenuTextView Class
 *  A text based view class to illustrate how the MainMenuModel conforms to MVC
 * 
 * @package textView
 * @author Jordan Robinson
 * @version v1.0.2.4 11 Dec 2011
 */


package textView;

import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

import model.*;
import viewInterfaces.MainMenuViewInterface;

public class MainMenuTextView implements Observer, MainMenuViewInterface {


	//variable assignment here
	private Scanner input;
	private MainMenuModel menuModel;
	private BoardTextView boardView; //required to draw and reset the board

	//constructors here
	public MainMenuTextView(MainMenuModel menuModel)
	{
		//variables passed in
		this.menuModel = menuModel;
		menuModel.addObserver(this);

		//variables not passed in
		input = new Scanner(System.in);
		boardView = new BoardTextView(menuModel.getBoardModel(), this);

		drawMainMenu();
	}

	//gets here
	public Scanner getInput()
	{
		return input;
	}

	//sets here

	//methods here

	@Override
	public void drawMainMenu() 
	{
		System.out.println("Welcome to Super Minesweeper Turbo, HD Edition!");
		System.out.println("Please select an option from the following:");
		System.out.println("----------------------------------------");
		System.out.println("-'new' ------------------ New Game     -");
		System.out.println("-'load' ----------------- Load Game    -");
		System.out.println("-'scores' --------------- High Scores  -");
		System.out.println("-'options' -------------- Options      -");
		System.out.println("-'exit' ----------------- Exit         -");
		System.out.println("----------------------------------------");

		String inputString = input.next().trim().toLowerCase();

		if (inputString.equals("new")) {
			drawGameBoard();
		}
		else if (inputString.equals("load")) {
			loadBoard();
		}
		else if (inputString.equals("scores")) {
			drawHighScores();
			drawMainMenu();
		}
		else if (inputString.equals("options")) {
			drawOptionsMenu();
		}
		else if (inputString.equals("exit")) {
			System.exit(1);
		}
		else {
			System.out.println("Sorry, I didn't recognise that command");
			drawMainMenu();
		}
	}

	@Override
	public void drawOptionsMenu() 
	{
		System.out.println("The following options are available:");
		System.out.println("----------------------------------------");
		System.out.println("-'difficulty' ------ Change Difficulty -");
		System.out.println("-'size' ------------ Change Board Size -");
		System.out.println("-'exit' ------------ Return to Menu    -");
		System.out.println("----------------------------------------");

		String inputString = input.next().trim().toLowerCase();

		if (inputString.equals("difficulty")) {

			System.out.println("The following difficulty options are available:");
			System.out.println("----------------------------------------");
			System.out.println("-'easy' -------------- Easy            -");
			System.out.println("-'medium' ------------ Medium          -");
			System.out.println("-'hard' -------------- Hard            -");
			System.out.println("-'back' -------------- Return to Menu  -");
			System.out.println("----------------------------------------");

			inputString = input.next().trim().toLowerCase();

			if (inputString.equals("easy")) {
				System.out.println("Changes saved");
				menuModel.setDifficulty(4);
			}
			else if (inputString.equals("medium")) {
				System.out.println("Changes saved");
				menuModel.setDifficulty(8);
			}
			else if (inputString.equals("hard")) {
				System.out.println("Changes saved");
				menuModel.setDifficulty(16);
			}
			else if (inputString.equals("back")) {

			}
			else {
				System.out.println("Sorry, I didn't recognise that command");
				System.out.println("Returning to options menu");
				drawOptionsMenu();
			}

		}
		else if (inputString.equals("size")) {
			System.out.println("The following size options are available:");
			System.out.println("----------------------------------------");
			System.out.println("-'small' ------------- Small Board     -");
			System.out.println("-'medium' ------------ Medium Board    -");
			System.out.println("-'large' ------------- Large Board     -");
			System.out.println("-'back' -------------- Return to Menu  -");
			System.out.println("----------------------------------------");

			inputString = input.next().trim().toLowerCase();

			if (inputString.equals("small")) {
				menuModel.setBoardSize(4);
				System.out.println("Changes saved");
			}
			else if (inputString.equals("medium")) {
				menuModel.setBoardSize(8);
				System.out.println("Changes saved");
			}
			else if (inputString.equals("large")) {
				menuModel.setBoardSize(12);
				System.out.println("Changes saved");
			}
			else if (inputString.equals("back")) {
			}
			else {
				System.out.println("Sorry, I didn't recognise that command");
				System.out.println("Returning to options menu");
				drawOptionsMenu();
			}

		}
		else if (inputString.equals("exit")) {
			drawMainMenu();
		}
		else {
			System.out.println("Sorry, I didn't recognise that command");
			drawOptionsMenu();
		}
	}

	@Override
	public void drawHighScores() 
	{
		for (int i = 0; i < menuModel.getScores().length; i++) {
			System.out.println(menuModel.getScores()[i].getName()+"\t\t"+menuModel.getScores()[i].getScore());
		}
		System.out.println("High Scores");
	}

	@Override
	public void drawGameBoard() 
	{
		menuModel.getBoardModel().generateNewBoard();

		boardView.drawBoard();
		System.out.println();
		input();


	}

	public void input() 
	{
		System.out.println("Available choices are score, flag, reveal, board, save, help, and exit");
		String inputString = input.next().trim().toLowerCase();

		if (inputString.equals("score")) {
			score();
		}
		else if (inputString.equals("flag")) {
			System.out.println("Where to flag?\nplease input as 'x,y'");
			accessCell(1);
			score();
		}
		else if (inputString.equals("reveal")) {
			System.out.println("Where to reveal?\nplease input as 'x,y'");
			accessCell(0);
			score();
		}
		else if (inputString.equals("board")) {
			System.out.println(boardView.boardToString());
		}
		else if (inputString.equals("save")) {
			menuModel.getBoardModel().saveBoard();
			System.out.println("game saved");
		}
		else if (inputString.equals("help")) {
			System.out.println("score: Shows the current score and flags");
			System.out.println("flag: Flags a cell on the board");
			System.out.println("reveal: Reveals a cell on the baord");
			System.out.println("save: Saves the game");
			System.out.println("exit: Exits back to the main menu");
		}
		else if (inputString.equals("exit")) {
			drawMainMenu();
			return;
		}
		else {
			System.out.println("Sorry, I didn't recognise that command");
		}
		input();

	}

	public void score()
	{
		System.out.println("Score: "+menuModel.getBoardModel().getScore()+" Flags: "+menuModel.getBoardModel().getFlagsRemaining());
	}

	public void accessCell(int accessType)
	{
		String inputString = input.next().trim().toLowerCase();

		if (!inputString.matches(".*,.*")) {
			return; //not a match
		}

		String[] coords = inputString.split(",");
		try {
			menuModel.getBoardModel().accessCell(accessType, (Integer.parseInt(coords[0])-1), (Integer.parseInt(coords[1])-1));
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Sorry, I didn't recognise that command");
		}
	}



	public void loadBoard()
	{
		System.out.println("Load Board");
		menuModel.loadBoard();
		input();
	}

	@Override
	public void update(Observable arg0, Object arg1) 
	{
	}

}
