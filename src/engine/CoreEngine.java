package engine;

import static org.lwjgl.glfw.GLFW.*;

public class CoreEngine
{
	private final Window window;
	private final KeyboardInput keyboardInput;
	private final MouseInput mouseInput;
	private final Timer timer;
	
	private final ILogic logic;
	
	private final static int MAX_FPS = 1000;
	
	public CoreEngine(Window window, ILogic logic) throws IllegalArgumentException
	{
		this.window = window;
		keyboardInput = new KeyboardInput();
		mouseInput = new MouseInput();
		timer = new Timer();
		
		this.logic = logic;
	}
	
	public void start()
    {
		try
		{
			init();
			loop();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			cleanup();
		}
    }
	
	private void init() throws Exception
	{
		window.init();
		keyboardInput.init(window);
		mouseInput.init(window);
		timer.init();
		
		logic.init(window);
	}
	
	private void loop()
	{				
		float fps = 0f;
		long counter = 0;
		long lastUpdate = 0;
		
		float accumulator = 0f;
	    float interval = 1f / 60f;
		
		while(true)
		{		
			float elapsedTime = timer.getElapsedTime();
			accumulator += elapsedTime;
			
			logic.input(window, keyboardInput, mouseInput);
			if(logic.wantToClose())
				break;	
			
			while(accumulator >= interval)
			{
				logic.update(interval);
				accumulator -= interval;
            }
						
			logic.render(window, fps);
			window.update();
			
		//	if(!window.vSync())
		//		cap();
				
			counter++;
			long now = System.nanoTime();
			if(now >= lastUpdate + 500_000_000)
			{		
				fps = counter * 1_000_000_000l / (float)(now - lastUpdate);
				counter = 0;
				
				lastUpdate = now;
			}
		}
	}
	
	private void cleanup()
	{
		logic.cleanup();
		window.cleanup();
		glfwTerminate();
	}
	
	private void cap()
	{
		float frameTime = 1f / MAX_FPS;
		double endTime = timer.getLastLoopTime() + frameTime;
		
		while(timer.getTime() < endTime)
			try { Thread.sleep(1, 0_000); } catch(Exception e) {}
	}
}