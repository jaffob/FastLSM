import java.awt.Graphics;
import java.util.ArrayList;

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
	public LSMGrid createGrid(int w)
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
		
		LSMGrid grid = new LSMGrid(topleft, 15, 15, botright.x - topleft.x, botright.y - topleft.y, w);
		return grid;
	}

}
