package org.amityregion5.tictactical;
//If you're reading this, you actually bothered to read my code. Hello.

import org.amityregion5.tictactical.ai.IAI;
import org.amityregion5.tictactical.ai.MinimaxHeuristicAI;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.amityregion5.tictactical.ai.MinimaxHeuristicAI;

public class TicTactical extends Game{
	// TODO things n stuff
	//Grids arranged as follows:
	/*
	* 6 7 8
	* 3 4 5
	* 0 1 2
	* 
	*/
		
	//WARNING//WARNING//WARNING//WARNING//WARNING//WARNING//WARNING//WARNING//WARNING//WARNING//
	//THE CODE AHEAD IS EXTREMELY MESSY. ATTEMPT TO COMPREHEND AT YOUR OWN RISK!!!
	OrthographicCamera cam;

	Texture X;
	Texture O;
	Texture grid;
	Texture movingDisplay;
	Texture winDisplay;
	Texture tieDisplay;
	Texture selectorR;
	Texture selectorB;
	Texture selectorG;
	Texture MenuNoneSelected;
	Texture MenuOneSelected;
	Texture MenuTwoSelected;
	
	
	SpriteBatch spritebatch;
	
	private IAI ai = new MinimaxHeuristicAI(5);
	private char[][] board = new char[9][9];
	private char[] big_board = new char[10];
	private boolean turn = false; // false is X, True is O
	private int next_move = 9;
	private char win = 0;
	private int players = 0;
	private String easter = "egg";
	private boolean ai_team = true; // same as the other thing, just selecting ai's team(if applicable)
	
	@Override
	public void create () {
		cam = new OrthographicCamera();

		X = new Texture(Gdx.files.internal("X.png"));
		O = new Texture(Gdx.files.internal("O.png"));
		selectorG = new Texture(Gdx.files.internal("selector.png"));
		selectorR = new Texture(Gdx.files.internal("redSelector.png"));
		selectorB = new Texture(Gdx.files.internal("bluSelector.png"));
		movingDisplay = new Texture(Gdx.files.internal("isMovingNow.png"));
		grid = new Texture(Gdx.files.internal("grid.png"));
		winDisplay = new Texture(Gdx.files.internal("hasWon.png"));
		tieDisplay = new Texture(Gdx.files.internal("GameTied.png"));
		MenuNoneSelected = new Texture(Gdx.files.internal("MenuNoSelection.png"));
		MenuOneSelected = new Texture(Gdx.files.internal("MenuOneSelected.png"));
		MenuTwoSelected = new Texture(Gdx.files.internal("MenuTwoSelected.png"));
		
		
		easter.contains("secrets");
		
		spritebatch = new SpriteBatch();
		spritebatch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		
		big_board[9] = 1;
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		int grid_size = Math.min(Gdx.graphics.getHeight(), Gdx.graphics.getWidth() * 75 / 100);
		int miniboard_size = ((grid_size - 60) / 3) - 10;
		int slot_size = (miniboard_size / 3) - (miniboard_size / 9);
		int in_x = Gdx.input.getX() - 30;
		int in_y = Gdx.graphics.getHeight() - Gdx.input.getY() - 30;
		
		
		int miniboard = in_x / (miniboard_size + 12) + 
						((in_y / (miniboard_size + 12)) * 3);
		int slot = ((in_x - (miniboard % 3) * (miniboard_size + 12)) / ((miniboard_size + 15) / 3)) + 
				   ((in_y - (miniboard / 3) * (miniboard_size + 12)) / ((miniboard_size + 15) / 3)) * 3;
		
		
		if(players == 0){
			render_Menu();
		}else{
			if(slot > 8){
				slot = 8;
			}else if(slot < 0){
				slot = 0;
			}
			if(miniboard > 8){
				miniboard = 8;
			}else if(miniboard < 0){
				miniboard = 0;
			}
			render_game(grid_size, miniboard_size, slot_size, miniboard, slot, in_x, in_y);
			if(players == 1 && win == 0){
				if(turn == ai_team){
					int[] move = aiMove();
					miniboard = move[0];
					slot = move[1];
				}
					
			}
			
			
			if (Gdx.input.justTouched() && (players == 2 || (players == 1 && turn != ai_team))) {
				
				if(win == 0 
						&& in_x < grid_size - 30 
						&& in_y < grid_size - 30 
						&& in_x >= 0 
						&& in_y >= 0){     
					System.out.println("Hello!");
					if(!(miniboard == -1 || slot == -1) && board[miniboard][slot] == 0 && (miniboard == next_move || big_board[next_move] != 0) && big_board[miniboard] == 0){
						game_logic(miniboard, slot);
					}
					
				}else if(win != 0){
					reset();
				}
			}else if(players == 1 && turn == ai_team){
				game_logic(miniboard, slot);
			}
			
		}
	}

