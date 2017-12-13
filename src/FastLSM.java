import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.*;
import javax.vecmath.Point2d;

public class FastLSM extends JComponent implements KeyListener, MouseListener, MouseMotionListener {

	private static final long serialVersionUID = 1L;
	public static boolean debugFlag = false;
	
	// Size of screen.
	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;
	
	// Other random constants.
	private static final int MOUSE_Y_OFFSET = -30;
	
	// Frame rate (frames per second).
	private final int FPS = 50;

	// The grid and mesh particles.
	public LSMState state;
	public LSMMesh mesh;
	public LSMGrid grid;
	
	// User input.
	public boolean mouseIsDown;
	public Point2d mousePos;
	private LSMGridParticle mouseSelectedParticle;
	
	// Designer.
	public static final double DESIGN_PLACE_TIME = 0.1;
	public double designPlaceTimeElapsed = 0.;

	// Options.
	public static boolean DRAW_MESH = true;
	public static boolean DRAW_GRID = true;
	
	public FastLSM() {
		//grid = new LSMGrid(new Point2d(100., 100.), 10, 10, 300, 300, 4);
		state = LSMState.LSM_Designing;
		mesh = null;
		grid = null;
		mousePos = new Point2d();
		mouseSelectedParticle = null;
	}
	
	public void run()
	{
		// Create a placeholder mesh.
		// TODO implement drawing a mesh.

		mesh = new LSMMesh();
		
		// Main loop, running at the desired FPS.
		while (true)
		{
			long startTime = System.nanoTime();
			
			processInput(1. / FPS);
			
			if (state == LSMState.LSM_Designing)
			{
				mesh.timestep(1. / FPS);
			}
			else if (state == LSMState.LSM_Running)
			{
				// Timestep the grid and the mesh.
				grid.timestep(1. / FPS);
				mesh.timestep(1. / FPS);
			}
			
			// Redraw the screen and sleep.
			repaint();
			try {
				Thread.sleep(Math.max(0, 1000/FPS - (System.nanoTime() - startTime) / 1000000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void processInput(double dt)
	{
		if (state == LSMState.LSM_Designing)
		{
			// Place a mesh particle every so often.
			designPlaceTimeElapsed += dt;
			if (mouseIsDown && designPlaceTimeElapsed >= DESIGN_PLACE_TIME)
			{
				System.out.println(mesh.addParticle(mousePos));
				designPlaceTimeElapsed = 0.;
			}
		}
		else if (state == LSMState.LSM_Running && mouseSelectedParticle != null)
		{
			//mouseSelectedParticle.v.sub(mousePos, mouseSelectedParticle.pos);
			//mouseSelectedParticle.v.scale(FPS * 0.3);
			mouseSelectedParticle.goalpos.set(mousePos);
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		if (grid != null && DRAW_GRID)
			grid.draw(g);
		if (mesh != null && DRAW_MESH)
			mesh.draw(g);
	}

	public static void main(String[] args) {
		JFrame window = new JFrame();
		FastLSM lsm = new FastLSM();
		
		window.getContentPane().add(lsm, BorderLayout.CENTER);
		
		window.setSize(WIDTH, HEIGHT);
		window.setResizable(false);
		window.setVisible(true);
		window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		window.addMouseListener(lsm);
		window.addMouseMotionListener(lsm);
		
		lsm.run();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
	
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		mouseIsDown = true;
		mousePos.x = e.getX();
		mousePos.y = e.getY() + MOUSE_Y_OFFSET;
		
		if (state == LSMState.LSM_Running && grid != null)
		{
			mouseSelectedParticle = grid.getNearestParticle(new Point2d(e.getX(), e.getY() + MOUSE_Y_OFFSET));
			mouseSelectedParticle.isDragging = true;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		if (state == LSMState.LSM_Running && mouseSelectedParticle != null)
		{
			mouseSelectedParticle.isDragging = false;
			mouseSelectedParticle.v.set(0.,0.);
			mouseSelectedParticle = null;
		}
		
		mouseIsDown = false;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (mouseIsDown)
		{
			mousePos.x = e.getX();
			mousePos.y = e.getY() + MOUSE_Y_OFFSET;
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

}