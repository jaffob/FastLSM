import java.util.ArrayList;

public class LSMPhysicsSystem {

	public final ArrayList<LSMObject> objs;
	
	public LSMPhysicsSystem(ArrayList<LSMObject> objs)
	{
		this.objs = objs;
	}
	
	/**
	 * Figure out all the forces on everything. Particles will integrate
	 * forces/velocities after this method runs.
	 * @param dt delta time in fraction of a second.
	 */
	public void timestep(double dt)
	{
		
	}
	
}
