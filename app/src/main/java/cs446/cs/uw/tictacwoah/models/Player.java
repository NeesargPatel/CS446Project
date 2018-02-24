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

    // get number of pieces remaining for a size for a player
    public Integer piecesRemaining(Integer pieceSize) {
        return this.availablePieces.get(pieceSize);
    }

    // player has added a piece to board and has one less piece
    public boolean removePiece(Integer pieceSize) {
        Integer pieces = this.availablePieces.get(pieceSize);
        if (pieces > 0)
            this.availablePieces.set(pieceSize, pieces-1);
        return true;
    }
}
