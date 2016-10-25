/*    Copyright 2013 Antti Kolehmainen

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License. */
package com.sturdyhelmetgames.roomforchange.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.sturdyhelmetgames.roomforchange.RoomForChangeGame;
import com.sturdyhelmetgames.roomforchange.assets.Assets;

public class MenuScreen extends Basic2DScreen {

	public MenuScreen(RoomForChangeGame game) {
		super(game, 12, 8);		
		Assets.getGameSound(Assets.SOUND_INTRO).loop(0.4f);

	}

	@Override
	protected void updateScreen(float fixedStep) {

	}

	@Override
	public void renderScreen(float delta) {
		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.begin();
		spriteBatch
				.draw(Assets.getFullGameObject("pyramid"), -6f, -4f, 12f, 8f);
		spriteBatch.end();
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.SPACE) {
			game.setScreen(new HelpScreen(game, new GameScreen(game)));
			return true;
		}
		return super.keyDown(keycode);
	}

	@Override
	public void show() {
		TextButtonStyle textButtonStyle = new TextButtonStyle();
		BitmapFont font = new BitmapFont();
		textButtonStyle.font = font;
		TextButton button1 = new TextButton("This is a button!!!", textButtonStyle);
		TextButton button2 = new TextButton("This is a button!!!", textButtonStyle);
		TextButton button3 = new TextButton("This is a button!!!", textButtonStyle);

		Table table = new Table();
		table.setFillParent(true);
		table.center().center();
		
		table.add(button1);
		table.row();
		table.add(button2);
		table.row();
		table.add(button3);
		table.row();
	}
}
