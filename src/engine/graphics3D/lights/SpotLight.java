package engine.graphics3D.lights;

import org.joml.Vector3f;

public class SpotLight
{
    private PointLight pointLight;
    private Vector3f coneDirection;
    private float cutOff;

    public SpotLight(SpotLight spotLight)
    {
        this(new PointLight(spotLight.pointLight), new Vector3f(spotLight.coneDirection), 0);
        cutOff = spotLight.cutOff;
    }
    
    public SpotLight(PointLight pointLight, Vector3f coneDirection, float cutOffAngle)
    {
        this.pointLight = pointLight;
        this.coneDirection = coneDirection;
        setCutOffAngle(cutOffAngle);
    }  
    
    public PointLight getPointLight()  { return pointLight; }
    public Vector3f getConeDirection() { return coneDirection; }
    public float getCutOff()		   { return cutOff; }
    
    public void setPointLight(PointLight pointLight) 	 { this.pointLight = pointLight; }
    public void setConeDirection(Vector3f coneDirection) { this.coneDirection = coneDirection; }    
    public void setCutOff(float cutOff)					 { this.cutOff = cutOff; }
    
    public final void setCutOffAngle(float cutOffAngleRad)
    {
       cutOff = (float)Math.cos(cutOffAngleRad);
    }
}
