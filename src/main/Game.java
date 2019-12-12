package main;

import static org.lwjgl.glfw.GLFW.*;

import java.io.IOException;

import org.joml.Vector3f;
import org.joml.Vector4f;

import engine.ILogic;
import engine.KeyboardInput;
import engine.MouseInput;
import engine.Window;
import engine.graphics3D.Camera;
import engine.graphics3D.Material;
import engine.graphics3D.Mesh;
import engine.graphics3D.OBJLoader;
import engine.graphics3D.Renderer;
import engine.graphics3D.GraphicsScene;
import engine.graphics3D.SceneLight;
import engine.graphics3D.Texture;
import engine.graphics3D.lights.DirectionalLight;
import engine.graphics3D.lights.PointLight;
import engine.graphics3D.lights.SpotLight;
import engine.graphics3D.objects.GraphicsObject;
import engine.graphics3D.objects.SkyBox;
import engine.graphics3D.weather.Fog;
import engine.physics3D.Box;
import engine.physics3D.PhysicsObject;
import engine.physics3D.PhysicsScene;
import engine.physics3D.Plane;
import engine.physics3D.Sphere;

public class Game implements ILogic
{
	private boolean wantToClose = false;
    
	private final Renderer renderer;
	
	private Camera camera;
	private GraphicsScene scene;	
	
	private GraphicsObject sphere1;
	private GraphicsObject sphere2;
	private GraphicsObject sphere3;
	private GraphicsObject cube;
	private GraphicsObject sword;
	
	
	///////////////////////////////////////////////////////////////////////////////////////////
	
	private PhysicsScene sceneP;
	
	
	private final Vector3f cameraMov = new Vector3f(0, 0, 0);
	private final Vector3f cameraRot = new Vector3f(0, 0, 0);    
	
	private final static Vector3f UP = new Vector3f(0, 1, 0);
	
	public Game()
	{		
		renderer = new Renderer();
		camera = new Camera(70f, 16f / 9f, 0.01f, 1000f);
	}

	@Override
	public void init(Window window) throws Exception
	{
		renderer.init(window);
		
		scene = new GraphicsScene();
		
		camera.setPosition(0, 2, 7);
		
		Mesh cubeMesh = OBJLoader.loadMesh("models/cube.obj");
        Texture grassblockTex = new Texture("textures/grassblock.png");
        Material cubeMaterial = new Material(grassblockTex, 0f);
        cubeMesh.setMaterial(cubeMaterial);
        cube = new GraphicsObject(cubeMesh);
		
        Mesh swordMesh = OBJLoader.loadMesh("models/sword.obj");
        Texture swordTex = new Texture("textures/sword.png");
        Material swordMaterial = new Material(swordTex, 65f);
        swordMesh.setMaterial(swordMaterial);
        sword = new GraphicsObject(swordMesh);
        sword.getTransform().moveBack(3f);
        
        
        Mesh sphereMesh = OBJLoader.loadMesh("models/sphere.obj");
        sphereMesh.setMaterial(new Material(new Vector4f(1f, 1f, 0f, 1f), 50f));
        sphere1 = new GraphicsObject(sphereMesh);
        sphere1.getTransform().setPosition(0, 4f, 0);
        
        
        Mesh sphereMesh2 = OBJLoader.loadMesh("models/sphere.obj");
        sphereMesh2.setMaterial(new Material(new Vector4f(1f, 0f, 0f, 1f), 50f));
        sphere2 = new GraphicsObject(sphereMesh2);
        sphere2.getTransform().setPosition(3, 0, 0);
        
        Mesh sphereMesh3 = OBJLoader.loadMesh("models/sphere.obj");
        sphereMesh3.setMaterial(new Material(new Vector4f(0f, 1f, 1f, 1f), 50f));
        sphere3 = new GraphicsObject(sphereMesh3);
        sphere3.getTransform().setPosition(-3, 5, 0);
        
		scene.setObjects(new GraphicsObject[] {sphere1, cube});
		
		SkyBox skyBox = new SkyBox("models/skybox.obj", "textures/skySkybox.png");
	    skyBox.getTransform().setScale(500f, 500f, 500f);
	    scene.setSkyBox(skyBox);
		
	    setupLights();
	    
	    scene.setFog(new Fog(true, new Vector3f(0.6f, 0.8f, 1f), 0.002f));
	    
	    //////////////////////////////////////////////////////////////////////////////////////////////////////
	    
	    sceneP = new PhysicsScene();
	    sceneP.setGravity(9.81f);
	    
	    PhysicsObject sphereP1 = new PhysicsObject(new Sphere(sphere1.getTransform().getPosition(), sphere1.getTransform().getRotation(), 1), new Vector3f(-5, 0, 0), false);
	//    PhysicsObject boxP1	   = new PhysicsObject(new Box(cube.getTransform().getPosition(), cube.getTransform().getRotation(), new Vector3f(-1, -1, 1), new Vector3f(1, 1, -1)), new Vector3f(), true);
	/*    PhysicsObject sphereP2 = new PhysicsObject(new Sphere(sphere2.getTransform().getPosition(), sphere1.getTransform().getRotation(), 1), new Vector3f(0, 0, 0), true);
	    PhysicsObject sphereP3 = new PhysicsObject(new Sphere(sphere3.getTransform().getPosition(), sphere1.getTransform().getRotation(), 1), new Vector3f(0, 5, 0), false);*/
	    PhysicsObject planeP1  = new PhysicsObject(new Plane(new Vector3f(), new Vector3f(0, 1, 0), 0), new Vector3f(0, 0, 0), true);
	    
	    sceneP.addObject(sphereP1);
	    sceneP.addObject(planeP1);
	 /*   sceneP.addObject(sphereP2);
	    sceneP.addObject(sphereP3);
	    sceneP.addObject(planeP1);*/
	}
	
