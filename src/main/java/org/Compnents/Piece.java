package org.Compnents;

enum Type {
    EMPTY,
    I, //cyan
    O,  //yellow
    T, // purple
    S, //green
    Z, //red
    J, //blue
    L //orange

}
public class Piece {
    Type type;
    int[] boundingSize;
//    public Piece(Type t) {
//        super();
//        type = t;
//
//    }
    public Piece() {
        super();
        setType(Type.EMPTY);
    }
    public void setType(Type t) {
        type = t;
        switch (t) {
            case I -> boundingSize = new int[] {4, 4};
            case O -> boundingSize = new int[] {2, 3};
            case EMPTY, null -> boundingSize = new int[] {-1, -1};
            case default -> boundingSize = new int[] {3, 3};
        }
    }
    public String toString(){return type.name();}
}
