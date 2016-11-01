package com.sturdyhelmetgames.roomforchange.screen;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.sturdyhelmetgames.roomforchange.RoomForChangeGame;
import com.sturdyhelmetgames.roomforchange.assets.Assets;

public class PauseScreen extends Basic2DScreen {

	private final GameScreen gameScreen;

	public PauseScreen(RoomForChangeGame game, GameScreen gameScreen) {
		super(game, 12, 8);
		this.gameScreen = gameScreen;
	}

	@Override
	protected void updateScreen(float fixedStep) {

	}

	@Override
	public void renderScreen(float delta) {
		gameScreen.renderScreen(delta);
		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.begin();
		final Color originalColor = spriteBatch.getColor();
		spriteBatch.setColor(1f, 1f, 1f, 0.5f);
		spriteBatch.draw(Assets.getFullGameObject("black"), -6f, -4f, 12f, 8f);
		spriteBatch.setColor(originalColor);
		spriteBatch
				.draw(Assets.getFullGameObject("pause"), -2f, -1f, 4f, 2f);
		spriteBatch.end();
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.Y) {
			this.hide();
			gameScreen.playSound();
			gameScreen.setPausedGame(false);
			gameScreen.resume();
			game.setScreen(gameScreen);
			Assets.getGameSound(Assets.SOUND_BUTTON).play(0.5f);
		} else if (keycode == Keys.N) {
			gameScreen.setPausedGame(false);
			game.setScreen(new MenuScreen(game));
		}
		return super.keyDown(keycode);
	}

	@Override
	public void show() {
		super.show();
		Assets.getGameSound(Assets.SOUND_LEVEL1).stop();
		Assets.getGameSound(Assets.SOUND_LEVEL2).stop();
		Assets.getGameSound(Assets.SOUND_LEVEL3).stop();	
		Assets.getGameSound(Assets.SOUND_LEVEL4).stop();	
		Assets.getGameSound(Assets.SOUND_BUTTON).play(0.5f);
	}

}
