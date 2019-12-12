package engine.physics3D;

import org.joml.Vector3f;

public class PhysicsObject
{
	private Collider collider;
	
	private Vector3f position;
	private Vector3f oldPosition;
	
	private Vector3f velocity;
	
	private boolean fixed;
	
	public PhysicsObject(Collider collider, Vector3f velocity, boolean fixed)
	{
		this.collider = collider;
		
		position = collider.getPosition();
		oldPosition = collider.getPosition();
		
		this.velocity = velocity;
		this.fixed = fixed;
	}
	
	public Vector3f getPosition() { return position; }
	public Vector3f getVelocity() { return velocity; }
	public boolean isFixed()	  { return fixed; }

	public void setVelocity(Vector3f velocity) { this.velocity = velocity; }
	public void setFixed(boolean fixed)		   { this.fixed = fixed; }
	
	public void integrate(float elapsedTime)
	{
		if(!fixed)
			position.add(new Vector3f(velocity).mul(elapsedTime));
	}
	
	public Collider getCollider()
	{
		Vector3f translation = new Vector3f(position).sub(oldPosition);
		oldPosition = position;
		collider.transform(translation);
		
		return collider;
	}
}