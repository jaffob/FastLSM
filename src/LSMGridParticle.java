import javax.vecmath.*;

public class LSMGridParticle extends LSMParticle {

	public final LSMGrid grid;
	public final LSMGridRegion r;
	
	// Grid x and y indices.
	public final int gx, gy;
	
	// Goal positions.
	public Point2d goalpos;
	
	public boolean isPinned;
	
	public LSMGridParticle(LSMGrid grid, int gx, int gy)
	{
		super(gx * grid.getCellWidth() + grid.pos.x, gy * grid.getCellHeight() + grid.pos.y);
		this.goalpos = new Point2d(pos);
		this.grid = grid;
		this.gx = gx;
		this.gy = gy;
		this.isPinned = false;
		this.r = new LSMGridRegion(grid, this);
	}

	@Override
	public String toString() {
		return "(" + gx + "," + gy + ")/(" + pos.x + "," + pos.y + ")";
	}

	public void initParticle() {
		r.initRegion();
	}
}