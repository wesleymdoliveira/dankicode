/**
 * Zelda - Clone
 * yeelws nodmance virilaeo - 21.06.2019
 * Curso Desenvolvimento de Games Completo
 * Módulo #10: Criando game engine e jogo #1 (Zelda Clone)
 */
package com.viagemvirtual.apps.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import com.viagemvirtual.apps.entities.BulletShoot;
import com.viagemvirtual.apps.entities.Enemy;
import com.viagemvirtual.apps.entities.Entity;
import com.viagemvirtual.apps.entities.Player;
import com.viagemvirtual.apps.graphics.Spritesheet;
import com.viagemvirtual.apps.graphics.UI;
import com.viagemvirtual.apps.world.Camera;
import com.viagemvirtual.apps.world.World;

public class Game extends Canvas implements Runnable, KeyListener, MouseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static JFrame frame;
	public static final int WIDTH = 240; // 320;
	public static final int HEIGHT = 160; // 320;
	private final int SCALE = 3;
	private Thread thread;
	private boolean isRunning;
	private BufferedImage image; // layer
	public static List<Entity> entities;
	public static List<Enemy> enemies;
	public static List<BulletShoot> bullets;
	public static Spritesheet spritesheet;
	public static Player player;

	public static World world;
	public String newWorld = "/level1.png";

	public static Random random;

	public UI ui;

	private int CUR_LEVEL = 1, MAX_LEVEL = 2;

	public static String gameState = "NORMAL";

	public Game() {

		random = new Random();

		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		initframe();

		// Iniialização de objetos
		ui = new UI();
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		entities = new ArrayList<Entity>();
		enemies = new ArrayList<Enemy>();
		bullets = new ArrayList<BulletShoot>();
		spritesheet = new Spritesheet("/spritesheet.png");
		player = new Player(0, 0, 16, 16, spritesheet.getSprite(32, 32, 16, 16));
		entities.add(player);

		world = new World(this.newWorld);

		addKeyListener(this);

		addMouseListener(this);

	}

	public void initframe() {
		frame = new JFrame("Zelda Clone 1.0 21.06.2019");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public synchronized void start() {
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}

	public synchronized void stop() {
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// System.exit(1);
	}

	public void tick() {

		if (gameState == "NORMAL") {
			for (int i = 0; i < entities.size(); i++) {
				Entity e = entities.get(i);
				e.tick();
			}

			for (int i = 0; i < bullets.size(); i++) {
				bullets.get(i).tick();
			}

			if (enemies.size() == 0) {
				CUR_LEVEL++;
				if (CUR_LEVEL > MAX_LEVEL) {
					CUR_LEVEL = 1;
				}
				newWorld = "level" + CUR_LEVEL + ".png";
				World.restartGame(newWorld);
			}
		} else if (gameState == "GAME_OVER") {
			// TODO
		}
	}

	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = image.getGraphics();
		g.setColor(new Color(0, 0, 0));
		g.fillRect(0, 0, WIDTH * SCALE, HEIGHT * SCALE);

		/* Renderização do Jogo */

		// World tem de ser renderizado antes das entidades pois senão "cobre" o jogador
		// (é a lógica, o "world" é a base, é o chão
		world.render(g);

		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.render(g);
		}

		for (int i = 0; i < bullets.size(); i++) {
			bullets.get(i).render(g);
		}

		/*****/
		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);

		// Fonte não pixelada
		ui.render(g);

		// Game Over
		if (gameState == "GAME_OVER") {
			Graphics2D g2D = (Graphics2D) g;
			g2D.setColor(new Color(0, 0, 0, 100));
			g2D.fillRect(0, 0, WIDTH * SCALE, HEIGHT * SCALE);
			g2D.setFont(new Font("Arial",Font.BOLD,40));
			g2D.setColor(Color.ORANGE);
			g2D.drawString("GAME OVER", ((WIDTH*SCALE)/2)-Camera.x-20, ((HEIGHT*SCALE)/2)-Camera.y+20);
			g2D.setFont(new Font("Arial",Font.BOLD,30));
			g2D.setColor(Color.WHITE);
			g2D.drawString("Press >ENTER< to Restart", ((WIDTH*SCALE)/2)-Camera.x-80, ((HEIGHT*SCALE)/2)-Camera.y+80);
			
		}

		bs.show();
	}

	public void run() {
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0; // 60 FPS
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();

		requestFocus();

		while (isRunning) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if (delta >= 1) {
				tick();
				render();
				frames++;
				delta--;
			}

			if ((System.currentTimeMillis() - timer) >= 1000) {

				// Debug FPS
				// System.out.println("FPS: " + frames);

				frames = 0;

				// timer += 1000;
				timer = System.currentTimeMillis();

			}

		}

		stop();
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			// display("Direita");
			player.right = true;
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			// display("Esquerda");
			player.left = true;
		}

		if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			// display("Up");
			player.up = true;
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			// display("Down");
			player.down = true;
		}

		if (e.getKeyCode() == KeyEvent.VK_X || e.getKeyCode() == KeyEvent.VK_SPACE) {
			player.shoot = true;
			if (Game.player.ammo > 0) {
				Game.player.ammo--;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			player.right = false;
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			player.left = false;
		}

		if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			player.up = false;
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			player.down = false;
		}

		if (e.getKeyCode() == KeyEvent.VK_X || e.getKeyCode() == KeyEvent.VK_SPACE) {
			player.shoot = false;
		}
	}

	public void display(String s) {
		System.out.println("$> " + s);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		Game.player.mouseShoot = true;
		Game.player.mx = (e.getX() / SCALE);
		Game.player.my = (e.getY() / SCALE);
		// System.out.println("Mouse: mx="+Game.player.mx+ " my="+Game.player.my);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) {
		Game game = new Game();
		game.start();
	}

}
