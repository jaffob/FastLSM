
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
	
	public static GMatrix vecMatMul(Tuple2d a,Tuple2d b) {
		GMatrix mat = new GMatrix(2,2);
		mat.setElement(0, 0, a.x*b.x);
		mat.setElement(1, 0, a.y*b.x);
		mat.setElement(0, 1, a.x*b.y);
		mat.setElement(1, 1, a.y*b.y);
		return mat;
	}
	
	public static Vector2d matVecMul(GMatrix a,Tuple2d b) {
		GMatrix m1 = new GMatrix(a);
		GMatrix m2 = new GMatrix(2,1);
		m2.setElement(0, 0, b.x);
		m2.setElement(1, 0, b.y);
		m2.mul(m1,m2);
		Vector2d v = new Vector2d(m2.getElement(0, 0),m2.getElement(1, 0));
		return v;
	}
}
