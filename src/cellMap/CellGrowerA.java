package cellMap;

import java.awt.Color;

public class CellGrowerA extends Cell {
	
	public CellGrowerA() {
		super();
		
		neighboursCausingDeathMax = 8;	// Change to 7 for a different look
		neighboursCausingDeathMin = 2;
		neighboursCausingBirthMax = 3;
		neighboursCausingBirthMin = 3;
		
		colour = Color.RED;
		
		updateRate = 1;
	}
	
	/**
	 * Returns whether cell at the given coordinates should be spawned next iteration
	 */
	@Override
	protected boolean shouldSpawn(CellMap cellMap, int x, int y, int z, int parentX, int parenty, int parentz) {
		// By default, looks for neighbors of the same type only		
		int adjacentCells = cellMap.getAdjacentCellsOfId(x, y, z, this.ID);
		return (!(cellMap.isActiveCell(x, y, z) && cellMap.getCellId(x, y, z) == this.ID) && 
				(adjacentCells <= neighboursCausingBirthMax &&
				 adjacentCells >= neighboursCausingBirthMin));	
	}
}
