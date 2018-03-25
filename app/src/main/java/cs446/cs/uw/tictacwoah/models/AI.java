package cs446.cs.uw.tictacwoah.models;
import java.util.List;
import java.util.Random;

public class AI {

    public enum LEVEL{
        EASY,
        MEDIUM,
        DIFFICULT
    }

    // for now randomly choose an empty cell
    public static Piece choosePos(Board board, Integer playerId, LEVEL level){
        switch (level){
            case EASY:
                return random(board, playerId);
            case MEDIUM:
                Piece toReturn = blockOrWin(board, playerId);
                if (toReturn != null) return toReturn;
                return random(board, playerId);
            case DIFFICULT:
                return null;
            default:
                return null;
        }
    }

    private static Piece random(Board board, Integer playerId){
        Integer numEmptyCells = board.getNumEmptyCells();
        Random random = new Random();
        Integer choice = random.nextInt(numEmptyCells);

        Integer counter = 0;
        Piece[][][] pieces = board.getPieces();
        for (int i = 0; i < pieces.length; ++i){
            for (int j = 0; j < pieces[i].length; ++j){
                for (int k = 0; k < pieces[i][j].length; ++k){
                    if (pieces[i][j][k] == null){
                        if (counter.equals(choice)){
                            return new Piece(playerId, i, j, k);
                        }
                        ++counter;
                    }
                }
            }
        }
        // This line should not be executed
        return null;
    }

    private static Piece blockOrWin(Board board, int playerId){

        Piece target = null;
        Piece[][][] pieces = board.getPieces();

        for (List<int[]> pattern : board.getPossibleWinPattern()){
            int id = -1;
            boolean change = false, oneEmpty = false, fail = false;
            for (int[] index : pattern) {
                Piece piece = pieces[index[0]][index[1]][index[2]];
                if (piece == null){
                    // There are at least two empty cells in this pattern
                    if (oneEmpty){
                        fail = true;
                        break;
                    }
                    oneEmpty = true;
                    target = new Piece(playerId, index[0], index[1], index[2]);
                }
                else if (id != piece.getId()) {
                    // There are at least two players' pieces in this pattern
                    if (change){
                        fail = true;
                        break;
                    }
                    change = true;
                    id = piece.getId();
                }
            }
            if (!fail) break;
            else target = null;
        }

        return target;
    }
}
