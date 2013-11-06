/**
 *  MainMenuView Class
 *  The view relating to the MainMenuModel Class
 * 
 * @package swingView
 * @author Jordan Robinson, Workshop Group 3
 * @version v1.0.2.4 12 Nov 2011
 */


package swingView;

import javax.swing.*;

import viewInterfaces.MainMenuViewInterface;

import model.MainMenuModel;
import model.Score;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;


public class MainMenuView implements Observer, MainMenuViewInterface {

	//variable assignment here
	private JFrame frame;
	private JPanel lPanel;
	private JPanel rPanel;
	private JLabel picLabel;
	private JButton newGameButton;
	private JButton loadGameButton;
	private JButton highScoresButton;
	private JButton optionsButton;
	private JButton backButton;
	private JButton saveBoardButton;
	private JButton saveOptionsButton;
	private JButton hintButton;
	private JLabel scoresDisplay;
	private JPanel topPanel;
	private JLabel scoreLabel;
	private JLabel flagsLabel;

	//private BoardModel board;
	private MainMenuModel menuModel;
	private BoardView boardView; //required to draw and reset the board

	private JComboBox difficultyComboBox;
	private JComboBox boardSizeComboBox;

	//constructors here
	public MainMenuView(MainMenuModel menuModel)
	{		
		//variables passed in
		this.menuModel = menuModel;
		menuModel.addObserver(this);

		//variables not passed in
		boardView = new BoardView(menuModel.getBoardModel(),this);

		frame = new JFrame("Super Minesweeper Turbo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		rPanel = new JPanel();
		lPanel = new JPanel();
		topPanel = new JPanel();

		newGameButton = new JButton("New Game");
		loadGameButton = new JButton("Load Game");
		highScoresButton = new JButton("High Scores");
		optionsButton = new JButton("Options");

		backButton = new JButton("Back");
		saveBoardButton = new JButton("Save");
		saveOptionsButton = new JButton("Save");
		hintButton = new JButton("Hint");

		String[] difficulties = {"Easy","Normal","Hard"}; //for the difficultyComboBox declared later
		String[] sizes = {"Small","Normal","Big","Huge"}; //for the boardSizeComboBox

		scoreLabel = new JLabel("Score"); //this should be set up with the initial score when it's drawn, but it needs some placeholder text until then or the window will be too small
		flagsLabel = new JLabel("Flags"); //same with this

		difficultyComboBox = new JComboBox(difficulties);
		difficultyComboBox.setSelectedIndex(1);
		boardSizeComboBox = new JComboBox(sizes);
		boardSizeComboBox.setSelectedIndex(1);

		scoresDisplay = new JLabel();

		newGameButton.addActionListener(new NewGamePressed());
		loadGameButton.addActionListener(new LoadGamePressed());
		highScoresButton.addActionListener(new HighScoresPressed());
		optionsButton.addActionListener(new OptionsPressed());
		saveOptionsButton.addActionListener(new SaveOptionsPressed());
		backButton.addActionListener(new BackPressed());
		saveBoardButton.addActionListener(new SaveBoardPressed());
		hintButton.addActionListener(new HintPressed());		

		frame.setIconImage(Resources.icon);
		drawMainMenu();
	}

	//gets here
	public JComboBox getDifficultyComboBox()
	{
		return difficultyComboBox;
	}
	public JComboBox getBoardSizeComboBox()
	{
		return boardSizeComboBox;
	}
	//sets here

	//methods here
	/**
	 * draws a new main menu, allowing the user to choose from the options of viewing high scores, starting games and modifying options
	 */
	public void drawMainMenu()
	{
		lPanel.setSize(new Dimension(300,200)); //setting up the left side of the main menu
		picLabel = new JLabel(new ImageIcon(Resources.logo));
		lPanel.add(picLabel);

		rPanel.setLayout(new GridLayout(4,1)); //setting up the right side of the main menu
		rPanel.setSize(new Dimension(200,200));
		rPanel.add(newGameButton);
		rPanel.add(loadGameButton);
		rPanel.add(highScoresButton);
		rPanel.add(optionsButton);

		frame.add(lPanel, BorderLayout.WEST); //adding both sides to the parent frame
		frame.add(rPanel, BorderLayout.EAST);

		frame.setSize(new Dimension(450,250));
		frame.setLocationRelativeTo(null); //for making the window centred

		frame.setVisible(true);
	}

	/**
	 * draws the options menu, which allows the user to modify the boardSize and difficulty attributes
	 */
	public void drawOptionsMenu()
	{
		JLabel options = new JLabel("Options");
		options.setVerticalAlignment(JLabel.CENTER);
		options.setHorizontalAlignment(JLabel.CENTER);
		options.setPreferredSize(new Dimension(100,50)); //since this is the main heading of the window, we want it spaced nicely
		
		JLabel difficultyLabel = new JLabel("Difficulty");
		difficultyLabel.setVerticalAlignment(JLabel.CENTER);
		difficultyLabel.setHorizontalAlignment(JLabel.CENTER);

		JLabel sizeLabel = new JLabel("Board Size");
		sizeLabel.setVerticalAlignment(JLabel.CENTER);
		sizeLabel.setHorizontalAlignment(JLabel.CENTER);

		lPanel.setLayout(new GridLayout(3,2,15,20)); //columns, rows, hgap, vgap
		lPanel.add(difficultyLabel);
		lPanel.add(difficultyComboBox);
		lPanel.add(sizeLabel);
		lPanel.add(boardSizeComboBox);
		lPanel.add(saveOptionsButton);
		lPanel.add(backButton);
		
		frame.setLayout(new BorderLayout());
		frame.add(options, BorderLayout.NORTH);
		frame.add(lPanel, BorderLayout.CENTER);

		frame.setSize(new Dimension(200,250));
		frame.setLocationRelativeTo(null); //set to centre of the user's screen. Call this every time the frame is resized, or it won't be centred.
		frame.setVisible(true);
	}

	/**
	 * Draws a graphical representation of the scores contained within the system
	 */
	public void drawHighScores()
	{
		String scoresString = "";
		Score[] scores = menuModel.getScores();

		//the following section converts the scores to html, so they can be displayed as a table in a JLabel

		scoresString = "<html>\n<body>\n<table border=\"0\" bordercolor=\"#000000\" width=\"400\" cellpadding=\"10\" cellspacing=\"0\">\n"; //top section of the html

		for (int i = 0; i < scores.length; i++) { //each line is one score
			scoresString += "<tr><td ALIGN=LEFT>" + scores[i].getName() + "</td><td ALIGN=RIGHT>" + scores[i].getScore() +"</td></tr>\n";
		}
		scoresString += "</table>\n</body>\n</html>"; //end of the html

		scoresDisplay.setText(scoresString); //populate the JLabel with the html table
		picLabel = new JLabel(new ImageIcon(Resources.highScoresImage)); //image at the head of the high scores screen
		lPanel.add(picLabel);
		lPanel.add(scoresDisplay);
		lPanel.add(backButton);
		
		frame.add(lPanel);
		frame.setSize(new Dimension(450,435));
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	/**
	 * Draws a new (or loaded) game board, so that the user can play the game
	 */
	public void drawGameBoard()
	{
		boardView.setBoardModel(menuModel.getBoardModel()); //in case the board has been loaded
		
		rPanel.setLayout(new GridLayout(4,1));
		rPanel.add(saveBoardButton);
		rPanel.add(backButton);
		rPanel.add(hintButton);

		topPanel.add(scoreLabel); //topPanel is the scores and flags
		topPanel.add(flagsLabel);

		lPanel.add(boardView);

		frame.add(topPanel, BorderLayout.NORTH);
		frame.add(lPanel, BorderLayout.CENTER);
		frame.add(rPanel, BorderLayout.EAST);
		boardView.drawBoard();
		
		frame.pack(); //since the board can theoretically be almost any size, it makes sense to pack here
		frame.setLocationRelativeTo(null);

		updateStats(); //updates the top panel with the current score and amount of flags, in this case it would be the default amount of each
		
		frame.setVisible(true);
	}

	/**
	 * Resets the GUI so that it can be used to draw a different window
	 */
	public void resetGUI()
	{
		frame.setVisible(false); //hide the frame
		frame = new JFrame("Super Minesweeper Turbo"); //set the necessary attributes needed for every window
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setIconImage(Resources.icon);

		//reset the panels, too
		rPanel = new JPanel();
		lPanel = new JPanel();
		topPanel = new JPanel();
	}

	/**
	 * updates the score and flags at the top of the board with the current values
	 */
	public void updateStats()
	{
		scoreLabel.setText("Score: " +menuModel.getBoardModel().getScore());
		flagsLabel.setText("\t\tFlags: " + menuModel.getBoardModel().getFlagsRemaining());
	}

	/**
	 * Updates the GUI, as called by all objects this observer observes
	 */
	@Override
	public void update(Observable o, Object arg) {

		if (arg != null) {
			if (arg.equals("drawBoard")) {
				resetGUI();
				menuModel.getBoardModel().generateNewBoard();
				drawGameBoard();
			}
			else if (arg.equals("loadBoard")) {
				
				menuModel.getBoardModel().addObserver(boardView); //when a board is loaded it loses it's observers, causing it to not update. This was a nightmare to debug.
				resetGUI();
				drawGameBoard();
			}
			else if (arg.equals("IOError")) {
				JOptionPane.showMessageDialog(null, "No save file found or save file corrupt\nStarting new game instead");
				resetGUI();
				drawGameBoard();
			}
			else if (arg.equals("classError")) {
				JOptionPane.showMessageDialog(null, "Serious error reading save file\nsave file may be corrupt\nStarting new game instead");				
				resetGUI();
				drawGameBoard();
			}
			else if (arg.equals("scoresLoadFNFError")) {
				JOptionPane.showMessageDialog(null, "Error reading in scores file\ncreating new scores file...\nstarting game");				
				resetGUI();
				drawGameBoard();
			}
			else if (arg.equals("scoresSaveFNFError")) {
				JOptionPane.showMessageDialog(null, "Error Saving Scores File\ncheck you have enough disk space?");
			}
		}

		topPanel.repaint();
		lPanel.repaint();
		frame.repaint();
	}

	/*
	 * 	Note that the actionListeners are in this class rather than the controller, contrary to some mvc implementations
	 * this is because I believe that they are not necessary for all views, and as such if there were many graphical views
	 * it would make more sense to extend a parent class with these in, rather than have them in the controller, 
	 * where they may not be used by any text views, or views that simply do not need them
	 */
	
	private class NewGamePressed implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			resetGUI();
			menuModel.getBoardModel().generateNewBoard();
			drawGameBoard();
		}
	}
	private class LoadGamePressed implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			menuModel.loadBoard();
		}
	}
	private class OptionsPressed implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			resetGUI();
			drawOptionsMenu();
		}
	}
	private class HighScoresPressed implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			resetGUI();
			drawHighScores();
		}
	}
	private class BackPressed implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			resetGUI();
			drawMainMenu();
		}
	}
	private class SaveBoardPressed implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			menuModel.getBoardModel().saveBoard();
		}
	}
	private class SaveOptionsPressed implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {

			switch (getDifficultyComboBox().getSelectedIndex()) {
			case 0: menuModel.setDifficulty(4); break;
			case 1: menuModel.setDifficulty(8); break;
			case 2: menuModel.setDifficulty(16); break;
			default:  menuModel.setDifficulty(8); break;
			}

			switch (getBoardSizeComboBox().getSelectedIndex()) {
			case 0: menuModel.setBoardSize(4); break;
			case 1: menuModel.setBoardSize(8); break;
			case 2: menuModel.setBoardSize(12); break;
			case 3: menuModel.setBoardSize(16); break;
			default: menuModel.setBoardSize(8); break;
			}

			resetGUI();
			drawMainMenu();
		}
	}
	private class HintPressed implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			menuModel.getBoardModel().displayHint();
		}
	}
}
