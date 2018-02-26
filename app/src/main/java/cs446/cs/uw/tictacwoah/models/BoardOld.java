package cs446.cs.uw.tictacwoah.models;

public class BoardOld {
    // 3 * 3 BoardOld
    public static final Integer boardSize = 3;

    private Integer numPlayers;

    // first dimension indicates size of piece
    // second dimension indicates row index
    // third dimension indicates column index
    private Piece pieces[][][];

    private Integer currPlayer;
    private boolean gameover;
    private Integer winPlayer;

    // 1 - all three sized shapes on same position
    // 2 - three same sized shapes horizontal or vertical
    // 3 - three shapes of ascending or descending order of size horizontally or vertically
    private Integer typeOfWin;


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

    public Integer getCurrPlayer() {
        return currPlayer;
    }

    public void setCurrPlayer(Integer currPlayer) {
        this.currPlayer = currPlayer;
    }

    // This function returns false if the desired position has been occupied,
    // returns true if the placement is successful
    public boolean placePiece(Piece piece, Integer pieceSize, Integer i, Integer j){
        if (this.pieces[pieceSize][i][j] != null) return false;
        this.pieces[pieceSize][i][j] = piece;

        if (checkWon(piece, pieceSize, i, j)) {
            this.gameover = true;
            this.winPlayer = piece.getId();
        }
        return true;
    }

    // constructor
    public BoardOld(Integer numPlayers) {
        this.numPlayers = numPlayers;
        this.gameover = false;
        this.winPlayer = 0;
        this.pieces = new Piece[Piece.numSizes][boardSize][boardSize];
    }


    // First way to win is different sized pieces in same position
    private boolean firstWayToWin(Piece piece, Integer i, Integer j) {
        for (int x = 0; x< BoardOld.boardSize; x++) {
            if (pieces[x][i][j] != piece)
                return false;
        }
        return true;
    }

    // Second way to win is same sized pieces horizontally, vertically or diagonally
    private boolean secondWayToWin(Piece piece, Integer pieceSize, Integer i, Integer j) {
        // Horizontal
        boolean win = true;
        for (int x = 0; x< BoardOld.boardSize; x++) {
            if (pieces[pieceSize][i][x] != piece) {
                win = false;
                break;
            }
        }
        if (win) return true;

        // Vertical
        win = true;
        for (int x = 0; x< BoardOld.boardSize; x++) {
            if (pieces[pieceSize][x][j] != piece) {
                win = false;
                break;
            }
        }
        if (win) return true;

        // left-to-right diagonal
        if (i == j) {
            win = true;
            for (int x = 0; x < BoardOld.boardSize; x++) {
                if (pieces[pieceSize][x][x] != piece) {
                    win = false;
                    break;
                }
            }
            if (win) return true;
        }

        // right-to-left diagonal
        if (j == BoardOld.boardSize-i-1 || i == BoardOld.boardSize-j-1) {
            win = true;
            for (int x = 0; x < BoardOld.boardSize; x++) {
                if (pieces[pieceSize][x][BoardOld.boardSize-x-1] != piece) {
                    win = false;
                    break;
                }
            }
            if (win) return true;
        }

        return false;
    }

    // Third way to win is different sized pieces in ascending or descending order horizontally, vertically or diagonally
    private boolean thirdWayToWin(Piece piece, Integer i, Integer j) {
        // Horizontal
        boolean win = true, winRev = true;
        for (int x = 0; x< BoardOld.boardSize; x++) {
            if (pieces[x][i][x] != piece) {
                win = false;
            }
            if (pieces[BoardOld.boardSize-x-1][i][x] != piece) {
                winRev = false;
            }
        }
        if (win || winRev) return true;

        // Vertical
        win = winRev = true;
        for (int x = 0; x< BoardOld.boardSize; x++) {
            if (pieces[x][x][j] != piece) {
                win = false;
            }
            if (pieces[BoardOld.boardSize-x-1][x][j] != piece) {
                winRev = false;
            }
        }
        if (win || winRev) return true;

        // left-to-right diagonal
        if (i == j) {
            win = winRev = true;
            for (int x = 0; x < BoardOld.boardSize; x++) {
                if (pieces[x][x][x] != piece) {
                    win = false;
                }
                if (pieces[BoardOld.boardSize-x-1][x][x] != piece) {
                    winRev = false;
                }
            }
            if (win || winRev) return true;
        }

        // right-to-left diagonal
        if (j == BoardOld.boardSize-i-1 || i == BoardOld.boardSize-j-1) {
            win = winRev = true;
            for (int x = 0; x < BoardOld.boardSize; x++) {
                if (pieces[x][x][BoardOld.boardSize-x-1] != piece) {
                    win = false;
                }
                if (pieces[BoardOld.boardSize-x-1][x][BoardOld.boardSize-x-1] != piece) {
                    winRev = false;
                }
            }
            if (win || winRev) return true;
        }

        return false;
    }

    // Check if a player has won after they have played
    private boolean checkWon(Piece piece, Integer pieceSize, Integer i, Integer j) {
        if (firstWayToWin(piece, i, j))
            typeOfWin = 1;
        else if(secondWayToWin(piece, pieceSize, i, j))
            typeOfWin = 2;
        else if(thirdWayToWin(piece, i, j))
            typeOfWin = 3;

        if (typeOfWin > 0)
            return true;
        return false;
    }
}
