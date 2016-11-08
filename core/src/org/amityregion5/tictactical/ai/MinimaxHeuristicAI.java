package org.amityregion5.tictactical.ai;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.swing.JOptionPane;

public class MinimaxHeuristicAI implements IAI {

	private List<Number[]> minimaxMoves;
	private Random rand = new Random();
	private int maxTurns;
	
	public MinimaxHeuristicAI(int maxTurnsAhead) {
		maxTurns = maxTurnsAhead;
	}

	@Override
	public int[] chooseNextMove(char[][] board, char[] bigboard, boolean[] availableBoards, char team) {
		minimaxMoves = new LinkedList<Number[]>();
		minimax(board, bigboard, availableBoards, team, team, team == 'x' ? 'o' : 'x', 0);
		
		int maxScore = Integer.MIN_VALUE;
		for (Number[] move : minimaxMoves) {
			maxScore = Math.max(maxScore, (int)(move[2].doubleValue()*100000));
		}
		
		Iterator<Number[]> iter = minimaxMoves.iterator();
		while (iter.hasNext()) {
			Number[] move = iter.next();
			if (maxScore > (int)(move[2].doubleValue()*100000)) iter.remove();
		}
		
		if (minimaxMoves.size() == 0) {
			System.err.println("No moves left!");
			//JOptionPane.showMessageDialog(null, "NO MOVES FOUND");
			return null;
		}
		
		Number[] move = minimaxMoves.get(rand.nextInt(minimaxMoves.size()));
		minimaxMoves = null;

		return new int[]{move[0].intValue(), move[1].intValue()};
	}

	private double minimax(char[][] board, char[] bigboard, boolean[] availableBoards, char turn, char player, char other, int depth) {
		if (hasWon(bigboard, Character.toUpperCase(player))) return 1;
		if (hasWon(bigboard, Character.toUpperCase(other))) return -1;
		if (depth >= maxTurns) return heuristic(board, bigboard, availableBoards, turn, player, other);

		List<int[]> moves = getAvailableMoves(board, bigboard, availableBoards);
		if (moves.size() == 0) return 0;

		List<Double> scores = new ArrayList<Double>();

		for (int[] move : moves) {
			if (turn == player) { //AI is moving
				board[move[0]][move[1]] = player; //Temporarily set the tile as if the ai has moved there
				if (hasWon(board[move[0]], player)) bigboard[move[0]] = Character.toUpperCase(player); //If won set it as such
				//Compute the score for this move
				double score = minimax(board, bigboard, getAvailableBoards(board, bigboard, move), getOtherTeam(turn, player, other), player, other, depth+1);
				scores.add(score); //Put the score in the list

				if (depth == 0) minimaxMoves.add(new Number[]{move[0], move[1], score});
			} else { //Player is moving
				board[move[0]][move[1]] = other; //Temporarily set the tile as if the player has moved there
				if (hasWon(board[move[0]], other)) bigboard[move[0]] = Character.toUpperCase(other); //If won set it as such
				//Compute the score for this move
				double score = minimax(board, bigboard, getAvailableBoards(board, bigboard, move), getOtherTeam(turn, player, other), player, other, depth+1);
				scores.add(score); //Put the score in the list

			}
			board[move[0]][move[1]] = 0; //Reset this tile
			bigboard[move[0]] = 0; //Reset the bigboard tile
		}

		return turn == player ? getMax(scores) : getMin(scores);
	}
	
	private double heuristic(char[][] board, char[] bigboard, boolean[] availableBoards, char turn, char player, char other) {
		double score = heuristicBoard(bigboard, player)*10;
		
		for (int i = 0; i<bigboard.length; i++) {
			if (bigboard[i] == 0) {
				score += heuristicBoard(board[i], player);
				score -= heuristicBoard(board[i], other);
			}
		}
		
		return score/90;
	}
	
	private double heuristicBoard(char[] board, char player) {
		double score = 0;
		
		int diagNE_player = (board[0]==player?1:0) + (board[4]==player?1:0) + (board[8]==player?1:0);
		int diagNE_total = (board[0]!=0?1:0) + (board[4]!=0?1:0) + (board[8]!=0?1:0);
		
		int diagNW_player = (board[2]==player?1:0) + (board[4]==player?1:0) + (board[6]==player?1:0);
		int diagNW_total = (board[2]!=0?1:0) + (board[4]!=0?1:0) + (board[6]!=0?1:0);
		
		if (diagNE_player == diagNE_total) score += diagNE_player;
		if (diagNW_player == diagNW_total) score += diagNW_player;
		
		for (int i = 0; i<3; i++) {
			int diagC_player = (board[i]==player?1:0) + (board[i+3]==player?1:0) + (board[i+6]==player?1:0);
			int diagC_total = (board[i]!=0?1:0) + (board[i+3]!=0?1:0) + (board[i+6]!=0?1:0);
			
			int diagR_player = (board[3*i]==player?1:0) + (board[3*i+1]==player?1:0) + (board[3*i+2]==player?1:0);
			int diagR_total = (board[3*i]!=0?1:0) + (board[3*i+1]!=0?1:0) + (board[3*i+2]!=0?1:0);
			
			if (diagC_player == diagC_total) score += diagC_player;
			if (diagR_player == diagR_total) score += diagR_player;
		}
		
		return score;
	}

	private double getMax(List<Double> list) {
		double max = Double.MIN_VALUE;
		for (int i = 0; i < list.size(); ++i) {
			if (list.get(i) > max) {
				max = list.get(i);
			}
		}
		return max;
	}

	private double getMin(List<Double> list) {
		double min = Double.MAX_VALUE;
		for (int i = 0; i < list.size(); ++i) {
			if (list.get(i) < min) {
				min = list.get(i);
			}
		}
		return min;
	}

	public static boolean[] getAvailableBoards(char[][] board, char[] bigboard, int[] lastMove) {
		boolean[] boards = new boolean[9];
		if (bigboard[lastMove[1]] == 0) {
			boards[lastMove[1]] = true;
			return boards;
		} else {
			for (int i = 0; i<bigboard.length; i++) {
				if (bigboard[i] == 0) boards[i] = true;
			}
			return boards;
		}
	}

	private char getOtherTeam(char team, char player, char other) {
		if (team == player) return other;
		if (team == other) return player;
		return 0;
	}

	private List<int[]> getAvailableMoves(char[][] board, char[] bigboard, boolean[] availableBoards) {
		LinkedList<int[]> list = new LinkedList<int[]>();

		for (int i = 0; i<availableBoards.length; i++) {
			if (availableBoards[i]) {
				for (int j = 0; j<board[i].length; j++) {
					if (board[i][j] == 0) list.add(new int[]{i, j});
				}
			}
		}

		return list;
	}

	private boolean hasWon(char[] board, char player) {
		//Check the diagonal
		if ((board[0] == board[4] && board[0] == board[8] && board[0] == player)||(board[2] == board[4] && board[2] == board[6] && board[2] == player)) {
			return true;
		}

		//Check the rows and columns
		for (int i = 0; i < 3; ++i) {
			if ((board[i] == player && board[i+3] == player && board[i+6] == player)||(board[3*i] == player && board[3*i+1] == player && board[3*i+2] == player)) {
				return true;
			}
		}
		return false;
	}
}
