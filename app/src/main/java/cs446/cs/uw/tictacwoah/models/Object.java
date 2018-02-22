package cs446.cs.uw.tictacwoah.models;

public class Object {
    /*
    Each object gets an id which can be easily differentiated for each player...
        11-19: Player 1's objects
        21-29: Player 2's objects
        31-39: Player 3's objects
        41-49: Player 4's objects
     */
    private Integer id;

    /*
    These are the positions on the board...
        0 : Object has not been placed on board yet
        1-9 : position of all 9 small objects on board
        11-19 : position of all 9 medium objects on board
        21-29 : position of all 9 large objects on board
     */
    private Integer pos;

    // The three sizes are small (1), medium (2), large (3)
    private Integer size;

    /*
    Object's shape...
        0 - Circle
        1 - Square
        2 - Triangle
        3 -
      */
    private Integer shape;

    /*
    Object's colour...
        0 - Blue
        1 - Red
        2 - Green
        3 - Yellow
        4 - Black
        5 - Brown
        6 -
      */
    private Integer colour;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPos() {
        return pos;
    }

    public void setPos(Integer pos) {
        this.pos = pos;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getShape() {
        return shape;
    }

    public void setShape(Integer shape) {
        this.shape = shape;
    }

    public Integer getColour() {
        return colour;
    }

    public void setColour(Integer colour) {
        this.colour = colour;
    }


    // constructor
    public Object(Integer id, Integer size, Integer colour, Integer shape) {
        this.setId(id);
        this.setPos(0);
        this.setSize(size);
        this.setColour(colour);
        this.setShape(shape);
    }
}
