package org.amityregion5.tictactical.ai;

/**
 * An interface for an AI to play the game through
 * 
 * @author sergeys
 *
 */
public interface IAI {
	/**
	 * Get the next move that this AI wants to make
	 * 
	 * This should always return a legal move
	 * If this returns null just let it crash. it shouldnt happen.
	 * 
	 * @param board the two dimensional array representing the entire board
	 * @param bigboard the array representing who has won each small board
	 * @param availableBoards an array of booleans representing which board can be moved into
	 * @param team the team that this AI is playing for
	 * @return an array where the first element is the board the AI wants to make the move in and the second element is the tile within that board 
	 */
	public int[] chooseNextMove(char[][] board, char[] bigboard, boolean[] availableBoards, char team);
}
