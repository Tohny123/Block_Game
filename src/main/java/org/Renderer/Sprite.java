package org.Renderer;

public class Sprite {
    private float[] vertexArray = {
            //pos                                   //col
            //x   y     z                     r     g    b     a      UV
            100f, 0f, 0.0f,                  1.0f, 0.0f, 0.0f, 1.0f,  0, 1, //bottom right 0        1, 0,
            0f, 100f, 0.0f,                  0.0f, 1.0f, 0.0f, 1.0f,  1, 0, //top left 1            0, 1,
            100f, 100f, 0.0f,                0.0f, 0.0f, 1.0f, 1.0f,  0, 0, //top right 2           1, 1,
            0.0f, 0.0f, 0.0f,                0.0f, 1.0f, 1.0f, 0.5f,  1, 1 //bottom left 3         0, 0,
    };
    //must be counterclockwise
    private int[] elementArray = {
            0, 2, 1, //top right tri
            1, 3, 0 //bottom left tri

    };
    public void init() {

    }
}
