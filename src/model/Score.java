/**
 *  Score Class
 *  A (very) simple class comprising of an int and String pair relating to 
 *  a score obtained in the minesweeper game
 * 
 * @package model
 * @author Jordan Robinson
 * @version v1.0.2.4 11 Dec 2011
 */

package model;

import java.io.Serializable;

public class Score implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private int score;
	private String name;
	
	public Score(String name, int score)
	{
		this.score = score;
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}
	public int getScore()
	{
		return score;
	}
	
	public void setScore(int score)
	{
		this.score = score;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String toString()
	{
		String ret = name + "," + score;
		return ret;
	}
}