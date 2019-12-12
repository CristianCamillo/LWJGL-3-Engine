package engine.graphics3D;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import static org.lwjgl.opengl.GL11.*;

import java.util.List;
import java.util.Map;

import engine.graphics3D.lights.DirectionalLight;
import engine.graphics3D.lights.PointLight;
import engine.graphics3D.lights.SpotLight;
import engine.graphics3D.objects.GraphicsObject;
import engine.graphics3D.objects.SkyBox;
import engine.Utils;
import engine.Window;
import engine.graphics3D.Camera;
import engine.graphics3D.Mesh;
import engine.graphics3D.ShaderProgram;

public class Renderer
{	
    private ShaderProgram sceneShaderProgram;
    private ShaderProgram skyBoxShaderProgram;
    
    private final static int MAX_POINT_LIGHTS = 5;
	private final static int MAX_SPOT_LIGHTS = 5;
    private final static float SPECULAR_POWER = 10f;

    public void init(Window window) throws Exception
    {
    	setupSceneShader();
    	setupSkyBoxShader();
    }
    
    private void setupSceneShader() throws Exception
    {
        sceneShaderProgram = new ShaderProgram();
        sceneShaderProgram.createVertexShader(Utils.loadResource("shaders/scene_vertex.vs"));
        sceneShaderProgram.createFragmentShader(Utils.loadResource("shaders/scene_fragment.fs"));
        sceneShaderProgram.link();        
       
        sceneShaderProgram.createUniform("projectionMatrix");
        sceneShaderProgram.createUniform("modelViewMatrix");
        sceneShaderProgram.createUniform("texture_sampler");
        
        sceneShaderProgram.createMaterialUniform("material");
        
        sceneShaderProgram.createUniform("specularPower");
        sceneShaderProgram.createUniform("ambientLight");
        sceneShaderProgram.createPointLightListUniform("pointLights", MAX_POINT_LIGHTS);
        sceneShaderProgram.createSpotLightListUniform("spotLights", MAX_SPOT_LIGHTS);
        sceneShaderProgram.createDirectionalLightUniform("directionalLight");
        sceneShaderProgram.createFogUniform("fog");
    }
    
    private void setupSkyBoxShader() throws Exception
    {
        skyBoxShaderProgram = new ShaderProgram();
        skyBoxShaderProgram.createVertexShader(Utils.loadResource("shaders/sb_vertex.vs"));
        skyBoxShaderProgram.createFragmentShader(Utils.loadResource("shaders/sb_fragment.fs"));
        skyBoxShaderProgram.link();

        skyBoxShaderProgram.createUniform("projectionMatrix");
        skyBoxShaderProgram.createUniform("modelViewMatrix");
        skyBoxShaderProgram.createUniform("texture_sampler");
        skyBoxShaderProgram.createUniform("skyBoxLight");
    }

