package org.Compnents.Renderer;

import org.joml.*;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;

public class Shader {
    private int shaderProgram;
    private boolean isUsed = false;

    private String vertexShaderSrc, fragmentShaderSrc;
    private final String filepath;
    public Shader(String fp) {
        this.filepath = fp;
        try {
            //read glsl shader
            String source = new String(Files.readAllBytes(Paths.get(filepath)));
            String[] splitString = source.split("(#type)( )+([a-zA-Z]+)");
            //find first pattern of first #type

            int index = source.indexOf("#type") + 6;
            int endOfLine = source.indexOf("\r\n", index);
            String patternOne = source.substring(index, endOfLine).trim();

            //find second pattern
            index = source.indexOf("#type", endOfLine) + 6;
            endOfLine = source.indexOf("\r\n", index);
            String patternTwo = source.substring(index,endOfLine).trim();
            //find first shader

            if (patternOne.equals("vertex")) {
                vertexShaderSrc = splitString[1];
                //System.out.println(vertexShaderSrc);
            } else if (patternOne.equals("fragment")) {
                fragmentShaderSrc = splitString[1];
            } else {
                throw new IOException("unexpected token ");
            }
            //find second shader
            if (patternTwo.equals("vertex")) {
                vertexShaderSrc = splitString[2];
            } else if (patternTwo.equals("fragment")) {
                fragmentShaderSrc = splitString[2];
            } else {
                throw new IOException("unexpected token");
            }
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
            System.err.println("IO EXCEPTION" + ioe);
        }
    compileShader();

//        System.out.println(vertexShaderSrc);
//        System.out.println(fragmentShaderSrc);
    }
    public void compileShader() {
        int vertexID, fragmentID;
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

    }
    public void use() {
        if (!isUsed)
        {
            glUseProgram(shaderProgram);
            isUsed = true;
        }
    }
    public void detach() {
        glUseProgram(0);
        isUsed = false;
    }
    public void uploadMat4f(String varName, Matrix4f mat4) {
        int varLocation = glGetUniformLocation(shaderProgram, varName);
        use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mat4.get(matBuffer);
        glUniformMatrix4fv(varLocation, false, matBuffer);
    }
    public void uploadMat3f(String varName, Matrix3f mat3) {
        int varLocation = glGetUniformLocation(shaderProgram, varName);
        use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(9);
        mat3.get(matBuffer);
        glUniformMatrix3fv(varLocation, false, matBuffer);
    }
    public void uploadVec4f(String varName, Vector4f v4f) {
        int varLocation = glGetUniformLocation(shaderProgram, varName);
        use();
        glUniform4f(varLocation, v4f.x, v4f.y, v4f.z, v4f.w);
    }
    public void uploadVec3f(String varName, Vector3f v3f) {
        int varLocation = glGetUniformLocation(shaderProgram, varName);
        use();
        glUniform3f(varLocation, v3f.x, v3f.y, v3f.z);
    }
    public void uploadVec2f(String varName, Vector2f v2f) {
        int varLocation = glGetUniformLocation(shaderProgram, varName);
        use();
        glUniform2f(varLocation, v2f.x, v2f.y);
    }
    public void uploadFloat(String varName, Float f) {
        int varLocation = glGetUniformLocation(shaderProgram, varName);
        use();
        glUniform1f(varLocation, f);
    }
    public void uploadInt(String varName, int i) {
        int varLocation = glGetUniformLocation(shaderProgram, varName);
        use();
        glUniform1i(varLocation, i);
    }

    public void uploadTexture(String varName, int slot) {
        //In shader use type sampler2D, not int
        int varLocation = glGetUniformLocation(shaderProgram, varName);
        use();
        glUniform1i(varLocation, slot);
    }
}