	private void setupLights()
	{
		SceneLight sceneLight = new SceneLight();
		scene.setSceneLight(sceneLight);
		
		// Ambient Light
		sceneLight.setAmbientLight(new Vector3f(0.3f, 0.3f, 0.3f));
		
		// SkyBox Light
		sceneLight.setSkyBoxLight(new Vector3f(1f, 1f, 1f));
		
		// Directional Light
        sceneLight.setDirectionalLight(new DirectionalLight(new Vector3f(0, 1, 1), new Vector3f(1, 1, 1), 1f));
		
        // Point Light
        PointLight pointLight = new PointLight(new Vector3f(0, 0, 1.5f), new Vector3f(1, 0, 0), 1f);
        PointLight.Attenuation att = new PointLight.Attenuation(0.0f, 0.0f, 1.0f);
        pointLight.setAttenuation(att);
        sceneLight.setPointLightList(new PointLight[] {pointLight});

        // Spot Light
        pointLight = new PointLight(new Vector3f(0, 0, 1.5f), new Vector3f(0, 0, 1), 100f);
        att = new PointLight.Attenuation(0.0f, 0.0f, 1f);
        pointLight.setAttenuation(att);
        Vector3f coneDir = new Vector3f(0, 0, -1);
        SpotLight spotLight = new SpotLight(pointLight, coneDir, (float)Math.PI / 20);
        sceneLight.setSpotLightList(new SpotLight[] {spotLight});
    }
	
	private void resetInputs()
	{
		cameraMov.set(0, 0, 0);
		cameraRot.set(0, 0, 0);
	}
	
	@Override
	public void input(Window window, KeyboardInput keyboardInput, MouseInput mouseInput)
	{
		if(window.shouldClose() || keyboardInput.key(GLFW_KEY_ESCAPE))
		{
			wantToClose = true;
			return;
		}
		
		if(keyboardInput.keyToggle(GLFW_KEY_F1))
			window.setResizable(!window.isResizable());
		
		if(keyboardInput.keyToggle(GLFW_KEY_F2))
			window.setSize(1600, 900, !window.isFullscreen());
		
		if(keyboardInput.keyToggle(GLFW_KEY_F3))
			window.setVSync(!window.vSync());
		
		if(keyboardInput.keyToggle(GLFW_KEY_F4))
			window.setFaceCulling(!window.faceCulling());
		
		if(keyboardInput.keyToggle(GLFW_KEY_F12))
			try { window.screenshot(); } catch(IOException e) { System.err.println("Could not take a screenshot!"); }
	
		
		resetInputs();
				
		
		if(window.key(GLFW_KEY_W))
            cameraMov.z = 1;
        if(window.key(GLFW_KEY_S))
            cameraMov.z = -1;        
        if(window.key(GLFW_KEY_A))
            cameraMov.x = 1;
        if(window.key(GLFW_KEY_D))
            cameraMov.x = -1;        
        if(window.key(GLFW_KEY_E))
            cameraMov.y = 1;
        if(window.key(GLFW_KEY_X))
            cameraMov.y = -1;
        
        if(window.key(GLFW_KEY_UP))
            cameraRot.x = -1;
        if(window.key(GLFW_KEY_DOWN))
            cameraRot.x = 1;
        if(window.key(GLFW_KEY_RIGHT))
            cameraRot.y = -1;
        if(window.key(GLFW_KEY_LEFT))
            cameraRot.y = 1;
        if(window.key(GLFW_KEY_RIGHT_SHIFT))
            cameraRot.z = -1;
        if(window.key(GLFW_KEY_KP_1))
            cameraRot.z = 1;
	}

	@Override
	public void update(float elapsedTime)
	{		
		if(cameraRot.x != 0)
			camera.rotatePitch(cameraRot.x * elapsedTime * 1.5f);
		if(cameraRot.y != 0)
			camera.rotate(cameraRot.y * elapsedTime * 1.5f, UP);
		if(cameraRot.z != 0)
			camera.rotateRoll(cameraRot.z * elapsedTime * 1.5f);
		
		if(cameraMov.z != 0)
			camera.moveForward(cameraMov.z * elapsedTime * 5);
		if(cameraMov.x != 0)
			camera.moveRight(cameraMov.x * elapsedTime * 5);
		if(cameraMov.y != 0)
			camera.moveUp(cameraMov.y * elapsedTime * 5);
		
		/*if(cameraMov.x != 0)
			sphere1.getTransform().moveLeft(cameraMov.x * elapsedTime);
		if(cameraMov.y != 0)
			sphere1.getTransform().moveUp(cameraMov.y * elapsedTime);
		if(cameraMov.z != 0)
			sphere1.getTransform().moveForward(cameraMov.z * elapsedTime);*/
		
		sceneP.simulate(elapsedTime);
		sceneP.handleCollisions();
	}

	@Override
	public void render(Window window, float fps)
	{		
		renderer.render(window, camera, scene);		
		window.setTitle(String.format("%.2f", fps));
	}

	@Override
	public void cleanup()
	{
		renderer.cleanup();
		scene.cleanup();
	}

	@Override
	public boolean wantToClose()
	{		
		return wantToClose;
	}	
}