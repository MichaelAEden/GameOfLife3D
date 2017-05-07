package main;

import gameState.GameStateManager;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.util.FPSAnimator;

// TODO: switch to LWJGL

@SuppressWarnings("serial")
public class GamePanel extends GLJPanel implements GLEventListener, MouseListener, MouseMotionListener, KeyListener {
	
	private static Rectangle screenSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
	
	// Dimensions
	public static final int WIDTH = (int) screenSize.getWidth();
	public static final int HEIGHT = (int) screenSize.getHeight();
	
	public static final String TITLE = "The Game of Life";
	public static final int FULLSCREEN = 0;	// 1 if full screen, 0 otherwise
	public static final int SCALE = 1;
	
	// Game Thread
	private int FPS = 30;
	private static int ticks = 0;
	
	// Game State Manager
	private GameStateManager gsm;
	
	// Rendering
	private FPSAnimator animator;

	/**
	 * Constructor.
	 */
	public GamePanel(GLCapabilities caps) {
		super(caps);
		
		gsm = new GameStateManager();
		
		// Setting size
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		
		// Adding listeners
		addGLEventListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
		
		// Creating an animator
		animator = new FPSAnimator(this, FPS);
    }
	
	public void run() {
		animator.start();
	}
	
	/*KeyListener and MouseListener Methods*/

	@Override
	public void keyPressed(KeyEvent key) 
	{
		gsm.keyPressed(key.getKeyCode());
	}
	
	@Override
	public void mouseClicked(MouseEvent mouse) 
	{		
		gsm.mouseClicked(mouse);
	}
	
	@Override
	public void mousePressed(MouseEvent mouse) 
	{
		gsm.mousePressed(mouse);
	}
	
	@Override
	public void mouseReleased(MouseEvent mouse)
	{
		gsm.mouseReleased(mouse);
	}
	
	@Override
	public void mouseMoved(MouseEvent mouse) 
	{
		gsm.mouseMoved(mouse);
	}
	
	@Override
	public void mouseDragged(MouseEvent mouse)
	{
		gsm.mouseDragged(mouse);
	}
	
	@Override public void keyTyped(KeyEvent key){}
	@Override public void keyReleased(KeyEvent key) {}
	@Override public void mouseEntered(MouseEvent mouse) {}
	@Override public void mouseExited(MouseEvent mouse) {}

	/**
	 * Updates and renders the cell map.
	 */
	@Override
	public void display(GLAutoDrawable drawable) {		
		// Updating
		gsm.update();
		
		// Rendering
		GL2 gl = drawable.getGL().getGL2();
		
		setRenderSettings(gl);
		gsm.draw(gl);
		
		render(gl);
	}
	
	private void setRenderSettings(GL2 gl) {
		gl.glClearColor(0,0,0,0);
		gl.glClear( GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT );
	}
	
	private void render(GL2 gl) {
		gl.glViewport(0, 0, WIDTH, WIDTH);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		float aspectRatio = WIDTH / (float) HEIGHT;
		gl.glFrustum(0, 1, 0, aspectRatio, 1, 10);
		
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
	}

	/**
	 * Called when the panel is created.
	 */
	@Override
	public void init(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glEnable(GL2.GL_DEPTH_TEST);
		gl.glEnable(GL2.GL_DEPTH_CLAMP_NV);
		gl.glEnable(GL2.GL_LIGHTING);
		gl.glEnable(GL2.GL_LIGHT0);
		gl.glEnable(GL2.GL_COLOR_MATERIAL);
		
		// Hide the cursor while it is in the window
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
				cursorImg, new Point(0, 0), "blank cursor"));
	}
	
	@Override public void dispose(GLAutoDrawable arg0) {}
	@Override public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4) {}
	
	public static int getTimeInTicks() {
		return ticks;
	}
}
