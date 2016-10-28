package org.amityregion5.tictactical;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TicTactical extends Game {
	//is jus gam. why yew hef to be mad?
	OrthographicCamera cam;

	Texture X;
	Texture O;
	Texture grid;
	Texture selector;
	Texture movingDisplay;
	SpriteBatch spritebatch;
	private char[][] board = new char[9][9];
	private boolean turn = false; // false is X, True is O
	private int next_move = -1;
	private char win = 0;
	
	@Override
	public void create () {
		cam = new OrthographicCamera();

		X = new Texture(Gdx.files.internal("X.png"));
		O = new Texture(Gdx.files.internal("O.png"));
		selector = new Texture(Gdx.files.internal("selector.png"));
		movingDisplay = new Texture(Gdx.files.internal("isMovingNow.png"));
		grid = new Texture(Gdx.files.internal("grid.png"));
		
		
		spritebatch = new SpriteBatch();
		spritebatch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		 
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		int grid_size = Math.min(Gdx.graphics.getHeight(), Gdx.graphics.getWidth());
		int miniboard_size = ((grid_size - 60) / 3) - 10;
		int slot_size = (miniboard_size / 3) - (miniboard_size / 9);
		
		if (Gdx.input.justTouched() && Gdx.input.getX() <= grid_size + 30 && Gdx.input.getY() <= Gdx.graphics.getHeight() - 30 && Gdx.input.getY() >= 30 && Gdx.input.getX() >= 30) {
			int in_x = Gdx.input.getX() - 30;
			int in_y = Gdx.graphics.getHeight() - Gdx.input.getY() - 30;
			int miniboard = -1;
			int slot = -1;
			
			miniboard = (((in_x / (miniboard_size + 10))) % 3) + 3 * (in_y / (miniboard_size + 10));
			slot = ((in_x - (miniboard % 3) * (miniboard_size + 10)) / ((miniboard_size + 10) / 3) % 3) + 3 * ((in_y - (miniboard / 3) * (miniboard_size + 10)) / ((miniboard_size + 10) / 3));
			
			if(!(miniboard == -1 || slot == -1) && board[miniboard][slot] == 0 && (miniboard == next_move || next_move == -1)){
				
				//Grids arranged as follows:
				/*
				 * 6 7 8
				 * 3 4 5
				 * 0 1 2
				 * 
				 */

				//byte[] input = new byte[2];
				if(turn){
					board[miniboard][slot] = 'o';
				}else{
					board[miniboard][slot] = 'x';
				}
				turn = !turn;
				char result = evaluate(board[miniboard], slot);
				if(result == 'x'){    
					for(int i = 0; i < 9; i++){
						board[miniboard][i] = 'X';
					}
				}else if(result == 'o'){
					for(int i = 0; i < 9; i++){
						board[miniboard][i] = 'O';
					}
				}
				char[] board0 = new char[9];
				for(int i = 0; i < 9; i++){
					board0[i] = board[i][0];
				}
				char winner = evaluate(board0, miniboard);
				if(winner == 'X'){
					win = 'X';
				}else if(winner == 'O'){
					win = 'O';;
				}else{
					int tie_test = 0;
					for(int i = 0; i < 9; i++){
						for(int j = 0; j < 9; j ++){
							if(board[i][j] != ' '){
								tie_test++;
							}
						}
					}
					if(tie_test == 81){
						win = 'T';
					}
				}
				if(board[slot][0] == 'X' || board[slot][0] == 'O'){
					next_move = -1;
				}else{
					next_move = slot;
				}
			}
		}


		spritebatch.setProjectionMatrix(cam.combined);
		spritebatch.begin();{

			spritebatch.draw(grid, 30, 30, grid_size - 60, grid_size - 60);

			for(int i = 1; i <= 3; i ++){
				for(int j = 1; j <= 3; j ++){
					spritebatch.draw(grid, 30 + (i * 2.5f) + ((grid_size - 60) * (i - 1)) / 3, 30 + (j * 2.5f) + ((grid_size - 60) * (j - 1)) / 3, miniboard_size, miniboard_size);
					if((i - 1) + (j- 1) * 3 == next_move || next_move == -1){
						spritebatch.draw(selector, 30 + (i * 2.5f) + ((grid_size - 60) * (i - 1)) / 3, 30 + (j * 2.5f) + ((grid_size - 60) * (j - 1)) / 3, miniboard_size, miniboard_size);
					}
				}	
			}

			//drawing x's and o's
			for(int i = 0; i < 9; i++){
				if(board[i][0] == 'X'){
					spritebatch.draw(X, 30 + (((i % 3) + 1) * 2.5f) + ((grid_size - 60) * (i % 3)) / 3, 30 + ((i / 3) * 2.5f) + ((grid_size - 60) * (i / 3)) / 3, miniboard_size, miniboard_size);
				}else if(board[i][0] == 'O'){
					spritebatch.draw(O, 30 + (((i % 3) + 1) * 2.5f) + ((grid_size - 60) * (i % 3)) / 3, 30 + ((i / 3) * 2.5f) + ((grid_size - 60) * (i / 3)) / 3, miniboard_size, miniboard_size);
				}else{
					for(int j = 0; j < 9; j ++){
						if(board[i][j] == 'x'){
							spritebatch.draw(X, 32.5f + ((i % 3) * 2.5f)  + ((j % 3) * 2.5f) + ((grid_size - 60) * (i % 3)) / 3 + ((miniboard_size * (j % 3) / 3) + slot_size / 2) - (slot_size / 3), 32.5f + ((i / 3) * 2.5f)  + ((j / 3) * 2.5f) + ((grid_size - 60) * (i / 3)) / 3 + ((miniboard_size * (j / 3) / 3) + slot_size / 2) - (slot_size / 3), slot_size, slot_size);
						}else if(board[i][j] == 'o'){
							spritebatch.draw(O, 32.5f + ((i % 3) * 2.5f)  + ((j % 3) * 2.5f) + ((grid_size - 60) * (i % 3)) / 3 + ((miniboard_size * (j % 3) / 3) + slot_size / 2) - (slot_size / 3), 32.5f + ((i / 3) * 2.5f)  + ((j / 3) * 2.5f) + ((grid_size - 60) * (i / 3)) / 3 + ((miniboard_size * (j / 3) / 3) + slot_size / 2) - (slot_size / 3), slot_size, slot_size);
						}else{

						}
					}
				}
			}
			
			//printing win statements
			{
				//spritebatch.
			}
			
			//printing whose turn it b
			{
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

	@Override
	public void dispose () {
		X.dispose();
		O.dispose();
		grid.dispose();
		selector.dispose();
		movingDisplay.dispose();
		spritebatch.dispose();
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

	public static char evaluate(char[] b, int s){
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
}
