package org.Scenes;

import org.Main.KeyListener;
import org.Main.Piece;
import org.Main.Scene;
import org.Main.Window;
import org.Renderer.Camera;
import org.Renderer.Shader;
import org.Renderer.Sprite;
import org.Renderer.Texture;
import org.joml.Vector2f;
import org.joml.Vector2i;

import java.awt.*;
import java.awt.event.KeyEvent;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glClearColor;

public class GameScene extends Scene {
    Sprite test, test2;
    Vector2f pos, size;
    Piece[][] board = new Piece[20][10];
    public GameScene() {

    }
    public void init() {
        cam = new Camera(new Vector2f());
        pos = new Vector2f(10.0f, 1.0f);
        size = new Vector2f(1.0f, 1.0f);
        test = new Sprite(new Shader("assets\\shaders\\stars.glsl"), new Texture("assets\\textures\\test texture.png"), cam);
        test2 = new Sprite(new Shader("assets\\shaders\\stars.glsl"), new Texture("assets\\textures\\test texture.png"), cam);
    }
    @Override
    public void update(float dt) {
        //pos.x += dt * 10;
        Shader s = new Shader("assets\\shaders\\stars.glsl");
        //s.compileShader();
        if (KeyListener.isKeyPressed(GLFW_KEY_D)) {
            pos.x += dt * 100;
        }
        if (KeyListener.isKeyPressed(GLFW_KEY_A)) {
            pos.x -= dt * 100;
        }
        if (KeyListener.isKeyPressed(GLFW_KEY_W)) {
            pos.y += dt * 100;
        }
        if (KeyListener.isKeyPressed(GLFW_KEY_S)) {
            pos.y -= dt * 100;
        }
        if(KeyListener.isKeyPressed(GLFW_KEY_Q))
       // if(pos.x > 50) test2.Shader = s;
                //new Shader("assets\\shaders\\stars.glsl");
        System.out.println(pos.x);
        test2.draw(dt, pos, size, 45.0f, new Color(1,2,3,1));
        //test.draw(dt, new Vector2f(1f, 1f), size, 0.0f, new Color(1,2,3,1));


    }
    public void checkRotation() {

    }
}
