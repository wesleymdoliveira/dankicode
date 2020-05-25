package com.viagemvirtual.apps.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.viagemvirtual.apps.main.Game;
import com.viagemvirtual.apps.world.Camera;
import com.viagemvirtual.apps.world.World;

public class Entity {

	// Life pack
	public static BufferedImage LIFEPACK_ENTITY = Game.spritesheet.getSprite(7 * World.TILE_SIZE, 0, World.TILE_SIZE, World.TILE_SIZE);
	// Arma
	public static BufferedImage WEAPON_ENTITY = Game.spritesheet.getSprite(5 * World.TILE_SIZE, 0, World.TILE_SIZE, World.TILE_SIZE);
	// Munição
	public static BufferedImage BULLET_ENTITY = Game.spritesheet.getSprite(6 * World.TILE_SIZE, 0, World.TILE_SIZE, World.TILE_SIZE);
	// Inimigo1 (Spider1)
	public static BufferedImage SPIDER_ENTITY = Game.spritesheet.getSprite(8 * World.TILE_SIZE, 0, World.TILE_SIZE, World.TILE_SIZE);
	// Inimigo
	// public static BufferedImage ENEMY2_ENTITY = Game.spritesheet.getSprite(9 *
	// 16, 0, 16, 16);
	public static BufferedImage ENEMY1_FEEDBACK = Game.spritesheet.getSprite(8 * World.TILE_SIZE, 1, World.TILE_SIZE, World.TILE_SIZE);

	public static BufferedImage GUN_RIGHT_ENTITY = Game.spritesheet.getSprite(5 * World.TILE_SIZE, 1 * World.TILE_SIZE,
			World.TILE_SIZE, World.TILE_SIZE);
	public static BufferedImage GUN_LEFT_ENTITY = Game.spritesheet.getSprite(5 * World.TILE_SIZE, 2 * World.TILE_SIZE,
			World.TILE_SIZE, World.TILE_SIZE);
	public static BufferedImage GUN_UP_ENTITY = Game.spritesheet.getSprite(5 * World.TILE_SIZE, 3 * World.TILE_SIZE,
			World.TILE_SIZE, World.TILE_SIZE);
	public static BufferedImage GUN_DOWN_ENTITY = Game.spritesheet.getSprite(5 * World.TILE_SIZE, 4 * World.TILE_SIZE,
			World.TILE_SIZE, World.TILE_SIZE);

	
	protected double x;
	protected double y;
	protected int width;
	protected int height;
	protected BufferedImage sprite;

	private int maskx = 8, masky = 8, mwidth = 10, mheight = 10;

	public Entity(int x, int y, int width, int height, BufferedImage sprite) {
		this.setX(x);
		this.setY(y);
		this.setWidth(width);
		this.setHeight(height);
		this.setSprite(sprite);

		this.maskx = 0;
		this.masky = 0;
		this.mwidth = width;
		this.mheight = height;
	}

	public void setMask(int maskx, int masky, int mwidth, int mheight) {
		this.maskx = maskx;
		this.masky = masky;
		this.mwidth = mwidth;
		this.mheight = mheight;
	}

	/**
	 * Renderização
	 */
	public void render(Graphics g) {
		g.drawImage(this.getSprite(), this.getX() - Camera.getX(), this.getY() - Camera.getY(), null);

		// Debug - ver a mascara do item;
		// g.setColor(Color.blue);
		// g.fillRect(this.getX()+maskx-Camera.x, this.getY()+masky-Camera.y, mwidth,
		// mheight);
	}

	/**
	 * Tick
	 */
	public void tick() {

	}

	public static boolean isColliding(Entity e1, Entity e2) {
		Rectangle e1Mask = new Rectangle(e1.getX() + e1.maskx, e1.getY() + e1.masky, e1.mwidth, e1.mheight);
		Rectangle e2Mask = new Rectangle(e2.getX() + e2.maskx, e2.getY() + e2.masky, e2.mwidth, e2.mheight);

		return e1Mask.intersects(e2Mask);
	}

	/**
	 * Getters / Setters
	 * 
	 * @return
	 */

	public int getX() {
		return (int) x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return (int) y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public BufferedImage getSprite() {
		return sprite;
	}

	public void setSprite(BufferedImage sprite) {
		this.sprite = sprite;
	}
}
