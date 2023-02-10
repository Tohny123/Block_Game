package org.Renderer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;

public class Shader {
    private int shaderProgram;

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


        System.out.println(vertexShaderSrc);
        System.out.println(fragmentShaderSrc);
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
        glUseProgram(shaderProgram);
    }
    public void detach() {
        glUseProgram(0);
    }
}
