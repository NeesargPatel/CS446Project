package cs446.cs.uw.tictacwoah.models;

import java.util.ArrayList;
import java.util.List;

public class Board {
    // 3 * 3 Board
    public static final Integer boardSize = 3;

    private Integer numPlayers;

    // first dimension indicates size of piece
    // second dimension indicates row index
    // third dimension indicates column index
    private Piece pieces[][][];

    private boolean gameover;
    private Integer winPlayer;


    public Integer getNumPlayers() {
        return numPlayers;
    }

    public void setNumPlayers(Integer numPlayers) {
        this.numPlayers = numPlayers;
    }

    public Piece[][][] getPieces() { return pieces; }

    public boolean isGameover() {
        return gameover;
    }

    public Integer getWinPlayer() {
        return winPlayer;
    }

    // This function returns false if the desired position has been occupied,
    // returns true if the placement is successful
    public boolean placePiece(Piece piece, Integer pieceSize, Integer i, Integer j){
        if (pieces[pieceSize][i][j] != null) return false;
        pieces[pieceSize][i][j] = piece;
        // check if piece.playerId just won here
        return true;
    }

    // constructor
    public Board(Integer numPlayers) {
        this.numPlayers = numPlayers;
        this.gameover = false;
        this.winPlayer = 0;
        this.pieces = new Piece[Piece.numSizes][boardSize][boardSize];
    }
}
