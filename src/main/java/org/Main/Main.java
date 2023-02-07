package org.Main;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Main {
    //https://www.youtube.com/watch?v=gYhEknnKFJY&ab_channel=GamesWithGabe
    //https://www.youtube.com/watch?v=liJac6RysE4
    //https://www.youtube.com/watch?v=vnqb9vdaxwA
    public static void main(String[] args) {
        Window win = Window.get();
        win.run();
    }
}
