package org.Main;

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
        if (MouseListener.listenerInstance == null) MouseListener.listenerInstance = new MouseListener();
        return MouseListener.listenerInstance;
    }
    public static void mousePosCallback(long window, double xpos, double ypos) {
        get().lastPosX = get().posX;
        get().lastPosY = get().posY;
        get().posX = xpos;
        get().posY = ypos;

    }
}
