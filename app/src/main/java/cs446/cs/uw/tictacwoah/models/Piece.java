package cs446.cs.uw.tictacwoah.models;

import java.io.Serializable;

public class Piece implements Serializable{

    // small, medium, large
    public static final Integer NUM_SIZES = 3;

    /*
    Each Piece gets an id which can be easily differentiated for each player...
        range from 1 to n (number of players) 
     */
    private final Integer playerId;
    private final Integer sizeId;
    private final Integer rowId;
    private final Integer colId;

    // constructor
    public Piece(Integer playerId, Integer sizeId, Integer rowId, Integer colId) {
        this.playerId = playerId;
        this.sizeId = sizeId;
        this.rowId = rowId;
        this.colId = colId;
    }

    public Integer getId() {
        return playerId;
    }
    public Integer getSizeId() { return sizeId; }
    public Integer getRowId() { return rowId; }
    public Integer getColId() { return colId; }
}
