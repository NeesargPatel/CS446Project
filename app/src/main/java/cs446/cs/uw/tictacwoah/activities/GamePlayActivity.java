package cs446.cs.uw.tictacwoah.activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

import cs446.cs.uw.tictacwoah.R;
import cs446.cs.uw.tictacwoah.activityModels.GameModel;
import cs446.cs.uw.tictacwoah.activityModels.HotseatGameModel;
import cs446.cs.uw.tictacwoah.activityModels.ServerGameModel;
import cs446.cs.uw.tictacwoah.models.AudioClip;
import cs446.cs.uw.tictacwoah.models.Board;
import cs446.cs.uw.tictacwoah.models.Piece;
import cs446.cs.uw.tictacwoah.views.BoardView;
import cs446.cs.uw.tictacwoah.views.piece.PieceView;
import cs446.cs.uw.tictacwoah.views.piece.TurnIndicator;

public class GamePlayActivity extends AppCompatActivity implements Observer{

    // margin between TurnIndicators
    private final int marginTI = TurnIndicator.WIDTH;
    private final int WARNING_TIME = 5;
    private final int MILLIS_PER_SECOND = 1000;
    private final int TEXT_SIZE = 40, TEXT_MARGIN_TOP = 50, TEXT_MARGIN_RIGHT = 200;

    private final CharSequence initialButtonText = "Start";
    private final CharSequence reStartButtonText = "Restart";

    private GameModel model;
    private Piece lastPlacedPiece;
    private Integer curPlayer;
    private boolean gameOver;
    private CountDownTimer countDownTimer;

    private RelativeLayout rootLayout;
    private BoardView boardView;
    private Button restartButton;

    private PieceView[][][] boardPieces;
    private List<TurnIndicator> turnIndicators;
    private TextView countdownTextView;

    // for dragging pieces
    private float origX, origY, dX, dY;

