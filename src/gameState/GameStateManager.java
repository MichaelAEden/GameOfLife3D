package gameState;

import java.awt.event.MouseEvent;

import com.jogamp.opengl.GL2;

public class GameStateManager {
	private GameState currentState;
	
	public static final int MENUSTATE = 0;
	public static final int MAINSTATE = 1;

	
	public GameStateManager() {
		setState(MAINSTATE);
	}
	
	public void setState(int state) {
		if(state == MAINSTATE) {
			currentState = new MainState(this);
		}
	}
	
	public void update() {
		currentState.update();
	}
	
	public void draw(GL2 gl) {
		currentState.draw(gl);
	}
	
	public void keyPressed(int k) {
		currentState.keyPressed(k);
	}

	public void mouseClicked(MouseEvent m) {
		currentState.mouseClicked(m);
	}

	public void mousePressed(MouseEvent m) {
		currentState.mousePressed(m);
	}

	public void mouseReleased(MouseEvent m) {
		currentState.mouseReleased(m);
	}
	
	public void mouseMoved(MouseEvent m) {
		currentState.mouseMoved(m);
	}
	
	public void mouseDragged(MouseEvent m) {
		currentState.mouseDragged(m);
	}
}
