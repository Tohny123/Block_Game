package org.Compnents;

import org.Compnents.Tile.Tile;
import org.Compnents.Tile.TileType;
import org.Scenes.GameScene;
import org.joml.Vector2i;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Piece {
    TileType currentType;
    public Vector2i JLSZT_offset, I_offset, O_offset;
    int[] bound;
    int rotIndex;
    int oldX, oldY;
    public int coordX, coordY;// dispCoord for center
    int[][] dispCoord = new int[4][2]; // coord displacement x horizontal y vertical
    int[][] oldDisp;
    int ghostX;
    int oldGhostX;

    public Piece() {
        //TODO:check if first tile is obscured
        ArrayList<TileType> arr = new ArrayList<TileType>(List.of(TileType.values()));
        arr.remove(0);
        currentType = arr.get((int)(Math.random() * arr.size()));

        //currentType = TileType.T;

        bound = new int[]{1,1};
        coordY = 5;
        coordX = 20;
        oldX = coordX;
        oldY = coordY;
        switch (currentType) {
            case T -> {
                dispCoord[0] = new int[]{0, 1};
                dispCoord[1] = new int[]{-1, 0};
                dispCoord[2] = new int[]{0, 0};
                dispCoord[3] = new int[]{1, 0};
            }
            case S -> {
                dispCoord[0] = new int[]{1, 1};
                dispCoord[1] = new int[]{0, 1};
                dispCoord[2] = new int[]{0, 0};
                dispCoord[3] = new int[]{-1, 0};
            }
            case Z -> {
                dispCoord[0] = new int[]{-1, 1};
                dispCoord[1] = new int[]{0, 1};
                dispCoord[2] = new int[]{0, 0};
                dispCoord[3] = new int[]{1, 0};
            }
            case L -> {
                dispCoord[0] = new int[]{1, 1};
                dispCoord[1] = new int[]{1, 0};
                dispCoord[2] = new int[]{0, 0};
                dispCoord[3] = new int[]{-1, 0};
            }
            case J -> {
                dispCoord[0] = new int[]{-1, 1};
                dispCoord[1] = new int[]{-1, 0};
                dispCoord[2] = new int[]{0, 0};
                dispCoord[3] = new int[]{1, 0};
            }
            case I -> {
                dispCoord[0] = new int[]{-2, 0};
                dispCoord[1] = new int[]{-1, 0};
                dispCoord[2] = new int[]{0, 0};
                dispCoord[3] = new int[]{1, 0};
            }
            case O -> {
                dispCoord[0] = new int[]{1, 0};
                dispCoord[1] = new int[]{1, 1};
                dispCoord[2] = new int[]{0, 1};
                dispCoord[3] = new int[]{0, 0};
            }
            case default -> {
                break;
            }
        }

        oldDisp = dispCoord.clone();
        for (int i = 0; i < dispCoord.length; i++) {
            oldDisp[i] = dispCoord[i].clone();
        }
        updateLoc();
    }
    public boolean moveDown() {
        //System.out.println(doesCollideX(-1));
        if(!doesCollideX(-1)) {
            coordX -= 1;
            updateLoc();
            return true;
        }
        updateLoc();
        return false;
    }
    public void moveDir(int transform) {
        //System.out.println(doesCollideY(transform));
        if(!doesCollideY(transform))
        {
            coordY += transform;
        }
        updateLoc();

    }
    public void ghostLoc() {
        int i = 0;
        while(!doesCollideX(i))
        {
            i--;
        }
        ghostX = coordX + i + 1;
    }

    public boolean doesCollideX(int transformX) {
        for (int[] coord : dispCoord) {
            int disp = coordX + transformX + coord[1];
            if(disp < 0) return true;
            Tile t = GameScene.board[disp][coordY + coord[0]];
            if((t.getType() != TileType.EMPTY && !t.isActive())) {
                return true;
            }
        }
        return false;
    }
    public boolean doesCollideY(int transformY) {
        int sourcePos = coordY + transformY;
        for (int[] coord : dispCoord) {
            int disp = sourcePos + coord[0];
            if(disp < 0 || disp >= 10) return true;
            Tile t = GameScene.board[coordX+ coord[1]][disp];
            if((t.getType() != TileType.EMPTY && !t.isActive())) {
                return true;
            }
        }
        return false;
    }
    public void rotate(boolean isCCW) {
        if(currentType == TileType.O) return;
        if(isCCW){
            for (int[] arr: dispCoord){
                int temp = arr[0];
                arr[0] = -arr[1];
                arr[1] = temp;
            }
        }
        else {
            for (int[] arr: dispCoord){
                int temp = arr[0];
                arr[0] = arr[1];
                arr[1] = -temp;
            }
        }
        boolean isCollide = false;
        for (int[] coord : dispCoord) {
            if(rotCollide(coord)) {
                isCollide = true;
                break;
            }
        }
        if(isCollide) dispCoord = oldDisp.clone();
        updateLoc();
    }
    public boolean rotCollide(int[] arr) {
        int xPos = coordX + arr[1];
        int yPos = coordY + arr[0];
        if(xPos < 0 || yPos < 0 || xPos >= GameScene.board.length || yPos >= 10) return true;
        Tile t = GameScene.board[xPos][yPos];
        return (t.getType() != TileType.EMPTY && !t.isActive());
        //return (xPos < 0 || yPos < 0 || xPos >= 10 || yPos >= 10);
    }

    public void hardDrop() {
        int i = 0;
        while (!doesCollideX(i)) {
            i--;
        }
        coordX += i + 1;
        updateLoc();
    }

    public void placePiece() {
        for (int[] arr : dispCoord) {
            Tile t = GameScene.board[coordX + arr[1]][coordY + arr[0]];
            t.setActive(false);
        }
    }

    public void updateLoc() {
        ghostLoc();
        for (int[] a : oldDisp) {
            Tile t = GameScene.board[oldX + a[1]][oldY + a[0]];
            t.setActive(false);
            t.setType(TileType.EMPTY);
            Tile g = GameScene.board[oldGhostX + a[1]][oldY + a[0]];
            g.setHasGhost(false);
        }
        for (int[] b : dispCoord) {
            Tile t = GameScene.board[coordX + b[1]][coordY + b[0]];
            t.setActive(true);
            t.setType(currentType);
            Tile g = GameScene.board[ghostX+ b[1]][coordY + b[0]];
            g.setHasGhost(true);
        }

        oldDisp = dispCoord.clone();
        for (int i = 0; i < dispCoord.length; i++) {
            oldDisp[i] = dispCoord[i].clone();
        }
        oldX = coordX;
        oldY = coordY;
        oldGhostX = ghostX;

    }

}
