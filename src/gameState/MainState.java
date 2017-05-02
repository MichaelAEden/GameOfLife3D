package gameState;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import com.jogamp.opengl.GL2;

import main.GamePanel;
import cellMap.Cell;
import cellMap.CellMap;

public class MainState extends GameState {
	
	private CellMap cellMap;
	
	private int updateTick = 0;
	private int updateRate = 3;
	private boolean isPaused = true;
		
	private int cellId = Cell.CELL_GROWER_A.ID;

	private Player player;

	public MainState(GameStateManager gsm) {
		super(gsm);
		
		player = new Player();
		cellMap = new CellMap(20, 20, 20, GamePanel.WIDTH, GamePanel.HEIGHT);
	}

	public void update() {
		player.update();
		
		if (!isPaused)
		{
			updateTick++;
			if (updateTick >= updateRate) {
				cellMap.update();
				updateTick = 0;
			}
		}
	}

	public void draw(GL2 gl) {
		player.setRenderPerspective(gl);
		cellMap.draw(gl);
	}

	public void keyPressed(int k) {
		if(k == KeyEvent.VK_P) isPaused = !isPaused;
		if(k == KeyEvent.VK_3) {
			if (updateRate >= 1) {
				updateRate--;
			}
		}
		if(k == KeyEvent.VK_4) {
			if (updateRate <= 60) {
				updateRate++;
			}
		}
		
		if(k == KeyEvent.VK_1) {
			cellId++;
			if (cellId > Cell.MAX_ID) {
				cellId = 1;
			}
		}
		if(k == KeyEvent.VK_2) {
			cellId--;
			if (cellId < 1) {
				cellId = Cell.MAX_ID;
			}
		}
		
		float rotationFactor = 3;
		float movementFactor = 0.01f;
		
		if ( k == KeyEvent.VK_W )
			player.accelerate(0, 0, movementFactor);
		else if ( k == KeyEvent.VK_S )
			player.accelerate(0, 0, -movementFactor);
		else if ( k == KeyEvent.VK_A )
			player.accelerate(movementFactor, 0, 0);
		else if ( k == KeyEvent.VK_D )
			player.accelerate(-movementFactor, 0, 0);
		else if ( k == KeyEvent.VK_Z )
			player.accelerate(0, movementFactor, 0);
		else if ( k == KeyEvent.VK_Q )
			player.accelerate(0, -movementFactor, 0);
		else if ( k == KeyEvent.VK_DOWN)
			player.rotate(rotationFactor, 0, 0);
		else if ( k == KeyEvent.VK_UP )
			player.rotate(-rotationFactor, 0, 0);
		else if ( k == KeyEvent.VK_LEFT )
			player.rotate(0, -rotationFactor, 0);
		else if ( k == KeyEvent.VK_RIGHT )
			player.rotate(0, rotationFactor, 0);
		else if ( k == KeyEvent.VK_R ) {
			player.resetPosition();
			player.resetRotation();
			player.resetVelocity();
		}
	}

	public void mouseClicked(MouseEvent m) {
		Point cell = cellMap.getCellFromMouse(m);
		cellMap.createCell(cell.x, cell.y, 5, cellId);
	}

	@Override
	public void mouseReleased(MouseEvent m) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent m) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(MouseEvent m) {
		mouseClicked(m);
	}

	@Override
	public void mousePressed(MouseEvent m) {
		// TODO Auto-generated method stub	
	}
}
