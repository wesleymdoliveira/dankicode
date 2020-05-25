package com.viagemvirtual.apps.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.viagemvirtual.apps.graphics.Spritesheet;
import com.viagemvirtual.apps.graphics.UI;
import com.viagemvirtual.apps.main.Game;
import com.viagemvirtual.apps.world.Camera;
import com.viagemvirtual.apps.world.World;

public class Player extends Entity {

	public boolean right, left, up, down;
	public double speed = 1.3;

	private int frames = 0, maxFrames = 9;
	private int index = 0, maxIndex = 2;
	private boolean moved = false;

	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;
	private BufferedImage[] upPlayer;
	private BufferedImage[] downPlayer;

	private BufferedImage[] rightGun;
	private BufferedImage[] leftGun;
	private BufferedImage[] upGun;
	private BufferedImage[] downGun;

	private BufferedImage playerDamaged;
	public boolean playerIsDamaged = false;
	private int damageFrames = 0;

	public boolean shoot = false;
	public boolean mouseShoot = false;

	public int mx=0, my=0;
	
	public int right_dir = 0;
	public int left_dir = 1;
	public int up_dir = 2;
	public int down_dir = 3;

	public int dir = down_dir;

	public double life = 100.0; // nr. de vidas;
	public static double maxLife = 100.0;

	public int ammo = 0; // munição
	public static int guns = 0; // arma
	public boolean hasGun = false;

	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		rightPlayer = new BufferedImage[3];
		leftPlayer = new BufferedImage[3];
		upPlayer = new BufferedImage[3];
		downPlayer = new BufferedImage[3];

		rightGun = new BufferedImage[1];
		leftGun = new BufferedImage[1];
		upGun = new BufferedImage[1];
		downGun = new BufferedImage[1];

		playerDamaged = Game.spritesheet.getSprite(3 * 16, 3 * 16, World.TILE_SIZE, World.TILE_SIZE);

		for (int i = 0; i < leftPlayer.length; i++) {
			leftPlayer[i] = Game.spritesheet.getSprite(32 + (i * 16), (16 * 0), 16, 16);
		}
		for (int i = 0; i < upPlayer.length; i++) {
			upPlayer[i] = Game.spritesheet.getSprite(32 + (i * 16), (16 * 1), 16, 16);
		}
		for (int i = 0; i < downPlayer.length; i++) {
			downPlayer[i] = Game.spritesheet.getSprite(32 + (i * 16), (16 * 2), 16, 16);
		}
		for (int i = 0; i < rightPlayer.length; i++) {
			rightPlayer[i] = Game.spritesheet.getSprite(32 + (i * 16), (16 * 4), 16, 16);
		}

		rightGun[0] = Entity.GUN_RIGHT_ENTITY;

		leftGun[0] = Entity.GUN_LEFT_ENTITY;

		upGun[0] = Entity.GUN_UP_ENTITY;

		downGun[0] = Entity.GUN_DOWN_ENTITY;

	}

	public void tick() {
		moved = false;
		if (right && World.isFree((int) (x + speed), this.getY())) {
			x += speed;
			dir = right_dir;
			moved = true;
		} else if (left && World.isFree((int) (x - speed), this.getY())) {
			x -= speed;
			dir = left_dir;
			moved = true;
		}

		if (up && World.isFree(this.getX(), (int) (y - speed))) {
			y -= speed;
			dir = up_dir;
			moved = true;
		} else if (down && World.isFree(this.getX(), (int) (y + speed))) {
			y += speed;
			dir = down_dir;
			moved = true;
		}

		if (moved) {
			frames++;
			if (frames >= maxFrames) {
				frames = 0;
				index++;
				if (index >= maxIndex) {
					index = 0;
				}
			}
		}

		// Posicionamento da Camera
		// Camera.setX(this.getX() - (Game.WIDTH/2));
		// Camera.setY(this.getY() - (Game.HEIGHT/2));
		Camera.x = Camera.clamp(this.getX() - (Game.WIDTH / 2), 0, (World.MAP_WIDTH * 16) - Game.WIDTH);
		Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT / 2), 0, (World.MAP_HEIGHT * 16) - Game.HEIGHT);

		
		if (playerIsDamaged) {
			this.damageFrames++;
			if (this.damageFrames == 10) {
				this.damageFrames = 0;
				playerIsDamaged = false;
			}
		}

		if ( ( shoot || mouseShoot ) && hasGun && Game.player.ammo > 0) {
			// criar projetil e atirar
			shoot = false;
			mouseShoot = false;
			int dx = 0;
			int dy = 0;
			if (dir == right_dir) {
				dx = 1;
			} else if (dir == left_dir) {
				dx = -1;
			} else if (dir == up_dir) {
				dy = -1;
			} else if (dir == down_dir) {
				dy = 1;
			}

			BulletShoot bullet = new BulletShoot(this.getX(), this.getY(), 4, 4, null, dx, dy);
			Game.bullets.add(bullet);
		}

