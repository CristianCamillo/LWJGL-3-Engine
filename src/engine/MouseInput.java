package engine;

import static org.lwjgl.glfw.GLFW.glfwSetCursorEnterCallback;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;

import org.joml.Vector2f;

public class MouseInput // done
{	
	private final boolean[] toggle = new boolean[8];	
	private Window window;
	
	private Vector2f position;	
	private boolean inWindow;
	
	public MouseInput()
	{
		position = new Vector2f(0, 0);		
		inWindow = false;
	}
	
	public void init(Window window) throws NullPointerException
	{
		if(window == null)
			throw new NullPointerException("The window received cannot be null!");
		
		this.window = window;
		
		glfwSetCursorPosCallback(window.getId(), (windowHandle, xpos, ypos) ->
        {
            position.x = (float)xpos;
            position.y = (float)ypos;
        });
        glfwSetCursorEnterCallback(window.getId(), (windowHandle, entered) ->
        {
            inWindow = entered;
        });
	}

	public boolean key(int keyCode) { return window.mouse(keyCode); }
	public boolean keyToggle(int keyCode)
	{
		if(key(keyCode))
		{
			if(!toggle[keyCode])
			{
				toggle[keyCode] = true;				
				return true;
			}
		}
		else
			toggle[keyCode] = false;
					
		return false;
	}
	
	public Vector2f getPosition() 	{ return new Vector2f(position); }
	public boolean isInsideWindow() { return inWindow; }
}