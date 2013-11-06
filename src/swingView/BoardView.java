/**
 *  BoardView Class
 *  Graphical representation of the boardModel class
 * 
 * @package swingView
 * @author Jordan Robinson
 * @version v3.0.2.4 26 Nov 2011
 */

package swingView;

import java.util.Observable;
import java.util.Observer;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import viewInterfaces.BoardViewInterface;

import model.BoardModel;

import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@SuppressWarnings("serial") //not needed since never saved; only the boardModel and its cells are ever saved
public class BoardView extends JPanel implements Observer, BoardViewInterface {

	//variable assignment here
	private BoardModel boardModel;
	private MainMenuView menuView; //required so that the board can go back to the main menu
	private MouseGridListener listener;

	//constructors here
	public BoardView(BoardModel boardModel, MainMenuView menuView)
	{
		this.menuView = menuView;
		listener = new MouseGridListener();
		addMouseListener(listener);
		this.boardModel = boardModel;
		boardModel.addObserver(this);
	}


	//sets here	
	public void setBoardModel(BoardModel boardModel)
	{
		this.boardModel = boardModel;
	}

	//methods here
	/**
	 * Performs the action of actually drawing the board, adding the cellViews to this object
	 */
	public void drawBoard()
	{
		int boardSize  = boardModel.getBoardSize(); //set here since it's used so much in this method

		this.setLayout(new GridLayout(boardSize,boardSize));
		this.removeAll(); //in case this has been used previously
		
		for (int x = 0; x<boardSize; x++) {
			for (int y = 0; y<boardSize; y++) {
				this.add(new CellView(boardModel.getCells()[y][x])); //y and x are reversed here because of the way gridlayout adds items
				boardModel.getCells()[y][x].update();
			}
		}
		this.setVisible(true);
	}

	@Override
	public void update(Observable o, Object arg) {

		if (arg != null && arg.equals("noHint")) {
			JOptionPane.showMessageDialog(null,"No more hints available");
			return;
		}	
		else if (arg != null && arg.equals("IOError")) {
			JOptionPane.showMessageDialog(null,"Issue saving board\nDo you have enough disk space?");
			return;
		}
		else if (arg != null && arg.equals("saveSuccess")) {
			JOptionPane.showMessageDialog(null,"Board Saved");
			return;
		}
		
		int boardSize  = boardModel.getBoardSize();
		
		for (int x = 0; x<boardSize; x++) { //this redraws all cells that have been affected, necessary to redraw all cells, due to the revealAdjacentCells method, otherwise I would only update the clicked cell
			for (int y = 0; y<boardSize; y++) {
				boardModel.getCells()[x][y].update();
			}
		}
		
		menuView.updateStats(); //updates the top panel, which has the score and number of flags remaining

		if (boardModel.getGameWon() || boardModel.getGameLost()) { //check win and loss conditions

			int choice = 0;
			Object[] options = {"Yes","No"};
			String windowMessage = null, windowTitle = null;

			//set up a window with message for each possible game outcome
			if (boardModel.getGameLost()) {
				windowMessage = "You Lose :/ \ntry again?";
				windowTitle = "You Lose";
			}
			else if (boardModel.getGameWon() && boardModel.checkHighScorePosition() > -1) { //-1 means no high score
				windowMessage = "Congratulations! You Win!\nYou got a high score!\nPlay again?";
				windowTitle = "High Score!";
				boardModel.setName(JOptionPane.showInputDialog("High Score! Please enter your name:")); //for capturing the name of the player, for high score screen
				boardModel.setHighScore();
			}
			else if (boardModel.getGameWon()) {
				windowMessage = "Congratulations! You Win!\nYou didn't get a high score :(\ntry again?"; 
				windowTitle = "You Win!";
			}

			choice = JOptionPane.showOptionDialog(null, windowMessage, windowTitle, JOptionPane.PLAIN_MESSAGE, JOptionPane.YES_NO_OPTION, null, options, options[0]); //show the game ended message box

			boardModel.generateNewBoard(); //here rather than later so that it resets the game won/lost conditions before anything relying on them is run
			menuView.resetGUI(); //set up the main menu in case the user wants to go back to it

			if (choice == 0) {
				menuView.drawGameBoard(); //new board using the current settings
			}
			else {
				menuView.drawMainMenu(); //return to main menu
			}
		}

	}
	
	/*
	 * 	Note that the actionListeners are in this class rather than the controller, contrary to some mvc implementations
	 * this is because I believe that they are not necessary for all views, and as such if there were many graphical views
	 * it would make more sense to extend a parent class with these in, rather than have them in the controller, 
	 * where they may not be used by any text views, or views that simply do not need them
	 */

	public class MouseGridListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {

			int xPos, yPos;

			xPos = (e.getX()/32); //32 to include cell of 30 and border of 1*2
			yPos = (e.getY()/32);

			if(e.getModifiers() == MouseEvent.BUTTON1_MASK) { //reveal (left click)
				boardModel.accessCell(0, xPos, yPos);
			}
			else if (e.getModifiers() == MouseEvent.BUTTON3_MASK) { //flag (right click)
				boardModel.accessCell(1, xPos, yPos);
			}			
		}
	}
}