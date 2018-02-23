package cs446.cs.uw.tictacwoah.models;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final Integer id;

    // for each size of pieces, the maximal number a player can place on the board
    private static final Integer maxPieces = 3;

    // for each size of pieces, how many pieces are available to be placed on the board
    private List<Integer> availablePieces;

    public Integer getId() {
        return id;
    }

    // constructor
    public Player(Integer id) {
        this.id = id;
        availablePieces = new ArrayList<>();
        for (int i = 0; i < Piece.numSizes; ++i) availablePieces.add(maxPieces);
    }
}
