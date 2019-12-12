package engine.graphics3D;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Transform
{
	// left-handed rotation based (for right-handed: left.x -> -1, right.x -> 1, pitch rot axis -> right)
	
	private Vector3f position;
	private Quaternionf rotation;
	private Vector3f scale;
	
	private final Matrix4f matrix;
	
	public Transform() 														  { this(new Vector3f(0,0,0), new Quaternionf(0,0,0,1), new Vector3f(1,1,1)); }
	public Transform(Vector3f position) 					  				  { this(position, new Quaternionf(0,0,0,1), new Vector3f(1,1,1)); }
	public Transform(Vector3f position, Quaternionf rotation) 				  { this(position, rotation, new Vector3f(1,1,1)); }
	public Transform(Vector3f position, Quaternionf rotation, Vector3f scale)
	{
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
		
		matrix = new Matrix4f();
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public Vector3f getPosition()	 { return position; }
	public Quaternionf getRotation() { return rotation; }
	public Vector3f getScale()		 { return scale; }
	public Vector3f getAngles()		 { return toEulerAngles(); }
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void setPosition(Vector3f position)					{ this.position = position; }
	public void setPosition(float x, float y, float z)			{ position.x = x; position.y = y; position.z = z; }
	public void setRotation(Quaternionf rotation)				{ this.rotation = rotation; }
	public void setRotation(float x, float y, float z, float w) { rotation.x = x; rotation.y = y; rotation.z = z; rotation.w = w; }
	public void setScale(Vector3f scale)						{ this.scale = scale; }
	public void setScale(float x, float y, float z) 			{ scale.x = x; scale.y = y;	scale.z = z; }
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void move(Vector3f movement)				{ position.add(movement); }
	public void move(float x, float y, float z) 	{ position.x += x; position.y += y; position.z += z; }
	public void move(Vector3f direction, float amt) { move(direction.mul(amt)); }
	
	public void moveForward(float amt)				{ move(getForward().mul(amt)); }
	public void moveBack(float amt)					{ move(getBack().mul(amt)); }
	public void moveUp(float amt)					{ move(getUp().mul(amt)); }
	public void moveDown(float amt)					{ move(getDown().mul(amt)); }
	public void moveLeft(float amt)					{ move(getLeft().mul(amt)); }
	public void moveRight(float amt)				{ move(getRight().mul(amt)); }
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void rotate(Quaternionf rotation)	   { rotation.mul(this.rotation, this.rotation).normalize(); }	
	public void rotate(float angle, Vector3f axis)
	{
		float sin = (float)Math.sin(angle * 0.5);
        float cos = (float)Math.cos(angle * 0.5);
        float x = axis.x * sin;
        float y = axis.y * sin;
        float z = axis.z * sin;
        float w = cos;
		
		Quaternionf toRotate = new Quaternionf(x, y, z, w);
		rotate(toRotate);
	}
		
	public void rotatePitch(float angle)		   { rotate(angle, getLeft()); }
	public void rotateYaw(float angle)		   	   { rotate(angle, getUp()); }
	public void rotateRoll(float angle)		   	   { rotate(angle, getForward()); }
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	// Directions relative to the current orientation (rotation)
	public Vector3f getForward() { return new Vector3f(0, 0, 1).rotate(rotation); }
	public Vector3f getBack()    { return new Vector3f(0, 0, -1).rotate(rotation); }
	public Vector3f getUp()		 { return new Vector3f(0, 1, 0).rotate(rotation); }
	public Vector3f getDown()	 { return new Vector3f(0, -1, 0).rotate(rotation); }
	public Vector3f getLeft()  	 { return new Vector3f(1, 0, 0).rotate(rotation); }
	public Vector3f getRight()	 { return new Vector3f(-1, 0, 0).rotate(rotation); }	
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public Matrix4f getMatrix()
	{
		matrix.identity();
			
		matrix.translate(position);
		matrix.rotate(rotation);
		matrix.scale(scale);
			
		return matrix;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Vector3f toEulerAngles()
	{
	    Vector3f angles = new Vector3f();

	    double sinr_cosp = 2 * (rotation.w * rotation.x + rotation.y * rotation.z);
	    double cosr_cosp = 1 - 2 * (rotation.x * rotation.x + rotation.y * rotation.y);
	    angles.x = (float)Math.atan2(sinr_cosp, cosr_cosp);

	    double sinp = 2 * (rotation.w * rotation.y - rotation.z * rotation.x);
	    if(Math.abs(sinp) >= 1)
	        angles.y = (float)Math.copySign(Math.PI / 2, sinp); // use 90 degrees if out of range
	    else
	        angles.y = (float)Math.asin(sinp);

	    double siny_cosp = 2 * (rotation.w * rotation.z + rotation.x * rotation.y);
	    double cosy_cosp = 1 - 2 * (rotation.y * rotation.y + rotation.z * rotation.z);
	    angles.z = (float)Math.atan2(siny_cosp, cosy_cosp);

	    angles.x = (float)Math.toDegrees(angles.x);
	    angles.y = (float)Math.toDegrees(angles.y);
	    angles.z = (float)Math.toDegrees(angles.z);
	    
	    return angles;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public String toString() { return "(" + position.toString() + ", " + rotation.toString() + ", " + scale.toString() + ")"; }
}