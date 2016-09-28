package org.amityregion5.tictactical;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class TicTactical extends Game {

	@Override
	public void create () {
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}
	
	@Override
	public void dispose () {
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
