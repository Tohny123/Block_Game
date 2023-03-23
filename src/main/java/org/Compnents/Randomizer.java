package org.Compnents;

import org.Compnents.Tile.TileType;

import java.util.ArrayList;
import java.util.List;

public class Randomizer {
    static ArrayList<TileType> bag = new ArrayList<>();
    public static TileType bagRandomizer () {
        TileType t = null;
        if(bag.size() > 0) {
            int index = (int)(Math.random() * bag.size());
            t = bag.get(index);
            bag.remove(index);
        }
        else {
            resetBag();
            t = bagRandomizer();
        }
        return t;
    }
    public static void resetBag() {
        ArrayList<TileType> arr = new ArrayList<>(List.of(TileType.values()));
        arr.remove(0);
        bag = arr;
    }
}
