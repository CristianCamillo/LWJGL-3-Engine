package engine.graphics3D;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import engine.graphics3D.Mesh;
import engine.graphics3D.objects.GraphicsObject;
import engine.graphics3D.objects.SkyBox;
import engine.graphics3D.weather.Fog;

public class GraphicsScene
{
    private Map<Mesh, List<GraphicsObject>> meshMap;    
    private SkyBox skyBox;    
    private SceneLight sceneLight;
    private Fog fog;

    public GraphicsScene()
    {
        meshMap = new HashMap<Mesh, List<GraphicsObject>>();
    }
    
    public Map<Mesh, List<GraphicsObject>> getMeshes() { return meshMap; }
    public SkyBox getSkyBox() 		  		 		   { return skyBox; }
    public SceneLight getSceneLight() 		 		   { return sceneLight; }
    public Fog getFog()						 		   { return fog; }
    
    public void setObjects(GraphicsObject[] objects)
    {
        int numItems = objects != null ? objects.length : 0;
        for(int i=0; i<numItems; i++)
        {
            GraphicsObject object = objects[i];
            Mesh mesh = object.getMesh();
            List<GraphicsObject> list = meshMap.get(mesh);
            if(list == null)
                list = new ArrayList<>();
                meshMap.put(mesh, list);
            
            list.add(object);
        }
    }
    
    public void setSkyBox(SkyBox skyBox) 			 { this.skyBox = skyBox; }
    public void setSceneLight(SceneLight sceneLight) { this.sceneLight = sceneLight; }
    public void setFog(Fog fog)						 { this.fog = fog; }
    
    public void cleanup()
    {
    	for(Mesh mesh : meshMap.keySet())
    		mesh.cleanup();
    }
}