package org.Scenes;

import org.Compnents.Piece;
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
    Sprite background, test1,  test2;
    Sprite drawTile;
    float ARR = 0.001f;
    float ARRLeft = 0;
    float DAS = 0.2f;
    float DASLeft;
    Vector2f pos, posStore, size;
    public static Tile[][] board = new Tile[30][10]; // X IS VERTICAL DIRECTION, Y IS HORIZONTAL,
    Texture emptyTex, fullTex;
    Piece currentPiece;
    float time = 0.5f;
    float graceTime = 0.01f;
    float graceTimeLeft;
    float timeLeft = time;
    boolean hasRotated = false;
    boolean hasPlaced = false;
    boolean hasMoved = false;

    public GameScene() {

    }
    public void init() {
        cam = new Camera(new Vector2f());
        pos = new Vector2f(350.0f, 19.0f);
        size = new Vector2f(2.8f, 2.8f);
        emptyTex = new Texture("assets\\textures\\Piece\\block_empty.png");
        fullTex = new Texture("assets\\textures\\Piece\\block_high_res.png");
        DASLeft = DAS;
        graceTimeLeft = graceTime;

        background = new Sprite(new Shader("assets\\shaders\\stars.glsl"),
                new Texture("assets\\textures\\Piece\\block_empty.png"), cam, Color.BLACK);
//        test1 = new Sprite(new Shader("assets\\shaders\\stars.glsl"),
//                new Texture("assets\\textures\\background texture.png"), cam, Color.BLACK);
//        test2 = new Sprite(new Shader("assets\\shaders\\default.glsl"),
//                new Texture("assets\\textures\\Piece\\block_high_res.png"), cam, Color.BLUE);


        drawTile = new Sprite(new Shader("assets\\shaders\\default.glsl"),
                new Texture("assets\\textures\\Piece\\block_empty.png"), cam, Color.BLACK);

        posStore = new Vector2f(pos);
        for (int i = 0; i < board.length; i++)
        {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = new Tile();
            }
        }
        currentPiece = new Piece();
    }
    @Override
    public void update(float dt) {

        //debug();
        background.draw(new Vector2f(0f,0f), new Vector2f(Window.width,Window.height),0.0f);


        rotatePiece();

        hardDropPiece();

        movePieceDown(dt);

        movePieceLR(dt);

        checkRow();

        drawBoard();

    }


    private void hardDropPiece() {
        if(KeyListener.isKeyPressed(GLFW_KEY_SPACE)) {
            if(!hasPlaced) {
                currentPiece.hardDrop();
                hasPlaced = true;
                placePiece();
            }
        } else hasPlaced = false;
    }

    private void rotatePiece() {
        if (KeyListener.isKeyPressed(GLFW_KEY_UP)) {
            if(!hasRotated) {
                currentPiece.rotate(false);
                hasRotated = true;
            }
        }  else if (KeyListener.isKeyPressed(GLFW_KEY_C))
        {
            if(!hasRotated) {
                currentPiece.rotate(true);
                hasRotated = true;
            }
        } else hasRotated = false;
    }

    private void movePieceDown(float dt) {
        boolean canMove = true;
        if(timeLeft > 0) timeLeft -= dt;
        //if(KeyListener.isKeyPressed(GLFW_KEY_DOWN)) timeLeft = -1;
        if(timeLeft < 0) {
            canMove = currentPiece.moveDown();
            if(KeyListener.isKeyPressed(GLFW_KEY_DOWN)) timeLeft = time;
            else timeLeft = time;
        }
        if(!canMove && currentPiece.coordX == 20) {
            System.out.println("cleared");
            clearBoard();
        }
        if(!canMove) {
            timeLeft = time;
            if(graceTimeLeft > 0) graceTimeLeft -= dt;
            else {
                graceTimeLeft = graceTime;
                placePiece();
            }
        }
    }

    private void movePieceLR(float dt) {

        if (KeyListener.isKeyPressed(GLFW_KEY_RIGHT)) {
            if(!hasMoved)
            {
                hasMoved = true;
                currentPiece.moveDir(1);
            } else if (DASLeft > 0) DASLeft -= dt;
            else if(ARRLeft > 0) ARRLeft -= dt;
            else {
                ARRLeft = ARR;
                currentPiece.moveDir(1);
            }


        } else if (KeyListener.isKeyPressed(GLFW_KEY_LEFT)) {
            if(!hasMoved)
            {
                hasMoved = true;
                currentPiece.moveDir(-1);
            } else if (DASLeft > 0) DASLeft -= dt;
            else if(ARRLeft > 0) ARRLeft -= dt;
            else {
                ARRLeft = ARR;
                currentPiece.moveDir(-1);
            }
        } else {
            hasMoved = false;
            ARRLeft = 0;
            DASLeft = DAS;
        }
    }

    private void placePiece() {
        currentPiece.placePiece();
        currentPiece = new Piece();
    }

    private void drawBoard() {
        posStore = new Vector2f(pos);

        for (int i = 0; i < 20; i++) {
            for (Tile tile : board[i]) {

                if(tile.hasGhost()) drawTile.color = Color.PINK;

                if (tile.isFilled()) {
                    drawTile.color = tile.col;

                    //if(tile.isActive()) drawTile.color = Color.MAGENTA;

                    drawTile.texture = fullTex;
                    //System.out.println(drawTile.color);
                    drawTile.draw(posStore, size, 0.0f);
                } else {
                    drawTile.color = Color.WHITE;

                    if (tile.hasGhost()) drawTile.color = Color.PINK;

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
    private void checkRow() {
        for (int i = 0; i < board.length; i++) {
            int count = 0;
            for (Tile t : board[i]) {
                if (t.isFilled()) count++;
            }
            if (count >= 10) clearRow(i);
        }
    }
//TODO: CLEAR ROW IS FUCKED
    private void clearRow(int clearedRow) {

        for (int i = clearedRow ; i < board.length - 1; i++) {
            board[i] = board[i + 1].clone();
        }
        for (Tile t : board[board.length -1]) {
            t.setType(TileType.EMPTY);
        }
    }

    private void clearBoard() {
        currentPiece = new Piece();
        board = new Tile[board.length][board[0].length];
        for (int i = 0; i < board.length; i++)
        {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = new Tile();
            }
        }
    }


    private void debug() {
        test1.draw(new Vector2f(2,2), new Vector2f(50,50), ARRLeft * 100);
        if (KeyListener.isKeyPressed(GLFW_KEY_Q)) {
            drawTile = background;
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
        if (KeyListener.isKeyPressed(GLFW_KEY_L))
        {
            board = new Tile[board.length][board[0].length];
            for (int i = 0; i < board.length; i++)
            {
                for (int j = 0; j < board[i].length; j++) {
                    board[i][j] = new Tile();
                }
            }
        }
    }
    public void checkRotation() {

    }
}
