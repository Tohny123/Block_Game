package org.Main;

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
    public Piece(Type t) {
        super();
        type = t;

    }
    public String toString(){return type.name();}
}
