package cs446.cs.uw.tictacwoah.models;

public class Piece {

    // small, medium, large
    public static Integer numSizes = 3;

    /*
    Each Piece gets an id which can be easily differentiated for each player...
        range from 1 to n (number of players) 
     */
    private Integer playerId;


    public Integer getId() {
        return playerId;
    }


    // constructor
    public Piece(Integer playerId) {
        this.playerId = playerId;
    }
}
