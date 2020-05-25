package com.viagemvirtual.apps.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.viagemvirtual.apps.entities.Player;
import com.viagemvirtual.apps.main.Game;

public class UI {

	public void render(Graphics g) {
		// Placar LifePack
		g.setColor(Color.RED);
		g.fillRect(8, 4, 140, 20);

		g.setColor(Color.GREEN);
		g.fillRect(8, 4, (int) ((Game.player.life / Game.player.maxLife) * 140), 20);

		g.setColor(Color.WHITE);

		if (Game.gameState == "NORMAL") {
			g.setFont(new Font("Arial", Font.BOLD, 20));
			g.drawString((int) Game.player.life + "/" + (int) Player.maxLife, 50, 20);
		} else if (Game.gameState == "GAME_OVER") {
			g.setFont(new Font("Times New Roman", Font.BOLD, 20));
			g.drawString("R.I.P.", 55, 20);
		}

		// Placar AMMO
		g.setFont(new Font("Arial", Font.BOLD, 20));
		g.setColor(Color.white);
		g.drawString("AMMO: " + Game.player.ammo, 600, 20);

	}
}