    private void clear()
    {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void render(Window window, Camera camera, GraphicsScene scene)
    {
        clear();
        
        camera.setAspectRatio(window.getWidth() * 1f / window.getHeight());
        camera.updateProjectionMatrix();
        camera.updateViewMatrix();
        
        renderScene(window, camera, scene);
        
        if(scene.getSkyBox() != null)
        	renderSkyBox(window, camera, scene);
    }
    
    public void renderScene(Window window, Camera camera, GraphicsScene scene)
    {
        sceneShaderProgram.bind();

        Matrix4f projectionMatrix = camera.getProjectionMatrix();
        Matrix4f viewMatrix = camera.getViewMatrix();
        SceneLight sceneLight = scene.getSceneLight();
        
        prepareLights(viewMatrix, sceneLight);

        sceneShaderProgram.setUniform("projectionMatrix", projectionMatrix);
        sceneShaderProgram.setUniform("texture_sampler", 0);
        sceneShaderProgram.setUniform("fog", scene.getFog());
        
        Map<Mesh, List<GraphicsObject>> mapMeshes = scene.getMeshes();
        for(Mesh mesh : mapMeshes.keySet())
        {
            sceneShaderProgram.setUniform("material", mesh.getMaterial());
            mesh.renderList(mapMeshes.get(mesh), (GraphicsObject object) ->
            {
                Matrix4f modelViewMatrix = new Matrix4f(viewMatrix).mul(object.getTransform().getMatrix());
                sceneShaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
            });
        }

        sceneShaderProgram.unbind();
    }
    
    private void prepareLights(Matrix4f viewMatrix, SceneLight sceneLight)
    {
    	sceneShaderProgram.setUniform("ambientLight", sceneLight.getAmbientLight());
        sceneShaderProgram.setUniform("specularPower", SPECULAR_POWER);

        // Process Point Lights
        PointLight[] pointLightList = sceneLight.getPointLightList();
        int numLights = pointLightList != null ? pointLightList.length : 0;
        for(int i = 0; i < numLights; i++)
        {
            // Get a copy of the point light object and transform its position to view coordinates
            PointLight currPointLight = new PointLight(pointLightList[i]);
            Vector3f lightPos = currPointLight.getPosition();
            Vector4f aux = new Vector4f(lightPos, 1);
            aux.mul(viewMatrix);
            lightPos.x = aux.x;
            lightPos.y = aux.y;
            lightPos.z = aux.z;
            sceneShaderProgram.setUniform("pointLights", currPointLight, i);
        }

        // Process Spot Ligths
        SpotLight[] spotLightList = sceneLight.getSpotLightList();
        numLights = spotLightList != null ? spotLightList.length : 0;
        for(int i = 0; i < numLights; i++)
        {
            // Get a copy of the spot light object and transform its position and cone direction to view coordinates
            SpotLight currSpotLight = new SpotLight(spotLightList[i]);
            Vector4f dir = new Vector4f(currSpotLight.getConeDirection(), 0);
            dir.mul(viewMatrix);
            currSpotLight.setConeDirection(new Vector3f(dir.x, dir.y, dir.z));

            Vector3f lightPos = currSpotLight.getPointLight().getPosition();
            Vector4f aux = new Vector4f(lightPos, 1);
            aux.mul(viewMatrix);
            lightPos.x = aux.x;
            lightPos.y = aux.y;
            lightPos.z = aux.z;

            sceneShaderProgram.setUniform("spotLights", currSpotLight, i);
        }

        // Get a copy of the directional light object and transform its position to view coordinates
        DirectionalLight currDirLight = new DirectionalLight(sceneLight.getDirectionalLight());
        Vector4f dir = new Vector4f(currDirLight.getDirection(), 0);
        dir.mul(viewMatrix);
        currDirLight.setDirection(new Vector3f(dir.x, dir.y, dir.z));
        sceneShaderProgram.setUniform("directionalLight", currDirLight);
    }    
    
    private void renderSkyBox(Window window, Camera camera, GraphicsScene scene)
    {
        skyBoxShaderProgram.bind();

        skyBoxShaderProgram.setUniform("texture_sampler", 0);

        Matrix4f projectionMatrix = camera.getProjectionMatrix();
        skyBoxShaderProgram.setUniform("projectionMatrix", projectionMatrix);
        SkyBox skyBox = scene.getSkyBox();
        Matrix4f viewMatrix = camera.getViewMatrix();
        viewMatrix.m30(0);
        viewMatrix.m31(0);
        viewMatrix.m32(0);
        Matrix4f modelViewMatrix = new Matrix4f(viewMatrix).mul(skyBox.getTransform().getMatrix());
        skyBoxShaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
        skyBoxShaderProgram.setUniform("skyBoxLight", scene.getSceneLight().getSkyBoxLight());

        scene.getSkyBox().getMesh().render();

        skyBoxShaderProgram.unbind();
    }

    public void cleanup()
    {
        if(sceneShaderProgram != null)
            sceneShaderProgram.cleanup();
        
        if(skyBoxShaderProgram != null)
        	skyBoxShaderProgram.cleanup();
    }
}