import java.awt.Color;
import java.awt.Graphics;

import javax.vecmath.*;

public class LSMParticle extends LSMObject {

	public Point2d pos;
	public Vector2d v;
	
	public LSMParticle(Point2d pos)
	{
		this.pos = pos;
		v = new Vector2d();
	}
	
	public LSMParticle(double x, double y)
	{
		this.pos = new Point2d(x,y);
		v = new Vector2d();
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
		return 2;
	}

	@Override
	public void timestep(double dt) {
		pos.scaleAdd(dt, v, pos);
	}
}
