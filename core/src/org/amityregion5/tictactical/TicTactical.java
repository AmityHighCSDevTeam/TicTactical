package org.amityregion5.tictactical;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TicTactical extends Game {
	
	OrthographicCamera cam;
	
	Texture X;
	Texture O;
	Texture grid;
	SpriteBatch spritebatch;
	private char[][] board = new char[9][9];
	private boolean turn = false; // false is X, True is O
	
	int tX = 0;
	int tY = 0;
	int c = 0;
	int mc = 60;
	
	@Override
	public void create () {
		cam = new OrthographicCamera();
		
		X = new Texture(Gdx.files.internal("X.png"));
		O = new Texture(Gdx.files.internal("O.png"));
		grid = new Texture(Gdx.files.internal("grid.png"));
		spritebatch = new SpriteBatch();
		spritebatch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		
		board[0][0] = 'o';

		/*
		for(int i = 0; i < 9; i++){
			for(int j = 0; j < 9; j++){
				if(turn){
					board[i][j] = 'x';
				}else{
					board[i][j] = 'o';
				}
				turn = !turn;
			}
		}
		*/
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		if (c < mc) {
			c++;
		} else {
			c = 0;
			board[tX][tY] = 0;
			tY++;
			tX += tY/9;
			tY %= 9;
			tX %= 9;
			board[tX][tY] = 'o';
		}
		
		if (Gdx.input.justTouched()) {
			int in_x = Gdx.graphics.getHeight() - Gdx.input.getX();
			int in_y = Gdx.graphics.getHeight() - Gdx.input.getY();
			int miniboard = -1;
			int slot = -1;
			if(30 < in_x && in_x < ((Gdx.graphics.getHeight() - 60) / 3) - 15){
				//column 1
				if(30 < in_y && in_y < ((Gdx.graphics.getHeight() - 60) / 3) - 15){
					miniboard = 0; 
				}else if(((Gdx.graphics.getHeight() - 60) / 3) - 15 < in_y && in_y < ((Gdx.graphics.getHeight() - 60) * 2 / 3) - 15){
					miniboard = 3;
				}else if(((Gdx.graphics.getHeight() - 60) * 2 / 3 < in_y && in_y < ((Gdx.graphics.getHeight() - 60)))){
					miniboard = 6;
				}	
			}else if(((Gdx.graphics.getHeight() - 60) / 3) - 15 < in_x && in_x < ((Gdx.graphics.getHeight() - 60) * 2 / 3) - 15){
				//column 2
				if(30 < in_y && in_y < ((Gdx.graphics.getHeight() - 60) / 3) - 15){
					miniboard = 1; 
				}else if(((Gdx.graphics.getHeight() - 60) / 3) - 15 < in_y && in_y < ((Gdx.graphics.getHeight() - 60) * 2 / 3) - 15){
					miniboard = 4;
				}else if(((Gdx.graphics.getHeight() - 60) * 2 / 3 < in_y && in_y < ((Gdx.graphics.getHeight() - 60)))){
					miniboard = 7;
				}
			}else if(((Gdx.graphics.getHeight() - 60) * 2 / 3 < in_x && in_x < ((Gdx.graphics.getHeight() - 60)))){
				//column 3
				if(30 < in_y && in_y < ((Gdx.graphics.getHeight() - 60) / 3) - 15){
					miniboard = 2; 
				}else if(((Gdx.graphics.getHeight() - 60) / 3) - 15 < in_y && in_y < ((Gdx.graphics.getHeight() - 60) * 2 / 3) - 15){
					miniboard = 5;
				}else if(((Gdx.graphics.getHeight() - 60) * 2 / 3 < in_y && in_y < ((Gdx.graphics.getHeight() - 60)))){
					miniboard = 8;
				}
			}
			if(!(miniboard == -1 || slot == -1)){
				
			
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
	            //The winner is X
	        }else if(winner == 'O'){
	            //The winner is O;
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
	            	//tie game
	            }
	        }
	            /*   
	            valid_grids = validate_grids(board);
	            if(valid_grids[slot]){
	                for(int i = 0; i < 9; i++){
	                    valid_grids[i] = false;
	                }
	                valid_grids[slot] = true;
	                miniboard = slot;
	            }else{
	                miniboard = -1;
	            }
	            */
	        }
	    }
	    

		int grid_size = Math.min(Gdx.graphics.getHeight(), Gdx.graphics.getWidth());
		int miniboard_size = ((grid_size - 60) / 3) - 15;
		
		spritebatch.setProjectionMatrix(cam.combined);
		spritebatch.begin();{

			spritebatch.draw(grid, 30, 30, grid_size - 60, grid_size - 60);
			
			//drawing column 1
			if(board[0][0] == 'X'){
				spritebatch.draw(X, 35, 35, ((grid_size - 60) / 3) - 15, ((grid_size - 60) / 3) - 15);
			}else if(board[0][0] == 'O'){
				spritebatch.draw(O, 35, 35, ((grid_size - 60) / 3) - 15, ((grid_size - 60) / 3) - 15);	
			}else{
				spritebatch.draw(grid, 35, 35, ((grid_size - 60) / 3) - 15, ((grid_size - 60) / 3) - 15);
			}
			
				spritebatch.draw(grid, 35, 35 + (grid_size - 60) / 3, ((grid_size - 60) / 3) - 15, ((grid_size - 60) / 3) - 15);
				spritebatch.draw(grid, 35, 35 + (grid_size - 60) * 2 / 3, ((grid_size - 60) / 3) - 15, ((grid_size - 60) / 3) - 15);
			//drawing column 2
			spritebatch.draw(grid, 40 + (grid_size - 60) / 3, 35, ((grid_size - 60) / 3) - 15, ((grid_size - 60) / 3) - 15);
			spritebatch.draw(grid, 40 + (grid_size - 60) / 3, 35 + (grid_size - 60) / 3, ((grid_size - 60) / 3) - 15, ((grid_size - 60) / 3) - 15);
			spritebatch.draw(grid, 40 + (grid_size - 60) / 3, 35 + (grid_size - 60) * 2 / 3, ((grid_size - 60) / 3) - 15, ((grid_size - 60) / 3) - 15);
			
			//drawing column 3
			spritebatch.draw(grid, 40 + (grid_size - 60) * 2 / 3, 35, ((grid_size - 60) / 3) - 15, ((grid_size - 60) / 3) - 15);
			spritebatch.draw(grid, 40 + (grid_size - 60) * 2 / 3, 35 + (grid_size - 60) / 3, ((grid_size - 60) / 3) - 15, ((grid_size - 60) / 3) - 15);
			spritebatch.draw(grid, 40 + (grid_size - 60) * 2 / 3, 35 + (grid_size - 60) * 2 / 3, ((grid_size - 60) / 3) - 15, ((grid_size - 60) / 3) - 15);
			
			
			//drawing x's and o's
			for(int i = 0; i < 9; i++){
				if(board[i][0] == 'X'){
					spritebatch.draw(X, 40 + (grid_size - 60) * (i % 3) / 3, 35 + (grid_size - 60) * (i / 3) / 3, ((grid_size - 60) / 3) - 15, ((grid_size - 60) / 3) - 15);
				}else if(board[i][0] == 'O'){
					spritebatch.draw(O, 40 + (grid_size - 60) * (i % 3) / 3, 35 + (grid_size - 60) * (i / 3) / 3, ((grid_size - 60) / 3) - 15, ((grid_size - 60) / 3) - 15);
				}else{
					for(int j = 0; j < 9; j ++){
						if(board[i][j] == 'x'){
							spritebatch.draw(X, 40 + ((grid_size - 60) * (i % 3) / 3) + ((((grid_size - 60) / 3) - 15) / 3) * (j % 3), 35 + ((grid_size - 60) * (i / 3) / 3) + ((((grid_size - 60) / 3) - 15) / 3) * (j / 3), (((grid_size - 60) / 3) - 15) / 3, (((grid_size - 60) / 3) - 15) / 3);
						}else if(board[i][j] == 'o'){
							spritebatch.draw(O, 40 + ((grid_size - 60) * (i % 3) / 3) + ((((grid_size - 60) / 3) - 15) / 3) * (j % 3), 35 + ((grid_size - 60) * (i / 3) / 3) + ((((grid_size - 60) / 3) - 15) / 3) * (j / 3), (((grid_size - 60) / 3) - 15) / 3, (((grid_size - 60) / 3) - 15) / 3);
						}else{
							
						}
					}
				}
			}
			
		}
		spritebatch.end();
	}

	@Override
	public void dispose () {
		X.dispose();
		O.dispose();
		grid.dispose();
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
        return ' ';
    }
}
