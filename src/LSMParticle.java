import java.awt.Color;
import java.awt.Graphics;

import javax.vecmath.*;

public class LSMParticle extends LSMObject {

	public Point2d pos;		// Position tuple.
	public Vector2d v;		// Velocity vector.
	public Vector2d f;		// Force accumulator.
	
	public double mass;
	
	public LSMParticle(Point2d pos)
	{
		this.pos = new Point2d(pos);
		v = new Vector2d();
		f = new Vector2d();
		mass = 0.;
	}
	
	public LSMParticle(double x, double y)
	{
		this(new Point2d(x, y));
	}
	
	@Override
	public void draw(Graphics g) {
		g.setColor(getParticleColor());
		g.fillOval((int)pos.x - getParticleRadius(), (int)pos.y - getParticleRadius(), getParticleRadius() * 2, getParticleRadius() * 2);
	}
	
	public Color getParticleColor()
	{
		return Color.BLACK;
	}
	
	public int getParticleRadius()
	{
		return 4;
	}

	@Override
	public void timestep(double dt) {
		//f.y += 100.0;
		v.scaleAdd(dt / mass, f, v);		// v += f/mass * dt
		pos.scaleAdd(dt, v, pos);			// pos += v * dt
		f.set(0., 0.);						// clear forces
	}
	
	@Override
	public String toString() {
		return pos.toString();
	}
}
