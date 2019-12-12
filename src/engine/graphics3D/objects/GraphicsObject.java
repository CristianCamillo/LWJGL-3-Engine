package engine.graphics3D.objects;

import engine.graphics3D.Mesh;
import engine.graphics3D.Transform;

public class GraphicsObject
{
	protected final Transform transform;
	protected Mesh mesh;
	
	public GraphicsObject()
	{
		transform = new Transform();
	}
	
	public GraphicsObject(Mesh mesh)
	{
		transform = new Transform();
		this.mesh = mesh;
	}
	
	public Transform getTransform() { return transform; }
	public Mesh getMesh() 			{ return mesh; }
	
	public void setMesh(Mesh mesh) { this.mesh = mesh; }
}