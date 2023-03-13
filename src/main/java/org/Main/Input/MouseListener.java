package org.Main.Input;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseListener {
    //https://www.glfw.org/
    //https://www.youtube.com/watch?v=88oZT7Aum6s&t=1420s
    //https://nicolbolas.github.io/oldtut/Basics/Introduction.html
    private static MouseListener listenerInstance;
    private double scrollX, scrollY;
    private double posX, posY, lastPosX, lastPosY;
    private boolean buttonPressed[] = new boolean[3];
    private boolean isDragging;
    private MouseListener()
    {
        //initialize variables
        this.scrollX = 0;
        this.scrollY = 0;
        this.posX = 0;
        this.posY = 0;
        this.lastPosX = 0;
        this.lastPosY = 0;

    }
    public static MouseListener get()
    {
        if (MouseListener.listenerInstance == null)
            MouseListener.listenerInstance = new MouseListener();
        return MouseListener.listenerInstance;
    }
    public static void mousePosCallback(long window, double xPos, double yPos) {
        get().lastPosX = get().posX;
        get().lastPosY = get().posY;
        get().posX = xPos;
        get().posY = yPos;
        get().isDragging = get().buttonPressed[0] || get().buttonPressed[1] || get().buttonPressed[2];

    }
    public static void mouseButtonCallback(long window, int button, int action, int mods) {
//        if(action == GLFW_PRESS){
//            if (button < get().buttonPressed.length) {
//                get().buttonPressed[button] = true;
//            }
//        } else if (action == GLFW_RELEASE) {
//            if (button < get().buttonPressed.length) {
//                get().buttonPressed[button] = false;
//                get().isDragging = false;
//            }
//        }
        if (button < get().buttonPressed.length) {
            if (action == GLFW_PRESS)
                get().buttonPressed[button] = true;
            else if (action == GLFW_RELEASE) {
                get().buttonPressed[button] = false;
                get().isDragging = false;
            }
        }


    }
    public static void mouseScrollCallback(long window, double xOffset, double yOffset)
    {
        get().scrollX = xOffset;
        get().scrollY = yOffset;
    }
    public static void endFrame()
    {
        get().scrollX = 0;
        get().scrollY = 0;
        get().lastPosX = get().posX;
        get().lastPosY = get().posY;
    }

    public static float getPosX() {
        return (float)get().posX;
    }

    public static double getPosY() {
        return (float)get().posY;
    }

    public float getDeltaX() {
        return (float)(get().lastPosX - get().posX);
    }
    public float getDeltaY() {
        return (float) (get().lastPosY - get().posY);
    }

    public static float getScrollX() {
        return (float)get().scrollX;
    }

    public static float getScrollY() {
        return (float)get().scrollY;
    }
    public static boolean isMouseClicked(int keycode) {
        return get().buttonPressed[keycode];
    }
}
