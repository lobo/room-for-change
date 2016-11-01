package com.sturdyhelmetgames.roomforchange.screen;

import com.badlogic.gdx.Input.Keys;
import com.sturdyhelmetgames.roomforchange.RoomForChangeGame;
import com.sturdyhelmetgames.roomforchange.assets.Assets;

public class CreditScreen extends Basic2DScreen {

	public CreditScreen(RoomForChangeGame game) {
		super(game, 12, 8);		
	}

	@Override
	protected void updateScreen(float fixedStep) {

	}

	@Override
	public void renderScreen(float delta) {
		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.begin();
		spriteBatch
				.draw(Assets.getFullGameObject("credit"), -6f, -4f, 12f, 8f);
		spriteBatch.end();
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.SPACE) {
			game.setScreen(new MenuScreen(game));
			Assets.getGameSound(Assets.SOUND_BUTTON).play(0.5f);
			return true;
		}
		return super.keyDown(keycode);
	}

	@Override
	public void show() {
	}
}
