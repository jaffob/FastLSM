import java.awt.Color;

import javax.vecmath.Point2d;

public class LSMMeshParticle extends LSMParticle {

	public LSMMeshParticle(Point2d pos) {
		super(pos);
		// TODO Auto-generated constructor stub
	}

	public LSMMeshParticle(double x, double y)
	{
		this(new Point2d(x,y));
	}
	
	@Override
	public Color getParticleColor() {
		return Color.CYAN;
	}
	
	@Override
	public int getParticleRadius() {
		return 4;
	}
	
	@Override
	public void timestep(double dt) {
		super.timestep(dt);
		
		// Bilinear interpolation of position.
	}
}
