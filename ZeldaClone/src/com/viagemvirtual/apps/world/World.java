/**
 * Aulas: 
 * - Módulo #10: Criando game engine e jogo #1(Zelda Clone) > Iniciando com mapa
 * - Módulo #10: Criando game engine e jogo #1(Zelda Clone) > Tiles e validando posições
 * - Módulo #10: Criando game engine e jogo #1(Zelda Clone) > Entities no mapa
 * yeelws 22.06.2019 20:38
 * 
 * Class World - package "world"
 * Trata dos mapas do jogo
 * Foi ensinado a fazer o mapa na aula "Finalizando Sprites" a partir dos 00:07:00 minutos
 * Tamanho do primeiro mapa: 20px x 20px
 * - Todo pixel preto 0xFF000000 é chão;
 * - Todo pixel branco 0xFFFFFFFF é parede;
 * - Azul 0xFF0000ff é o jogador no centro do mapa
 * - Vermelho 0xFFFF0000 é o inimigo
 * - Item de vida 0xFF00FF00 Verde 
 * - Munição: Amarelo 0xFFFFFF00
 * - Arma: Laranja OxFFFF5100
 */
package com.viagemvirtual.apps.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.viagemvirtual.apps.entities.Bullet;
import com.viagemvirtual.apps.entities.Enemy;
import com.viagemvirtual.apps.entities.Entity;
import com.viagemvirtual.apps.entities.Lifepack;
import com.viagemvirtual.apps.entities.Player;
import com.viagemvirtual.apps.entities.Weapon;
import com.viagemvirtual.apps.graphics.Spritesheet;
import com.viagemvirtual.apps.main.Game;

public class World {

	public static Tile[] tiles;
	public static int MAP_WIDTH, MAP_HEIGHT;
	public static final int PIXEL_FLOOR = 0xFF000000; // preto
	public static final int PIXEL_WALL = 0xFFFFFFFF; // branco
	public static final int PIXEL_PLAYER = 0xFF0000FF; // azul
	public static final int PIXEL_ENEMY = 0xFFFF0000; // vermelho
	public static final int PIXEL_LIFEPACK = 0xFF00FF00; // verde
	public static final int PIXEL_BULLET = 0xFFFFFF00; // amarelo
	public static final int PIXEL_WEAPON = 0xFFFF5100; // laranja

	public static final int TILE_SIZE = 16;
	public static BufferedImage map;

