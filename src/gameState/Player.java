package gameState;

import com.jogamp.opengl.GL2;

public class Player {
	
	private float rotateX;
	private float rotateY;
	private float rotateZ;
	
	private float x;
	private float y;
	private float z;
	
	private float vx;
	private float vy;
	private float vz;
	
	private float slownessFactor = 0.95f;
	
	public Player() {
		resetRotation();
		resetPosition();
		resetVelocity();
	}
	
	public void update() {
		vx *= slownessFactor;
		vy *= slownessFactor;
		vz *= slownessFactor;
		
		x += vx;
		y += vy;
		z += vz;
	}

	/**
	 * Sets the rotation and position of the camera based on that
	 * of the player.
	 */
	public void setRenderPerspective(GL2 gl) {		
		gl.glTranslatef(x, y, z);
		
		gl.glRotatef(rotateZ, 0, 0, 1);
		gl.glRotatef(rotateY, 0, 1, 0);
		gl.glRotatef(rotateX, 1, 0, 0);
	}
	
	/* Speed, position, and rotation operations */
	
	/**
	 * Accelerate the player by a set amount of cells/s^2.
	 * @param rx Acceleration in the x-axis
	 * @param ry Acceleration in the y-axis
	 * @param rz Acceleration in the z-axis
	 */
	public void accelerate(float dx, float dy, float dz) {
		vx += dx;
		vy += dy;
		vz += dz;
	}
	
	/**
	 * Resets the speed of the player to 0.
	 */
	public void resetVelocity() {
		vx = 0;
		vy = 0;
		vz = 0;
	}
	
	/**
	 * Translate the player by a set amount of cells.
	 * @param rx Cells in the x-axis
	 * @param ry Cells in the y-axis
	 * @param rz Cells in the z-axis
	 */
	public void translate(float dx, float dy, float dz) {
		x += dx;
		y += dy;
		z += dz;
	}
	
	/**
	 * Resets the player to the origin.
	 */
	public void resetPosition() {		
		x = 1;
		y = 0;
		z = -2;
	}
	
	/**
	 * Rotate the player by a set amount of degrees.
	 * @param rx Rotation in degrees on the x-axis
	 * @param ry Rotation in degrees on the y-axis
	 * @param rz Rotation in degrees on the z-axis
	 */
	public void rotate(float rx, float ry, float rz) {
		rotateX += rx;
		rotateY += ry;
		rotateZ += rz;
	}
	
	/**
	 * Resets the rotation of the player to 0 in each axis.
	 */
	public void resetRotation() {
		rotateX = 0;
		rotateY = 180;
		rotateZ = 0;
	}
}
