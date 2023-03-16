package org.Compnents;

import org.Compnents.Tile.TileType;
import org.Scenes.GameScene;

import java.util.ArrayList;
import java.util.List;

public class Piece {
    TileType t;
    int[] bound;
    int oldX, oldY;
    int coordX, coordY;
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

    }
    public boolean moveDown() {
        updateLoc();
        if(!doesCollideX(-1)) {
            coordX -= 1;
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
                GameScene.board[coordX + transformY][coordY].getType() != TileType.EMPTY;
    }
    public void updateLoc() {
        //System.out.println(oldX);
        System.out.println(oldX + " " + oldY);
        GameScene.board[oldX][oldY].setType(TileType.EMPTY);
        GameScene.board[coordX][coordY].setType(t);
        oldX = coordX;
        oldY = coordY;
    }

}
