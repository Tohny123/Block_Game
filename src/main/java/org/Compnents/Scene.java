package org.Compnents;

import org.Compnents.Renderer.Camera;

public abstract class Scene {

    protected Camera cam;
    public Scene() {

    }
    public void init() {

    }

    public abstract void update (float dt);
}
