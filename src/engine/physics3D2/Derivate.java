package engine.physics3D2;

import org.joml.Vector3f;

public class Derivate
{
	private Vector3f velocity;
	private Vector3f force;
	
	public Derivate(Vector3f velocity, Vector3f force)
	{
		this.velocity = velocity;
		this.force = force;
	}
	
	public Vector3f getVecloity() { return velocity; }
	public Vector3f getForce()	  { return force; }
}