/*
		if (mouseShoot && hasGun && Game.player.ammo > 0) {
			// criar projetil e atirar
			mouseShoot = false;
			int dx = 0;
			int dy = 0;
			if (dir == right_dir) {
				dx = 1;
			} else if (dir == left_dir) {
				dx = -1;
			} else if (dir == up_dir) {
				dy = -1;
			} else if (dir == down_dir) {
				dy = 1;
			}

			//Criar projetil e atirar
			double angle = Math.toDegrees(Math.atan2(my - (this.getY() - Camera.y), mx - (this.getY() - Camera.x)));
			System.out.println("Angulo = "+angle);
			Game.player.ammo--;
			
			BulletShoot bullet = new BulletShoot(this.getX(), this.getY(), 4, 4, null, dx, dy);
			Game.bullets.add(bullet);
		}
*/
		
		// Game Over 
		if (life <= 0) {
			life = 0;
			Game.gameState = "GAME_OVER";
		}

		// Coleta life pack
		checkCollisionLifePack();

		// Coleta arma (gun)
		checkCollisionGun();

		// Coleta munição (ammo) - só pode coletar munição se tiver arma
		if (hasGun) {
			checkCollisionAmmo();
		}


	}

	public void render(Graphics g) {
		if (!playerIsDamaged) {
			if (dir == right_dir) {
				g.drawImage(rightPlayer[index], this.getX() - Camera.getX(), this.getY() - Camera.getY(), null);
				if (hasGun) {
					g.drawImage(Entity.GUN_RIGHT_ENTITY, this.getX() - Camera.getX(), (this.getY() - Camera.getY()) + 4,
							null);

				}
			} else if (dir == left_dir) {
				g.drawImage(leftPlayer[index], this.getX() - Camera.getX(), this.getY() - Camera.getY(), null);
				if (hasGun) {
					g.drawImage(Entity.GUN_LEFT_ENTITY, this.getX() - Camera.getX(), (this.getY() - Camera.getY()) + 5,
							null);

				}
			} else if (dir == up_dir) {
				g.drawImage(upPlayer[index], this.getX() - Camera.getX(), this.getY() - Camera.getY(), null);
				if (hasGun) {
					g.drawImage(Entity.GUN_UP_ENTITY, (this.getX() - Camera.getX()) + 5, this.getY() - Camera.getY(),
							null);

				}
			} else if (dir == down_dir) {
				g.drawImage(downPlayer[index], this.getX() - Camera.getX(), this.getY() - Camera.getY(), null);
				if (hasGun) {
					g.drawImage(Entity.GUN_DOWN_ENTITY, (this.getX() - Camera.getX()) - 5,
							(this.getY() - Camera.getY()) + 4, null);

				}
			} else {
				g.drawImage(downPlayer[index], this.getX() - Camera.getX(), this.getY() - Camera.getY(), null);
				if (hasGun) {
					if (hasGun) {
						g.drawImage(Entity.GUN_DOWN_ENTITY, (this.getX() - Camera.getX()) - 15,
								this.getY() - Camera.getY(), null);

					}

				}
			}
		} else {
			g.drawImage(playerDamaged, this.getX() - Camera.getX(), this.getY() - Camera.getY(), null);
		}

	}

	public void checkCollisionLifePack() {
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity e = Game.entities.get(i);
			if (e instanceof Lifepack) {
				if (Entity.isColliding(this, e)) {
					life += 10;
					if (life > maxLife) {
						life = maxLife;
					}
					Game.entities.remove(i);
					return;
				}
			}
		}
	}

	public void checkCollisionAmmo() {
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity e = Game.entities.get(i);
			if (e instanceof Bullet) {
				if (Entity.isColliding(this, e)) {
					ammo += 10;
					// System.out.println(ammo);
					Game.entities.remove(i);
					return;
				}
			}
		}
	}

	public void checkCollisionGun() {
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity e = Game.entities.get(i);
			if (e instanceof Weapon) {
				if (Entity.isColliding(this, e)) {
					hasGun = true;
					guns++;
					ammo += 20;
					Game.entities.remove(i);
					return;
				}
			}
		}
	}

}
