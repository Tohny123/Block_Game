package org.Scenes;

import org.Compnents.Renderer.Camera;
import org.Compnents.Renderer.Sprite;
import org.Compnents.Scene;
import org.Compnents.Renderer.Shader;
import org.Compnents.Renderer.Texture;
import org.Main.Input.KeyListener;
import org.joml.Vector2f;
import org.Main.Window;

import java.awt.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class MenuScene extends Scene {
    
    private float[] vertexArray = {
        //pos                                   //col
        //x   y     z                     r     g    b     a      UV
        100f, 0f, 0.0f,                  1.0f, 0.0f, 0.0f, 1.0f,  0, 1, //bottom right 0        1, 0,
        0f, 100f, 0.0f,                  0.0f, 1.0f, 0.0f, 1.0f,  1, 0, //top left 1            0, 1,
        100f, 100f, 0.0f,                0.0f, 0.0f, 1.0f, 1.0f,  0, 0, //top right 2           1, 1,
        0.0f, 0.0f, 0.0f,                0.0f, 1.0f, 1.0f, 0.5f,  1, 1 //bottom left 3         0, 0,
    };
    //must be counterclockwise
    private int[] elementArray = {
            0, 2, 1, //top right tri
            1, 3, 0 //bottom left tri

    };
    private int vaoID, vboID, eboID;

    private Shader defaultShader;

    Texture tempTex;


    public MenuScene() {

    }
    @Override
    public void init() {
            this.cam = new Camera(new Vector2f());
//        defaultShader = new Shader("assets\\shaders\\stars.glsl");
//        defaultShader.compileShader();
//        //TODO:VERY TEMPORARY
//        tempTex = new Texture("assets\\textures\\test texture.png");
//
//        // make vao, vbo, ebo
//        vaoID = glGenVertexArrays();
//        glBindVertexArray(vaoID);
//
//        //make float buffer to verts
//        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
//        vertexBuffer.put(vertexArray).flip();
//
//        //create VBO and upload vertex buffer object
//        vboID = glGenBuffers();
//        glBindBuffer(GL_ARRAY_BUFFER, vboID);
//        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
//
//        //create indicies and upload
//        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
//        elementBuffer.put(elementArray).flip();
//
//        eboID = glGenBuffers();
//        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
//        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);
//
//        //add vertex attribute pointers
//        int posXYSize = 3;
//        int colorSize = 4;
//        int uvSize = 2;
//        int floatSizeBytes = Float.BYTES; //the size of a float is always 4 bytes
//        int vertexSizeBytes = (posXYSize + colorSize + uvSize) * floatSizeBytes; // used for stride
//        //pos
//        glVertexAttribPointer(0, posXYSize, GL_FLOAT, false, vertexSizeBytes,
//                0);
//        glEnableVertexAttribArray(0);
//        //color attribute
//        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes,
//                posXYSize * floatSizeBytes);
//        glEnableVertexAttribArray(1);
//        //UV
//        glVertexAttribPointer(2, uvSize, GL_FLOAT, false, vertexSizeBytes,
//                (posXYSize + colorSize) * floatSizeBytes);
//        glEnableVertexAttribArray(2);
    }

    @Override
    public void update(float dt) {
//        //bind shader program, vao
//        defaultShader.use();
//
//        //uploading texture
//        defaultShader.uploadTexture("TEX_SAMPLER", 0);
//        glActiveTexture(0);
//        tempTex.bind();
//
//        //glEnable(GL_ALPHA_TEST);
//        //glAlphaFunc(GL_GREATER, 0.1f);
//
//        defaultShader.uploadMat4f("uProj", cam.getProjectionMatrix());
//        defaultShader.uploadMat4f("uView", cam.getViewMatrix());
//        defaultShader.uploadFloat("fTime", Time.getTime());
//
//
//        defaultShader.uploadVec3f("iResolution", new Vector3f(Window.get().width,Window.get().height, 1.0f));
//
//        glBindVertexArray(vaoID);
//
//        //enable vertex attrib pointers
//        glEnableVertexAttribArray(0);
//        glEnableVertexAttribArray(1);
//
//        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);
//
//        //unbind everything
//        glDisableVertexAttribArray(0);
//        glDisableVertexAttribArray(1);
//        glBindVertexArray(0);
//        defaultShader.detach();
        //Sprite s = new Sprite();
        Sprite drawTile = new Sprite(new Shader("assets\\shaders\\stars.glsl"),
                new Texture("assets\\textures\\Piece\\block_empty.png"), cam, Color.BLACK);
        drawTile.draw(new Vector2f(1,1),new Vector2f(10,10),0.0f);
        if(KeyListener.isKeyPressed(GLFW_KEY_O)) {
            Window.changeScene(0);
        }
    }
}
