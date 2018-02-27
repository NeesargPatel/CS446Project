package cs446.cs.uw.tictacwoah.models;
import java.util.Random;

public class AI {

    // for now randomly choose an empty cell
    public static Piece choosePos(Board board, Integer playerId){
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
        return new Piece(-1, -1, -1, -1);
    }
}
