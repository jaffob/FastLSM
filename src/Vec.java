
import javax.vecmath.*;

public class Vec {

	public static Vector2d sum(Tuple2d a, Tuple2d b)
	{
		Vector2d out = new Vector2d();
		out.add(a, b);
		return out;
	}
	
	public static Vector2d sumScaled(Tuple2d a, Tuple2d b, double b_scale)
	{
		Vector2d out = new Vector2d();
		out.scaleAdd(b_scale, b, a);
		return out;
	}
	
	public static Vector2d diff(Tuple2d a, Tuple2d b)
	{
		Vector2d out = new Vector2d();
		out.sub(a, b);
		return out;
	}
	
	public static double dot(Vector2d a, Vector2d b)
	{
		Vector2d out = new Vector2d(a);
		return out.dot(b);
	}
	
	/**
	 * Returns the normal of an edge defined by two points.
	 * @param q One point of the edge
	 * @param r Another point of the edge
	 * @param pointTowards The resulting normal will face towards this point.
	 * @return Normal unit vector
	 */
	public static Vector2d normal(Point2d q, Point2d r, Point2d pointTowards)
    {
    	Vector2d out = Vec.diff(q, r);
    	out = new Vector2d(-out.y, out.x);
    	out.normalize();
    	Vector2d toPoint = Vec.diff(pointTowards, q);
    	toPoint.normalize();
    	double dotResult = Vec.dot(toPoint, out);
    	
    	if (dotResult < 0.)
    	{
    		out.negate();
    	}
    	
    	return out;
    }

	public static Vector2d lerp(Tuple2d a, Tuple2d b, double alpha) {
		Vector2d out = new Vector2d(a);
		a.scale(1. - alpha);
		out.scaleAdd(alpha, b);
		return out;
	}
}
