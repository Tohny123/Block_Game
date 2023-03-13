package org.Scenes;

import org.Main.Input.KeyListener;
import org.Compnents.Piece;
import org.Compnents.Scene;
import org.Compnents.Renderer.Camera;
import org.Compnents.Renderer.Shader;
import org.Compnents.Renderer.Sprite;
import org.Compnents.Renderer.Texture;
import org.joml.Vector2f;

import java.awt.*;

import static org.lwjgl.glfw.GLFW.*;

public class GameScene extends Scene {
    Sprite test, test1,  test2, test3;
    Vector2f pos, size;
    //maybe make board its own class
    Piece[][] board = new Piece[20][10];
    Piece[][] buffer = new Piece[20][10];
    public GameScene() {

    }
    public void init() {
        cam = new Camera(new Vector2f());
        pos = new Vector2f(10.0f, 1.0f);
        size = new Vector2f(10.0f, 10.0f);

        test = new Sprite(new Shader("assets\\shaders\\default.glsl"), new Texture("assets\\textures\\Piece\\block_empty.png"), cam);
        test1 = new Sprite(new Shader("assets\\shaders\\default.glsl"), new Texture("assets\\textures\\test texture.png"), cam);
        test2 = new Sprite(new Shader("assets\\shaders\\default.glsl"), new Texture("assets\\textures\\Piece\\block_high_res.png"), cam);
        test3 = test2;
    }
    @Override
    public void update(float dt) {
        //pos.x += dt * 10;ddddd
        Shader s = new Shader("assets\\shaders\\stars.glsl");
        //s.compileShader();
        if (KeyListener.isKeyPressed(GLFW_KEY_Q)) {
            test3 = test;
        }
        if (KeyListener.isKeyPressed(GLFW_KEY_W)) {
            test3 = test1;
        }
        if (KeyListener.isKeyPressed(GLFW_KEY_E)) {
            test3 = test2;
        }



      //  System.out.println(pos.x);
        test3.draw(dt, pos, size, 0.0f, Color.MAGENTA);
        //test.draw(dt, new Vector2f(1f, 1f), size, 0.0f, new Color(1,2,3,1));


    }
    public void checkRotation() {

    }
}
