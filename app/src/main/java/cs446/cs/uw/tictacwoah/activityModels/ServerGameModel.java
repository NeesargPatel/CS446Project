package cs446.cs.uw.tictacwoah.activityModels;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import cs446.cs.uw.tictacwoah.activities.BluetoothActivity;
import cs446.cs.uw.tictacwoah.models.AudioClip;
import cs446.cs.uw.tictacwoah.models.BluetoothService;
import cs446.cs.uw.tictacwoah.models.Piece;
import cs446.cs.uw.tictacwoah.models.Setting;
import cs446.cs.uw.tictacwoah.activities.GamePlayActivity;

/**
 * Created by ASUS on 2018/3/12.
 */

public class ServerGameModel extends MultiPlayerGameModel {

    private static String TAG = "ServerGameModel";

    private static ServerGameModel model = null;
    static ServerGameModel getInstance() {
        if (model == null) model = new ServerGameModel();
        return model;
    }

    private static class GameHandler extends Handler {
        private final ServerGameModel model;

        private GameHandler(ServerGameModel model){
            this.model = model;
        }

        @Override
        public void handleMessage(Message msg) {
            Log.d("myTag", Integer.toString(msg.what));
            switch (msg.what) {
                case BluetoothService.ACCEPT_FAILED:
                    Log.d("myTag","there was an error");
//                    if (!model.isGaming()){
//                        int i = msg.arg1;
//                        if (model.bluetoothServices.size() > i){
//                            BluetoothService b = model.bluetoothServices.get(i);
//                            // maybe one of the insecure or secure accept thread fails
//                            // but the other successfully accepted
//                            if (b.getState() != BluetoothService.STATE_CONNECTED){
//                                // keep listening until the game starts
//                                model.bluetoothServices.get(msg.arg1).start();
//                            }
//                        }
//                    }
                case BluetoothService.FROM_OTHER_DEVICES:
                    Log.d("myTag", "inside FROM_OTHER_DEVICES");

                    if (msg.obj instanceof GameMessage){
                        Log.d("myTag", "inside instanceof GameMessage");
                        GameMessage gameMessage = (GameMessage) msg.obj;
                        GameMessage.Type type = gameMessage.getType();
                        if (type.equals(GameMessage.Type.REQUEST)){

                            model.setNumPlayers(model.getNumPlayers() + 1);

                            gameMessage.setType(GameMessage.Type.ACCEPT);
                            gameMessage.setNumPlayer(model.getNumPlayers());
                            // respond to the client who requested
                            model.bluetoothServices.get(msg.arg1).write(gameMessage);
                            model.bluetoothServices.get(msg.arg1).write(model.getSetting());

                            // broadcast to all the clients that numPlayers has changed
                            gameMessage.setType(GameMessage.Type.UPDATE);
                            for (int i = 0; i < model.bluetoothServices.size(); ++i){
                                if (i != msg.arg1 && model.bluetoothServices.get(i) != null){
                                    model.bluetoothServices.get(i).write(gameMessage);
                                }
                            }

                            if (model.getNumPlayers() < GameModel.MAX_PLAYERS) model.addNewSession();
                        }
                    } else if (msg.obj instanceof Piece){
                        Log.d("myTag", "inside instanceof Piece");
                        Piece piece = (Piece) msg.obj;
                        model.placePiece(piece);
                        // broadcast to all the clients except for the one who placed this piece
                        for (int i = 0;i < model.bluetoothServices.size(); ++i){
                            // -1 because piece.getId() == 0 means host
                            // so bluetoothServices[0] actually means the client with playerID 1
                            if (i != piece.getId() - 1){
                                model.bluetoothServices.get(i).write(piece);
                            }
                        }
                    } else if (msg.obj instanceof AudioClip){
                        Log.d("myTag", "recieved!");
                        AudioClip audioClip = (AudioClip) msg.obj;
                        model.playAudio(audioClip);
                        /*
                        //if (msg.obj instanceof AudioClip) {
                        Log.d("myTag", "GREAT");
                        MediaPlayer mPlayer = (MediaPlayer) msg.obj;
                        mPlayer.start();
                        //}
                        //broadcast to all the clients except for the one who sent the audio
                        for (int i = 0; i < model.bluetoothServices.size(); ++i) {
                            // -1 because audioClip.getId() == 0 means host
                            // so bluetoothServices[0] actually meas the client with playerID 1
                            if (i != audioClip.getId() - 1) {
                                model.bluetoothServices.get(i).write(audioClip);
                            }
                        }
                        */
                    } else {
                        Log.d("myTag", "shouldnt get here");
                    }
                    break;
                default:
                    Log.d("myTag", "defaulted");
                    // we should not run into this block
            }
        }
    }

    private List<BluetoothService> bluetoothServices;
    private GameHandler handler;

    private ServerGameModel(){
        // this will never change for a host,
        // so I put it in the ctor
        myPlayerId = 0;  // 0 for host
        bluetoothServices = new ArrayList<>();
        handler = new GameHandler(this);
    }

    @Override
    public boolean hasRightToStartGame() { return true; }

    @Override
    public void init(Setting setting){
        super.init(setting);
        numPlayers = 1;
    }

    @Override
    public void newGame(){
        super.newGame();
        GameMessage gameMessage = new GameMessage(GameMessage.Type.START_GAME);
        broadcast(gameMessage);
    }

    @Override
    public void reset(){
        super.reset();
        for (BluetoothService bluetoothService : bluetoothServices){
            if (bluetoothService != null) bluetoothService.stop();
        }
        bluetoothServices.clear();
    }

    @Override
    public Boolean placePiece(Piece piece){
        if (super.placePiece(piece)){
            // If this newly placed piece was placed by myself
            if (piece.getId().equals(myPlayerId)){
                broadcast(piece);
            }
            return true;
        }
        return false;
    }

    @Override
    public Boolean sendAudio (AudioClip audioClip) {
        Log.d("myTag", "ClientGameModel sending byte array");
        //bluetoothService.write(audioClip);
        broadcast(audioClip);
        return true;
    }

    public Boolean playAudio(AudioClip audioClip) {
            // If the audio clip was sent by myself

           // if (audioClip.getId().equals(myPlayerId)) {
                broadcast(audioClip);
          //  }


        //}
        //return false;


        byte[] audioFile = audioClip.getAudioClip();
        try {
            String mFileName = GamePlayActivity.cacheDir.getAbsolutePath();
            mFileName += "/audiorecordtest.3gp";
            FileOutputStream out = new FileOutputStream(mFileName);
            out.write(audioFile);
            out.close();
            MediaPlayer mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(mFileName);
            mediaPlayer.prepare();
            mediaPlayer.start();

        } catch (IOException e) {

        }
        return true;
    }

    public void startListening(){
        addNewSession();
    }

    private void addNewSession(){
        BluetoothService bluetoothService = new BluetoothService(handler, bluetoothServices.size());
        bluetoothServices.add(bluetoothService);
        bluetoothService.start();  // start accept threads in bluetoothService
    }

    private void broadcast(Object o){
        // broadcast to all the clients
        for (BluetoothService bluetoothService : bluetoothServices){
            bluetoothService.write(o);
        }
    }
}
