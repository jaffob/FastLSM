import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.*;

public class FastLSM extends JComponent implements KeyListener {

	private static final long serialVersionUID = 1L;
	
	// Size of screen.
	private static final int WIDTH = 1280;
	private static final int HEIGHT = 720;
	
	// Frame rate (frames per second).
	private final int FPS = 30;
	
	private long lastStep;
	
	// LSMObjects representing physical objects.
	public final ArrayList<LSMObject> objs;
	
	// Physics system.
	private final LSMPhysicsSystem phys;
	
	public FastLSM() {
		objs = new ArrayList<>();
		phys = new LSMPhysicsSystem(objs);
	}
	
	public void run() {
		
		lastStep = System.nanoTime();
		
		while (true) {
			
			processInput();
			
			long currTime = System.nanoTime();
			double dt = (double)(currTime - lastStep) / 1000000000.;
			
			// Timestep the physics system.
			phys.timestep(dt);
			
			// Timestep all the LSMObjects.
			for (LSMObject obj : objs)
			{
				obj.timestep(dt);
			}
			
			lastStep = currTime;
			
			// Redraw the screen and sleep.
			repaint();
			try {
				Thread.sleep(1000 / FPS);
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
		
		for (LSMObject obj : objs)
		{
			obj.draw(g);
		}
	}

	public static void main(String[] args) {
		JFrame window = new JFrame();
		FastLSM lsm = new FastLSM();
		
		window.getContentPane().add(lsm, BorderLayout.CENTER);
		
		window.setSize(WIDTH, HEIGHT);
		window.setResizable(false);
		window.setVisible(true);
		window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		lsm.objs.add(new LSMParticle(100., 200.));
		lsm.objs.add(new LSMParticle(200., 200.));
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