	@Override
	public void dispose () {
		X.dispose();
		O.dispose();
		grid.dispose();
		selectorG.dispose();
		selectorR.dispose();
		selectorB.dispose();
		movingDisplay.dispose();
		winDisplay.dispose();
		tieDisplay.dispose();
		spritebatch.dispose();
		MenuNoneSelected.dispose();
		MenuOneSelected.dispose();
		MenuTwoSelected.dispose();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		cam.setToOrtho(false, width, height);
		cam.update();
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		super.resume();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		super.pause();
	}

	public char evaluate(char[] b, int s){
		if(b[s] != ' '){
			if(s == 0){
				if((b[0] == b[1] && b[1] == b[2]) || (b[0] == b[3] && b[3] == b[6]) || (b[0] == b[4] && b[4] == b[8])){
					return b[s];
				}
			}else if(s == 1){
				if((b[0] == b[1] && b[1] == b[2]) || (b[1] == b[4] && b[4] == b[7])){
					return b[s];
				}
			}else if(s == 2){
				if((b[0] == b[1] && b[1] == b[2]) || (b[2] == b[5] && b[5] == b[8]) || (b[2] == b[4] && b[4] == b[6])){
					return b[s];
				}
			}else if(s == 3){
				if((b[0] == b[3] && b[3] == b[6]) || (b[3] == b[4] && b[4] == b[5])){
					return b[s];
				}
			}else if(s == 4){
				if((b[3] == b[4] && b[4] == b[5]) || (b[1] == b[4] && b[4] == b[7]) || (b[0] == b[4] && b[4] == b[8] || (b[2] == b[4] && b[4] == b[6]))){
					return b[s];
				}
			}else if(s == 5){
				if((b[2] == b[5] && b[5] == b[8]) || (b[3] == b[4] && b[4] == b[5])){
					return b[s];
				}
			}else if(s == 6){
				if((b[6] == b[7] && b[7] == b[8]) || (b[0] == b[3] && b[3] == b[6]) || (b[2] == b[4] && b[4] == b[6])){
					return b[s];
				}
			}else if(s == 7){
				if((b[6] == b[7] && b[7] == b[8]) || (b[1] == b[4] && b[4] == b[7])){
					return b[s];
				}
			}else if(s == 8){
				if((b[6] == b[7] && b[7] == b[8]) || (b[2] == b[5] && b[5] == b[8]) || (b[0] == b[4] && b[4] == b[8])){
					return b[s];
				}
			}
		}
		return 0;
	}
	
	public void reset(){
		win = 0;
		next_move = 9;
		turn = false;
		board = new char[9][9];
		big_board = new char[10];
		players = 0;
		big_board[9] = 1;
	}
	
