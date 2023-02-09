package org.Scenes;

import org.Main.KeyListener;
import org.Main.Scene;
import org.Main.Window;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class MenuScene extends Scene {

    private String vertexShaderSrc =
            "#version 330 core \n" +
            "layout (location = 0) in vec3 aPos;\n" +
            "layout (location = 1) in vec4 aColor;\n" +
            "\n" +
            "out vec4 fColor;\n" +
            "\n" +
            "void main() {\n" +
            "    fColor = aColor;\n" +
            "    gl_Position = vec4(aPos, 1.0);\n" +
            "}";
    private String fragmentShaderSrc =
            "#version 330 core \n" +
            "in vec4 fColor;\n" +
            "\n" +
            "out vec4 color;\n" +
            "\n" +
            "void main() {\n" +
            "    color = fColor;\n" +
            "}";
    private int vertexID, fragmentID, shaderProgram;
    private float[] vertexArray = {
        //pos                                   //col
        0.5f, -0.5f, 0.0f,                  0.0f, 1.0f, 0.0f, 1.0f, //bottom right 0
        -0.5f, 0.5f, 0.0f,                  1.0f, 0.0f, 0.0f, 1.0f, //top left 1
        0.5f, 0.5f, 0.0f,                   1.0f, 0.0f, 0.0f, 1.0f, //top right 2
        -0.5f, 0.5f, 0.0f,                  0.0f, 0.0f, 0.0f, 1.0f //bottom left 3
    };
    //must be counterclockwise
    private int[] elementArray = {
            0, 2, 1, //top right tri
            1, 3, 0 //bottom left tri

    };
    private int vaoID, vboID, eboID;
    public MenuScene() {

    }
    @Override
    public void init() {
        //compile and link shaders
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexID, vertexShaderSrc);
        glCompileShader(vertexID);

        //check for problems in compiling
        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.err.println("ERROR: default.glsl Vertex Shader Compilation Failed");
            System.out.println(glGetShaderInfoLog(vertexID, len));
            assert false : "";
        }

        //compile and link fragment shader
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentID, fragmentShaderSrc);
        glCompileShader(fragmentID);

        //check for problems in compiling
        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.err.println("ERROR: default.glsl Fragment Shader Compilation Failed");
            System.out.println(glGetShaderInfoLog(fragmentID, len));
            assert false : "";
        }

        //link shaders and check for errors
        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexID);
        glAttachShader(shaderProgram, fragmentID);
        glLinkProgram(shaderProgram);

        //check for linking errors
        success = glGetProgrami(shaderProgram, GL_LINK_STATUS);
        if (success == GL_FALSE) {
            int len = glGetProgrami(shaderProgram, GL_INFO_LOG_LENGTH);
            System.err.println("LINKING ERROR");
            System.out.println(glGetProgramInfoLog(shaderProgram, len));
            assert false : "";
        }
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
        int vertexSizeBytes = (posSize + colorSize) * floatSizeBytes;
        glVertexAttribPointer(0, posSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);
        //color attribute
        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, colorSize * floatSizeBytes);
        glEnableVertexAttribArray(1);
    }

    @Override
    public void update(float dt) {
        //bind shader program, vao
        glUseProgram(shaderProgram);
        glBindVertexArray(vaoID);

        //enable vertex attrib pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);


        //TODO: only rendering one triangle from ebo i think

        //unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
        glUseProgram(0);

    }
}
