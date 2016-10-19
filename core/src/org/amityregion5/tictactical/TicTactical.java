package org.amityregion5.tictactical;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TicTactical extends Game {

	Texture X;
	Texture O;
	Texture grid;

	SpriteBatch spritebatch;

	@Override
	public void create () {
		X = new Texture(Gdx.files.internal("X.png"));
		O = new Texture(Gdx.files.internal("O.png"));
		grid = new Texture(Gdx.files.internal("grid.png"));
		spritebatch = new SpriteBatch();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		if (Gdx.input.justTouched()) {
			int in_x = Gdx.input.getX();
			int in_y = Gdx.input.getY();
			//If an empty tile that can be pressed on was pressed
				//Do the things
		}

		spritebatch.begin();{

			spritebatch.draw(grid, 30, 30, Gdx.graphics.getHeight() - 60, Gdx.graphics.getHeight() - 60);
			
			//drawing column 1
			spritebatch.draw(grid, 35, 35, ((Gdx.graphics.getHeight() - 60) / 3) - 15, ((Gdx.graphics.getHeight() - 60) / 3) - 15);
			spritebatch.draw(grid, 35, 35 + (Gdx.graphics.getHeight() - 60) / 3, ((Gdx.graphics.getHeight() - 60) / 3) - 15, ((Gdx.graphics.getHeight() - 60) / 3) - 15);
			spritebatch.draw(grid, 35, 35 + (Gdx.graphics.getHeight() - 60) * 2 / 3, ((Gdx.graphics.getHeight() - 60) / 3) - 15, ((Gdx.graphics.getHeight() - 60) / 3) - 15);
			
			//drawing column 2
			spritebatch.draw(grid, 40 + (Gdx.graphics.getHeight() - 60) / 3, 35, ((Gdx.graphics.getHeight() - 60) / 3) - 15, ((Gdx.graphics.getHeight() - 60) / 3) - 15);
			spritebatch.draw(grid, 40 + (Gdx.graphics.getHeight() - 60) / 3, 35 + (Gdx.graphics.getHeight() - 60) / 3, ((Gdx.graphics.getHeight() - 60) / 3) - 15, ((Gdx.graphics.getHeight() - 60) / 3) - 15);
			spritebatch.draw(grid, 40 + (Gdx.graphics.getHeight() - 60) / 3, 35 + (Gdx.graphics.getHeight() - 60) * 2 / 3, ((Gdx.graphics.getHeight() - 60) / 3) - 15, ((Gdx.graphics.getHeight() - 60) / 3) - 15);
			
			//drawing column 3
			spritebatch.draw(grid, 40 + (Gdx.graphics.getHeight() - 60) * 2 / 3, 35, ((Gdx.graphics.getHeight() - 60) / 3) - 15, ((Gdx.graphics.getHeight() - 60) / 3) - 15);
			spritebatch.draw(grid, 40 + (Gdx.graphics.getHeight() - 60) * 2 / 3, 35 + (Gdx.graphics.getHeight() - 60) / 3, ((Gdx.graphics.getHeight() - 60) / 3) - 15, ((Gdx.graphics.getHeight() - 60) / 3) - 15);
			spritebatch.draw(grid, 40 + (Gdx.graphics.getHeight() - 60) * 2 / 3, 35 + (Gdx.graphics.getHeight() - 60) * 2 / 3, ((Gdx.graphics.getHeight() - 60) / 3) - 15, ((Gdx.graphics.getHeight() - 60) / 3) - 15);
			
			
			
		}spritebatch.end();
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
		// TODO Auto-generated method stub
		super.resize(width, height);
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
}
