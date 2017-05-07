package gameState;

import com.jogamp.opengl.GL2;

import cellMap.Cell;
import cellMap.CellMap;

public class Player {
	
	private int cellId = Cell.CELL_GROWER_A.ID;
	
	private float rotateX;
	private float rotateY;
	private float rotateZ;
	
	private float x;
	private float y;
	private float z;
	
	private float vx;
	private float vy;
	private float vz;
	
	private float accelRate = 0.1f;
	private float rotateRate = 0.1f;
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
	public void setRenderPerspective(GL2 gl, CellMap cellMap) {		
		gl.glRotatef(rotateZ, 0, 0, 1);
		gl.glRotatef(rotateY, 0, 1, 0);
		gl.glRotatef(rotateX, 1, 0, 0);
		
		gl.glTranslatef(
				x / (float) cellMap.getColumns(), 
				y / (float) cellMap.getRows(), 
				z / (float) cellMap.getDepth());
	}
	
	/* Cell Placement Functions */
	
	/**
	 * Switches cell being placed to the previous cell.
	 */
	public void prevCell() {
		cellId--;
		if (cellId < 1)
			cellId = Cell.MAX_ID;
	}
	
	/**
	 * Switches cell being placed to the next cell.
	 */
	public void nextCell() {
		cellId++;
		if (cellId > Cell.MAX_ID)
			cellId = 1;
	}
	
	/**
	 * Places cell in front of the player.
	 */
	public void placeCell(CellMap cellMap) {
		System.out.println("X: " + x);
		System.out.println("Y: " + y);
		System.out.println("Z: " + z);
		
		Cell.getCellFromId(cellId).onCreation(cellMap, 15, 15, 15);

		//cellMap.createCell((int)(x + 1), (int)y, (int)z, cellId);
	}
	
	/* Speed, position, and rotation operations */
	
	/**
	 * Accelerate the player in the direction they are facing.
	 */
	public void accelerate(float accelFactor) {
		double radRotateX = Math.toRadians(rotateX);
		double radRotateY = Math.toRadians(rotateY);
		
		double dx = Math.sin(radRotateY);
		double dy = Math.sin(radRotateX);
		//double dz = Math.cos(radRotateX) + Math.cos(radRotateY);
		
		accelerate(
				(float)dx * accelRate * accelFactor,
				(float)dy * accelRate * accelFactor,
				(float)0 * accelRate * accelFactor);
	}
	
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
		x = 0;
		y = 0;
		z = 0;
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
		rotateY = 0;
		rotateZ = 0;
	}
	
	/* Getters and Setters */
	
	public float getX() { return x; }
	public float getY() { return y; }
	public float getZ() { return z; }
}
