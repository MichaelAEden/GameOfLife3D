package cellMap;

import java.awt.Color;

import com.jogamp.opengl.GL2;

import ui.Settings;

/**
 * A map of living and dead cells.
 */
public class CellMap {
	
	private byte[][][] cellsState;
	private int[][][] cellsAge;
	private int[][][] cellsGeneration;
	private int[][][] cellsType;
	private byte[][][] cellsMetadata;
	
	private int columns;
	private int rows;
	private int depth;
		
	private float cellSize;
	
	private int updateTick = 0;
	
	/**
	 * Constructor.
	 *
	 * @param cellWidth 	Width of each cell in pixels
	 * @param width			Width of map, in pixels
	 * @param height		Height of map, in pixels
	 */
	public CellMap(int columns, int rows, int depth)
	{
		this.cellSize = 1.0f / columns;
		this.columns = columns;
		this.rows = rows;
		this.depth = depth;
		
		init();
	}
	
	/**
	 * Initializes a blank cell map.
	 */
	private void init() {
		cellsState = initByteGrid(Cell.DEAD);
		cellsMetadata = initByteGrid((byte)0);
		cellsAge = initIntGrid(0);
		cellsGeneration = initIntGrid(0);
		cellsType = initIntGrid(Cell.CELL_DEAD.ID);
		
		for (int i = 0; i <= 20; i++) {
			Cell.CELL_BASIC.onCreation(this, i, i, i);
		}
	}
	
	/**
	 * Updates all cells, including dead cells.
	 */
	public void update() {		
		updateTick++;
		
		for (int state = 0; state < 2; state++) {
			for (int x = 0; x < columns; x++) {
				for (int y = 0; y < rows; y++) {
					for (int z = 0; z < depth; z++) {
						
						// Brings spawning cells to life, kills dying cells.
						if (state == 0) {
							if (getState(x, y, z) == Cell.SPAWNING) {
								getCell(x, y, z).onBirth(this, x, y, z);
							}
							else if (getState(x, y, z) == Cell.DYING) {
								getCell(x, y, z).onDeath(this, x, y, z);
							}
						}
						
						else if (state == 1) {
							if (isActiveCell(x, y, z))
								getCell(x, y, z).update(this, x, y, z);
						}
						
						// Note that dead cells are not updated directly, but
						// are updated through live neighbor cells
					}
				}
			}
		}
	}

	/**
	 * Called upon cell creation by user.
	 */
	public void createCell(int x, int y, int z, int type) {
		System.out.println("Creating cell at: " + x + ", " + y + ", " + z);
		
		Cell.getCellFromId(type).onCreation(this, x, y, z);
	}
	
	/**
	 * Called upon cell deletion by user.
	 */
	public void deleteCell(int x, int y, int z) {
		if (!isActiveCell(x, y, z)) return;
		Cell.getCellFromId(cellsType[x][y][z]).onDeletion(this, x, y, z);
	}
	
	
	
	/* Rendering Functions */
	
	/**
	 * Renders the cell map.
	 */
	public void draw(GL2 gl) {
		for (int x = 0; x < columns; x++) {
			for (int y = 0; y < rows; y++) {
				for (int z = 0; z < rows; z++) {
					if (isActiveCell(x, y, z)) {
						drawCell(gl, getCellColour(x, y, z), x, y, z);
					}
				}
			}
		}
		
		// TODO: add 3d grid option
	}
	
	
	/* Getters and Setters*/
	
	public int getUpdateTick() {
		return updateTick;
	}
	
	public void setId(int x, int y, int z, int id) {
		if (isInBounds(x, y, z))
			cellsType[x][y][z] = id;
	}
	
	public Cell getCell(int x, int y, int z) {
		return Cell.getCellFromId(getCellId(x, y, z));
	}
	public int getCellId(int x, int y, int z) {
		if (!isActiveCell(x, y, z)) return Cell.CELL_DEAD.ID;
		return cellsType[x][y][z];
	}
	
	public void setState(int x, int y, int z, byte state) {
		if (isInBounds(x, y, z))
			cellsState[x][y][z] = state;
	}
	public void incrementAge(int x, int y, int z) {
		if (isInBounds(x, y, z))
			cellsAge[x][y][z]++;
	}
	public void setAge(int x, int y, int z, int age) {
		if (isInBounds(x, y, z))
			cellsAge[x][y][z] = age;
	}
	public void setGeneration(int x, int y, int z, int generation) {
		if (isInBounds(x, y, z))
			cellsGeneration[x][y][z] = generation;
	}
	public void setMetadata(int x, int y, int z, byte metadata) {
		if (isInBounds(x, y, z))
			cellsMetadata[x][y][z] = metadata;
	}
	
	public byte getState(int x, int y, int z) {
		if (!isInBounds(x, y, z)) return Cell.DEAD;
		return cellsState[x][y][z];
	}
	public int getAge(int x, int y, int z) {
		if (!isInBounds(x, y, z)) return 0;
		return cellsAge[x][y][z];
	}
	public int getGeneration(int x, int y, int z) {
		if (!isInBounds(x, y, z)) return 0;
		return cellsGeneration[x][y][z];
	}
	public byte getMetadata(int x, int y, int z) {
		if (!isInBounds(x, y, z)) return 0;
		return cellsMetadata[x][y][z];
	}
	public Color getCellColour(int x, int y, int z) {
		if (!isActiveCell(x, y, z)) return Settings.DEFAULT_DEAD_CELL_COLOUR;
		return getCell(x, y, z).getColour(this, x, y, z);
	}
	public boolean isActiveCell(int x, int y, int z) {
		if (!isInBounds(x, y, z)) return false;
		return cellsType[x][y][z] != Cell.CELL_DEAD.ID && (getState(x, y, z) == Cell.ALIVE || getState(x, y, z) == Cell.DYING);
	}
	
	
	/* Helper Functions */
	
