package engine;

public class KeyboardInput // done
{	
	private final boolean[] toggle = new boolean[512];
	private Window window;
	
	public KeyboardInput() {}
	
	public void init(Window window) throws NullPointerException
	{
		if(window == null)
			throw new NullPointerException("The window received cannot be null!");
		
		this.window = window; 
	}

	public boolean key(int keyCode) { return window.key(keyCode); }
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
}