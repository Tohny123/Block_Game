package org.Renderer;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera {
    private Matrix4f projectionMatrix, viewMatrix;
    private Vector2f position;
    public Camera (Vector2f pos) {
        this.position = pos;
        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();
        changeProjection();
    }

    public void changeProjection() {
        projectionMatrix.identity();
        projectionMatrix.ortho(0.0f, 32.0f * 40.0f, 0.0f, 32.0f * 21.0f, 0.0f, 100.0f);

    }
    public Matrix4f getViewMatrix() {
        //cam position
        Vector3f front = new Vector3f(0.0f, 0.0f, -1.0f);//front of camera
        Vector3f up = new Vector3f(0.0f, 1.0f, 0.0f);//front of camera
        this.viewMatrix.identity();
        this.viewMatrix = viewMatrix.lookAt(new Vector3f(position.x, position.y, 20.0f),
                front.add(position.x , position.y, 0.0f),
                up);
        return this.viewMatrix;
    }
    public Matrix4f getProjectionMatrix() {
        return this.projectionMatrix;
    }
}
