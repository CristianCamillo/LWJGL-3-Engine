package engine.graphics3D.objects;

import engine.graphics3D.Material;
import engine.graphics3D.Mesh;
import engine.graphics3D.OBJLoader;
import engine.graphics3D.Texture;

public class SkyBox extends GraphicsObject
{
    public SkyBox(String objModel, String textureFile) throws Exception
    {
        super();
        Mesh skyBoxMesh = OBJLoader.loadMesh(objModel);
        Texture skyBoxtexture = new Texture(textureFile);
        skyBoxMesh.setMaterial(new Material(skyBoxtexture, 0.0f));
        mesh = skyBoxMesh;
        transform.setPosition(0, 0, 0);
    }
}