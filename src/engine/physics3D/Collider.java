package engine.physics3D;

import org.joml.Vector3f;

public abstract class Collider
{
	public enum Shape {PLANE, SPHERE, BOX};
	
	private final Shape shape;	
	protected final Vector3f position;
	
	public Collider(Shape shape, Vector3f position)
	{
		this.shape = shape;
		this.position = position;
	}
	
	public Shape getShape() 	  	 { return shape; }
	public Vector3f getPosition()    { return position; }
	
	public IntersectData intersect(Collider other) // only sphere -> plane, sphere -> sphere, sphere -> box
	{		
		if(shape == Shape.SPHERE)
			if(other.getShape() == Shape.PLANE)
				return ((Plane)other).intersectSphere((Sphere)this);	
			else if(other.shape == Shape.SPHERE)
				return ((Sphere)this).intersectSphere((Sphere)other);
			else if(other.shape == Shape.BOX)
				return ((Box)other).intersectSphere((Sphere)this);		

		return null; 
	}
	
	public abstract void transform(Vector3f translation);	
}