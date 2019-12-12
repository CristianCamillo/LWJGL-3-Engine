package engine.physics3D;

import java.util.ArrayList;

import org.joml.Vector3f;
public class PhysicsScene
{
	private ArrayList<PhysicsObject> objects;
	private Vector3f gravityVector;
	
	public PhysicsScene()
	{
		objects = new ArrayList<PhysicsObject>();
		gravityVector = new Vector3f();
	}
	
	public float getGravity() { return - gravityVector.y; }
	
	public void setGravity(float gravity) { gravityVector.y = - gravity; } 
	
	public void addObject(PhysicsObject object)
	{
		objects.add(object);
	}
	
	public void simulate(float elapsedTime)
	{
		for(PhysicsObject object : objects)
		{
			object.getVelocity().add(new Vector3f(gravityVector).mul(elapsedTime));			
			object.integrate(elapsedTime);
		}
	}
	
	public void handleCollisions()
	{
		for(int i = 0; i < objects.size(); i++)
			for(int j = i + 1; j < objects.size(); j++)
			{
				IntersectData intersectData = objects.get(i).getCollider().intersect(objects.get(j).getCollider());
				
				if(intersectData != null && intersectData.isIntersecting())
				{
					Vector3f direction = intersectData.getDirection().normalize();
					Vector3f otherDirection = new Vector3f(direction).reflect(new Vector3f(objects.get(i).getVelocity()).normalize());
					
					objects.get(i).setVelocity(new Vector3f(objects.get(i).getVelocity()).reflect(otherDirection));
					objects.get(j).setVelocity(new Vector3f(objects.get(j).getVelocity()).reflect(direction));
				}
			}
	}
}