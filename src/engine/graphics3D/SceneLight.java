package engine.graphics3D;

import org.joml.Vector3f;

import engine.graphics3D.lights.DirectionalLight;
import engine.graphics3D.lights.PointLight;
import engine.graphics3D.lights.SpotLight;

public class SceneLight
{
    private Vector3f ambientLight;
    private Vector3f skyBoxLight;
    private DirectionalLight directionalLight;
    private PointLight[] pointLightList;    
    private SpotLight[] spotLightList;    

    public Vector3f getAmbientLight() 			  { return ambientLight; }
    public Vector3f getSkyBoxLight()			  { return skyBoxLight; }
    public DirectionalLight getDirectionalLight() { return directionalLight; }
    public PointLight[] getPointLightList() 	  { return pointLightList; }
    public SpotLight[] getSpotLightList() 		  { return spotLightList; }
    
    public void setAmbientLight(Vector3f ambientLight) 				   { this.ambientLight = ambientLight; }
    public void setSkyBoxLight(Vector3f skyBoxLight)				   { this.skyBoxLight = skyBoxLight; }
    public void setDirectionalLight(DirectionalLight directionalLight) { this.directionalLight = directionalLight; }
    public void setPointLightList(PointLight[] pointLightList) 		   { this.pointLightList = pointLightList; }
    public void setSpotLightList(SpotLight[] spotLightList) 		   { this.spotLightList = spotLightList; }
}