package com.viagemvirtual.apps.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.viagemvirtual.apps.main.Game;
import com.viagemvirtual.apps.world.Camera;

public class BulletShoot extends Entity {

	private int dx;
	private int dy;
	private double speed = 2;

	private int lifeOfBullet = 60, curLifeOfBullet = 0;

	public BulletShoot(int x, int y, int width, int height, BufferedImage sprite, int dx, int dy) {
		super(x, y, width, height, sprite);
		this.dx = dx;
		this.dy = dy;

	}

	public void tick() {
		x += dx * speed;
		y += dy * speed;

		curLifeOfBullet++;
		if(curLifeOfBullet == lifeOfBullet) {
			Game.bullets.remove(this);
		}
	
	}

	public void render(Graphics g) {
		g.setColor(Color.YELLOW);
		if (dx == 1) { // right
			g.fillOval((this.getX() - Camera.x) + 13, (this.getY() - Camera.y) + 10, 3, 3);
		}
		if (dx == -1) { // left
			g.fillOval((this.getX() - Camera.x) - 1, (this.getY() - Camera.y) + 10, 3, 3);
		}
		if (dy == -1) { // up
			g.fillOval((this.getX() - Camera.x) + 10, (this.getY() - Camera.y) - 1, 3, 3);
		}

		if (dy == 1) { // down
			g.fillOval((this.getX() - Camera.x) + 2, (this.getY() - Camera.y) + 17, 3, 3);
		}
	}

}
