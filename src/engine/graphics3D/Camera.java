package engine.graphics3D;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Camera
{
    private final Transform transform;
    
    private float fov;
    private float aspectRatio;
    private float zNear;
    private float zFar;
    
    private final Matrix4f projectionMatrix;
    private final Matrix4f viewMatrix;
    
    public Camera(float fov, float aspectRatio, float zNear, float zFar)
    {
    	transform = new Transform();
    	
    	this.fov = fov;
    	this.aspectRatio = aspectRatio;
    	this.zNear = zNear;
    	this.zFar = zFar;
    	
    	projectionMatrix = new Matrix4f();
    	viewMatrix = new Matrix4f();
    }
    
    public Vector3f getPosition()	 { return transform.getPosition(); }
	public Quaternionf getRotation() { return transform.getRotation(); }
	public Vector3f getAngles()		 { return transform.getAngles(); }
	
	public Vector3f getForward() { return new Vector3f(0, 0, -1).rotate(transform.getRotation()); }
	public Vector3f getBack()    { return new Vector3f(0, 0, 1).rotate(transform.getRotation()); }
	public Vector3f getUp()		 { return new Vector3f(0, 1, 0).rotate(transform.getRotation()); }
	public Vector3f getDown()	 { return new Vector3f(0, -1, 0).rotate(transform.getRotation()); }
	public Vector3f getLeft()  	 { return new Vector3f(1, 0, 0).rotate(transform.getRotation()); }
	public Vector3f getRight()	 { return new Vector3f(-1, 0, 0).rotate(transform.getRotation()); }
	
    public float getFov()			{ return fov; }
    public float getAspectRatio()	{ return aspectRatio; }
    public float getZNear()			{ return zNear; }
    public float getZFar()			{ return zFar; }
    
    public Matrix4f getProjectionMatrix() { return projectionMatrix; }
    public Matrix4f getViewMatrix()		  { return viewMatrix; }
    
    
    public void setPosition(float x, float y, float z)			{ transform.setPosition(x, y, z); }
	public void setRotation(float x, float y, float z, float w) { transform.setRotation(x, y, z, w); }	
    
    public void setFov(float fov) 				  { this.fov = fov; }
    public void setAspectRatio(float aspectRatio) { this.aspectRatio = aspectRatio; }
    public void setZNear(float zNear)			  { this.zNear = zNear; }
    public void setZFar(float zFar)				  { this.zFar = zFar; }
    
    public void move(Vector3f movement)				{ transform.move(movement); }	
	public void move(float x, float y, float z) 	{ transform.move(x, y, z); }	
	public void moveForward(float amt)				{ move(getForward().mul(amt)); }
	public void moveBack(float amt)					{ move(getBack().mul(amt)); }
	public void moveUp(float amt)					{ move(getUp().mul(amt)); }
	public void moveDown(float amt)					{ move(getDown().mul(amt)); }
	public void moveLeft(float amt)					{ move(getLeft().mul(amt)); }
	public void moveRight(float amt)				{ move(getRight().mul(amt)); }
	
	public void rotate(float angle, Vector3f axis) { transform.rotate(angle, axis); }	
	public void rotatePitch(float angle)		   { transform.rotate(-angle, getLeft()); }
	public void rotateYaw(float angle)		   	   { transform.rotate(angle, getUp()); }
	public void rotateRoll(float angle)		   	   { transform.rotate(angle, getForward()); }
	
	public Matrix4f updateProjectionMatrix()
    {
    	projectionMatrix.identity();
    	projectionMatrix.perspective((float)Math.toRadians(fov), aspectRatio, zNear, zFar);
    	
    	return projectionMatrix;
    }
	
    public Matrix4f updateViewMatrix()
    {
    	viewMatrix.identity();
    	viewMatrix.rotate(new Quaternionf(transform.getRotation()).conjugate());
    	viewMatrix.translate(new Vector3f(transform.getPosition()).mul(-1));
    	
    	return viewMatrix;
    }      
}