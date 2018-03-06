package cs446.cs.uw.tictacwoah.models;

public class Piece {

    // small, medium, large
    public static final Integer NUM_SIZES = 3;

    // playerId, sizeId, rowId, colId
    private static final Integer numInts = 4;

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

    public Piece(String str){
        assert str.length() == numInts;

        playerId = Integer.parseInt(Character.toString(str.charAt(0)));
        sizeId = Integer.parseInt(Character.toString(str.charAt(1)));
        rowId = Integer.parseInt(Character.toString(str.charAt(2)));
        colId = Integer.parseInt(Character.toString(str.charAt(3)));
    }

    public Integer getId() {
        return playerId;
    }
    public Integer getSizeId() { return sizeId; }
    public Integer getRowId() { return rowId; }
    public Integer getColId() { return colId; }

    /**
     * We want to transfer a Piece object between devices, so we're going to make it parcelable
     */
    public byte[] toByteArray(){
        // The order should be identical with that in Piece(String)
        String str = playerId.toString() + sizeId.toString() + rowId.toString() + colId.toString();
        return str.getBytes();
    }
}
