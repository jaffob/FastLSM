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
	
	// Constants (or not-so-constants).
	public static boolean ENABLE_SM_ROTATION = true;
	public static boolean DRAW_RCM = false;
	public static boolean DRAW_CCM = true;
	
	public Point2d ccmdraw;
	
	public LSMGridRegion(LSMGrid grid, LSMGridParticle owner)
	{
		this.grid = grid;
		this.owner = owner;
		this.particles = new ArrayList<>();
		this.rigidParticles = new ArrayList<>();
		this.rcm = new Point2d();
		this.rMass = 0;
		
		this.ccmdraw = new Point2d();
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
	public void draw(Graphics g)
	{
		if (DRAW_RCM)
		{
			g.setColor(Color.BLUE);
			g.fillRect((int)rcm.x - 2, (int)rcm.y - 2, 4, 4);
		}
		if (DRAW_CCM)
		{
			g.setColor(Color.BLUE);
			g.fillRect((int)ccmdraw.x - 2, (int)ccmdraw.y - 2, 4, 4);
		}
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
		
		//calculate sqrt of S
		double B = (S.getElement(1, 1) - S.getElement(0, 0))/(2*S.getElement(0, 1));
		double t = Math.signum(B)/(Math.abs(B) + Math.sqrt(B*B + 1));
		double c = 1/(Math.sqrt(t*t + 1));
		double s = c*t;
		
		GMatrix R = new GMatrix(2,2);
		R.setElement(0, 0, c);
		R.setElement(0, 1, s);
		R.setElement(1, 0, -s);
		R.setElement(1, 1, c);
		
		System.out.println("s = " + s + ", angle = " + Math.asin(s));
		System.out.println("c = " + c + ", angle = " + Math.acos(c));
		
		try{
			S.invert();
		} catch (Exception e){
			System.out.println(e);
			FastLSM.debugFlag = true;
		}
		R.mul(A, S);
		
		for(int i=0; i<particles.size(); i++) {
			LSMGridParticle p = particles.get(i);
			
			Vector2d goal = ENABLE_SM_ROTATION ?
					Vec.matVecMul(R, rigidParticles.get(i))
					: new Vector2d(rigidParticles.get(i));
			goal.add(ccm);
			
			if (!p.isDragging)
				p.goalpos.set(goal);
			
			// Update velocity based on goal position.
			p.v_accum.scaleAdd(LSMGrid.STIFFNESS_ALPHA / (dt * particles.size()), Vec.diff(p.goalpos, p.pos), p.v_accum);
		}
		
		ccmdraw.set(ccm);
	}

}
