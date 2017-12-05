import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.vecmath.*;

public class LSMGridRegion extends LSMObject {

	public final LSMGrid grid;
	public final LSMGridParticle owner;
	public final ArrayList<LSMGridParticle> particles;
	public final ArrayList<Vector2d> rigidParticles;
	public final Point2d rcm;
	public double rMass;
	
	public Point2d gcmdraw;
	
	public LSMGridRegion(LSMGrid grid, LSMGridParticle owner)
	{
		this.grid = grid;
		this.owner = owner;
		this.particles = new ArrayList<>();
		this.rigidParticles = new ArrayList<>();
		this.rcm = new Point2d();
		this.rMass = 0;
		
		this.gcmdraw = new Point2d();
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
						rcm.scaleAdd(p.mass, p.pos, rcm);
						rMass += p.mass;
					}
				}
			}
		}
		rcm.scale(1/rMass);
		for (LSMGridParticle p : particles)
		{
			rigidParticles.add(Vec.diff(p.pos, rcm));
		}
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(Color.BLUE);
		g.fillRect((int)rcm.x - 2, (int)rcm.y - 2, 4, 4);
	}

	@Override
	public void timestep(double dt) {
		
	}

	public void shapeMatch(double dt) {
		
		Point2d ccm = new Point2d();
		
		for (LSMGridParticle p : particles)
		{
			ccm.scaleAdd(p.mass, p.pos, ccm);
		}
		ccm.scale(1/rMass);
		
		if (FastLSM.debugFlag)
		{
			System.out.println("debugflag");
		}
		
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
		
		GMatrix S = new GMatrix(A);
		S.transpose();
		S.mul(A);
		
		for(int i=0; i<2; i++) {
			for(int j=0; j<2; j++) {
				double e = S.getElement(i, j);
				e = Math.sqrt(e);
				S.setElement(i, j, e);
			}
		}
		
		GMatrix R = new GMatrix(2,2);
		
		try{
		S.invert();
		} catch (Exception e){
			System.out.println(e);
			FastLSM.debugFlag = true;
		}
		R.mul(A, S);
		
		for(int i=0; i<particles.size(); i++) {
			LSMGridParticle p = particles.get(i);
			Vector2d goal = Vec.matVecMul(R, rigidParticles.get(i));
			goal.add(ccm);
			
			//Vector2d goal = Vec.sum(rigidParticles.get(i), gcm);
			p.goalpos.set(goal);
		}
		
		gcmdraw.set(ccm);
	}

}
