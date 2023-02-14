package org.Scenes;

import org.Main.KeyListener;
import org.Main.Scene;
import org.Main.Window;
import org.Renderer.Camera;
import org.Renderer.Shader;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class MenuScene extends Scene {

    private float[] vertexArray = {
        //pos                                   //col
        //x    y     z                        r     g    b     a
        100.5f, 0.5f, 0.0f,                  1.0f, 0.0f, 0.0f, 1.0f, //bottom right 0
        0.5f, 100.5f, 0.0f,                  0.0f, 1.0f, 0.0f, 1.0f, //top left 1
        100.5f, 100.5f, 0.0f,                   0.0f, 0.0f, 1.0f, 1.0f, //top right 2
        0.5f, 0.5f, 0.0f,                 0.0f, 1.0f, 1.0f, 0.5f //bottom left 3
    };
    //must be counterclockwise
    private int[] elementArray = {
            0, 2, 1, //top right tri
            1, 3, 0 //bottom left tri

    };
    private int vaoID, vboID, eboID;

    private Shader defaultShader;
    public MenuScene() {

    }
    @Override
    public void init() {
        this.cam = new Camera(new Vector2f());
        defaultShader = new Shader("assets\\shaders\\default.glsl");
        defaultShader.compileShader();
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
        int posSize = 3;
        int colorSize = 4;
        int floatSizeBytes = 4; //the size of a float is always 4 bytes
        int vertexSizeBytes = (posSize + colorSize) * floatSizeBytes; // used for stride
        glVertexAttribPointer(0, posSize, GL_FLOAT, false, vertexSizeBytes, 0);
        //glEnableVertexAttribArray(0);
        //color attribute
        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, posSize * floatSizeBytes);
        //glEnableVertexAttribArray(1);
    }

    @Override
    public void update(float dt) {
        //bind shader program, vao
        defaultShader.use();
        defaultShader.upload4f("uProj", cam.getProjectionMatrix());
        defaultShader.upload4f("uView", cam.getViewMatrix());

        glBindVertexArray(vaoID);

        //enable vertex attrib pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);



        //unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
        defaultShader.detach();

    }
}