	public void game_logic(int miniboard, int slot){
		
		if(turn){
			board[miniboard][slot] = 'o';
		}else{
			board[miniboard][slot] = 'x';
		}
		turn = !turn;
		char result = evaluate(board[miniboard], slot);
		if(result == 'x'){
			big_board[miniboard] = 'X';
		}else if(result == 'o'){
			big_board[miniboard] = 'O';
		}else{
			int j = 0;
			for(int i = 0; i < 9; i ++){
				if(board[miniboard][i] != 0){
					j++;
				}
			}
			if(j == 9){
				big_board[miniboard] = 3;
			}
		}
		char[] board0 = new char[9];
		for(int i = 0; i < 9; i++){
			board0[i] = big_board[i];
		}
		char winner = evaluate(board0, miniboard);
		if(winner == 'X' || winner == 'O'){
			win = winner;
		}else{
			int tie_test = 0;
			for(int i = 0; i < 9; i++){
				if(big_board[i] != 0){
					tie_test += 9;
				}else{
					for(int j = 0; j < 9; j ++){
						if(board[i][j] != 0){
							tie_test++;
						}
					}
				}
			}
			if(tie_test == 81){
				win = 'T';
			}
		}
		next_move = slot;
	}
	
	public void render_game (int grid_size, int miniboard_size, int slot_size, int miniboard, int slot, int in_x, int in_y){
		
		spritebatch.setProjectionMatrix(cam.combined);
		
		spritebatch.begin();{

			spritebatch.draw(grid, 30, 30, grid_size - 60, grid_size - 60);
			for(int i = 1; i <= 3; i ++){
				for(int j = 1; j <= 3; j ++){
					spritebatch.draw(grid, 30 + (i * 2.5f) + ((grid_size - 60) * (i - 1)) / 3, 30 + (j * 2.5f) + ((grid_size - 60) * (j - 1)) / 3, miniboard_size, miniboard_size);
					if(((i - 1) + (j- 1) * 3 == next_move || big_board[next_move] != 0) && win == 0 && big_board[(i - 1) + (j - 1) * 3] == 0){
						spritebatch.draw(selectorG, 30 + (i * 2.5f) + ((grid_size - 60) * (i - 1)) / 3, 30 + (j * 2.5f) + ((grid_size - 60) * (j - 1)) / 3, miniboard_size, miniboard_size);
					}
				}	
			}
			
			//printing highlight for moused over tile
			if(in_x <= grid_size - 60 && in_y <= grid_size - 60 && in_x >= 0 && in_y >= 0){
				if(turn){
					spritebatch.draw(selectorB, 32.5f + ((miniboard % 3) * 2.5f)  + ((slot % 3) * 2.5f) + ((grid_size - 60) * (miniboard % 3)) / 3 + ((miniboard_size * (slot % 3) / 3) + slot_size / 2) - (slot_size / 3), 32.5f + ((miniboard / 3) * 2.5f)  + ((slot / 3) * 2.5f) + ((grid_size - 60) * (miniboard / 3)) / 3 + ((miniboard_size * (slot / 3) / 3) + slot_size / 2) - (slot_size / 3), slot_size, slot_size);
				}else{
					spritebatch.draw(selectorR, 32.5f + ((miniboard % 3) * 2.5f)  + ((slot % 3) * 2.5f) + ((grid_size - 60) * (miniboard % 3)) / 3 + ((miniboard_size * (slot % 3) / 3) + slot_size / 2) - (slot_size / 3), 32.5f + ((miniboard / 3) * 2.5f)  + ((slot / 3) * 2.5f) + ((grid_size - 60) * (miniboard / 3)) / 3 + ((miniboard_size * (slot / 3) / 3) + slot_size / 2) - (slot_size / 3), slot_size, slot_size);
				}
				//System.out.println(miniboard + " " + slot);
			}

			//drawing x's and o's
			for(int i = 0; i < 9; i++){
				
				for(int j = 0; j < 9; j ++){
					if(board[i][j] == 'x'){
						spritebatch.draw(X, 32.5f + ((i % 3) * 2.5f)  + ((j % 3) * 2.5f) + ((grid_size - 60) * (i % 3)) / 3 + ((miniboard_size * (j % 3) / 3) + slot_size / 2) - (slot_size / 3), 32.5f + ((i / 3) * 2.5f)  + ((j / 3) * 2.5f) + ((grid_size - 60) * (i / 3)) / 3 + ((miniboard_size * (j / 3) / 3) + slot_size / 2) - (slot_size / 3), slot_size, slot_size);
					}else if(board[i][j] == 'o'){
						spritebatch.draw(O, 32.5f + ((i % 3) * 2.5f)  + ((j % 3) * 2.5f) + ((grid_size - 60) * (i % 3)) / 3 + ((miniboard_size * (j % 3) / 3) + slot_size / 2) - (slot_size / 3), 32.5f + ((i / 3) * 2.5f)  + ((j / 3) * 2.5f) + ((grid_size - 60) * (i / 3)) / 3 + ((miniboard_size * (j / 3) / 3) + slot_size / 2) - (slot_size / 3), slot_size, slot_size);
					}
				}
				if(big_board[i] == 'X'){
					spritebatch.draw(X, 30 + (((i % 3) + 1) * 2.5f) + ((grid_size - 60) * (i % 3)) / 3, 30 + ((i / 3) * 2.5f) + ((grid_size - 60) * (i / 3)) / 3, miniboard_size, miniboard_size);
				}else if(big_board[i] == 'O'){
					spritebatch.draw(O, 30 + (((i % 3) + 1) * 2.5f) + ((grid_size - 60) * (i % 3)) / 3, 30 + ((i / 3) * 2.5f) + ((grid_size - 60) * (i / 3)) / 3, miniboard_size, miniboard_size);
				}
				
			}
			
			//printing win statements
			if(win != 0){
				if(win == 'X'){
					spritebatch.draw(X, grid_size - 30 + slot_size, miniboard_size * 2 , miniboard_size, miniboard_size);
					spritebatch.draw(winDisplay, grid_size - 30 + slot_size * 1.25f, miniboard_size * 2 - slot_size * 1.25f, miniboard_size, miniboard_size);
					
				}else if(win == 'O'){
					spritebatch.draw(O, grid_size - 30 + slot_size, miniboard_size * 2 , miniboard_size, miniboard_size);	
					spritebatch.draw(winDisplay, grid_size - 30 + slot_size * 1.25f, miniboard_size * 2 - slot_size * 1.25f, miniboard_size, miniboard_size);	
				}else{
					spritebatch.draw(tieDisplay, grid_size - 30 + slot_size * 1.25f, miniboard_size * 2 - slot_size * 1.25f, miniboard_size * 1.5f, miniboard_size);		
				}
			}
			
			//printing whose turn it b
			if(win == 0){
				if(turn){
					spritebatch.draw(O, grid_size - 30 + slot_size, miniboard_size * 2 , miniboard_size, miniboard_size);
				}else{
					spritebatch.draw(X, grid_size - 30 + slot_size, miniboard_size * 2 , miniboard_size, miniboard_size);
				}
				spritebatch.draw(movingDisplay, grid_size - 30 + slot_size, miniboard_size * 2 - slot_size, miniboard_size, miniboard_size);
			}
			
		}
		spritebatch.end();
	}
	
	public int[] aiMove(){
		int[] lastMove = new int[2];
		lastMove[1] = next_move;
		boolean[] availableBoards = MinimaxHeuristicAI.getAvailableBoards(board, big_board, lastMove);
		char temp;
		
		if(ai_team){
			temp = 'o';
		}else{
			temp = 'x';
		}
		
		int[] out = ai.chooseNextMove(board, big_board, availableBoards, temp);
		
		return out;
	}
	
	public void render_Menu(){
		spritebatch.begin();{
			if(!Gdx.input.justTouched()){
				if(Gdx.input.getX() < Gdx.graphics.getWidth() / 3){
					spritebatch.draw(MenuOneSelected, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
				}else if(Gdx.graphics.getWidth() - Gdx.input.getX() < Gdx.graphics.getWidth() / 3){
					spritebatch.draw(MenuTwoSelected, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
					
				}else{
					spritebatch.draw(MenuNoneSelected, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());	
				}
			}else{
				if(Gdx.input.getX() < Gdx.graphics.getWidth() / 3){
					players = 1;
				}else if(Gdx.graphics.getWidth() - Gdx.input.getX() < Gdx.graphics.getWidth() / 3){
					players = 2;
				}
			}
		}
		spritebatch.end();
	}
}
