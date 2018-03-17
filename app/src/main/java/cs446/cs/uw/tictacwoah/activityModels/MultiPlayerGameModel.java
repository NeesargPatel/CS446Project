package cs446.cs.uw.tictacwoah.activityModels;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;

import java.io.Serializable;

import cs446.cs.uw.tictacwoah.activities.LobbyActivity;
import cs446.cs.uw.tictacwoah.models.AI;
import cs446.cs.uw.tictacwoah.models.BluetoothService;
import cs446.cs.uw.tictacwoah.models.Piece;
import cs446.cs.uw.tictacwoah.models.Setting;

/**
 * Created by ASUS on 2018/3/12.
 */

public abstract class MultiPlayerGameModel extends GameModel {

    static class GameMessage implements Serializable{
        enum Type{
            REQUEST,
            ACCEPT,
            UPDATE,
            START_GAME
        }

        private Type type;
        private Integer numPlayer;

        GameMessage(Type type){
            this.type = type;
        }

        Type getType() { return type; }
        Integer getNumPlayer() { return numPlayer; }

        void setType(Type type) { this.type = type; }
        void setNumPlayer(int numPlayer) { this.numPlayer = numPlayer; }
    }

    void setNumPlayers(int numPlayers){
        this.numPlayers = numPlayers;
        setChangedAndNotify();
    }
}
