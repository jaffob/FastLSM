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
	public boolean isDragging;
	
	private static double BOUNCE = 0.4;
	
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
		this.isDragging = false;
	
		this.r = new LSMGridRegion(grid, this);
	}

	public void initParticle()
	{
		r.initRegion();
	}
	
	@Override
	public void timestep(double dt) {
		super.timestep(dt);
		pos.scaleAdd(dt, v_accum, pos);
		//v_accum.set(0., 0.);
		v.scale(0.99999);
		v.scaleAdd(dt, v_accum, v);
		v_accum.scale(0.7);
	
		if(pos.x < 0.0 && v.x < 0) {
			v.x = -BOUNCE*v.x;
			pos.x = 0.0;
		}
		if(pos.y < 0.0 && v.y < 0) {
			v.y = -BOUNCE*v.y;
			pos.y = 0.0;
		}
		if(pos.x > FastLSM.WIDTH  && v.x > 0) {
			v.x = -BOUNCE*v.x;
			pos.x = FastLSM.WIDTH;
		}
		if(pos.y > FastLSM.HEIGHT && v.y > 0) {
			v.y = -BOUNCE*v.y;
			pos.y = FastLSM.HEIGHT;
		}
	}
	
	@Override
	public Color getParticleColor() {
		return Color.GRAY;
	}
	
	@Override
	public int getParticleRadius() {
		return 3;
	}
	
	@Override
	public void draw(Graphics g) {
		super.draw(g);
		
		if (DRAW_GOALPOS)
		{
			g.setColor(isDragging ? Color.GREEN : Color.RED);
			g.fillRect((int)goalpos.x - 1, (int)goalpos.y - 1, 3, 3);
		}
	}
	
	@Override
	public String toString() {
		return "(" + gx + "," + gy + ")/(" + pos.x + "," + pos.y + ")";
	}
}
