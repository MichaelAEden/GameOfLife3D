package cellMap;

import java.awt.Color;

/**
 * Acts as a placeholder for dead cells.
 */
public class CellDead extends Cell {
	
	@Override
	public void update(CellMap cellMap, int x, int y, int z) {
		System.out.println("Why was this called?");
	}

	@Override
	public void onCreation(CellMap cellMap, int x, int y, int z) {
		System.out.println("Why was this called?");
	}
	
	@Override
	public void onDying(CellMap cellMap, int x, int y, int z) {
		System.out.println("Why was this called?");
	}

	@Override
	public void onDeletion(CellMap cellMap, int x, int y, int z) {
		System.out.println("Why was this called?");

	}
	
	@Override
	public void updateSelf(CellMap cellMap, int x, int y, int z) {
		System.out.println("Why was this called?");
	}
	
	@Override
	public void updateNeighbours(CellMap cellMap, int x, int y, int z) {
		System.out.println("Why was this called?");
	}
	
	
	/* Getters and Setters */

	@Override
	public Color getColour(CellMap cellMap, int x, int y, int z) {
		System.out.println("Why was this called?");
		return null;
	}
}
