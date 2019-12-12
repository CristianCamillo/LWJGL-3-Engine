package main;

import engine.CoreEngine;
import engine.Window;

public class Main
{
	public static void main(String[] args) throws Exception
	{				
		Window.Options opts = new Window.Options();
		
		opts.resizable = true;
		opts.fullscreen = false;
		opts.vSync = true;
		opts.faceCulling = true;
		
		Window window = new Window(1600, 900, "[LWJGL3]", opts);
		
		CoreEngine coreEng = new CoreEngine(window, new Game());
		coreEng.start();
	}
}