    public static File cacheDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        cacheDir = getExternalCacheDir();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameplay);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );

        Bundle bundle = getIntent().getExtras();
        GameModel.GameMode gameMode = (GameModel.GameMode) bundle.getSerializable(GameModel.GAME_MODE_KEY);
        Boolean isHost = bundle.getBoolean(GameModel.HOST_KEY);
        model = GameModel.getInstance(gameMode, isHost);

        assert model != null;
        model.addObserver(this);
        gameOver = true;
        lastPlacedPiece = null;
        curPlayer = null;
        countDownTimer = new CountDownTimer(model.getSetting().getTimeLimit() * MILLIS_PER_SECOND, MILLIS_PER_SECOND) {
            @Override
            public void onTick(long millisUntilFinished) {
                long secondsUntilFinished = millisUntilFinished / MILLIS_PER_SECOND;
                if (secondsUntilFinished <= WARNING_TIME){
                    countdownTextView.setTextColor(Color.RED);
                }
                else {
                    countdownTextView.setTextColor(Color.BLACK);
                }
                String text = String.format(Locale.CANADA, "%d", secondsUntilFinished);
                countdownTextView.setText(text);
            }

            @Override
            public void onFinish() {
                countdownTextView.setText("0");
                if (model.isMyTurn()){
                    model.AIPlacePiece();
                }
            }
        };

        rootLayout = findViewById(R.id.thisisalayout);

        int marginTop = marginTI * 2 + TurnIndicator.WIDTH;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float cellWidth = displayMetrics.widthPixels / Board.SIZE;
        boardView = new BoardView(this, marginTop, cellWidth);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        boardView.setLayoutParams(layoutParams);
        rootLayout.addView(boardView);

        restartButton = new Button(this);
        restartButton.setText(initialButtonText);
        restartButton.setY(boardView.MARGIN_TOP + boardView.getCellWidth() * (Board.SIZE + 1));

        layoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        restartButton.setLayoutParams(layoutParams);
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                model.newGame();
            }
        });
        rootLayout.addView(restartButton);
        if (!model.hasRightToStartGame()) restartButton.setVisibility(View.GONE);

        createDraggablePieces();

        boardPieces = new PieceView[PieceView.SIZES.length][Board.SIZE][Board.SIZE];

        turnIndicators = new LinkedList<>();
        inflateTurnIndicators();

        countdownTextView = new TextView(this);
        countdownTextView.setTextSize(TEXT_SIZE);
        countdownTextView.setText(String.format(Locale.CANADA, "%d", model.getSetting().getTimeLimit()));
        countdownTextView.setX(boardView.getCellWidth() * Board.SIZE - TEXT_MARGIN_RIGHT);
        countdownTextView.setY(TEXT_MARGIN_TOP);
        layoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        countdownTextView.setLayoutParams(layoutParams);
        rootLayout.addView(countdownTextView);

        if (model instanceof ServerGameModel){
            ((ServerGameModel)(model)).startListening();
        }

        // Record to the external cache directory for visibility
        defaultFileName = getExternalCacheDir().getAbsolutePath();
        defaultFileName += "/cachedAudioForSending.3gp";

        // Request permissions for audio recording
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        sendAudioButton = new RecordButton(this);
        sendAudioButton.setY(0);
        sendAudioButton.setLayoutParams(layoutParams);
        //rootLayout.addView(sendAudioButton);
    }

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static String defaultFileName = null;

    private RecordButton sendAudioButton = null;
    private MediaRecorder mediaRecorder = null;

    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};

    @Override
    public void onDestroy() {
        super.onDestroy();
        model.reset();
        // If the user presses the back button, and we let it continue to countdown
        // in onFinish(), it will invoke model.isMyTurn(),
        // and in isMyTurn(), equals() will be invoked on model.curPlayer,
        // but model.curPlayerId has been set to null in model.reset(),
        // so this app will crash
        countDownTimer.cancel();
    }

    private void createDraggablePieces(){
        float cellWidth = boardView.getCellWidth();
        // For each size, I create 2 pieces,
        // so when user drags a piece, there will still be another piece on the UI
        for (int j = 0; j < 2; ++j) {
            for (int i = 0; i < PieceView.SIZES.length; ++i) {
                float offset = (cellWidth - PieceView.SIZES[i]) / 2;
                float x = cellWidth * i + offset;
                float y = boardView.MARGIN_TOP + cellWidth * Board.SIZE + offset;
                PieceView view = PieceView.getPieceView(this, (int)x, (int)y,
                        PieceView.SIZES[i], PieceView.COLORS[model.getMyPlayerId()], model.getSetting().getShape());
                rootLayout.addView(view);

                final int sizeId = i;
                view.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent event) {
                        int curAction = event.getActionMasked();
                        Integer rowId;
                        Integer colId;
                        switch (curAction) {
                            case MotionEvent.ACTION_DOWN:
                                origX = view.getX();
                                origY = view.getY();
                                // view.getX() refers to the top-left point of the view
                                // while event.getRawX() is the point where the cursor is
                                dX = view.getX() - event.getRawX();
                                dY = view.getY() - event.getRawY();
                                break;

                            case MotionEvent.ACTION_MOVE:
                                view.setY(event.getRawY() + dY);
                                view.setX(event.getRawX() + dX);
                                break;

                            case MotionEvent.ACTION_UP:
                                rowId = boardView.getRowId(getCenterXCoordinate(view));
                                colId = boardView.getColId(getCenterYCoordinate(view));

                                // the view was not on the board when the user finished dragging
                                if (rowId == null || colId == null){
                                    break;
                                }

                                // The piece is dragged onto the board, but it's not the user's turn
                                if (!model.isMyTurn()){
                                    Toast toast = Toast.makeText(getApplicationContext(),
                                            "It's not your turn", Toast.LENGTH_SHORT);
                                    toast.show();
                                    break;
                                }

                                Piece piece = new Piece(model.getMyPlayerId(), sizeId, rowId, colId);
                                if (!model.placePiece(piece)){
                                    Toast toast = Toast.makeText(getApplicationContext(),
                                            "This space is occupied.", Toast.LENGTH_SHORT);
                                    toast.show();
                                }

                                break;
                            default:
                                return false;
                        }
                        // we need to reset the position of the draggable piece
                        // when user finished dragging
                        if (curAction == MotionEvent.ACTION_UP){
                            view.setX(origX);
                            view.setY(origY);
                        }
                        return true;
                    }
                });
            }
        }
    }

    private float getCenterXCoordinate (View view) {
        return view.getX() + view.getWidth() / 2;
    }

    private float getCenterYCoordinate (View view) {
        return view.getY() + view.getHeight() / 2;
    }

    private void newGame() {
        restartButton.setVisibility(View.GONE);
        for (int i = 0; i < boardPieces.length; ++i){
            for (int j = 0; j < boardPieces[i].length; ++j){
                for (int k = 0; k < boardPieces[i][j].length; ++k){
                    if (boardPieces[i][j][k] != null){
                        rootLayout.removeView(boardPieces[i][j][k]);
                        boardPieces[i][j][k] = null;
                    }
                }
            }
        }
        countdownTextView.setText(String.format(Locale.CANADA, "%d", model.getSetting().getTimeLimit()));
        lastPlacedPiece = null;
        // we have to set it to null before invoke reset() on model
        // because model will then notify this Activity to update
        // and in update(), curPlayer will be compared to model.getCurPlayer()
        curPlayer = null;
        countDownTimer.start();
    }

    @Override
    public void update(Observable observable, Object object) {

        // when number of players increase
        inflateTurnIndicators();

        if (model.isGaming()){
            if (gameOver){
                gameOver = false;
                newGame();
            }

            Piece piece = model.getLastPlacedPiece();
            if (piece != null && lastPlacedPiece != piece){
                drawPieceOnBoard(piece);
                lastPlacedPiece = piece;
            }

            // because curPlay may have not been initialized (null)
            // so I invoke equals() on model.getCurPlayer()
            if (!model.getCurPlayer().equals(curPlayer)){
                changeTurn();
                countDownTimer.start();
            }
        }
        // check if game is over and show the winning pattern
        else if (model.isGameOver()){
            turnIndicators.get(curPlayer).stopAnimation();
            countDownTimer.cancel();
            showWinningPattern(model.getWinningPattern());
            showWinOrLoseMessage(model.getWinningPattern()[0].getId());
            restartButton.setText(reStartButtonText);
            if (model.hasRightToStartGame()) restartButton.setVisibility(View.VISIBLE);
            gameOver = true;
        }
        // else the game has not started
    }

    private void inflateTurnIndicators(){
        while (turnIndicators.size() < model.getNumPlayers()){
            int index = turnIndicators.size();  // the index of the TurnIndicator being added
            int x = marginTI * (index + 1) + TurnIndicator.WIDTH * index;
            int y = marginTI;
            int color = PieceView.COLORS[index];
            TurnIndicator turnIndicator = new TurnIndicator(this, x, y, color);
            turnIndicators.add(turnIndicator);
            rootLayout.addView(turnIndicator);
        }
    }

    private void changeTurn(){
        if (curPlayer != null){
            // stop the animation because the turn changes
            turnIndicators.get(curPlayer).stopAnimation();
        }
        // Since Integer is mutable, so we can directly use assignment operator here
        // (assign the underlying value of model.getCurPlayer() to curPlayer)
        curPlayer = model.getCurPlayer();
        if (model instanceof HotseatGameModel)
            createDraggablePieces();
        turnIndicators.get(curPlayer).startAnimation();
    }

    private void drawPieceOnBoard(Piece piece){
        int sizeId = piece.getSizeId();
        int rowId = piece.getRowId();
        int colId = piece.getColId();
        float cellWidth = boardView.getCellWidth();

        float offset = (cellWidth - PieceView.SIZES[sizeId]) / 2;
        float x = cellWidth * rowId + offset;
        float y = boardView.MARGIN_TOP + cellWidth * colId + offset;

        PieceView view = PieceView.getPieceView(this, (int)x, (int)y,
                PieceView.SIZES[sizeId], PieceView.COLORS[piece.getId()], model.getSetting().getShape());
        rootLayout.addView(view);
        boardPieces[sizeId][rowId][colId] = view;
    }

    private void showWinningPattern(Piece[] pieces){
        for (Piece p : pieces){
            boardPieces[p.getSizeId()][p.getRowId()][p.getColId()].startAnimation();
        }
    }

    public void showWinOrLoseMessage(Integer winnerId){
        String msg = "";
        if (model instanceof HotseatGameModel)
            msg = "Player " + Integer.toString(winnerId + 1) + " wins! ";
        else
            msg = (winnerId.equals(model.getMyPlayerId())) ? "You win. " : "You lose. ";
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
    // function to get permissions for audio recording
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) finish();

    }

    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void startRecording() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(defaultFileName);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            Log.e("myTag", "prepare() failed");
        }

        mediaRecorder.start();
    }

    private void stopRecording() {
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
        File file = new File(defaultFileName);
        byte[] b = new byte[(int) file.length()];
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(b);
            Log.d("myTag","sending mPlayer");
            AudioClip audioClipForSending = new AudioClip(model.getMyPlayerId(),b);
            model.sendAudio(audioClipForSending);
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found.");
            e.printStackTrace();
        }
        catch (IOException e1) {
            System.out.println("Error Reading The File.");
            e1.printStackTrace();
        }
    }

    class RecordButton extends android.support.v7.widget.AppCompatButton {
        boolean mStartRecording = true;

        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                onRecord(mStartRecording);
                if (mStartRecording) {
                    setText("Stop recording");
                } else {
                    setText("Start recording");
                }
                mStartRecording = !mStartRecording;
            }
        };

        public RecordButton(Context ctx) {
            super(ctx);
            setText("Start recording");
            setOnClickListener(clicker);
        }
    }
}