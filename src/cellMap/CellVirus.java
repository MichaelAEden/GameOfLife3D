package cellMap;

import java.awt.Color;

public class CellVirus extends Cell {

	public CellVirus() {
		super();
		
		colour = Color.PINK;
		
		updateRate = 1;
		cellLife = 10;
	}
	
	@Override
	protected boolean shouldSpawn(CellMap cellMap, int x, int y, int z, int parentX, int parenty, int parentz) {
		return (cellMap.isActiveCell(x, y, z) && cellMap.getCellId(x, y, z) != this.ID);
	}
	
	@Override
	protected boolean shouldDie(CellMap cellMap, int x, int y, int z) {
		return cellMap.getAge(x, y, z) > cellLife;
	}
}
