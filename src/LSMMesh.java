import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

import javax.vecmath.Point2d;

public class LSMMesh extends LSMObject {

	public final ArrayList<LSMMeshParticle> mparticles;
	
	public LSMMesh()
	{
		mparticles = new ArrayList<>();
	}
	
	@Override
	public void draw(Graphics g)
	{
		for (LSMMeshParticle mp : mparticles)
		{
			mp.draw(g);
		}
	}

	@Override
	public void timestep(double dt)
	{
		for (LSMMeshParticle mp : mparticles)
		{
			mp.timestep(dt);
		}
	}
	
	/**
	 * Creates and adds a new particle to the mesh
	 * @param pos The position
	 * @return The new particle
	 */
	public LSMMeshParticle addParticle(Point2d pos)
	{
		LSMMeshParticle mp = new LSMMeshParticle(pos);
		mparticles.add(mp);
		return mp;
	}
	
	/**
	 * Creates a grid that covers the particles in this mesh.
	 * @return
	 */
	public LSMGrid createGrid(int nx, int ny, int w)
	{
		// Get the outer bounds of the whole mesh.
		Point2d topleft = new Point2d(100000., 100000.);
		Point2d botright = new Point2d(-100000., -100000.);
		for (LSMMeshParticle mp : mparticles)
		{
			if (mp.pos.x < topleft.x) topleft.x = mp.pos.x;
			if (mp.pos.y < topleft.y) topleft.y = mp.pos.y;
			if (mp.pos.x > botright.x) botright.x = mp.pos.x;
			if (mp.pos.y > botright.y) botright.y = mp.pos.y;
		}
		
		// Create a grid.
		LSMGrid grid = new LSMGrid(topleft, nx, ny, botright.x - topleft.x, botright.y - topleft.y, w);
		
		// Associate each point in the mesh with a grid cell.
		for (LSMMeshParticle mp : mparticles)
		{
			int col = (int)Math.floor((mp.pos.x - grid.pos.x) * (grid.nx - 1)/ grid.width);
			int row = (int)Math.floor((mp.pos.y - grid.pos.y) * (grid.ny - 1)/ grid.height);
			
			col = Math.min(col, grid.nx - 2);
			row = Math.min(row, grid.ny - 2);
			
			mp.connectToGridParticles(grid.particles[col][row],
						grid.particles[col+1][row],
						grid.particles[col][row+1],
						grid.particles[col+1][row+1]);
		}
		
		for(LSMGridParticle[] ps : grid.particles) {
			for(LSMGridParticle p : ps) {
				if(p.mass == 0)
					System.out.println("please work");
			}
		}
		
		return grid;
	}
	
	public static LSMMesh createRandomMesh(int nparticles, Point2d pos, int width, int height)
	{
		LSMMesh mesh = new LSMMesh();
		Random rand = new Random();
		for (int i = 0; i < nparticles; i++) {
			mesh.addParticle(new Point2d(rand.nextInt(width) + pos.x, rand.nextInt(height) + pos.y));
		}
		return mesh;
	}

}
