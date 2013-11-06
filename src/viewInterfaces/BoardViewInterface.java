/**
 * BoardViewInterface Interface
 * Interface for views of the boardModel 
 * 
 * @package viewInterfaces
 * @author Jordan Robinson
 * @version v1.0.2.4 11 Dec 2011
 */

package viewInterfaces;

import model.BoardModel;

public interface BoardViewInterface {
	
	void setBoardModel(BoardModel board);
	void drawBoard();

}
