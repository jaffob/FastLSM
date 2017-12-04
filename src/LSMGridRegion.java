import java.awt.Graphics;
import java.util.ArrayList;

import javax.vecmath.*;

public class LSMGridRegion extends LSMObject {

	public final LSMGrid grid;
	public final LSMGridParticle owner;
	public final ArrayList<LSMGridParticle> particles;
	public final ArrayList<Vector2d> rigidParticles;
	public final Point2d ccm;
	public double rMass;
	
	public LSMGridRegion(LSMGrid grid, LSMGridParticle owner)
	{
		this.grid = grid;
		this.owner = owner;
		this.particles = new ArrayList<>();
		this.rigidParticles = new ArrayList<>();
		this.ccm = new Point2d();
		this.rMass = 0;
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
						LSMGridParticle p = grid.particles[i][j];
						particles.add(p);
						ccm.scaleAdd(p.mass, p.pos, ccm);
						rMass += p.mass;
					}
				}
			}
		}
		ccm.scale(1/rMass);
		for (LSMGridParticle p : particles)
		{
			rigidParticles.add(Vec.diff(p.pos, ccm));
		}
	}

	@Override
	public void draw(Graphics g) {
		
	}

	@Override
	public void timestep(double dt) {
		
	}

	public void shapeMatch(double dt) {
		Point2d ccm = new Point2d();
		Point2d gcm = new Point2d();
		for (LSMGridParticle p : particles)
		{
			gcm.scaleAdd(p.mass, p.goalpos, gcm);
		}
		gcm.scale(1/rMass);
		
		// calculate A_pq matrix
		GMatrix A = new GMatrix(2,2);
		A.setZero();
		
		for(int i=0; i<particles.size(); i++) {
			LSMGridParticle p = particles.get(i);
			Vector2d q = Vec.diff(p.pos, ccm);
			q.scale(p.mass);
			GMatrix qr = Vec.vecMatMul(q, rigidParticles.get(i));
			A.add(qr);
		}
		
		GMatrix S = new GMatrix(2,2);
		S.mulTransposeLeft(A, A);
		for(int i=0; i<2; i++) {
			for(int j=0; j<2; j++) {
				double e = S.getElement(i, j);
				e = Math.sqrt(e);
				S.setElement(i, j, e);
			}
		}
		
		GMatrix R = new GMatrix(2,2);
		S.invert();
		R.mul(A, S);
		
		for(int i=0; i<particles.size(); i++) {
			LSMGridParticle p = particles.get(i);
			Vector2d goal = Vec.matVecMul(R, rigidParticles.get(i));
			goal.add(gcm);
			p.goalpos.set(goal);
		}
		
		
	}

}
