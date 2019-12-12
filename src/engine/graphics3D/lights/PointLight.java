package engine.graphics3D.lights;

import org.joml.Vector3f;

public class PointLight
{
	private Vector3f position;	
    private Vector3f color;
    private float intensity;
    private Attenuation attenuation;
    
    public PointLight(PointLight pointLight)
    {
        this(new Vector3f(pointLight.position),
        	 new Vector3f(pointLight.color),
        	 pointLight.intensity,
        	 new Attenuation(pointLight.getAttenuation().getConstant(),
        			 		 pointLight.getAttenuation().getLinear(),
        			 		 pointLight.getAttenuation().getExponent()));
    }
    
    public PointLight(Vector3f position, Vector3f color, float intensity)
    {
    	this(position, color, intensity, new Attenuation(1, 0, 0));
    }

    public PointLight(Vector3f position, Vector3f color, float intensity, Attenuation attenuation)
    {
    	this.position = position;
        this.color = color;       
        this.intensity = intensity;
        this.attenuation = attenuation;
    }   
    
    public Vector3f getPosition() 		{ return position; }
    public Vector3f getColor()	  		{ return color; }
    public float getIntensity()	  		{ return intensity; }
    public Attenuation getAttenuation() { return attenuation; }
    
    public void setPosition(Vector3f position) 		    { this.position = position; }    
    public void setColor(Vector3f color)				{ this.color = color; }    
    public void setIntensity(float intensity) 			{ this.intensity = intensity; }
    public void setAttenuation(Attenuation attenuation)	{ this.attenuation = attenuation; }

    public static class Attenuation
    {
        private float constant;
        private float linear;
        private float exponent;

        public Attenuation(float constant, float linear, float exponent)
        {
            this.constant = constant;
            this.linear = linear;
            this.exponent = exponent;
        }
        
        public float getConstant() { return constant; }
        public float getLinear()   { return linear; }
        public float getExponent() { return exponent; }
        
        public void setConstant(float constant) { this.constant = constant; }
        public void setLinear(float linear)		{ this.linear = linear; }
        public void setExponent(float exponent) { this.exponent = exponent; }
    }
}