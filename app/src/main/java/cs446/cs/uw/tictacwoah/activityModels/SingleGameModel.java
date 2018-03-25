package cs446.cs.uw.tictacwoah.activityModels;

import cs446.cs.uw.tictacwoah.models.AI;
import cs446.cs.uw.tictacwoah.models.Piece;
import cs446.cs.uw.tictacwoah.models.Setting;

/**
 * Created by ASUS on 2018/3/12.
 */

public class SingleGameModel extends GameModel {

    private static SingleGameModel model = null;
    static SingleGameModel getInstance() {
        if (model == null) model = new SingleGameModel();
        return model;
    }

    private SingleGameModel(){
        // this will never change for single mode game,
        // so I put it in the ctor
        myPlayerId = 0;
    }

    public void init(Setting setting){
        super.init(setting);
        numPlayers = setting.getNumAIs() + 1;  // +1 for the human player
    }

    @Override
    public boolean hasRightToStartGame() { return true; }

    @Override
    public Boolean placePiece(Piece piece){
        if (super.placePiece(piece)){
            // If this newly placed piece was placed by myself
            if (piece.getId().equals(myPlayerId)){
                AIPlacePieces();
            }
            return true;
        }
        return false;
    }

    private void AIPlacePieces(){
        // while it's not the turn of human player and nobody has won
        // we must check isGameOver() first
        // because if it's over, curPlayer will be set to null
        while (!board.isGameOver() && !curPlayer.equals(myPlayerId)){
            super.placePiece(AI.choosePos(board, curPlayer, setting.getLevel()));
        }
    }
}
