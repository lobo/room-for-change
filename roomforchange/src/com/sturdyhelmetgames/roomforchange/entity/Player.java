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
package com.sturdyhelmetgames.roomforchange.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.sturdyhelmetgames.roomforchange.assets.Assets;
import com.sturdyhelmetgames.roomforchange.level.Level;
import com.sturdyhelmetgames.roomforchange.level.Level.LevelTile;

public class Player extends Entity {

	public float dyingAnimState = 0f;
	public float dyingTime = 0f;
	public float maxDyingTime = 3f;
	public int health = 4;
	public int bombs = 2;
	public int maxHealth = 4;
	public final Rectangle hitBounds = new Rectangle(0f, 0f, 0.8f, 0.8f);
	private float tryHitTime = 0.3f;

	public boolean gotScroll = false;
	public boolean gotTalisman = false;
	public boolean gotGem = false;

	public boolean getHeart1 = false;
	public boolean getHeart2 = false;
	public boolean getHeart3 = false;
	
	public int counter_fps = 0;
	
	public boolean hasFallen = false;
	
	public Player(float x, float y, Level level) {
		super(x, y, 1f, 0.6f, level);
		direction = Direction.UP;
		hasFallen = false;
		gotScroll = false;
		gotTalisman = false;
		gotGem = false;
	}

	@Override
	public void render(float delta, SpriteBatch batch) {
		super.render(delta, batch);

		Animation animation = null;

		if (blinkTick < BLINK_TICK_MAX) {
			if (isFalling()) {
				animation = Assets.playerFalling;
				batch.draw(animation.getKeyFrame(dyingAnimState),
						bounds.x - 0.1f, bounds.y, width, height + 0.4f);
			} else if (isDying() || isDead()) {
				if(!hasFallen) {
					animation = Assets.playerDying;
					batch.draw(animation.getKeyFrame(dyingAnimState),
							bounds.x - 0.1f, bounds.y, width, height + 0.4f);					
				}
			} else {
				if (direction == Direction.UP) {
					animation = Assets.playerWalkBack;
				} else if (direction == Direction.DOWN) {
					animation = Assets.playerWalkFront;
				} else if (direction == Direction.RIGHT) {
					animation = Assets.playerWalkRight;
				} else if (direction == Direction.LEFT) {
					animation = Assets.playerWalkLeft;
				}
				if (isNotWalking()) {
					batch.draw(animation.getKeyFrame(0.25f), bounds.x - 0.1f,
							bounds.y, width, height + 0.4f);
				} else {
					batch.draw(animation.getKeyFrame(stateTime, true),
							bounds.x - 0.1f, bounds.y, width, height + 0.4f);
							
					//Codigo para los pasos. horrible
					if(counter_fps == 0) {
						if(!level.gameScreen.isLeverOpen() && !level.gameScreen.isFishedGame() && !level.gameScreen.isPausedGame())
							Assets.getGameSound(Assets.SOUND_STEP).play(0.5f);

						counter_fps++;
					} else if(counter_fps == 60)
						counter_fps = 0;
					else
						counter_fps++;
					
				}
			}
		}

		if (tryHitTime < 0.3) {
			float rotation = 0f;
			float x = this.bounds.x;
			float y = this.bounds.y;
			if (direction == Direction.UP) {
				rotation = 270f;
				y += .8f;
			} else if (direction == Direction.DOWN) {
				rotation = 90f;
				y -= .8f;
			} else if (direction == Direction.RIGHT) {
				rotation = 180f;
				x += .8f;
			} else if (direction == Direction.LEFT) {
				x -= .8f;
			}
			batch.draw(Assets.hitTarget.getKeyFrame(tryHitTime, true), x, y,
					1f / 2, 1f / 2, 1f, 1f, 1f, 1f, rotation);
		}

	}

