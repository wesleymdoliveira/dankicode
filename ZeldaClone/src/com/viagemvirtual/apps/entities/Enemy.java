package com.viagemvirtual.apps.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.viagemvirtual.apps.main.Game;
import com.viagemvirtual.apps.world.Camera;
import com.viagemvirtual.apps.world.World;

public class Enemy extends Entity {

	private double speed = 0.7;
	private int frames = 0, maxFrames = 10;
	private int index = 0, maxIndex = 2;

	private int maskx = 8, masky = 8, maskw = 10, maskh = 10;

	private BufferedImage[] enemySprites;
	private BufferedImage[] enemySpritesDamaged;

	private int life = 3;

	private boolean enemyIsDamaged = false;
	private int damageFrames = 30, damageCurrent = 0;

	public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, null);
		enemySprites = new BufferedImage[2];
		enemySpritesDamaged = new BufferedImage[2];

		enemySprites[0] = Game.spritesheet.getSprite(World.TILE_SIZE * 8, World.TILE_SIZE * 0, World.TILE_SIZE,
				World.TILE_SIZE);
		enemySprites[1] = Game.spritesheet.getSprite(World.TILE_SIZE * 9, World.TILE_SIZE * 0, World.TILE_SIZE,
				World.TILE_SIZE);

		enemySpritesDamaged[0] = Game.spritesheet.getSprite(World.TILE_SIZE * 8, World.TILE_SIZE * 1, World.TILE_SIZE,
				World.TILE_SIZE);
		enemySpritesDamaged[1] = Game.spritesheet.getSprite(World.TILE_SIZE * 9, World.TILE_SIZE * 1, World.TILE_SIZE,
				World.TILE_SIZE);
	}

	public void tick() {

		if (isCollidingWithPlayer() == false) {

			if (getX() < Game.player.getX() && World.isFree((int) (x + speed), getY())
					&& !isColliding((int) (x + speed), getY())) {
				x += speed;
			} else if (getX() > Game.player.getX() && World.isFree((int) (x - speed), getY())
					&& !isColliding((int) (x - speed), getY())) {
				x -= speed;
			}

			if (getY() < Game.player.getY() && World.isFree(getX(), (int) (y + speed))
					&& !isColliding(getX(), (int) (y + speed))) {
				y += speed;
			} else if (getY() > Game.player.getY() && World.isFree(getX(), (int) (y - speed))
					&& !isColliding(getX(), (int) (y - speed))) {
				y -= speed;
			}

		} else {
			// Inimigo está colidindo com o player
			if (Game.random.nextInt(100) < 20) {
				Game.player.life -= 0.5;
				Game.player.playerIsDamaged = true;
			}

			// if (Game.player.life <= 0) {
			// System.out.println(" --- GAME OVER ---");
			// System.exit(1);
			// }
			// System.out.println("Player tem " + Game.player.life + " vidas");

		}

		// if(moved) <-- deixar se mexer sempre
		frames++;
		if (frames >= maxFrames) {
			frames = 0;
			index++;
			if (index >= maxIndex) {
				index = 0;
			}
		}

		collidingBullet();
		if (life <= 0) {
			destroySelf();
			return;
		}

		if (this.enemyIsDamaged) {
			this.damageCurrent++;
			if (this.damageCurrent >= this.damageFrames) {
				this.damageCurrent = 0;
				this.enemyIsDamaged = false;
			}
		}
	}

	public void render(Graphics g) {
		if (this.enemyIsDamaged) {
			g.drawImage(enemySpritesDamaged[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
			//System.out.println("Spider damaged");
		} else {
			g.drawImage(enemySprites[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
			//System.out.println("...");
		}
	}

	public boolean isColliding(int xnext, int ynext) {
		Rectangle enemyCurrent = new Rectangle(xnext, ynext, World.TILE_SIZE, World.TILE_SIZE);
		for (int i = 0; i < Game.enemies.size(); i++) {
			Enemy e = Game.enemies.get(i);
			if (e == this)
				continue;
			Rectangle targetEnemy = new Rectangle(e.getX(), e.getY(), World.TILE_SIZE, World.TILE_SIZE);
			if (enemyCurrent.intersects(targetEnemy)) {
				return true;
			}
		}
		return false;
	}

	public boolean isCollidingWithPlayer() {
		Rectangle enemyCurrent = new Rectangle(this.getX(), this.getY(), World.TILE_SIZE, World.TILE_SIZE);
		Rectangle player = new Rectangle(Game.player.getX(), Game.player.getY(), World.TILE_SIZE, World.TILE_SIZE);
		return enemyCurrent.intersects(player);
	}

	public void collidingBullet() {
		for (int i = 0; i < Game.bullets.size(); i++) {
			Entity e = Game.bullets.get(i);
			if (e instanceof BulletShoot) {
				if (Entity.isColliding(this, e)) {
					this.enemyIsDamaged = true;
					this.life--;
					Game.bullets.remove(i);
					// return;
				}
			}
		}

	}

	public void destroySelf() {
		Game.entities.remove(this);
		Game.enemies.remove(this);
	}

}
