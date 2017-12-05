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
	private static final int WIDTH = 1280;
	private static final int HEIGHT = 720;
	
	// Frame rate (frames per second).
	private final int FPS = 30;

	// The grid containing all the grid particles.
	public final LSMGrid grid;
	
	// User input.
	private Point2d mousePos;
	private LSMGridParticle mouseSelectedParticle;
	
	public FastLSM() {
		grid = new LSMGrid(new Point2d(300., 200.), 3, 3, 300, 300, 2);
		mousePos = new Point2d();
		mouseSelectedParticle = null;
	}
	
	public void run()
	{
		// Main loop, running at the desired FPS.
		while (true)
		{
			long startTime = System.nanoTime();
			
			processInput();
			
			// Timestep the grid.
			grid.timestep(1. / FPS);
			
			// Redraw the screen and sleep.
			repaint();
			try {
				Thread.sleep(Math.max(0, 1000/FPS - (System.nanoTime() - startTime) / 1000000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void processInput()
	{
		if (mouseSelectedParticle != null)
		{
			mouseSelectedParticle.pos.set(mousePos);
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		grid.draw(g);
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
	public void mousePressed(MouseEvent e) {
		mouseSelectedParticle = grid.getNearestParticle(new Point2d(e.getX(), e.getY()));
		mousePos.x = e.getX();
		mousePos.y = e.getY();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		mouseSelectedParticle = null;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (mouseSelectedParticle != null)
		{
			mousePos.x = e.getX();
			mousePos.y = e.getY();
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

}