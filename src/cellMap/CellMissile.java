package cellMap;

import java.awt.Color;
import java.util.Random;

public class CellMissile extends Cell {
	
	// private static final int HAS_SPAWNED = 4;
	
	private Random rand = new Random();
	
	private int spawnChance = 10;
	private int directionShiftChance = 5;
	// private int directionOffsetChance = 5;
	
	public CellMissile() {
		super();
		
		colour = Color.CYAN;
		cellLife = 3;
		updateRate = 1;
	}
	
	/**
	 * Called upon cell creation by user.
	 */
	@Override
	public void onCreation(CellMap cellMap, int x, int y, int z) {
		super.onCreation(cellMap, x, y, z);
		cellMap.setMetadata(x, y, z, (byte)(rand.nextInt(4)));
	}
	
	/**
	 * Called upon spawning a child cell.
	 */
	@Override
	protected void spawnNewCell(CellMap cellMap, int x, int y, int z, int parentX, int parenty, int parentz) {
		super.spawnNewCell(cellMap, x, y, z, parentX, parenty, parentz);
		
		// Set the metadata of the cell to that of its parent, unless shifting directions
		if (rand.nextInt(directionShiftChance) == 0)
			cellMap.setMetadata(x, y, z, (byte)rand.nextInt(4));
		else
			cellMap.setMetadata(x, y, z, cellMap.getMetadata(parentX, parenty, parentz));
	}
	
	/**
	 * Returns whether cell at the given coordinates should be spawned next iteration
	 */
	@Override
	protected boolean shouldSpawn(CellMap cellMap, int x, int y, int z, int parentX, int parenty, int parentz) {
		byte metadata = cellMap.getMetadata(x, y, z);		
		if (metadata == 0 || cellMap.getMetadata(x, y, z) == 2) {
			int ty = metadata == 0 ? y - 1 : y + 1;
			for (int tx = x - 1; tx <= x + 1; tx++) {
				if (!cellMap.isActiveCell(tx, ty, z)) continue;
				if (cellMap.getCellId(tx, ty, z) == this.ID && rand.nextInt(spawnChance) == 0) {
					return true;
				}
			}
		}
		
		if (metadata == 1 || cellMap.getMetadata(x, y, z) == 3) {
			int tx = metadata == 1 ? x - 1 : x + 1;
			for (int ty = y - 1; ty <= y + 1; ty++) {
				if (!cellMap.isActiveCell(tx, ty, z)) continue;
				if (cellMap.getCellId(tx, ty, z) == this.ID && rand.nextInt(spawnChance) == 0) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Returns whether cell at the given coordinates should die next iteration
	 */
	@Override
	protected boolean shouldDie(CellMap cellMap, int x, int y, int z) {
		return (cellMap.getAge(x, y, z) > cellLife);
	}
	
}
