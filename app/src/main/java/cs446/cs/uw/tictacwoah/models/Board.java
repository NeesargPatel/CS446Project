package cs446.cs.uw.tictacwoah.models;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import cs446.cs.uw.tictacwoah.views.piece.PieceView;

public class Board {
    // 3 * 3 Board
    public static final Integer SIZE = 3;

    // ps stands for permutations
    // Each number refers to size of the piece
    // first three elements mean same size,
    // fourth means ascending, and fifth means descending
    private static final int[][] ps = {
            { 0, 0, 0 },
            { 1, 1, 1 },
            { 2, 2, 2 },
            { 0, 1, 2 },
            { 2, 1, 0 }
    };

    private final List<List<int[]>> possibleWinPattern;

    // first dimension indicates size of piece
    // second dimension indicates row index
    // third dimension indicates column index
    private Piece pieces[][][];

    private Piece[] winningPattern;

    private Piece lastPlacedPiece;

    // constructor
    public Board() {
        reset();
        possibleWinPattern = new ArrayList<>();
        populatePossibleWinPatterns();
    }

    public void reset(){
        this.pieces = new Piece[PieceView.SIZES.length][SIZE][SIZE];
        this.lastPlacedPiece = null;
        this.winningPattern = null;
    }

    Piece[][][] getPieces() { return pieces; }
    List<List<int[]>> getPossibleWinPattern() { return possibleWinPattern; }
    public Piece getLastPlacedPiece(){
        return lastPlacedPiece;
    }
    public Piece[] getWinningPattern() { return winningPattern; }

    public boolean isGameOver() {
        // if somebody won, then winningPattern is not null, and game is over
        return winningPattern != null;
    }

    public PossibleWinPatternsIter getPossibleWinPattensIter(){
        return new PossibleWinPatternsIter();
    }

    final class PossibleWinPatternsIter {
        int cursor = 0;
        public boolean hasnext(){
            return cursor < possibleWinPattern.size();
        }
        public List<Piece> next(){
            List<Piece> toReturn = new LinkedList<>();
            for (int[] index : possibleWinPattern.get(cursor)){
                toReturn.add(pieces[index[0]][index[1]][index[2]]);
            }
            ++cursor;
            return toReturn;
        }
    }

    // for AI to generate random number
    Integer getNumEmptyCells(){
        Integer emptyCells = 0;
        for (int i = 0; i < pieces.length; ++i){
            for (int j = 0; j < pieces[i].length; ++j){
                for (int k = 0; k < pieces[i][j].length; ++k){
                    if (pieces[i][j][k] == null) ++emptyCells;
                }
            }
        }
        return emptyCells;
    }

    // This function returns false if the desired position has been occupied,
    // returns true if the placement is successful
    public boolean placePiece(Piece piece){
        int pieceSize = piece.getSizeId(), i = piece.getRowId(), j = piece.getColId();

        if (this.pieces[pieceSize][i][j] != null) return false;

        this.pieces[pieceSize][i][j] = piece;
        lastPlacedPiece = piece;

        checkWon(piece, pieceSize, i, j);

        return true;
    }

    private void populatePossibleWinPatterns(){
        for (int i = 0; i < SIZE; ++i){
            for (int j = 0; j < SIZE; ++j){
                List<int[]> pattern = new ArrayList<>();
                for (int k = 0; k < PieceView.SIZES.length; ++k){
                    int[] tmp = {k, i, j};
                    pattern.add(tmp);
                }
                possibleWinPattern.add(pattern);
            }
        }

        for (int[] p : ps){
            for (int i = 0; i < SIZE; ++i){
                // horizontal
                List<int[]> pattern = new ArrayList<>();
                for (int j = 0; j < SIZE; ++j) {
                    int[] tmp = {p[j], i, j};
                    pattern.add(tmp);
                }
                possibleWinPattern.add(pattern);

                // vertical
                pattern = new ArrayList<>();
                for (int j = 0; j < SIZE; ++j) {
                    int[] tmp = {p[j], j, i};
                    pattern.add(tmp);
                }
                possibleWinPattern.add(pattern);
            }

            // upper left to lower right diagonal
            List<int[]> pattern1 = new ArrayList<>();
            // upper right to lower left diagonal
            List<int[]> pattern2 = new ArrayList<>();
            for (int i = 0; i < SIZE; ++i){
                int[] tmp1 = {p[i], i, i};
                pattern1.add(tmp1);
                int[] tmp2 = {p[i], i, SIZE - i - 1};
                pattern2.add(tmp2);
            }
            possibleWinPattern.add(pattern1);
            possibleWinPattern.add(pattern2);
        }
    }

    // Check if a player has won after they have played
    private boolean checkWon(Piece piece, Integer pieceSize, Integer i, Integer j) {
        for (List<int[]> pattern : possibleWinPattern){
            if (checkAndSetWinPattern(
                    pieces[pattern.get(0)[0]][pattern.get(0)[1]][pattern.get(0)[2]],
                    pieces[pattern.get(1)[0]][pattern.get(1)[1]][pattern.get(1)[2]],
                    pieces[pattern.get(2)[0]][pattern.get(2)[1]][pattern.get(2)[2]]
                    )){
                return true;
            }
        }
        PossibleWinPatternsIter iter = getPossibleWinPattensIter();
        while (iter.hasnext()){
            List<Piece> pattern = iter.next();
            if (checkAndSetWinPattern(pattern.get(0), pattern.get(1), pattern.get(2))){
                return true;
            }
        }
        return false;
//        if (samePosToWin() || lineToWin()) return true;
//        return false;
    }

    private Boolean samePosToWin(){
        for (int i = 0; i < SIZE; ++i){
            for (int j = 0; j < SIZE; ++j){
                if (checkAndSetWinPattern(pieces[0][i][j], pieces[1][i][j], pieces[2][i][j])){
                    return true;
                }
            }
        }
        return false;
    }

    private Boolean lineToWin(){
        for (int p = 0; p < ps.length; ++p){

            // horizontal
            for (int i = 0; i < SIZE; ++i){
                if (checkAndSetWinPattern(pieces[ps[p][0]][i][0], pieces[ps[p][1]][i][1], pieces[ps[p][2]][i][2])){
                    return true;
                }
            }

            // vertical
            for (int i = 0; i < SIZE; ++i){
                if (checkAndSetWinPattern(pieces[ps[p][0]][0][i], pieces[ps[p][1]][1][i], pieces[ps[p][2]][2][i])){
                    return true;
                }
            }

            // up-left to lower right diagonal
            if (checkAndSetWinPattern(pieces[ps[p][0]][0][0], pieces[ps[p][1]][1][1], pieces[ps[p][2]][2][2])){
                return true;
            }

            // up-right to lower left diagonal
            if (checkAndSetWinPattern(pieces[ps[p][0]][0][2], pieces[ps[p][1]][1][1], pieces[ps[p][2]][2][0])){
                return true;
            }
        }
        return false;
    }

    private boolean checkAndSetWinPattern(Piece p1, Piece p2, Piece p3){
        if (p1 == null || p2 == null || p3 == null) return false;
        if (p1.getId().equals(p2.getId()) && p2.getId().equals(p3.getId())){
            winningPattern = new Piece[]{p1, p2, p3};
            return true;
        }
        else return false;
    }
}