	@Override
	public void update(float fixedStep) {

		if (tryHitTime < 0.3) {
			tryHitTime += fixedStep;
		}

		if (isDying() || isFalling()) {
			dyingAnimState += fixedStep;
			dyingTime += fixedStep;
			if (dyingTime >= maxDyingTime) {
				state = EntityState.DEAD;
				level.gameScreen.gameOver();
			}
		}

		if(collectedItems() == 1 && !getHeart1 ) {
			maxHealth++;
			getHeart1 = true;
		}

		if(collectedItems() == 2 && !getHeart2 ) {
			maxHealth++;
			getHeart2 = true;
		}
		
		if(collectedItems() == 3 && !getHeart3 ) {
			maxHealth++;
			getHeart3 = true;
		}
		
		super.update(fixedStep);

	}

	private static final float HIT_DISTANCE = 0.5f;
	private final Rectangle leverRect = new Rectangle();

	public void tryHit() {
		if (!isDying() && !isDead() && !isFalling()) {
			tryHitTime = 0f;
			hitBounds.x = bounds.x;
			hitBounds.y = bounds.y;
			if (direction == Direction.LEFT) {
				hitBounds.x -= HIT_DISTANCE;
			} else if (direction == Direction.RIGHT) {
				hitBounds.x += HIT_DISTANCE;
			} else if (direction == Direction.UP) {
				hitBounds.y += HIT_DISTANCE;
			} else if (direction == Direction.DOWN) {
				hitBounds.y -= HIT_DISTANCE;
			}

			double aux_health;
			boolean miss_hit = true;
			for (int i = 0; i < level.entities.size; i++) {
				final Entity entity = level.entities.get(i);
				aux_health = entity.height;
				entity.hit(hitBounds);
				if(aux_health != entity.height)
					miss_hit = false;				
			}
			
			if(miss_hit)
				Assets.getGameSound(Assets.SOUND_HIT_AIR).play(0.5f);

			// double the hit distance for tiles
			if (direction == Direction.LEFT) {
				hitBounds.x -= HIT_DISTANCE;
			} else if (direction == Direction.RIGHT) {
				hitBounds.x += HIT_DISTANCE;
			} else if (direction == Direction.UP) {
				hitBounds.y += HIT_DISTANCE;
			} else if (direction == Direction.DOWN) {
				hitBounds.y -= HIT_DISTANCE;
			}

			tryHitLever((int) hitBounds.x, (int) hitBounds.y);
			tryHitLever((int) hitBounds.x + 1, (int) hitBounds.y);
			tryHitLever((int) hitBounds.x, (int) hitBounds.y + 1);
		}
	}

	private void tryHitLever(int x, int y) {
		LevelTile tile = level.getTiles()[x][y];
		if (tile.type == Level.LevelTileType.LEVER) {
			level.gameScreen.openLeverScreen();
		}
	}

	public void takeDamage() {
		if (!isInvulnerable()) {
			health--;
			invulnerableTick = INVULNERABLE_TIME_MIN;
			if(health > 0 && !hasFallen)
				Assets.getGameSound(Assets.SOUND_HURT).play(0.4f);
		}
		if (health <= 0 && !isDead() && !isDying()) {
			state = EntityState.DYING;
			Assets.getGameSound(Assets.SOUND_DYING).play(0.4f);
		}
	}

	@Override
	protected void fall() {
		if (!isFalling() && !isDead()) {
			Assets.getGameSound(Assets.SOUND_FALLING).play(0.5f);
			hasFallen = true;
			super.fall();
		}
	}

	public void gainHealth() {
		if(health + 1 <= maxHealth)
			health++;
	}

	public boolean isInvulnerable() {
		return invulnerableTick > 0f;
	}

	public boolean canFinishGame() {
		return gotGem && gotScroll && gotTalisman;
	}

	public void dropBomb() {
		if (level.player.bombs > 0) {
			level.player.bombs--;
			Assets.getGameSound(Assets.SOUND_DROPBOMB).play(0.3f);
			level.entities.add(new ExplodingBomb(bounds.x, bounds.y, level));
		} else 
			Assets.getGameSound(Assets.SOUND_EMPTYBOMB).play(0.5f);
	}
	
	public void moveWithAccel(Direction dir) {
			super.moveWithAccel(dir);
	}
	
	public int collectedItems() {
		int collectedItems = 0;
		
		if(gotGem)
			collectedItems++;
		if(gotScroll)
			collectedItems++;
		if(gotTalisman)
			collectedItems++;
		
		return collectedItems;
	}
}
