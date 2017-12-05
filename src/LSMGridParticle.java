import java.awt.Color;
import java.awt.Graphics;

import javax.vecmath.*;

public class LSMGridParticle extends LSMParticle {

	public final LSMGrid grid;
	public final LSMGridRegion r;
	
	// Grid x and y indices.
	public final int gx, gy;
	
	// Goal position.
	public Point2d goalpos;
	
	// Physics stuff.
	public Vector2d v_accum;
	public boolean isPinned;
	
	// Drawing parameters.
	public static boolean DRAW_GOALPOS = true;
	
	public LSMGridParticle(LSMGrid grid, int gx, int gy)
	{
		super(gx * grid.getCellWidth() + grid.pos.x, gy * grid.getCellHeight() + grid.pos.y);
		this.goalpos = new Point2d(pos);
		this.grid = grid;
		this.gx = gx;
		this.gy = gy;
		this.v_accum = new Vector2d();
		this.isPinned = false;
		this.r = new LSMGridRegion(grid, this);
	}

	public void initParticle() {
		r.initRegion();
	}
	
	@Override
	public void timestep(double dt) {
		super.timestep(dt);
		
		pos.scaleAdd(dt, v_accum, pos);
		v_accum.set(0., 0.);
	}
	
	@Override
	public void draw(Graphics g) {
		super.draw(g);
		
		if (DRAW_GOALPOS)
		{
			g.setColor(Color.RED);
			g.fillRect((int)goalpos.x - 1, (int)goalpos.y - 1, 3, 3);
		}
	}
	
	@Override
	public String toString() {
		return "(" + gx + "," + gy + ")/(" + pos.x + "," + pos.y + ")";
	}
}
