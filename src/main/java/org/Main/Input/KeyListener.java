package org.Main.Input;

import static org.lwjgl.glfw.GLFW.*;

public class KeyListener {
    private static KeyListener keyInst;
    private boolean[] keyPressed = new boolean[GLFW_KEY_LAST];
    private KeyListener()
    {

    }
    public static KeyListener get()
    {
        if (KeyListener.keyInst == null)
            KeyListener.keyInst = new KeyListener();
        return KeyListener.keyInst;
    }
    public static void keyCallback(long window, int key, int scancode, int action, int mods)
    {
        if (action == GLFW_PRESS) {
            get().keyPressed[key] = true;
        }
        else if (action == GLFW_RELEASE)
            get().keyPressed[key] = false;
    }
    public static boolean isKeyPressed(int keycode) {
        //System.out.println("test");
        if (keycode < get().keyPressed.length)
            return get().keyPressed[keycode];
        else
        {
            System.err.println("INVALID KEYCODE: " + keycode);
            return false;
        }
    }
}

