package org.Scenes;

import org.Main.KeyListener;
import org.Main.Scene;
import org.Main.Window;

import java.awt.event.KeyEvent;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glClearColor;

public class GameScene extends Scene {
    public GameScene() {
        System.out.println("gamescene");
    }

    @Override
    public void update(float dt) {
    }
}
