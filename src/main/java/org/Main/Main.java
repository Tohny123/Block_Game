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
    //https://www.shadertoy.com/view/4dSBDt could be bg shader
    //https://www.shadertoy.com/view/NslGRN less laggy
    //https://www.shadertoy.com/view/XlfGRj
    //https://www.shadertoy.com/view/DtjSWh
    //https://www.shadertoy.com/view/4ttSWf
    //https://www.shadertoy.com/view/DllSWB
    //https://www.shadertoy.com/view/MtdBDS
    //Thank you Games with gabe for basic architecture
    public static void main(String[] args) {
        Window win = Window.get();
        win.run();
    }
}
