/**
 * BoardTests JUnit Tests
 * Tests for the boardModel
 * 
 * @package tests
 * @author Jordan Robinson, Workshop Group 3
 * @version v1.0.2.4 1 Dec 2011
 */

package tests;

import static org.junit.Assert.*;

import model.BoardModel;
import model.MainMenuModel;

import org.junit.Test;


public class boardTests {
	
	private MainMenuModel menuModel;
	private BoardModel boardModel;
	
	public void setUp()
	{
		menuModel = new MainMenuModel();
		boardModel = new BoardModel(menuModel);
	}

	/**
	 * Test method for {@link model.BoardModel#getScore()}.
	 */
	@Test
	public void testGetScore() {
		setUp();
		int expectedScore = 100; //the default score
		assertEquals(boardModel.getScore(), expectedScore);
	}

	/**
	 * Test method for {@link model.BoardModel#getBoardSize()}.
	 */
	@Test
	public void testGetBoardSize() {
		setUp();
		int expectedSize = 8;
		assertEquals(expectedSize, boardModel.getBoardSize());
	}

	/**
	 * Test method for {@link model.BoardModel#getFlagsRemaining()}.
	 */
	@Test
	public void testGetFlagsRemaining() {
		setUp();
		int expectedFlagsRemaining = 8;
		assertEquals(expectedFlagsRemaining, boardModel.getFlagsRemaining());
		boardModel.accessCell(1, 1, 1);
		expectedFlagsRemaining--;
		assertEquals(expectedFlagsRemaining, boardModel.getFlagsRemaining());
	}

	/**
	 * Test method for {@link model.BoardModel#getGameWon()}.
	 */
	@Test
	public void testGetGameWon() {
		setUp();
		boolean expectedGameWon = false;
		
		assertEquals(expectedGameWon, boardModel.getGameWon());
	}

	/**
	 * Test method for {@link model.BoardModel#getGameLost()}.
	 */
	@Test
	public void testGetGameLost() {
		setUp();
		boolean expectedGameLost = false;
		
		assertEquals(expectedGameLost, boardModel.getGameLost());
	}

	/**
	 * Test method for {@link model.BoardModel#getNoOfMines()}.
	 */
	@Test
	public void testGetNoOfMines() {
		setUp();
		int expectedNoOfMines = 8;
		assertEquals(expectedNoOfMines, boardModel.getNoOfMines());
	}

	/**
	 * Test method for {@link model.BoardModel#setFlagsRemaining(int)}.
	 */
	@Test
	public void testSetFlagsRemaining() {		
		setUp();
		int expectedFlagsRemaining = 5;
		boardModel.setFlagsRemaining(5);
		assertEquals(expectedFlagsRemaining, boardModel.getFlagsRemaining());
	}

	/**
	 * Test method for {@link model.BoardModel#setScore(int)}.
	 */
	@Test
	public void testSetScore() {
		setUp();
		int expectedScore = 22;
		boardModel.setScore(22);
		assertEquals(expectedScore, boardModel.getScore());
	}

	/**
	 * Test method for {@link model.BoardModel#accessCell(int, int, int)}.
	 */
	@Test
	public void testAccessCell() {
		
		setUp();	
		boardModel.accessCell(1, 2, 2); //should flag cell at 2,2
		assertTrue(boardModel.getCells()[2][2].getFlagged());
		
		boardModel.accessCell(1, 2, 2); //should unflag cell at 2,2
		assertTrue(!boardModel.getCells()[2][2].getFlagged());
		
		boardModel.accessCell(0, 1, 1); //should reveal cell at 1,1	
		assertTrue(boardModel.getCells()[1][1].getRevealed());
	}

	/**
	 * Test method for {@link model.BoardModel#saveBoard()}.
	 */
	@Test
	public void testSaveBoard() {
		setUp();
		boardModel.accessCell(0, 1, 1); //should reveal cell at 1,1
		assertTrue(boardModel.getCells()[1][1].getRevealed());
		boardModel.saveBoard();
		boardModel.generateNewBoard();
		assertTrue(!boardModel.getCells()[1][1].getRevealed()); //to test that a new board has been created
		menuModel.loadBoard();
		this.boardModel = menuModel.getBoardModel();
		assertTrue(boardModel.getCells()[1][1].getRevealed()); //this should now be true, since the old board has been loaded in
	}

	/**
	 * Test method for {@link model.BoardModel#checkHighScorePosition()}.
	 */
	@Test
	public void testCheckHighScorePosition() {
		setUp();
		int expectedPosition = 0;
		boardModel.setScore(101);
		assertEquals(expectedPosition, boardModel.checkHighScorePosition());
		boardModel.setScore(0);
		expectedPosition = -1;
		assertEquals(expectedPosition, boardModel.checkHighScorePosition());
	}

	/**
	 * Test method for {@link model.BoardModel#generateNewBoard()}.
	 */
	@Test
	public void testGenerateNewBoard() {
		setUp();
		boardModel.accessCell(0, 1, 1); //should reveal cell at 1,1
		assertTrue(boardModel.getCells()[1][1].getRevealed());
		boardModel.saveBoard();
		boardModel.generateNewBoard();
		assertTrue(!boardModel.getCells()[1][1].getRevealed()); //to test that a new board has been created
	}
}
