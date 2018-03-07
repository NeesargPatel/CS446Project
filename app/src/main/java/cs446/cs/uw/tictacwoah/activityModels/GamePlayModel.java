package cs446.cs.uw.tictacwoah.activityModels;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.util.Observable;

import cs446.cs.uw.tictacwoah.activities.LobbyActivity;
import cs446.cs.uw.tictacwoah.models.AI;
import cs446.cs.uw.tictacwoah.models.BluetoothService;
import cs446.cs.uw.tictacwoah.models.Board;
import cs446.cs.uw.tictacwoah.models.Piece;

/**
 * Created by ASUS on 2018/2/26.
 */

/**
 This class serves as a model for GamePlayActivity
 */
public class GamePlayModel extends Observable {
    public static final String GAME_MODE_KEY = "gameMode";
    public static final String SINGLE_MODE = "singleMode";
    public static final String MULTIPLAYER_MODE = "multiplayerMode";
    public static final String HOST_KEY = "host";

    private final String gameMode;
    private final Integer myPlayerId;
    private Boolean isHost;
    private String hostAddress;

    private Integer numPlayers;
    private Integer curPlayer;
    private Board board;

    private BluetoothService bluetoothService;
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                // when the blue tooth socket receives messages from other players
                case 2:
                    Piece piece = (Piece) msg.obj;
                    placePiece(piece);
                    break;
                default:
                    // we should not run into this block
            }
        }
    };

    // the intent passed to start GamePlayActivity
    public GamePlayModel(Intent intent, Context context){
        numPlayers = 4;  // it should be passed as a parameter to ctor in the future
        curPlayer = -1;
        board = new Board();

        Bundle bundle = intent.getExtras();

        gameMode = bundle.getString(GAME_MODE_KEY);
        if (gameMode.equals(MULTIPLAYER_MODE)){
            isHost = bundle.getBoolean(HOST_KEY);
            // Because we only support 2 players now
            // assign 0 to host, 1 if not
            myPlayerId = isHost ? 0 : 1;

            if (!isHost){
                hostAddress = bundle.getString(LobbyActivity.EXTRA_DEVICE_ADDRESS);
            }
            establishBltConn(context);
        }
        else{
            bluetoothService = null;
            myPlayerId = 0;
        }
    }

    public Integer getMyPlayerId() { return myPlayerId; }
    public Integer getCurPlayer() { return curPlayer; };
    public Integer getNumPlayers() { return numPlayers; }
    public Boolean isMyTurn(){
        return myPlayerId.equals(curPlayer);
    }
    public Boolean isGameOver() { return board.isGameOver(); }
    public Piece[] getWinningPattern() { return board.getWinningPattern(); }
    public Piece getLastPlacedPiece() { return board.getLastPlacedPiece(); }

    // This function should be called when GamePlayActivity is opened again
    public void establishBltConn(Context context){
        bluetoothService = new BluetoothService(context, handler);
        if (isHost) bluetoothService.start();  // start accept threads in bluetoothService
        else {
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            // The user should enable blue tooth in BluetoothActivity,
            // so I do not check if bluetooth is enabled here
            BluetoothDevice device = bluetoothAdapter.getRemoteDevice(hostAddress);
            bluetoothService.connect(device, false);  // false stands for insecure
        }
    }

    public void closeBltConn(){
        if (bluetoothService != null) {
            bluetoothService.stop();
        }
    }

    public void reset(){
        curPlayer = 0;
        board.reset();
        setChangedAndNotify();
    }

    public void setChangedAndNotify(){
        setChanged();
        notifyObservers();
    }

    public Boolean placePiece(Piece piece){
        if (!board.placePiece(piece)) return false;
        else {
            // update UI
            // 1. draw this new piece and
            // 2. maybe show the winning pattern if somebody just won)
            setChangedAndNotify();

            if (board.isGameOver()){
                curPlayer = -1;  // nobody can place pieces onto the board until the game restarts
            }
            else {
                // if this piece was placed at a valid position,
                // then move to the next player
                nextPlayer();
            }

            // If this newly placed piece was placed by myself
            if (piece.getId().equals(myPlayerId)){
                // tell other human players I've placed this piece
                if (gameMode.equals(MULTIPLAYER_MODE)){
                    bluetoothService.write(piece);
                }
                else {  // single player mode
                    AIPlacePieces();
                }
            }

            return true;
        }
    }

    // This method will be invoked when a player exhausts the time
    public void AIPlacePiece(){
        placePiece(AI.choosePos(board, curPlayer));
    }

    private void nextPlayer(){
        curPlayer = (curPlayer + 1) % numPlayers;
        setChangedAndNotify();
    }

    private void AIPlacePieces(){
        // while it's not the turn of human player and nobody has won
        while (!curPlayer.equals(myPlayerId) && !board.isGameOver()){
            placePiece(AI.choosePos(board, curPlayer));
        }
    }
}
