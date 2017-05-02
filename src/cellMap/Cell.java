package cellMap;

import java.awt.Color;
import java.util.ArrayList;

import ui.Settings;

public abstract class Cell {
	
	private static ArrayList<Cell> cells = new ArrayList<Cell>();
	private static int currentID = 0;
	
	// Cell state constants
	public static final byte DEAD = 0;
	public static final byte SPAWNING = 1;
	public static final byte ALIVE = 2;
	public static final byte DYING = 3;
	
	// Cell life constants
	public static final int IMMORTAL = -1;

	// Cell types
	public static final Cell CELL_DEAD = new CellDead();
	public static final Cell CELL_BASIC = new CellBasic();
	public static final Cell CELL_GROWER_A = new CellGrowerA();
	public static final Cell CELL_GROWER_B = new CellGrowerB();
	public static final Cell CELL_VIRUS = new CellVirus();
	public static final Cell CELL_MISSILE = new CellMissile();
	
	public static int MAX_ID = currentID - 1;
	
	// Cell attributes
	public int ID;
	protected int cellLife = Settings.CELL_LIFE;
	protected Color colour = Settings.DEFAULT_CELL_COLOUR;
	protected int updateRate = Settings.DEFAULT_UPDATE_RATE;
	
	// Cell rules
	protected int neighboursCausingDeathMax = Settings.NEIGHBOURS_CAUSING_DEATH_MAX;	// n or more surrounding cells and cell dies
	protected int neighboursCausingDeathMin = Settings.NEIGHBOURS_CAUSING_DEATH_MIN;	// n or less surrounding cells and cell dies
	protected int neighboursCausingBirthMax = Settings.NEIGHBOURS_CAUSING_BIRTH_MAX;	// Between n - m surrounding cells - inclusive - cell is born
	protected int neighboursCausingBirthMin = Settings.NEIGHBOURS_CAUSING_BIRTH_MIN;
	
	/**
	 * Constructor.
	 */
	public Cell() {
		ID = currentID;
		currentID++;
		
		cells.add(this);
	}
	
	/**
	 * Gets Cell object given its ID.
	 */
	public static Cell getCellFromId(int id) {
		return cells.get(id);
	}
	
	
	/**
	 * Called upon cell spawn, which comes BEFORE cell birth.
	 */
	public void onSpawn(CellMap cellMap, int x, int y, int z) {
		int generation = cellMap.getGenerationOfAdjacentCells(x, y, z);
		cellMap.setGeneration(x, y, z, generation + 1);
		cellMap.setState(x, y, z, Cell.SPAWNING);
	}
	
	/**
	 * Called upon cell birth, which comes AFTER cell spawn.
	 */
	public void onBirth(CellMap cellMap, int x, int y, int z) {
		int generation = cellMap.getGenerationOfAdjacentCells(x, y, z);
		cellMap.setGeneration(x, y, z, generation + 1);
		cellMap.setState(x, y, z, Cell.ALIVE);
	}

	/**
	 * Called upon cell creation by user.
	 */
	public void onCreation(CellMap cellMap, int x, int y, int z) {
		cellMap.setState(x, y, z, Cell.ALIVE);
		cellMap.setId(x, y, z, this.ID);
		cellMap.setGeneration(x, y, z, 0);
	}
	
	
	/**
	 * Called upon cell dying.
	 */
	public void onDying(CellMap cellMap, int x, int y, int z) {
		cellMap.setState(x, y, z, Cell.DYING);
	}
	
	/**
	 * Called upon cell death.
	 */
	public void onDeath(CellMap cellMap, int x, int y, int z) {
		resetCellData(cellMap, x, y, z);
		cellMap.setState(x, y, z, Cell.DEAD);
		cellMap.setId(x, y, z, Cell.CELL_DEAD.ID);
	}

	/**
	 * Called upon cell deletion by user.
	 */
	public void onDeletion(CellMap cellMap, int x, int y, int z) {
		resetCellData(cellMap, x, y, z);
		cellMap.setState(x, y, z, Cell.DEAD);
		cellMap.setId(x, y, z, Cell.CELL_DEAD.ID);
	}
	
	/**
	 * Resets cell data at a given point to a default state.
	 */
	private void resetCellData(CellMap cellMap, int x, int y, int z) {
		cellMap.setMetadata(x, y, z, (byte)0);
		cellMap.setGeneration(x, y, z, 0);
		cellMap.setAge(x, y, z, 0);
	}
	
	
	/**
	 * Updates the state of the cell.
	 */
	public void update(CellMap cellMap, int x, int y, int z) {
		if (cellMap.getUpdateTick() % updateRate == 0) {
			updateSelf(cellMap, x, y, z);
			updateNeighbours(cellMap, x, y, z);
		}
	}
	
	/**
	 * Updates the cell's age and state.
	 */
	public void updateSelf(CellMap cellMap, int x, int y, int z) {
		if (cellMap.isActiveCell(x, y, z))
		{
			cellMap.incrementAge(x, y, z);
		
			if (shouldDie(cellMap, x, y, z))
				onDying(cellMap, x, y, z);
		}
	}
	
	/**
	 * Spawns neighbor cells accordingly.
	 */
	public void updateNeighbours(CellMap cellMap, int x, int y, int z) {
		for (int tx = x - 1; tx <= x + 1; tx++) {
			for (int ty = y - 1; ty <= y + 1; ty++) {
				for (int tz = z - 1; tz <= z + 1; tz++) {
					if (tx == x && ty == y && tz == z) continue;
					if (shouldSpawn(cellMap, tx, ty, tz, x, y, z)) {
						spawnNewCell(cellMap, tx, ty, tz, x, y, z);
					}
				}
			}
		}
	}
	
	/**
	 * Called upon spawning a child cell.
	 */
	protected void spawnNewCell(CellMap cellMap, int x, int y, int z, int parentX, int parenty, int parentz) {
		cellMap.setId(x, y, z, this.ID);
		cellMap.setState(x, y, z, Cell.SPAWNING);
	}
	
	/**
	 * Returns whether cell at the given coordinates should be spawned next iteration
	 */
	protected boolean shouldSpawn(CellMap cellMap, int x, int y, int z, int parentX, int parenty, int parentz) {
		// By default, looks for neighbors of the same type only		
		int adjacentCells = cellMap.getAdjacentCellsOfId(x, y, z, this.ID);
		return (!cellMap.isActiveCell(x, y, z) &&
				(adjacentCells <= neighboursCausingBirthMax &&
				 adjacentCells >= neighboursCausingBirthMin));	
	}
	
	/**
	 * Returns whether cell at the given coordinates should die next iteration
	 */
	protected boolean shouldDie(CellMap cellMap, int x, int y, int z) {
		// By default, looks for neighbors of the same type only
		int adjacentCells = cellMap.getAdjacentCellsOfId(x, y, z, this.ID);
		
		// Cells can die by loneliness, overcrowding, or old age
		return (cellMap.isActiveCell(x, y, z) &&
				(adjacentCells >= neighboursCausingDeathMax ||
				 adjacentCells <= neighboursCausingDeathMin) ||
				(cellMap.getAge(x, y, z) > cellLife &&
				 cellLife != Cell.IMMORTAL));
	}
	
	
	/* Getters and Setters */

	public Color getColour(CellMap cellMap, int x, int y, int z) {
		return colour;
	}
}