	public World(String path) {
		try {
			map = ImageIO.read(getClass().getResource(path));
			int pixels[] = new int[map.getWidth() * map.getHeight()];
			MAP_WIDTH = map.getWidth();
			MAP_HEIGHT = map.getHeight();
			tiles = new Tile[map.getWidth() * map.getHeight()];
			map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels, 0, map.getWidth());
			// int auxEnemy = 0;
			for (int xx = 0; xx < map.getWidth(); xx++) {
				for (int yy = 0; yy < map.getHeight(); yy++) {
					int pixelAtual = pixels[xx + (yy * map.getWidth())];
					tiles[xx + (yy * MAP_WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);
					if (pixelAtual == PIXEL_FLOOR) {
						tiles[xx + (yy * MAP_WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);
					} else if (pixelAtual == PIXEL_WALL) {
						tiles[xx + (yy * MAP_WIDTH)] = new WallTile(xx * 16, yy * 16, Tile.TILE_WALL);
					} else if (pixelAtual == PIXEL_PLAYER) {
						Game.player.setX(xx * 16);
						Game.player.setY(yy * 16);
					} else if (pixelAtual == PIXEL_ENEMY) {
						Enemy spider = new Enemy(xx * 16, yy * 16, 16, 16, Entity.SPIDER_ENTITY);
						// Enemy enemy2 = new Enemy(xx * 16, yy * 16, 16, 16, Entity.ENEMY2_ENTITY);
						// if (auxEnemy == 0) {
						Game.entities.add(spider);
						Game.enemies.add(spider);
						// auxEnemy++;
						// } else if (auxEnemy == 1) {
						// Game.entities.add(enemy2);
						// Game.enemies.add(enemy2);
						// auxEnemy = 0;
						// }
					} else if (pixelAtual == PIXEL_LIFEPACK) {
						Lifepack lifepack = new Lifepack(xx * 16, yy * 16, 16, 16, Entity.LIFEPACK_ENTITY);
						lifepack.setMask(8, 8, 8, 8);
						Game.entities.add(lifepack);
					} else if (pixelAtual == PIXEL_WEAPON) {
						Weapon weapon = new Weapon(xx * 16, yy * 16, 16, 16, Entity.WEAPON_ENTITY);
						weapon.setMask(8, 8, 8, 8);
						Game.entities.add(weapon);
					} else if (pixelAtual == PIXEL_BULLET) {
						Bullet bullet = new Bullet(xx * 16, yy * 16, 16, 16, Entity.BULLET_ENTITY);
						bullet.setMask(8, 8, 8, 8);
						Game.entities.add(bullet);
					} else {
						System.out.println("Pixel inesperado: " + pixelAtual);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void render(Graphics g) {

		// Camera
		int xstart = Camera.getX() / 16;
		int ystart = Camera.getY() / 16;

		int xfinal = xstart + (Game.WIDTH / 16);
		int yfinal = ystart + (Game.HEIGHT / 16);

		for (int xx = xstart; xx <= xfinal; xx++) {
			for (int yy = ystart; yy <= yfinal; yy++) {
				if (xx < 0 || yy < 0 || xx >= MAP_WIDTH || yy >= MAP_HEIGHT) {
					continue;
				} else {
					Tile tile = tiles[xx + (yy * MAP_WIDTH)];
					tile.render(g);
				}
			}
		}
	}

	// Restart Game
	public static void restartGame(String level) {
		Game.entities.clear();
		Game.enemies.clear();
		Game.entities = new ArrayList<Entity>();
		Game.enemies = new ArrayList<Enemy>();
		Game.spritesheet = new Spritesheet("/spritesheet.png");
		Game.player = new Player(0, 0, 16, 16, Game.spritesheet.getSprite(32, 32, 16, 16));
		Game.entities.add(Game.player);
		Game.world = new World("/" + level);
		return;

	}

	// Tile Collision
	public static boolean isFree(int xnext, int ynext) {
		int x1 = xnext / TILE_SIZE;
		int y1 = ynext / TILE_SIZE;

		int x2 = (xnext + TILE_SIZE - 1) / TILE_SIZE;
		int y2 = ynext / TILE_SIZE;

		int x3 = xnext / TILE_SIZE;
		int y3 = (ynext + TILE_SIZE - 1) / TILE_SIZE;

		int x4 = (xnext + TILE_SIZE - 1) / TILE_SIZE;
		int y4 = (ynext + TILE_SIZE - 1) / TILE_SIZE;

		// System.out.println("xnext="+xnext+" ynext="+ynext+ " TILE_SIZE="+TILE_SIZE);
		// System.out.println("World.MAP_WIDTH="+World.MAP_WIDTH+"
		// World.MAP_HEIGHT="+World.MAP_HEIGHT);
		// System.out.println("x1="+x1+" x2="+x2+" x3="+x3+" x4="+x4);
		// System.out.println("y1="+y1+" y2="+y2+" y3="+y3+" y4="+x4);
		System.out.println("tiles.length=" + tiles.length);

		int v1, v2, v3, v4;
		int MAX_V = (map.getWidth() * map.getHeight()) - 1;
		v1 = x1 + (y1 * map.getWidth());
		if (v1 > MAX_V) {
			v1 = MAX_V;
		} else if (v1 < 0) {
			v1 = 0;
		}
		v2 = x2 + (y2 * map.getWidth());
		if (v2 > MAX_V) {
			v2 = MAX_V;
		}else if (v2 < 0) {
			v2 = 0;
		}
		v3 = x3 + (y3 * map.getWidth());
		if (v3 > MAX_V) {
			v3 = MAX_V;
		}else if (v3 < 0) {
			v3 = 0;
		}
		v4 = x4 + (y4 * map.getWidth());
		if (v4 > MAX_V) {
			v4 = MAX_V;
		}else if (v4 < 0) {
			v4 = 0;
		}
		return !(tiles[v1] instanceof WallTile 
				|| tiles[v2] instanceof WallTile 
				|| tiles[v3] instanceof WallTile
				|| tiles[v4] instanceof WallTile);

	}

}
