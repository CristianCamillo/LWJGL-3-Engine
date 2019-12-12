package engine.graphics3D;

import org.joml.Vector4f;

public class Material
{
    private Vector4f ambientColor;
    private Vector4f diffuseColor;
    private Vector4f specularColor;
    private Texture texture;
    private float reflectance;
    
    private final static Vector4f DEFAULT_COLOR = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);

    public Material()
    {
    	this(new Vector4f(DEFAULT_COLOR), new Vector4f(DEFAULT_COLOR), new Vector4f(DEFAULT_COLOR), null, 0f);
    }

    public Material(Vector4f color, float reflectance)
    {
        this(color, color, color, null, reflectance);
    }

    public Material(Texture texture)
    {
        this(new Vector4f(DEFAULT_COLOR), new Vector4f(DEFAULT_COLOR), new Vector4f(DEFAULT_COLOR), texture, 0);
    }

    public Material(Texture texture, float reflectance)
    {
        this(new Vector4f(DEFAULT_COLOR), new Vector4f(DEFAULT_COLOR), new Vector4f(DEFAULT_COLOR), texture, reflectance);
    }

    public Material(Vector4f ambientColor, Vector4f diffuseColor, Vector4f specularColor, Texture texture, float reflectance)
    {
        this.ambientColor = ambientColor;
        this.diffuseColor = diffuseColor;
        this.specularColor = specularColor;
        this.texture = texture;
        setReflectance(reflectance);
    }
        
    public Vector4f getAmbientColor()  { return ambientColor; }
    public Vector4f getDiffuseColor()  { return diffuseColor; }
    public Vector4f getSpecularColor() { return specularColor; }
    public Texture getTexture()		   { return texture; }
    public boolean isTextured()   	   { return texture != null; }
    public float getReflectance() 	   { return reflectance; }
    
    public void setAmbientColor(Vector4f ambientColor)   { this.ambientColor = ambientColor; }
    public void setDiffuseColor(Vector4f diffuseColor)   { this.diffuseColor = diffuseColor; }
    public void setSpecularColor(Vector4f specularColor) { this.specularColor = specularColor; }
    public void setTexture(Texture texture)				 { this.texture = texture; }    
    public void setReflectance(float reflectance)
    {    	
    	if(reflectance < 0f)
			reflectance = 0f;
		if(reflectance > 100f)
			reflectance = 100f;
		
		this.reflectance = reflectance;
    }
}