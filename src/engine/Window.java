package engine;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

public class Window
{
	private int width;
	private int height;
	private String title;
	
	private Options opts;
	
	private long id;
	
	public Window(int width, int height, String title, Options opts) throws IllegalArgumentException
	{		
		if(opts == null)
			throw new NullPointerException("The options container cannot be null!");
		
		if(!checkWidthHeight(width, height))
			throw new IllegalArgumentException("Illegal screen resolution! (" + width + "x" + height + ")");
		
		this.width = width;
		this.height = height;
		this.title = title != null ? title : "";
		
		this.opts = opts;
	}
	
	public void init() throws Exception
	{
		// setup the error callback
		GLFWErrorCallback.createPrint(System.err).set();
		
		
		// initialize glfw	
		if(!glfwInit())
			throw new IllegalStateException("GLFW could not be initialized!");

		
		// set OpenGL version to use
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
	    glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 6);
	    

	    // allow to use updated opengl versions if available
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
		
        
		// create the window
		if((id = glfwCreateWindow(width, height, title, 0, 0)) == 0)
			throw new RuntimeException("Could not create a new window!");
		
		
		setSize(width, height, opts.fullscreen);
		setResizable(opts.resizable);

		
		// add callbacks
		addCallbacks();
		
		
		// show the window		
		glfwMakeContextCurrent(id);
		glfwShowWindow(id);
		
		
		// create capabilities
		GL.createCapabilities();
		
		
		setVSync(opts.vSync);
		setFaceCulling(opts.faceCulling);
	//	glPolygonMode( GL_FRONT_AND_BACK, GL_LINE ); TODO: implement
		
		// enables depth buffer
		glEnable(GL_DEPTH_TEST);
		
		// enables stencil buffer
		glEnable(GL_STENCIL_TEST);
	}
	
	public boolean shouldClose()
	{
		return glfwWindowShouldClose(id);
	}
	
	private void addCallbacks()
	{		
		glfwSetFramebufferSizeCallback(id, (window, width, height) ->
		{
		    this.width = width;
		    this.height = height;
		    
		    glViewport(0, 0, this.width, this.height);
		});
	}
	
	public void update()
    {
		// execute poll events
		glfwPollEvents();
		 
		// swap buffers (draw)
        glfwSwapBuffers(id);       
    }
	
	public void cleanup()
	{
		glfwDestroyWindow(id);
	}
	
	public long getId()			  { return id; }
	public int getWidth() 		  { return width; }
	public int getHeight() 		  { return height; }
	public String getTitle() 	  { return title; }
	public boolean isResizable()  { return opts.resizable; }
	public boolean isFullscreen() { return opts.fullscreen; }
	public boolean vSync() 		  { return opts.vSync; }
	public boolean faceCulling()  { return opts.faceCulling; }
	
	public boolean key(int keyCode)   { return glfwGetKey(id, keyCode) == GLFW_PRESS; }
	public boolean mouse(int keyCode) { return glfwGetMouseButton(id, keyCode) == GLFW_PRESS; }
	
	public void setClearColor(float r, float g, float b, float a) { glClearColor(r, g, b, a); }
	
	public void setSize(int width, int height, boolean fullscreen)
	{
		if(!checkWidthHeight(width, height))
			return;
		
		opts.fullscreen = fullscreen;
		
		int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
		int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
		
		if(fullscreen)
		{						
			width = screenWidth;
			height = screenHeight;
		}
		else
			glfwSetWindowPos(id, (screenWidth - width) / 2,	(screenHeight - height) / 2);

		glfwSetWindowAttrib(id, GLFW_DECORATED, fullscreen ? 0 : 1);
		glfwSetWindowSize(id, width, height);
		
		this.width = width;
		this.height = height;
	}
	
	public void setTitle(String title)
	{
		this.title = title != null ? title : "";
		glfwSetWindowTitle(id, this.title);
	}	
	
	public void setResizable(boolean resizable)
	{
		opts.resizable = resizable;
		glfwSetWindowAttrib(id, GLFW_RESIZABLE, resizable ? 1 : 0);
	}
	
	public void setVSync(boolean vSync)
	{
		opts.vSync = vSync;
		glfwSwapInterval(vSync ? 1 : 0);
	}
	
	public void setFaceCulling(boolean faceCulling)
	{
		opts.faceCulling = faceCulling;
		if(faceCulling)
			glEnable(GL_CULL_FACE);
		else
			glDisable(GL_CULL_FACE);
	}
	
	public void screenshot() throws IOException
	{		
		int size = 3 * width * height;
        short[] pixels = new short[size];
        glReadPixels(0, 0, width, height, GL_RGB, GL_UNSIGNED_SHORT, pixels);
        
        BufferedImage screenshot = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		byte[] components = ((DataBufferByte)screenshot.getRaster().getDataBuffer()).getData();
        
		int i = 0;
		int width3 = width * 3;
		
		for(int y = height - 1; y >= 0; y--)
			for(int x = 0; x < width3; x+=3)
			{
				components[i]   = (byte)pixels[x + y * width3 + 2];
				components[i+1] = (byte)pixels[x + y * width3 + 1];
				components[i+2] = (byte)pixels[x + y * width3];
				i+=3;
			}
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy - HH-mm-ss");
		
		File outputImage = new File("Screenshot " + formatter.format(new Date()) + ".png");
		ImageIO.write(screenshot, "png", outputImage);
		screenshot.flush();        
	}
	
	private boolean checkWidthHeight(int width, int height)
	{
		int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
		int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
		
		if(width < 1 || height < 1 || width > screenWidth || height > screenHeight)
			return false;
		
		return true;
	}
	
	public static class Options
	{
		public boolean resizable = false;
		public boolean fullscreen = false;
		public boolean vSync = true;
		public boolean faceCulling = false;
	}
}