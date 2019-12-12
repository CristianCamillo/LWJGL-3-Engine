package engine.physics3D;

import org.joml.Vector3f;

public class Plane extends Collider
{
	private final Vector3f normal;
	private final float distance;
	
	public Plane(Vector3f position, Vector3f normal, float distance)
	{
		super(Shape.PLANE, position);
		this.normal = normal;
		this.distance = distance;
	}
	
	public Vector3f getNormal() { return normal; }
	public float getDistance()  { return distance; }
	
	public Plane normalized()
	{
		float magnitude = normal.length();		
		return new Plane(position, new Vector3f(normal).div(magnitude), distance / magnitude);
	}
	
	public IntersectData intersectSphere(Sphere other)
	{		
		float distanceFromSphereCenter = Math.abs(normal.dot(other.getCenter()) - distance);
		float distanceFromSphere = distanceFromSphereCenter - other.getRadius();
		
		return new IntersectData(distanceFromSphere < 0, new Vector3f(normal).mul(distanceFromSphere));
	}

	@Override
	public void transform(Vector3f translation)
	{
		position.add(translation);	
	}
}