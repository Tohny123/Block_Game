package org.Scenes;

import org.Compnents.Font.GameFont;
import org.Compnents.Piece;
import org.Compnents.Randomizer;
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
import java.io.File;
import java.util.*;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class GameScene extends Scene {
    Vector2f pos, posStore, size;
    Sprite background , uiSprite;
    //Sprite test1,  test2;
    Sprite drawTile;
    float ARR = 0.001f;
    float ARRLeft = 0;
    float DAS = 0.2f;
    float DASLeft;
    public static Tile[][] board = new Tile[30][10]; // X IS VERTICAL DIRECTION, Y IS HORIZONTAL,
    Texture emptyTex, fullTex;
    Piece currentPiece;
    float time = 0.5f;
    float graceTime = 0.9f;
    float graceTimeLeft;
    float timeLeft = time;
    boolean hasRotated = false;
    boolean hasPlaced = false;
    boolean hasMoved = false;
    boolean canMove = true;
    boolean hasHeld = false;


    TileType currentType;
    TileType holdType = TileType.EMPTY;
    TileType[] queue = new TileType[3];

    //GameFont font;

    public GameScene() {

    }
    public void init() {
        cam = new Camera(new Vector2f());
        size = new Vector2f(2.8f, 2.8f);
        pos = new Vector2f((1000 - (2.8f * 100)) / 2, 19.0f);
        emptyTex = new Texture("assets\\textures\\Piece\\block_empty.png");
        fullTex = new Texture("assets\\textures\\Piece\\block_high_res.png");
        DASLeft = DAS;
        graceTimeLeft = graceTime;

        background = new Sprite(new Shader("assets\\shaders\\stars.glsl"),
                new Texture("assets\\textures\\Piece\\uiBlock.png"), cam, Color.BLACK);
        uiSprite = new Sprite(new Shader("assets\\shaders\\default.glsl"),
                new Texture("assets\\textures\\Piece\\uiBlock.png"), cam, Color.white);
//        test1 = new Sprite(new Shader("assets\\shaders\\stars.glsl"),
//                new Texture("assets\\textures\\background texture.png"), cam, Color.BLACK);
     //   test2 = new Sprite(new Shader("assets\\shaders\\stars.glsl"),
        //        new Texture("assets\\textures\\test texture.png"), cam, Color.BLUE);


//        drawTile = new Sprite(new Shader("assets\\shaders\\default.glsl"),
//                new Texture("assets\\textures\\Piece\\block_empty.png"), cam, Color.BLACK);
//
//        try {
//            Font f = Font.createFont(Font.TRUETYPE_FONT, new File("assets\\fonts\\Barlow-Bold.ttf"));
//            font = new GameFont(f, true, new Shader("assets\\shaders\\default.glsl"));
//        }
//        catch (Exception e) {
//            System.err.println(e);
//        }


        posStore = new Vector2f(pos);
        for (int i = 0; i < board.length; i++)
        {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = new Tile();
            }
        }

        for (int i = 0; i < queue.length; i++) {
            queue[i] = Randomizer.bagRandomizer();
        }
        currentType = randomizePiece();
        currentPiece = new Piece(currentType);
    }
    @Override
    public void update(float dt) {

        //debug();
        background.draw(new Vector2f(0f,0f), new Vector2f(Window.width,Window.height),0.0f);

        debug();

        rotatePiece();

        hardDropPiece();

        //if (KeyListener.isKeyPressed(GLFW_KEY_J))

        movePieceDown(dt);

        movePieceLR(dt);

       // System.out.println(graceTimeLeft);


        drawBoard();

        holdLogic();

        uiSprite.draw(new Vector2f(200,430), new Vector2f(15,15), 0.0f);
        uiSprite.draw(new Vector2f(290 + pos.x ,430 - (150 * 2)), new Vector2f(15,15 * 3 ), 0.0f);

        drawPiece(new Vector2f(260,480), holdType);


        drawPiece(new Vector2f(350 + pos.x ,480), queue[0]);
        drawPiece(new Vector2f(350 + pos.x ,480 - 150), queue[1]);
        drawPiece(new Vector2f(350 + pos.x ,480 - 300), queue[2]);


        //font.drawText("ZZZ", 1, 1, Color.BLACK, new Vector2f(10 , 10));


    }

    private void holdLogic() {
        if(KeyListener.isKeyPressed(GLFW_KEY_C) && !hasHeld) {
            if(holdType == TileType.EMPTY) {
                holdType = currentType;
                currentPiece.deletePiece();
                currentPiece = new Piece(randomizePiece());
            } else {
                TileType temp = currentType;
                currentPiece.deletePiece();
                currentPiece = new Piece(holdType);
                currentType = holdType;
                holdType = temp;
            }
            hasHeld = true;
        }
    }


    private void rotatePiece() {
        if (KeyListener.isKeyPressed(GLFW_KEY_UP)) {
            if(!hasRotated) {
                currentPiece.rotate(false);
                hasRotated = true;
            }
        }  else if (KeyListener.isKeyPressed(GLFW_KEY_X))
        {
            if(!hasRotated) {
                currentPiece.rotate(true);
                hasRotated = true;
            }
        } else hasRotated = false;
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


    private void movePieceDown(float dt) {
        if(timeLeft > 0) timeLeft -= dt;
        if(KeyListener.isKeyPressed(GLFW_KEY_DOWN)) timeLeft = -1;
        if(timeLeft < 0) {
            canMove = currentPiece.moveDown();
            if(KeyListener.isKeyPressed(GLFW_KEY_DOWN)) timeLeft = time/5;
            else timeLeft = time;
            //System.out.println(canMove);
        }
        if(!canMove) {
            timeLeft = time;
            if(!currentPiece.doesCollideX(-1)) {
                canMove = true;
                graceTimeLeft = graceTime;
                return;
            }
            if(graceTimeLeft > 0) graceTimeLeft -= dt;
            else {
                graceTimeLeft = graceTime;
                placePiece();
            }
        }
    }
    public void checkLose() {
        if (currentPiece.coordX >= 19)
            clearBoard();
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
        checkLose();
        canMove = true;
        graceTimeLeft = graceTime;
        hasHeld = false;
        checkRows(currentPiece.placePiece());
        currentPiece = new Piece(randomizePiece());
    }

    private void drawBoard() {
        posStore = new Vector2f(pos);

        for (int i = 0; i < 20; i++) {
            for (Tile tile : board[i]) {

               // if(tile.hasGhost()) drawTile.color = Color.PINK;
                if (tile.isFilled()) {
                    drawTile.color = tile.col;

                    //if(tile.isActive()) drawTile.color = Color.MAGENTA;

                    drawTile.texture = fullTex;
                    //System.out.println(drawTile.color);
                    drawTile.draw(posStore, size, 0.0f);
                } else {
                    drawTile.color = Color.WHITE;

                    if (tile.hasGhost()) drawTile.color = Color.GRAY;

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
    public void drawPiece(Vector2f pos, TileType t) {
        float centerOI = 0;
        if(t == TileType.I)  {
            centerOI = (size.x * 10) / 2;
        }
        if(t == TileType.O) {
            centerOI = -(size.x * 10) / 2;
        }
        if (t == TileType.EMPTY) return;
        int[][] dispCoord = new int[4][2];
        float move = size.x * 10;
        Color c = null;
        switch (t) {
            case T -> {
                dispCoord[0] = new int[]{0, 1};
                dispCoord[1] = new int[]{-1, 0};
                dispCoord[2] = new int[]{0, 0};
                dispCoord[3] = new int[]{1, 0};
                c = new Color(160, 0, 255);
            }
            case S -> {
                dispCoord[0] = new int[]{1, 1};
                dispCoord[1] = new int[]{0, 1};
                dispCoord[2] = new int[]{0, 0};
                dispCoord[3] = new int[]{-1, 0};
                c = Color.red;
            }
            case Z -> {
                dispCoord[0] = new int[]{-1, 1};
                dispCoord[1] = new int[]{0, 1};
                dispCoord[2] = new int[]{0, 0};
                dispCoord[3] = new int[]{1, 0};
                c = Color.green;
            }
            case L -> {
                dispCoord[0] = new int[]{1, 1};
                dispCoord[1] = new int[]{1, 0};
                dispCoord[2] = new int[]{0, 0};
                dispCoord[3] = new int[]{-1, 0};
                c = new Color(255, 100, 0);
            }
            case J -> {
                dispCoord[0] = new int[]{-1, 1};
                dispCoord[1] = new int[]{-1, 0};
                dispCoord[2] = new int[]{0, 0};
                dispCoord[3] = new int[]{1, 0};
                c = Color.blue;
            }
            case I -> {
                dispCoord[0] = new int[]{1, 0};
                dispCoord[1] = new int[]{0, 0};
                dispCoord[2] = new int[]{-1, 0};
                dispCoord[3] = new int[]{-2, 0};
                c = Color.CYAN;
            }
            case O -> {
                dispCoord[0] = new int[]{1, 0};
                dispCoord[1] = new int[]{1, 1};
                dispCoord[2] = new int[]{0, 1};
                dispCoord[3] = new int[]{0, 0};
                c = Color.YELLOW;
            }
            case default -> {

            }
        }
        drawTile.color = c;
        drawTile.texture = fullTex;
        for (int[] disp : dispCoord) {
            drawTile.draw(new Vector2f(pos.x + (move * disp[0]) + centerOI, pos.y + (move * disp[1])),
                    size, 0.0f);
        }
    }

    private void checkRows(int[] placedRows) {
        int max = placedRows[1];
        for (int i = placedRows[0]; i <= max; i++) {
            if(checkRow(i)) {
                i--;
                max--;
            }
        }
    }
    private boolean checkRow(int row) {
        int count = 0;
        for (Tile t : board[row]) {
            if(t.isFilled()) count++;
        }
        if (count >= 10) {
            clearRow(row);
            return true;
        }
        return false;
    }
    private void clearRow(int clearedRow) {
        for (int i = clearedRow ; i < board.length - 1; i++) {
            board[i] = board[i + 1].clone();
        }
        clearBuffer();
    }


    private void clearBuffer() {
        for (int i = 20; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = new Tile();
            }
        }
    }

    private void clearBoard() {
        currentPiece = new Piece(randomizePiece());
        board = new Tile[board.length][board[0].length];
        for (int i = 0; i < board.length; i++)
        {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = new Tile();
            }
        }
        holdType = TileType.EMPTY;
    }
    public TileType randomizePiece() {
        TileType t = queue[0];
        currentType = t;
        for (int i = 1; i < queue.length; i++) {
            queue[i - 1] = queue[i];
        }
        queue[2] = Randomizer.bagRandomizer();
        return t;
    }


    private void debug() {
//        test1.draw(new Vector2f(2,2), new Vector2f(50,50), ARRLeft * 100);
//        if (KeyListener.isKeyPressed(GLFW_KEY_Q)) {
//            drawTile = background;
//        }
//        if (KeyListener.isKeyPressed(GLFW_KEY_W)) {
//            drawTile = test1;
//        }
//        if (KeyListener.isKeyPressed(GLFW_KEY_E)) {
//            drawTile = test2;
//        }
//        if(KeyListener.isKeyPressed(GLFW_KEY_P)) {
//            Window.changeScene(1);
//        }
        if (KeyListener.isKeyPressed(GLFW_KEY_L))
        {
            clearBoard();
        }
    }

}
