package org.Compnents.Renderer;

import org.Main.Window;
import org.Util.Time;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import java.awt.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Sprite {
    private float[] vertexArray = {
            //pos                                   //col temporary only for shader stuff
            //x   y     z                     r     g    b     a      UV
            10f, 0f, 0.0f,                  1.0f, 0.0f, 0.0f, 1.0f,  1, 1, //bottom right 0        1, 0,
            0f, 10f, 0.0f,                  0.0f, 1.0f, 0.0f, 1.0f,  0, 0, //top left 1            0, 1,
            10f, 10f, 0.0f,                0.0f, 0.0f, 1.0f, 1.0f,  1, 0, //top right 2           1, 1,
            0.0f, 0.0f, 0.0f,                0.0f, 1.0f, 1.0f, 0.5f,  0, 1 //bottom left 3         0, 0,
    };
    //must be counterclockwise
    private int[] elementArray = {
            0, 2, 1, //top right tri
            1, 3, 0 //bottom left tri

    };
    public Texture texture;
    public org.Compnents.Renderer.Shader Shader;
    public Camera cam;
    int vaoID, vboID, eboID;
    public Sprite(Shader s, Texture t, Camera c) {
        //this.cam = new Camera(new Vector2f());
//        defaultShader = new Shader("assets\\shaders\\default.glsl");
//        defaultShader.compileShader();
//        //TODO:VERY TEMPORARY
//        texture = new Texture("assets\\textures\\test texture.png");
        Shader = s;
        //Shader.compileShader();
        texture = t;
        cam = c;


        // make vao, vbo, ebo
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        //make float buffer to verts
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        //create VBO and upload vertex buffer object
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        //create indicies and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        //add vertex attribute pointers
        int posXYSize = 3;
        int colorSize = 4;
        int uvSize = 2;
        int floatSizeBytes = Float.BYTES; //the size of a float is always 4 bytes
        int vertexSizeBytes = (posXYSize + colorSize + uvSize) * floatSizeBytes; // used for stride
        //pos
        glVertexAttribPointer(0, posXYSize, GL_FLOAT, false, vertexSizeBytes,
                0);
        glEnableVertexAttribArray(0);
        //color attribute
        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes,
                posXYSize * floatSizeBytes);
        glEnableVertexAttribArray(1);
        //UV
        glVertexAttribPointer(2, uvSize, GL_FLOAT, false, vertexSizeBytes,
                (posXYSize + colorSize) * floatSizeBytes);
        glEnableVertexAttribArray(2);
    }
    public void draw(float dt, Vector2f pos, Vector2f size, float rotate, Color col) {
        //use shader
        Shader.use();

        Matrix4f model = new Matrix4f();
        model.translate(new Vector3f(pos, 1.0f));
        model.rotate((float)Math.toRadians((double) rotate), 0.0f, 0.0f, 1.0f);
        model.scale(new Vector3f(size, 1.0f));
       // System.out.println(model);

        //texture uploading
        Shader.uploadTexture("TEX_SAMPLER", 0);
        glActiveTexture(0);
        texture.bind();

        //uploading uniforms 
        Shader.uploadMat4f("uProj", cam.getProjectionMatrix());
        Shader.uploadMat4f("uView", cam.getViewMatrix());
        Shader.uploadFloat("fTime", Time.getTime());
        Shader.uploadVec3f("iResolution", new Vector3f(Window.get().width,Window.get().height, 1.0f));
        Shader.uploadMat4f("model", model);
        //color
        float[] colArr = new float[4];
        col.getColorComponents(colArr);
        Shader.uploadVec4f("spriteCol", new Vector4f(colArr));
        //transparency
        Shader.uploadVec3f("maskColor", new Vector3f(1.0f,0.0f,1.0f));

        glBindVertexArray(vaoID);

        //enable vertex attrib pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        //unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
        Shader.detach();

    }
}
