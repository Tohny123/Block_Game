package org.Compnents;

import org.Compnents.Tile.Tile;
import org.Compnents.Tile.TileType;
import org.Scenes.GameScene;


public class Piece {
    TileType currentType;
    int[] bound;
    int rotIndex = 0;
    int oldX, oldY;
    public int coordX, coordY;// dispCoord for center
    int[][] dispCoord = new int[4][2]; // coord displacement x horizontal y vertical
    int[][] oldDisp;
    int ghostX;
    int oldGhostX;
    //x horizontal y vertical
    int[][][] offset = new int[5][4][2]; //1st value: test count 2nd value: rotation index: 3rd value: offsets

    public Piece(TileType type) {
//        ArrayList<TileType> arr = new ArrayList<TileType>(List.of(TileType.values()));
//        arr.remove(0);
//        currentType = arr.get((int)(Math.random() * arr.size()));
        currentType = type;

        //currentType = TileType.T;

        bound = new int[]{1,1};
        coordY = 5;
        coordX = 20;
        oldX = coordX;
        oldY = coordY;
        setTypeDisp();
        setWallKick();

        oldDisp = dispCoord.clone();
        for (int i = 0; i < dispCoord.length; i++) {
            oldDisp[i] = dispCoord[i].clone();
        }
        updateLoc();
    }
    private void setWallKick() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 4; j++) {
                offset[i][j] = new int[] {0,0};
            }
        }

        if (currentType == TileType.I) {
            offset[0][1] = new int[]{-1, 0};
            offset[0][2] = new int[]{-1, 1};
            offset[0][3] = new int[]{0, 1};

            offset[1][0] = new int[]{-1, 0};
            offset[1][2] = new int[]{1, 1};
            offset[1][3] = new int[]{0, 1};

            offset[2][0] = new int[]{2, 0};
            offset[2][2] = new int[]{-2, 1};
            offset[2][3] = new int[]{0, 1};

            offset[3][0] = new int[]{-1, 0};
            offset[3][1] = new int[]{0, 1};
            offset[3][2] = new int[]{1, 0};
            offset[3][3] = new int[]{0, -1};

            offset[4][0] = new int[]{2, 0};
            offset[4][1] = new int[]{0, -2};
            offset[4][2] = new int[]{-2, 0};
            offset[4][3] = new int[]{0, 2};
        } else {

            offset[1][1] = new int[]{1, 0};
            offset[1][3] = new int[]{-1, 0};

            offset[2][1] = new int[]{1, -1};
            offset[2][3] = new int[]{-1, -1};

            offset[3][1] = new int[]{0, 2};
            offset[3][3] = new int[]{0, 2};

            offset[4][1] = new int[]{1, 2};
            offset[4][3] = new int[]{-1, 2};
        }
    }

    private void setTypeDisp() {
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
                dispCoord[0] = new int[]{1, 0};
                dispCoord[1] = new int[]{0, 0};
                dispCoord[2] = new int[]{-1, 0};
                dispCoord[3] = new int[]{-2, 0};
            }
            case O -> {
                dispCoord[0] = new int[]{1, 0};
                dispCoord[1] = new int[]{1, 1};
                dispCoord[2] = new int[]{0, 1};
                dispCoord[3] = new int[]{0, 0};
            }
            case default -> {
            }
        }
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
            doRot(-1);
        }
        else {
            doRot(1);
        }
        doWallKicks(isCCW);
        updateLoc();
    }

    private void doWallKicks(boolean isCCW) {
        int[] off;
        for (int i = 0; i < 5; i++) {
            off = new int[]
                    { offset[i][rotIndex][0] - offset[i][updateRotIndex(isCCW)][0],
                            offset[i][rotIndex][1] - offset[i][updateRotIndex(isCCW)][1]};
            if(!isCollide(off)) {
                coordX += off[1];
                coordY += off[0];
                rotIndex = updateRotIndex(isCCW);
                return;
            }
        }
        dispCoord = oldDisp.clone();
    }
//

    public void doRot(int ccw) {
        for (int[] arr: dispCoord){
            int temp = arr[0];
            arr[0] = arr[1] * ccw;
            arr[1] = temp * -ccw;
        }
    }
    private int updateRotIndex(boolean isCCW) {
        int rot = rotIndex;
        if(isCCW) {
            if(rotIndex - 1 < 0) rot = 3;
            else rot--;
        } else {
            if(rotIndex + 1 > 3) rot = 0;
            else rot++;
        }
        return rot;
    }

    private boolean isCollide(int[] offset) {
        boolean isCollide = false;
        for (int[] coord : dispCoord) {
            if(rotCollide(coord, offset)) {
                isCollide = true;
                break;
            }
        }
        return isCollide;
    }

    public boolean rotCollide(int[] arr, int[] offset) {
        int xPos = coordX + arr[1] + offset[1];
        int yPos = coordY + arr[0] + offset[0];
        if(xPos < 0 || yPos < 0 || xPos >= GameScene.board.length || yPos >= 10) return true;
        Tile t = GameScene.board[xPos][yPos];
        //t.setHasGhost(true);
        return (t.getType() != TileType.EMPTY && !t.isActive());
    }

    public void hardDrop() {
        int i = 0;
        while (!doesCollideX(i)) {
            i--;
        }
        coordX += i + 1;
        updateLoc();
    }

    public int[] placePiece() {
        int lowest = 100;
        int highest = -1;
        for (int[] arr : dispCoord) {
            if(coordX + arr[1] > highest) highest = coordX + arr[1];
            if(coordX + arr[1] < lowest) lowest = coordX + arr[1];
            //System.out.println(coordX + arr[1]);
            Tile t = GameScene.board[coordX + arr[1]][coordY + arr[0]];
            t.setActive(false);

        }
       // System.out.println(lowest + " " + highest);
        return new int[]{lowest, highest};
    }
    public void deletePiece() {
        for (int[] arr : dispCoord) {
            Tile t = GameScene.board[coordX + arr[1]][coordY + arr[0]];
            t.setActive(false);
            t.setType(TileType.EMPTY);
            Tile g = GameScene.board[ghostX+ arr[1]][coordY + arr[0]];
            g.setHasGhost(false);
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
    //   private void rotateI(boolean isCCW) {
//        int offsetX = 0; //horizontal
//        int offsetY = 0; //vertical
//        if (isCCW) {
//            switch (rotIndex) {
//                case 0 -> offsetX = -1;
//                case 1 -> offsetY = 1;
//                case 2 -> offsetX = 1;
//                case 3 -> offsetY = -1;
//            }
//            doRot(-1);
//
//        } else {
//            switch (rotIndex) {
//                case 0 -> offsetY = -1;
//                case 1 -> offsetX = -1;
//                case 2 -> offsetY = 1;
//                case 3 -> offsetX = 1;
//            }
//           // System.out.println(rotIndex);
//
//            doRot(1);
//        }
//        int[] offset = new int[] {offsetX, offsetY};
//        if(isCollide(offset)) {
//            dispCoord = oldDisp.clone();
//        } else {
//            rotationIndex(isCCW);
//            coordX += offsetY; //i know intellij
//            coordY += offsetX;
//        }
//        //System.out.println(rotIndex);
//        updateLoc();
//    }

    @Override
    public String toString() {
        return currentType.toString();
    }
}
