package cs446.cs.uw.tictacwoah.activityModels;

import android.util.Log;

import cs446.cs.uw.tictacwoah.models.Piece;
import cs446.cs.uw.tictacwoah.models.Setting;

/**
 * Created by vicdragon on 2018-03-24.
 */

public class HotseatGameModel extends GameModel {
    private static HotseatGameModel model = null;
    static HotseatGameModel getInstance() {
        if (model == null) model = new HotseatGameModel();
        return model;
    }

    private HotseatGameModel() {
        myPlayerId = 0;
    }

    @Override
    public void init(Setting setting){
        super.init(setting);
        numPlayers = setting.getNumHsPlayers() + 1;  // +1 for the human player
    }

    @Override
    public boolean hasRightToStartGame() { return true; }

    @Override
    public Boolean placePiece(Piece piece){
        if (super.placePiece(piece)) {
            return true;
        }
        return false;
    }
}
