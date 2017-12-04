import java.awt.Graphics;

import javax.vecmath.Point2d;

public class LSMGrid extends LSMObject {

	// Position of the grid.
	public Point2d pos;
	
	// Number of particles in each dimension.
	public final int nx, ny;
	
	// Size of the grid in world coords.
	public final double width, height;
	
	public final LSMGridParticle[][] particles;
	
	// Half width of the regions.
	public final int w;
	
	public LSMGrid(Point2d pos, int nx, int ny, double width, double height, int w)
	{
		this.pos = pos;
		this.nx = nx;
		this.ny = ny;
		this.width = width;
		this.height = height;
		this.w = w;
		
		particles = new LSMGridParticle[nx][ny];
		
		// Create particles to put in the grid.
		for (int i = 0; i < particles.length; i++)
		{
			for (int j = 0; j < particles[0].length; j++)
			{
				particles[i][j] = new LSMGridParticle(this, i, j);
			}
		}
		
		// Initialize each particle now that they all exist.
		for (int i = 0; i < particles.length; i++)
		{
			for (int j = 0; j < particles[0].length; j++)
			{
				particles[i][j].initParticle();
			}
		}
	}
	
	public LSMGrid(Point2d pos, double cellwidth, double cellheight, double width, double height, int w)
	{
		this(pos, (int)Math.ceil(width / cellwidth), (int)Math.ceil(height / cellheight), width, height, w);
	}
	
	@Override
	public void timestep(double dt) {
		
		// Shape match each region, thus accumulating forces in the particles.
		for (int i = 0; i < particles.length; i++)
		{
			for (int j = 0; j < particles[0].length; j++)
			{
				particles[i][j].r.shapeMatch(dt);
			}
		}
		
		// Timestep each particle (f -> v -> pos).
		for (int i = 0; i < particles.length; i++)
		{
			for (int j = 0; j < particles[0].length; j++)
			{
				particles[i][j].timestep(dt);
			}
		}
	}
	
	@Override
	public void draw(Graphics g) {
		
		// Draw cell outlines.
		for (int i = 0; i < particles.length; i++)
		{
			for (int j = 0; j < particles[0].length; j++)
			{
				particles[i][j].draw(g);
			}
		}
	}
	
	public int getNumParticles()
	{
		return nx*ny;
	}
	
	public double getCellWidth()
	{
		return width / (nx - 1);
	}
	
	public double getCellHeight()
	{
		return height / (ny - 1);
	}

}
