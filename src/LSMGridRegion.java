import java.awt.Graphics;
import java.util.ArrayList;

import javax.vecmath.*;

public class LSMGridRegion extends LSMObject {

	public final LSMGrid grid;
	public final LSMGridParticle owner;
	public final ArrayList<LSMGridParticle> particles;
	
	public LSMGridRegion(LSMGrid grid, LSMGridParticle owner)
	{
		this.grid = grid;
		this.owner = owner;
		this.particles = new ArrayList<>();
	}
	
	public void initRegion() {
		
		// Loop over all the particles that would be in this region (careful of grid edges).
		for (int i = owner.gx - grid.w; i <= owner.gx + grid.w; i++)
		{
			if (i >= 0 && i < grid.nx)
			{
				for (int j = owner.gy - grid.w; j <= owner.gy + grid.w; j++)
				{
					if (j >= 0 && j < grid.ny && grid.particles[i][j] != null)
					{
						particles.add(grid.particles[i][j]);
					}
				}
			}
		}
	}

	@Override
	public void draw(Graphics g) {
		
	}

	@Override
	public void timestep(double dt) {
		
	}

	public void shapeMatch(double dt) {
		for (LSMGridParticle p : particles)
		{
			p.f.add(new Vector2d(0, 10.));
		}
	}

}
