import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;
import javax.vecmath.Point2d;

public class FastLSM extends JComponent implements KeyListener {

	private static final long serialVersionUID = 1L;
	
	// Size of screen.
	private static final int WIDTH = 1280;
	private static final int HEIGHT = 720;
	
	// Frame rate (frames per second).
	private final int FPS = 30;

	// The grid containing all the grid particles.
	public final LSMGrid grid;
	
	public FastLSM() {
		grid = new LSMGrid(new Point2d(300., 200.), 3, 3, 300, 300, 2);
	}
	
	public void run() {
				
		while (true) {
			
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
	
	private void processInput() {
		
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
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

}