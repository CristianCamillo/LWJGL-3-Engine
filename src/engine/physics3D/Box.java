package engine.physics3D;

import java.util.ArrayList;

import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Box extends Collider
{
	private final Quaternionf rotation;
	
	private final Vector3f v1;
	private final Vector3f v2;
	private final Vector3f v3;
	private final Vector3f v4;
	private final Vector3f v5;
	private final Vector3f v6;
	private final Vector3f v7;
	private final Vector3f v8;
	
	/*
	 * 			
	 * 			v6-------v7
	 *		   /		/ |
	 * 		  /		   /  |
	 * 		 /		  /   |
	 * 		v2-------v3	 v8
	 * 		|		 |   /
	 * 		|		 |  /
	 *		| 		 | /
	 * 		v1-------v4
	 * 
	 * 
	 */
	
	private final float halfWidth;
	private final float halfHeight;
	private final float halfDepth;
	
	public Box(Vector3f position, Quaternionf rotation, Vector3f extreme1, Vector3f extreme2)
	{
		super(Shape.BOX, position);
		this.rotation = rotation;
		
		v1 = new Vector3f(extreme1);
		v2 = new Vector3f(v1);			v2.y = extreme2.y;
		v3 = new Vector3f(v2);			v3.x = extreme2.x;
		v4 = new Vector3f(v1);			v4.x = extreme2.x;
		v5 = new Vector3f(v1);			v5.z = extreme2.z;
		v6 = new Vector3f(v2);			v6.z = extreme2.z;
		v7 = new Vector3f(extreme2);
		v8 = new Vector3f(v7);			v8.y = extreme1.y;
		
		halfWidth  = (v4.x - v1.x) / 2f;
		halfHeight = (v2.y - v1.y) / 2f;
		halfDepth  = (v1.z - v5.z) / 2f;
	}
	
	public IntersectData intersectSphere(Sphere sphere)
	{
		if(position == sphere.getPosition())
			return new IntersectData(true, new Vector3f());
		
		Vector3f centerDifference = new Vector3f(sphere.getPosition()).sub(position);
		Vector3f sphereDirection = new Vector3f(centerDifference).normalize();
		float centerSphere = centerDifference.length() - sphere.getRadius();
		
		
		ArrayList<Vector3f[]> toIntersectPlanes = new ArrayList<Vector3f[]>();

		
		
		
		
		for(int i = 0; i < 3; i++)
		{
			Vector3f normal = getNextNormal(i);
			float dot = sphereDirection.dot(normal);
			
			if(dot != 0f) //if(absDotF >= 0.00001f)
			{
				Vector3f pointOnPlane;
				
				if(dot > 0f)
					pointOnPlane = v1;
				else
				{
					normal.x *= -1f;
					normal.y *= -1f;
					normal.z *= -1f;
					
					pointOnPlane = v7;
				}		
				
				if(dot == 1f || dot == -1f) //if(0.99999f <= absDotF && absDotF <= 1.00001f) // avoided "absDotF == 1f" to account for floating point imprecision
				{
					Vector3f intersectionPoint = lineIntersectPlane(position, sphere.getPosition(), pointOnPlane, normal);
					float centerBoxIntersection = new Vector3f(intersectionPoint).sub(position).length();
					
					System.out.println(centerSphere < centerBoxIntersection);
					
					return new IntersectData(centerSphere < centerBoxIntersection, sphereDirection);
				}
				else
					toIntersectPlanes.add(new Vector3f[] {pointOnPlane, normal});
			}
		}
		
		// comes out with 2 or 3 planes
		
		float centerBoxIntersection = lineIntersectPlane(position, sphere.getPosition(), toIntersectPlanes.get(0)[0], toIntersectPlanes.get(0)[1]).length();
		
		for(int i = 1; i < toIntersectPlanes.size(); i++)
		{
			Vector3f intersectionPoint = lineIntersectPlane(position, sphere.getPosition(), toIntersectPlanes.get(i)[0], toIntersectPlanes.get(i)[1]);
			float distance = intersectionPoint.length();
			
			if(distance < centerBoxIntersection)
				centerBoxIntersection = distance;
		}
		
		System.out.println(centerSphere < centerBoxIntersection);
		
		return new IntersectData(centerSphere < centerBoxIntersection, sphereDirection);
		
		
		
		/*float dot1 = sphereDirection.dot(getForward());
		float dot2 = sphereDirection.dot(getUp());
		float dot3 = sphereDirection.dot(getLeft());
		
		Vector3f intersectionPoint = new Vector3f(halfWidth * dot3, halfHeight * dot2, halfDepth * dot1);
		
		float centerIntersection = new Vector3f(intersectionPoint).sub(position).length();
		float centerSphere = centerDifference.length() - sphere.getRadius();
		
		System.out.println(dot1 + " " + dot2 + " " + dot3 + " " + centerIntersection + " " + centerSphere + " " + (centerSphere < centerIntersection));*/
		
		//return new IntersectData(centerSphere < centerIntersection, sphereDirection);
	}
	
	private Vector3f getNextNormal(int i)
	{
		if(i == 0)
			return getForward();
		else if(i == 1)
			return getUp();
		else
			return getRight();
	}
	
	private Vector3f getForward() { return new Vector3f(0, 0, 1).rotate(rotation); }
	private Vector3f getUp()	  { return new Vector3f(0, 1, 0).rotate(rotation); }
	private Vector3f getRight()   { return new Vector3f(-1, 0, 0).rotate(rotation); }
	
	private static Vector3f lineIntersectPlane(Vector3f linePointA, Vector3f linePointB, Vector3f pointOnPlane, Vector3f planeNormal)
	{
		float planeD = - planeNormal.dot(pointOnPlane);
		float ad = linePointA.dot(planeNormal);
		float bd = linePointB.dot(planeNormal);
		float t = (- planeD - ad) / (bd - ad);
		Vector3f lineStartToEnd = new Vector3f(linePointB).sub(linePointA);
		Vector3f lineToIntersect = new Vector3f(lineStartToEnd).mul(t);
		
		return new Vector3f(linePointA).add(lineToIntersect);
	}
	

	@Override
	public void transform(Vector3f translation)
	{
		
	}
}