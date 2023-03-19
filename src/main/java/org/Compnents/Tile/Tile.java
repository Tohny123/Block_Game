package org.Compnents.Tile;

import org.joml.Matrix2f;

import java.awt.*;

public class Tile {
    TileType type;
    public Color col;

    boolean hasGhost = false;

    boolean isActive = false;


    boolean isFilled = false;
    public Tile() {
        setType(TileType.EMPTY);
    }
    public TileType getType() {
        return type;
    }
    public void setType(TileType t) {
        type = t;
        isFilled = type != TileType.EMPTY;
        switch (t) {
            case I -> col = Color.CYAN;
            case O -> col = Color.YELLOW;
            case J -> col = Color.BLUE;
            case L -> col = new Color(255, 100, 0);
            case S -> col = Color.RED;
            case Z -> col = Color.GREEN;
            case T -> col = new Color(160, 0, 255);
            case EMPTY -> col = Color.WHITE;
            case null, default -> col = Color.MAGENTA;
        }
    }
    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean hasGhost() {
        return hasGhost;
    }

    public void setHasGhost(boolean hasGhost) {
        this.hasGhost = hasGhost;
    }

    public boolean isFilled() {
        return isFilled;
    }

    public void setFilled(boolean filled) {
        isFilled = filled;
    }

    public String toString(){return type.name();}
}
