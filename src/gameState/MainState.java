package gameState;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import com.jogamp.opengl.GL2;

import cellMap.CellMap;
import main.GamePanel;

public class MainState extends GameState {
	
	private CellMap cellMap;
	
	private int updateTick = 0;
	private int updateRate = 3;
	private boolean isPaused = true;
		
	private Player player;
	
	// Mouse handling
	private Robot robot;

	public MainState(GameStateManager gsm) {
		super(gsm);
		
		player = new Player();
		cellMap = new CellMap(30, 30, 30);
		
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
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
		player.setRenderPerspective(gl, cellMap);
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
		
		if(k == KeyEvent.VK_1)
			player.nextCell();
		if(k == KeyEvent.VK_2)
			player.prevCell();
		
		float rotationFactor = 3;
		float movementFactor = 0.1f;
		
		if ( k == KeyEvent.VK_W )
			player.accelerate(1);
		else if ( k == KeyEvent.VK_S )
			player.accelerate(-1);
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
		player.placeCell(cellMap);
	}

	@Override
	public void mouseReleased(MouseEvent m) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent m) {		
		float rotationFactor = 100f;
		
		player.rotate(
				- (m.getY() - GamePanel.HEIGHT / 2) / (float) GamePanel.HEIGHT * rotationFactor,
				(m.getX() - GamePanel.WIDTH / 2) / (float) GamePanel.WIDTH * rotationFactor,
				0);
		
		// Re-centers the mouse
		robot.mouseMove(
				m.getXOnScreen() - m.getX() + GamePanel.WIDTH / 2, 
				m.getYOnScreen() - m.getY() + GamePanel.HEIGHT / 2);
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
