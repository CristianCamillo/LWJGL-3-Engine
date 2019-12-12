package engine.physics3D;

import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Sphere extends Collider
{
	private final float radius;
	private final Quaternionf rotation;
	
	public Sphere(Vector3f position, Quaternionf rotation, float radius)
	{
		super(Shape.SPHERE, position);
		this.rotation = rotation;
		this.radius = radius;
	}
	
	public Vector3f getCenter() 	 { return position; }
	public Quaternionf getRotation() { return rotation; }
	public float getRadius()		 { return radius; }
	
	public IntersectData intersectSphere(Sphere other)
	{
		//if(position == other.getPosition())
		//	return new IntersectData(true, new Vector3f());
		
		float radiusDistance = radius + other.getRadius();
		Vector3f direction = new Vector3f(other.getCenter()).sub(getCenter());
		float centerDistance = direction.length();
		direction.div(centerDistance);
		float distance = centerDistance - radiusDistance;
	
		return new IntersectData(distance < 0, new Vector3f(direction).mul(distance));
	}

	@Override
	public void transform(Vector3f translation)
	{
		position.add(translation);
	}
}