package org.Compnents;

import org.Compnents.Tile.TileType;
import org.Scenes.GameScene;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.List;

public class Piece {
    TileType t;
    public Vector2i JLSZT_offset, I_offset, O_offset;
    int[] bound;
    int rotIndex;
    int oldX, oldY;
    public int coordX, coordY;// coords for center
    int[][] coords = new int[4][2]; // coord displacement
    public Piece() {
        //TODO:check if first tile is obscured
        //TileType[] arr = TileType.values();
        ArrayList<TileType> arr = new ArrayList<TileType>(List.of(TileType.values()));
        arr.remove(0);
        t = arr.get((int)(Math.random() * arr.size()));
        //t = arr[(int)(Math.random() * arr.length)];
        bound = new int[]{1,1};
        coordY = 5;
        coordX = 19;
        oldX = coordX;
        oldY = coordY;
        switch (t) {
            case T -> {
                coords[0] = new int[]{0, 1};
                coords[1] = new int[]{-1, 0};
                coords[2] = new int[]{0, 0};
                coords[3] = new int[]{1, 0};
            }
            case S -> {
                coords[0] = new int[]{1, 1};
                coords[1] = new int[]{0, 1};
                coords[2] = new int[]{0, 0};
                coords[3] = new int[]{-1, 0};
            }
            case Z -> {
                coords[0] = new int[]{-1, 1};
                coords[1] = new int[]{0, 1};
                coords[2] = new int[]{0, 0};
                coords[3] = new int[]{1, 0};
            }
            case L -> {
                coords[0] = new int[]{1, 1};
                coords[1] = new int[]{1, 0};
                coords[2] = new int[]{0, 0};
                coords[3] = new int[]{-1, 0};
            }
            case J -> {
                coords[0] = new int[]{-1, 1};
                coords[1] = new int[]{-1, 0};
                coords[2] = new int[]{0, 0};
                coords[3] = new int[]{1, 0};
            }
            case I -> {
                coords[0] = new int[]{-2, 0};
                coords[1] = new int[]{-1, 0};
                coords[2] = new int[]{1, 0};
                coords[3] = new int[]{2, 0};
            }
            case O -> {
                coords[0] = new int[]{1, 0};
                coords[1] = new int[]{1, 1};
                coords[2] = new int[]{0, 1};
                coords[3] = new int[]{0, 0};
            }
            case default -> {
                break;
            }

        }

    }
    public boolean moveDown() {
        updateLoc();
        if(!doesCollideX(-1)) {
            coordX -= 1;
            return true;
        }
        return false;
    }
    public boolean moveDir(int transform) {
        updateLoc();
        if(!doesCollideY(transform))
        {
            coordY += transform;
            return true;
        }
        return false;
    }

    public boolean doesCollideX(int transformX) {
        return coordX + transformX < 0 ||
                GameScene.board[coordX + transformX][coordY].getType() != TileType.EMPTY;
    }
    public boolean doesCollideY(int transformY) {
        return coordY + transformY < 0 || coordY + transformY >= 10 ||
                GameScene.board[coordX][coordY + transformY].getType() != TileType.EMPTY;
    }
    public void updateLoc() {
        //System.out.println(oldX);
        //System.out.println(oldX + " " + oldY);
        GameScene.board[oldX][oldY].setType(TileType.EMPTY);
        GameScene.board[coordX][coordY].setType(t);
        oldX = coordX;
        oldY = coordY;

        for (int[] arr: coords){
            GameScene.board[10 + arr[1]][5 + arr[0]].setType(t);
        }
        GameScene.board[10][5].setType(TileType.O);

        //GameScene[10][5]
    }

}
