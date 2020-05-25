package com.viagemvirtual.apps.world;

public class Camera {

	public static int x;
	public static int y;
	
	public Camera() {

	}

	public static int clamp(int atual, int min, int max) {
		if(atual < min) {
			atual = min;
		}

		if(atual > max) {
			atual = max;
		}
		
		return atual;
	}
	
	
	/*****************************************
	 * Getters and Setters
	 *****************************************
	 */
	
	
	public static int getX() {
		return x;
	}

	public static void setX(int x) {
		Camera.x = x;
	}

	public static int getY() {
		return y;
	}

	public static void setY(int y) {
		Camera.y = y;
	}

}
