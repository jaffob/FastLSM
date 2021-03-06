import java.awt.Color;
import java.awt.Graphics;
//import java.util.ArrayList;
import java.util.*;

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
	public static boolean DRAW_CCM = false;
	
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
		Queue<LSMGridParticle> que = new LinkedList<LSMGridParticle>();
		HashMap<LSMGridParticle, Integer> map = new HashMap<>();
		que.add(grid.particles[owner.gx][owner.gy]);
		map.put(grid.particles[owner.gx][owner.gy], 0);
		
		while(!que.isEmpty()){
			LSMGridParticle cur = que.poll();
			for (int i = cur.gx - 1; i <= cur.gx + 1; i++)
			{
				if (i >= 0 && i < grid.nx)
				{
					for (int j = cur.gy - 1; j <= cur.gy + 1; j++)
					{
						if (j >= 0 && j < grid.ny && grid.particles[i][j] != null)
						{
							LSMGridParticle p = grid.particles[i][j];
							if(p.mass == 0) {
								continue;
							}

							particles.add(p);
							rcm.scaleAdd(p.mass, p.pos, rcm);
							rMass += p.mass;
						
							int d = map.get(cur);
							if(d >= grid.w)
								continue;
							
							for (int k = cur.gx - 1; k <= cur.gx + 1; k++)
							{
								if (k >= 0 && k < grid.nx)
								{
									for (int l = cur.gy - 1; l <= cur.gy + 1; l++)
									{
										if (l >= 0 && l < grid.ny && grid.particles[k][l] != null)
										{
											LSMGridParticle n = grid.particles[k][l];
											if(map.containsKey(n))
												continue;
											map.put(n, d+1);
											if (n.mass != 0.)
												que.add(n);
										}
									}
								}
							}
						}
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
	
	public void initRegionOld() {
		
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
		
		//System.out.println(A);
		
		GMatrix R = new GMatrix(2,2);
//		GMatrix S = new GMatrix(A);
//		
//		S.transpose();
//		S.mul(A);
//		
//		if (S.getElement(0, 1) != 0.)
//		{
//			//calculate sqrt of S
//			double B = (S.getElement(1, 1) - S.getElement(0, 0))/(2*S.getElement(0, 1));
//			double t = Math.signum(B)/(Math.abs(B) + Math.sqrt(B*B + 1));
//			//double c = 1/(Math.sqrt(t*t + 1));
//			//double s = c*t;
//		
//			double temp1 = S.getElement(0, 0) - t*S.getElement(0, 1);
//			double temp2 = S.getElement(1, 1) - t*S.getElement(0, 1);
//			S.setElement(0, 0, Math.sqrt(temp1));
//			S.setElement(0, 1, 0);
//			S.setElement(1, 0, 0);
//			S.setElement(1, 1, Math.sqrt(temp2));
//			
//			//System.out.println("s = " + s + ", angle = " + Math.asin(s));
//			//System.out.println("c = " + c + ", angle = " + Math.acos(c));
//			//System.out.println("c^2 + s^2 = " + (c*c + s*s));
//			
//			try{
//				S.invert();
//			} catch (Exception e){
//				System.out.println(e);
//				FastLSM.debugFlag = true;
//			}
//			
//			R.mul(A, S);
//		}
		//System.out.println(R.getElement(0, 0)*R.getElement(0, 0) + R.getElement(0, 1)*R.getElement(0, 1));
		
		GMatrix U = new GMatrix(2,2);
		GMatrix W = new GMatrix(2,2);
		GMatrix V = new GMatrix(2,2);
		A.SVD(U, W, V);
		V.transpose();
	
		R.mul(U, V);
		
		GMatrix Udet = new GMatrix(2,2);
		Udet.setElement(0, 0, R.getElement(0, 0) * R.getElement(1, 1) - R.getElement(1, 0) * R.getElement(0, 1));
		
		R.set(U);
		
		R.mul(Udet);
		R.mul(V);
		
		double arccos = Math.acos(R.getElement(0,0));
		
		if (arccos > Math.PI/2.)
		{
			R.negate();
		}
		
		//System.out.println(arccos);
		//System.out.println("(0,0) = " + Math.toDegrees(Math.acos(R.getElement(0,0))) + ", (1,0) = " +  + Math.toDegrees(Math.asin(R.getElement(1,0))));
		
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
