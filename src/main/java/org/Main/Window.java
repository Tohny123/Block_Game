package org.Main;

import org.Scenes.GameScene;
import org.Scenes.MenuScene;
import org.Util.Time;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;
import java.security.Key;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private int width, height;
    private String title;
    private long glfwWindow;
    private static Scene currentScene;
    private static Window window = null;
    private Window() {
        this.width = 720;
        this.height = 720;
        this.title = "TetraBlox";
    }
    public static void changeScene(int newScene) {
        switch (newScene) {
            case 0:
                currentScene = new MenuScene();
                break;
            case 1:
                currentScene = new GameScene();
                break;
            default:
                assert false : "Unknown Scene " + newScene;
                break;
        }
        currentScene.init();
    }
    public static Window get() {
        if (Window.window == null)
            Window.window = new Window();
        return Window.window;
    }

    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        loop();

        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        System.err.println("error init");
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        // Create the window
        glfwWindow = glfwCreateWindow(300, 300, "Hello World!", NULL, NULL);
        if ( glfwWindow == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        //Setup Callbacks
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);
        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
//        glfwSetKeyCallback(glfwWindow, (window, key, scancode, action, mods) -> {
//            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
//                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
//        });

        // Get the thread stack and push a new frame
        try ( MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(glfwWindow, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                    glfwWindow,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically

        // Make the OpenGL context current
        glfwMakeContextCurrent(glfwWindow);
//        // Enable v-sync
//        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(glfwWindow);

        Window.changeScene(0);
    }

    private void loop() {
        // This line is critical for LWJGL's interoperation with GLFW's OpenGL context, or any context that is managed externally. LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL bindings available for use.
        GL.createCapabilities();

        //init time variables
        float beginTime = Time.getTime();
        float endTime =  Time.getTime();
        float deltaTime = -1.0f;
        // Run the rendering loop until the user has attempted to close the window or has pressed the ESCAPE key.
        while ( !glfwWindowShouldClose(glfwWindow) ) {

            // Poll for window events. The key callback above will only be invoked during this call.
            glfwPollEvents();



            //System.out.println(KeyListener.isKeyPressed(GLFW_KEY_SPACE));

            glClearColor(1.0f,1.0f,1.0f,1.0f); // clear the framebuffer
            glClear(GL_COLOR_BUFFER_BIT);
            //glfwSwapBuffers(glfwWindow); // swap the color buffers

            //glClearColor(1.0f,1.0f,1.0f,1.0f);

            if (deltaTime >= 0)
            {
                currentScene.update(deltaTime);
            }

            glfwSwapBuffers(glfwWindow);


            //Calculate DeltaTime
            endTime = Time.getTime();
            deltaTime = endTime - beginTime;
            beginTime = endTime;
        }
    }
}
