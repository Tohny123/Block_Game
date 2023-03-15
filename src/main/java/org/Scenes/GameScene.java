package org.Scenes;

import org.Compnents.Tile.Tile;
import org.Compnents.Tile.TileType;
import org.Main.Input.KeyListener;
import org.Compnents.Scene;
import org.Main.Window;
import org.Compnents.Renderer.Camera;
import org.Compnents.Renderer.Shader;
import org.Compnents.Renderer.Sprite;
import org.Compnents.Renderer.Texture;
import org.joml.Vector2f;

import java.awt.*;

import static org.lwjgl.glfw.GLFW.*;

public class GameScene extends Scene {
    Sprite test, test1,  test2;
    Sprite drawTile;
    Vector2f pos, posStore, size;
    //maybe make board its own class
    Tile[][] board = new Tile[20][10];
    Tile[][] buffer = new Tile[board.length][board[0].length];
    Texture emptyTex, fullTex;
    public GameScene() {

    }
    public void init() {
        cam = new Camera(new Vector2f());
        pos = new Vector2f(100.0f, 15.0f);
        size = new Vector2f(2.8f, 2.8f);
        emptyTex = new Texture("assets\\textures\\Piece\\block_empty.png");
        fullTex = new Texture("assets\\textures\\Piece\\block_high_res.png");

        test = new Sprite(new Shader("assets\\shaders\\stars.glsl"),
                new Texture("assets\\textures\\Piece\\block_empty.png"), cam, Color.BLACK);
        test1 = new Sprite(new Shader("assets\\shaders\\default.glsl"),
                new Texture("assets\\textures\\test texture.png"), cam, Color.BLACK);
        test2 = new Sprite(new Shader("assets\\shaders\\default.glsl"),
                new Texture("assets\\textures\\Piece\\block_high_res.png"), cam, Color.BLUE);

        drawTile = new Sprite(new Shader("assets\\shaders\\default.glsl"),
                new Texture("assets\\textures\\Piece\\block_empty.png"), cam, Color.BLACK);

        posStore = new Vector2f(pos);
        for (int i = 0; i < 20; i++)
        {
            for (int j = 0; j < 10; j++) {
                board[i][j] = new Tile();
                buffer[i][j] = new Tile();
            }
        }
    }
    @Override
    public void update(float dt) {
        //pos.x += dt * 10;
        //Shader s = new Shader("assets\\shaders\\stars.glsl");
        //s.compileShader();
        debug();
        //test.draw(new Vector2f(0f,0f), new Vector2f((float )Window.width / emptyTex.width, (float) Window.height / emptyTex.width),0.0f);
        test.draw(new Vector2f(0f,0f), new Vector2f(Window.width,Window.height),0.0f);

        board[2][3].setType(TileType.T);
        drawBoard();

    }


    private void drawBoard() {
        posStore = new Vector2f(pos);
        for (Tile[] tiles : board) {
            for (Tile tile : tiles) {
                if (tile.isFilled) {
                    drawTile.color = tile.col;
                    drawTile.texture = fullTex;
                    //System.out.println(drawTile.color);
                    drawTile.draw(posStore, size, 0.0f);
                } else {
                    drawTile.color = Color.WHITE;
                    drawTile.texture = emptyTex;
                    drawTile.draw(posStore, size, 0.0f);
                }

                posStore.x += size.x * 10;
            }
            posStore.x = pos.x;
            posStore.y += size.y * 10;
        }
        posStore = pos;
    }

    private void debug() {
        if (KeyListener.isKeyPressed(GLFW_KEY_Q)) {
            drawTile = test;
        }
        if (KeyListener.isKeyPressed(GLFW_KEY_W)) {
            drawTile = test1;
        }
        if (KeyListener.isKeyPressed(GLFW_KEY_E)) {
            drawTile = test2;
        }
        if(KeyListener.isKeyPressed(GLFW_KEY_P)) {
            Window.changeScene(1);
        }
    }
    public void checkRotation() {

    }
}
