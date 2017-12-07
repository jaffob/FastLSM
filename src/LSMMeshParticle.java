import java.awt.Color;

import javax.vecmath.Point2d;

public class LSMMeshParticle extends LSMParticle {

	public LSMGridParticle gul, gur, gll, glr;
	public double alpha, beta;
	
	public LSMMeshParticle(Point2d pos) {
		super(pos);
		gul = gur = gll = glr = null;
		alpha = beta = -1.;
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
	public void timestep(double dt)
	{
		// Bilinear interpolation of position.
		if (alpha >= 0. && beta >= 0.)
		{
			pos.x = (1.-alpha)*(1.-beta)*gul.pos.x + (alpha)*(1.-beta)*gur.pos.x + (1.-alpha)*(beta)*gll.pos.x + alpha*beta*glr.pos.x;
			pos.y = (1.-alpha)*(1.-beta)*gul.pos.y + (alpha)*(1.-beta)*gur.pos.y + (1.-alpha)*(beta)*gll.pos.y + alpha*beta*glr.pos.y;
		}
	}
	
	public void connectToGridParticles(LSMGridParticle gul, LSMGridParticle gur, LSMGridParticle gll, LSMGridParticle glr)
	{
		this.gul = gul;
		this.gur = gur;
		this.gll = gll;
		this.glr = glr;
		
		this.alpha = (this.pos.x - gul.pos.x) / (glr.pos.x - gul.pos.x);
		this.beta = (this.pos.y - gul.pos.y) / (glr.pos.y - gul.pos.y);
	}
}
