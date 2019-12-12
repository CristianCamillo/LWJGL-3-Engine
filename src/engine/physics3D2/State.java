package engine.physics3D2;

import org.joml.Vector3f;

public class State
{
	private Vector3f position;
	private Vector3f momentum;
	
	private Vector3f velocity;
	
	private float mass;
	private float inverseMass;
	
	public State(Vector3f position, Vector3f momentum, Vector3f velocity, float mass)
	{
		this.position = position;
		this.momentum = momentum;
		this.velocity = velocity;
		this.mass = mass;
		inverseMass = 1f / mass;
	}
	
	public Vector3f getPosition() { return position; }
	public Vector3f getMomentum() { return momentum; }
	public Vector3f getVelocity() { return velocity; }
	public float getMass()		  { return mass; }
	
	public void recalculate()
	{
		momentum.mul(inverseMass, velocity);
	}
}