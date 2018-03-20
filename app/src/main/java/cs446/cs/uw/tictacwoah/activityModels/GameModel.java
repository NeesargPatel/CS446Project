package cs446.cs.uw.tictacwoah.activityModels;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Observable;

import cs446.cs.uw.tictacwoah.activities.LobbyActivity;
import cs446.cs.uw.tictacwoah.models.AI;
import cs446.cs.uw.tictacwoah.models.AudioClip;
import cs446.cs.uw.tictacwoah.models.BluetoothService;
import cs446.cs.uw.tictacwoah.models.Board;
import cs446.cs.uw.tictacwoah.models.Piece;
import cs446.cs.uw.tictacwoah.models.Setting;

/**
 * Created by ASUS on 2018/2/26.
 */

/**
 This class serves as a model for GamePlayActivity
 */
public abstract class GameModel extends Observable {

    public static final String HOST_KEY = "host";

    public static final String GAME_MODE_KEY = "gameMode";
    public enum GameMode {
        SINGLE,
        MULTI_PLAYER
    }

    public static final int MAX_TIME_LIMIT = 30;
    public static final int TIME_LIMIT_INTERVAL = 5;
    public static final int MAX_PLAYERS = 4;
    public static final int MAX_AI_PLAYERS = 3;

    public static GameModel getInstance(GameMode mode, @Nullable Boolean isHost){
        if (mode.equals(GameMode.SINGLE)) return SingleGameModel.getInstance();
        else if (mode.equals(GameMode.MULTI_PLAYER)){
            if (isHost) return ServerGameModel.getInstance();
            else return ClientGameModel.getInstance();
        }
        return null;
    }

    Setting setting;
    Integer curPlayer;
    Board board;
    Integer myPlayerId;
    Integer numPlayers;
    private boolean gaming;

    // the intent passed to start GamePlayActivity
    GameModel(){
        curPlayer = null;
        board = new Board();
        myPlayerId = null;
        numPlayers = null;
    }

    public abstract boolean hasRightToStartGame();

    public Setting getSetting() { return setting; }
    public Integer getMyPlayerId() { return myPlayerId; }
    public Integer getCurPlayer() { return curPlayer; }
    public Integer getNumPlayers() { return numPlayers; }
    public boolean isGaming() { return gaming; }

    public Boolean isMyTurn(){
        return myPlayerId.equals(curPlayer);
    }

    public Boolean isGameOver() { return board.isGameOver(); }
    public Piece[] getWinningPattern() { return board.getWinningPattern(); }
    public Piece getLastPlacedPiece() { return board.getLastPlacedPiece(); }

    public void init(Setting setting){
        gaming = false;
        curPlayer = null;
        this.setting = setting;
    }

    public void newGame(){
        gaming = true;
        curPlayer = 0;
        board.reset();
        setChangedAndNotify();
    }

    public void reset(){
        setting = null;
        curPlayer = null;
        numPlayers = null;
        deleteObservers();
    }

    public Boolean sendAudio(AudioClip audioClip) {
        return true;
    }

    public Boolean playAudio(AudioClip audioClip) {
        return true;
    }

    public Boolean placePiece(Piece piece){
        if (!board.placePiece(piece)) return false;
        else {
            // update UI
            // 1. draw this new piece and
            // 2. maybe show the winning pattern if somebody just won)
            setChangedAndNotify();

            if (board.isGameOver()){
                curPlayer = null;  // nobody can place pieces onto the board until the game restarts
                gaming = false;
                setChangedAndNotify();
            }
            else {
                // if this piece was placed at a valid position,
                // then move to the next player
                nextPlayer();
            }
            return true;
        }
    }

    // This method will be invoked when a player exhausts the time
    public void AIPlacePiece(){
        placePiece(AI.choosePos(board, curPlayer, AI.LEVEL.EASY));
    }

    private void nextPlayer(){
        curPlayer = (curPlayer + 1) % numPlayers;
        setChangedAndNotify();
    }

    void setChangedAndNotify(){
        setChanged();
        notifyObservers();
    }
}
