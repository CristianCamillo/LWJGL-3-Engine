package engine.graphics3D;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import org.lwjgl.system.MemoryUtil;

import engine.graphics3D.objects.GraphicsObject;

public class Mesh
{
    private final int vaoId;
    private final List<Integer> vboIdList;
    private final int vertexCount;

    private Material material;

    public Mesh(float[] positions, float[] textCoords, float[] normals, int[] indices)
    {
    	 FloatBuffer posBuffer = null;
         FloatBuffer textCoordsBuffer = null;
         FloatBuffer vecNormalsBuffer = null;
         IntBuffer indicesBuffer = null;
         
         try
         {
             vertexCount = indices.length;
             vboIdList = new ArrayList<>();

             vaoId = glGenVertexArrays();
             glBindVertexArray(vaoId);

             // Position VBO
             int vboId = glGenBuffers();
             vboIdList.add(vboId);
             posBuffer = MemoryUtil.memAllocFloat(positions.length);
             posBuffer.put(positions).flip();
             glBindBuffer(GL_ARRAY_BUFFER, vboId);
             glBufferData(GL_ARRAY_BUFFER, posBuffer, GL_STATIC_DRAW);
             glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

             // Texture coordinates VBO
             vboId = glGenBuffers();
             vboIdList.add(vboId);
             textCoordsBuffer = MemoryUtil.memAllocFloat(textCoords.length);
             textCoordsBuffer.put(textCoords).flip();
             glBindBuffer(GL_ARRAY_BUFFER, vboId);
             glBufferData(GL_ARRAY_BUFFER, textCoordsBuffer, GL_STATIC_DRAW);
             glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

             // Vertex normals VBO
             vboId = glGenBuffers();
             vboIdList.add(vboId);
             vecNormalsBuffer = MemoryUtil.memAllocFloat(normals.length);
             vecNormalsBuffer.put(normals).flip();
             glBindBuffer(GL_ARRAY_BUFFER, vboId);
             glBufferData(GL_ARRAY_BUFFER, vecNormalsBuffer, GL_STATIC_DRAW);
             glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);

             // Index VBO
             vboId = glGenBuffers();
             vboIdList.add(vboId);
             indicesBuffer = MemoryUtil.memAllocInt(indices.length);
             indicesBuffer.put(indices).flip();
             glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboId);
             glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

             glBindBuffer(GL_ARRAY_BUFFER, 0);
             glBindVertexArray(0);
         }
        finally
        {
            if(posBuffer != null)
                MemoryUtil.memFree(posBuffer);
            
            if(textCoordsBuffer != null)
                MemoryUtil.memFree(textCoordsBuffer);
            
            if(vecNormalsBuffer != null)
                MemoryUtil.memFree(vecNormalsBuffer);
            
            if(indicesBuffer != null)
                MemoryUtil.memFree(indicesBuffer);            
        }
    }

    public int getVaoId() 		  { return vaoId; }
    public int getVertexCount()   { return vertexCount; }
    public Material getMaterial() { return material; }

    public void setMaterial(Material material) { this.material = material; }

    private void initRender()
    {
        Texture texture = material.getTexture();
        if(texture != null)
        {
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, texture.getId());
        }
        
        glBindVertexArray(getVaoId());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
    }

    private void endRender()
    {
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glBindVertexArray(0);

        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public void render()
    {
        initRender();

        glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);

        endRender();
    }

    public void renderList(List<GraphicsObject> objects, Consumer<GraphicsObject> consumer)
    {
        initRender();

        for(GraphicsObject object : objects)
        {
            // Set up data requiered by gameItem
            consumer.accept(object);
            // Render this game object
            glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);
        }

        endRender();
    }
    public void cleanup()
    {
        glDisableVertexAttribArray(0);

        // Delete the VBOs
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        for(int vboId : vboIdList)
            glDeleteBuffers(vboId);        

        // Delete the texture
        Texture texture = material.getTexture();
        if(texture != null)
            texture.cleanup();
        
        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }

    public void deleteBuffers()
    {
        glDisableVertexAttribArray(0);

        // Delete the VBOs
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        for(int vboId : vboIdList)
            glDeleteBuffers(vboId);
        
        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }
}