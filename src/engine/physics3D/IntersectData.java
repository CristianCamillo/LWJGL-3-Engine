package engine.physics3D;

import org.joml.Vector3f;

public class IntersectData
{
	private final boolean doesIntersect;	
	private final Vector3f direction;
	
	public IntersectData(boolean doesIntersect, Vector3f direction)
	{
		this.doesIntersect = doesIntersect;
		this.direction = direction;
	}
	
	public boolean isIntersecting()  { return doesIntersect; }
	public Vector3f getDirection()	 { return direction; }
}