	/**
	 * Gets the number of adjacent live cells of a certain type, at a given point.
	 */
	public int getAdjacentCellsOfId(int x, int y, int z, int id) {		
		int adjacentCells = 0;
		for (int tx = x - 1; tx <= x + 1; tx++) {
			for (int ty = y - 1; ty <= y + 1; ty++) {
				for (int tz = z - 1; tz <= z + 1; tz++) {
					if (tx == x && ty == y && tz == z) continue;
					if (isActiveCell(tx, ty, tz) && getCellId(tx, ty, tz) == id) {
						adjacentCells++;
					}
				}
			}
		}
		
		return adjacentCells;
	}
	
	/**
	 * Gets the largest generation of adjacent live cells to determine the generation of the new cell.
	 */
	public int getGenerationOfAdjacentCells(int x, int y, int z) {
		int generation = 0;
		for (int tx = x - 1; tx <= x + 1; tx++) {
			for (int ty = y - 1; ty <= y + 1; ty++) {
				for (int tz = z - 1; tz <= z + 1; tz++) {
					if (tx == x && ty == y && tz == z) continue;
					if (isActiveCell(tx, ty, z)) {
						generation = Math.max(getGeneration(tx, ty, z), generation);
					}
				}
			}
		}
		
		return generation;
	}
	
	public boolean isInBounds(int x, int y, int z) {
		return (x < columns && y < rows && z < depth) && (x >= 0 && y >= 0 && z >= 0);
	}
	
	/* Cell Rendering Functions */
	
	private void square(GL2 gl, Color colour) {
		float r = colour.getRed() / 255f;
		float g = colour.getGreen() / 255f;
		float b = colour.getBlue() / 255f;
		
		float cellSize = this.cellSize / 2;
								
		gl.glTranslatef(0, 0, cellSize);    // Move square 0.5 units forward.
		gl.glColor3f(r, g, b);         // The color for the square.
		gl.glNormal3f(0,0,1);        // Normal vector to square (this is actually the default).
		gl.glBegin(GL2.GL_TRIANGLE_FAN);
		gl.glVertex2f(-cellSize, -cellSize);
		gl.glVertex2f(cellSize, -cellSize);
		gl.glVertex2f(cellSize, cellSize);
		gl.glVertex2f(-cellSize, cellSize);
		gl.glEnd();
	}

	private void drawCell(GL2 gl, Color colour, int x, int y, int z) {						
		gl.glPushMatrix();
		gl.glColor3f(1f, 1f, 1f);
		recentre(gl, x, y, z);
		square(gl, colour);
		gl.glPopMatrix();

		gl.glPushMatrix();
		gl.glColor3f(0.9f, 0.9f, 0.9f);
		recentre(gl, x, y, z);
		gl.glRotatef(180,0,1,0); // rotate square to back face
		square(gl, colour);
		gl.glPopMatrix();

		gl.glPushMatrix();
		gl.glColor3f(0.6f, 0.9f, 0.9f);
		recentre(gl, x, y, z);
		gl.glRotatef(-90,0,1,0); // rotate square to left face
		square(gl, colour);
		gl.glPopMatrix();

		gl.glPushMatrix();
		gl.glColor3f(0.6f, 0.4f, 0.9f);
		recentre(gl, x, y, z);
		gl.glRotatef(90,0,1,0); // rotate square to right face
		square(gl, colour);
		gl.glPopMatrix();

		gl.glPushMatrix();
		gl.glColor3f(0.6f, 0.4f, 0.5f);
		recentre(gl, x, y, z);
		gl.glRotatef(-90,1,0,0); // rotate square to top face
		square(gl, colour);
		gl.glPopMatrix();

		gl.glPushMatrix();
		gl.glColor3f(0.1f, 0.5f, 0.5f);
		recentre(gl, x, y, z);
		gl.glRotatef(90,1,0,0); // rotate square to bottom face
		square(gl, colour);
		gl.glPopMatrix();
	}
	
	private void recentre(GL2 gl, int x, int y, int z) {
		gl.glTranslatef(
				x / (float)columns,
				y / (float)rows,
				z / (float)depth);
	}
	
	/* Helper functions */
	
	private int[][][] initIntGrid(int defaultVal) {
		int[][][] array = new int[columns][rows][depth];
		
		for(int x = 0; x < columns; x++)
		{
			array[x] = new int[rows][depth];
			for(int y = 0; y < rows; y++) {
				array[x][y] = new int[depth];
				for(int z = 0; z < depth; z++) {
					array[x][y][z] = defaultVal;
				}
			}
		}
		
		return array;
	}
	
	private byte[][][] initByteGrid(byte defaultVal) {
		byte[][][] array = new byte[columns][rows][depth];
		
		for(int x = 0; x < columns; x++)
		{
			array[x] = new byte[rows][depth];
			for(int y = 0; y < rows; y++) {
				array[x][y] = new byte[depth];
				for(int z = 0; z < depth; z++) {
					array[x][y][z] = defaultVal;
				}
			}
		}
		
		return array;
	}
	
	/* Getters and Setters */
	
	public int getRows() { return rows; }
	public int getColumns() { return columns; }
	public int getDepth() { return depth; }
	
}
