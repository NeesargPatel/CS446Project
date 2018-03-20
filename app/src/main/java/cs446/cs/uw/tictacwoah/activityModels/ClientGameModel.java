package cs446.cs.uw.tictacwoah.activityModels;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import cs446.cs.uw.tictacwoah.models.AudioClip;
import cs446.cs.uw.tictacwoah.models.BluetoothService;
import cs446.cs.uw.tictacwoah.models.Piece;
import cs446.cs.uw.tictacwoah.models.Setting;

/**
 * Created by ASUS on 2018/3/12.
 */

public class ClientGameModel extends MultiPlayerGameModel {

    private static String TAG = "ClientGameModel";

    private static ClientGameModel model = null;
    static ClientGameModel getInstance() {
        if (model == null) model = new ClientGameModel();
        return model;
    }

    // use a static class because we don't need access to private members
    // of the enclosing class
    private static class GameHandler extends Handler {
        private final ClientGameModel model;

        private GameHandler(ClientGameModel model){
            this.model = model;
        }

        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG, Integer.toString(msg.what));
            switch (msg.what) {
                // keep trying connecting to the host
                case BluetoothService.CONNECT_FAILED:
                    // model.connect(null);
                    break;
                // send a request to the host if the connection has been established
                case BluetoothService.CONNECTION_ESTABLISHED:{
                    GameMessage gameMessage = new GameMessage(GameMessage.Type.REQUEST);
                    model.bluetoothService.write(gameMessage);
                }
                case BluetoothService.FROM_OTHER_DEVICES:
                    if (msg.obj instanceof GameMessage){
                        GameMessage gameMessage = (GameMessage) msg.obj;
                        GameMessage.Type type = gameMessage.getType();
                        switch (type){
                            case ACCEPT:{
                                assert model.myPlayerId == null;
                                model.myPlayerId = gameMessage.getNumPlayer() - 1;
                                model.numPlayers = gameMessage.getNumPlayer();

                                // notify the LobbyActivity to start GameActivity
                                if (model.setting != null) model.setChangedAndNotify();
                            }
                            case UPDATE:{
                                model.setNumPlayers(gameMessage.getNumPlayer());
                            }
                            case START_GAME:{
                                assert model.isGameOver();
                                model.newGame();
                            }
                        }
                    }
                    else if (msg.obj instanceof Setting){
                        model.init((Setting) msg.obj);
                        // notify the LobbyActivity to start GameActivity
                        if (model.numPlayers != null && model.myPlayerId != null){
                            // I don't know why sometimes at this point model.setting is null
                            // which should have been initialized in model.init()
                            while (true){
                                if (model.setting != null){
                                    break;
                                }
                            }
                            model.setChangedAndNotify();
                        }
                    }
                    else if (msg.obj instanceof Piece){
                        Piece piece = (Piece) msg.obj;
                        model.placePiece(piece);
                    } else if (msg.obj instanceof AudioClip){
                        Log.d("myTag", "recieved!");
                        AudioClip audioClip = (AudioClip) msg.obj;
                        // Just play the audio clip
                        model.playAudio(audioClip);
                    }
                    break;
                default:
                    // we should not run into this block
            }
        }
    }

    private BluetoothService bluetoothService;
    private String hostAddr;

    private ClientGameModel(){
        Handler handler = new GameHandler(this);
        bluetoothService = new BluetoothService(handler, null);
    }

    @Override
    public boolean hasRightToStartGame() { return false; }

    @Override
    public Boolean placePiece(Piece piece){
        if (super.placePiece(piece)){
            // If this newly placed piece was placed by myself
            if (piece.getId().equals(myPlayerId)){
                // tell other human players I've placed this piece
                bluetoothService.write(piece);
            }
            return true;
        }
        return false;
    }

    @Override
    public Boolean sendAudio (AudioClip audioClip) {
        Log.d("myTag", "ClientGameModel sending byte array");
        bluetoothService.write(audioClip);
        return true;
    }

    @Override
    public Boolean playAudio (AudioClip audioClip) {
        // TODO: play the audio clip
        audioClip.getAudioClip();
        return true;
    }

    @Override
    public void reset(){
        super.reset();
        if (bluetoothService != null) bluetoothService.stop();
        myPlayerId = null;
    }

    // This function should be called when GamePlayActivity is opened again
    public void connect(String hostAddress){

        if (hostAddress == null) hostAddress = this.hostAddr;
        // this.hostAddr will be initialized when this method is invoked in LobbyActivity
        else this.hostAddr = hostAddress;

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // The user should enable blue tooth in BluetoothActivity,
        // so I do not check if bluetooth is enabled here
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(hostAddress);
        if (device == null) Log.d(TAG, " device null, Life sucks so bad");
        bluetoothService.connect(device, false);  // false stands for insecure
    }
}