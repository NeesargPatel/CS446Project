package cs446.cs.uw.tictacwoah.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import cs446.cs.uw.tictacwoah.R;
import cs446.cs.uw.tictacwoah.activityModels.GamePlayModel;
import cs446.cs.uw.tictacwoah.models.Piece;

import static cs446.cs.uw.tictacwoah.R.id.board;

public class GamePlayActivity extends AppCompatActivity implements Observer{

    private GamePlayModel model;

    private RelativeLayout rootLayout;
    private BoardView boardView;
    private Button restartButton;

    private Drawable[] drawables;
    private int[] pieceSizes;
    float widthOfSquare;

    private List<View> boardPieces;  // clear these views for a new game

    // for dragging pieces
    private int lastAction;
    private float origX, origY, dX, dY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        model = new GamePlayModel(getIntent(), getApplicationContext());
        model.addObserver(this);

        rootLayout = findViewById(R.id.thisisalayout);
        boardView = findViewById(R.id.board);
        restartButton = findViewById(R.id.restart_button);

        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newGame();
            }
        });

        drawables = new Drawable[model.getNumPlayers()];
        drawables[0] = getResources().getDrawable(R.drawable.square_blue);
        drawables[1] = getResources().getDrawable(R.drawable.sqaure_green);

        // I don't know why I can't use array initialization here
        pieceSizes = new int[3];
        pieceSizes[0] = 80;
        pieceSizes[1] = 140;
        pieceSizes[2] = 200;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        widthOfSquare = displayMetrics.widthPixels / 3;

        float heightOfScreen = displayMetrics.heightPixels;
        createDraggablePieces(heightOfScreen);

        boardPieces = new LinkedList<>();
    }

    private void createDraggablePieces(float heightOfScreen){
        // For each size, I create 2 pieces,
        // so when user drags a piece, there will still be another piece on the UI
        for (int j = 0; j < 2; ++j) {
            for (int i = 0; i < pieceSizes.length; ++i) {
                ImageView imageView = new ImageView(this);
                imageView.setImageDrawable(drawables[model.getMyPlayerId()]);

                float offset = (widthOfSquare - pieceSizes[i]) / 2;
                imageView.setX(widthOfSquare * i + offset);
                imageView.setY(heightOfScreen - widthOfSquare + offset);

                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(pieceSizes[i], pieceSizes[i]);
                imageView.setLayoutParams(layoutParams);
                rootLayout.addView(imageView);

                imageView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent event) {
                        int sizeId = -1;
                        int xCoord = 0;
                        int yCoord = 0;
                        switch (event.getActionMasked()) {
                            case MotionEvent.ACTION_DOWN:
                                origX = view.getX();
                                origY = view.getY();
                                dX = view.getX() - event.getRawX();
                                dY = view.getY() - event.getRawY();
                                lastAction = MotionEvent.ACTION_DOWN;
                                break;

                            case MotionEvent.ACTION_MOVE:
                                view.setY(event.getRawY() + dY);
                                view.setX(event.getRawX() + dX);
                                lastAction = MotionEvent.ACTION_MOVE;
                                break;

                            case MotionEvent.ACTION_UP:
                                if (getCenterYCoordinate(view) < widthOfSquare) {
                                    view.setY(widthOfSquare / 2 - view.getWidth() / 2);
                                    yCoord = 0;
                                } else if (getCenterYCoordinate(view) > widthOfSquare && getCenterYCoordinate(view) < widthOfSquare * 2) {
                                    view.setY(widthOfSquare * (float) 1.5 - view.getWidth() / 2);
                                    yCoord = 1;
                                } else if (getCenterYCoordinate(view) > widthOfSquare * 2 && getCenterYCoordinate(view) < widthOfSquare * 3) {
                                    view.setY(widthOfSquare * (float) 2.5 - view.getWidth() / 2);
                                    yCoord = 2;
                                } else {  // if the center of this piece is not in the board
                                    view.setX(origX);
                                    view.setY(origY);
                                    break;
                                }

                                // The piece is dragged onto the board, but it's not the user's turn
                                if (!model.isMyTurn()){
                                    view.setX(origX);
                                    view.setY(origY);
                                    Toast toast = Toast.makeText(getApplicationContext(), "It's not your turn", Toast.LENGTH_SHORT);
                                    toast.show();
                                    break;
                                }

                                if (getCenterXCoordinate(view) < widthOfSquare) {
                                    view.setX(widthOfSquare / 2 - view.getWidth() / 2);
                                    xCoord = 0;
                                } else if (getCenterXCoordinate(view) > widthOfSquare && getCenterXCoordinate(view) < widthOfSquare * 2) {
                                    view.setX(widthOfSquare * (float) 1.5 - view.getWidth() / 2);
                                    xCoord = 1;
                                } else if (getCenterXCoordinate(view) > widthOfSquare * 2) {
                                    view.setX(widthOfSquare * (float) 2.5 - view.getWidth() / 2);
                                    xCoord = 2;
                                }

                                // This part should be refactored
                                int size = view.getLayoutParams().width;
                                for (int i = 0; i < pieceSizes.length; ++i){
                                    if (size == pieceSizes[i]) sizeId = i;
                                }

                                if (!model.placePiece(new Piece(model.getMyPlayerId(), sizeId, xCoord, yCoord))){
                                    Toast toast = Toast.makeText(getApplicationContext(), "This space is occupied.", Toast.LENGTH_SHORT);
                                    toast.show();
                                }

                                view.setX(origX);
                                view.setY(origY);

                                break;
                            default:
                                return false;
                        }
                        return true;
                    }
                });
            }
        }
    }

    private float getCenterXCoordinate (View view) {
        return view.getX() + view.getWidth()  / 2;
    }

    private float getCenterYCoordinate (View view) {
        return view.getY() + view.getHeight()  / 2;
    }

    @Override
    protected void onResume(){

        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_new_game) {
            newGame();
        }
        return super.onOptionsItemSelected(item);
    }

        @Override
    public void onDestroy() {
        super.onDestroy();
        model.closeBltConn();
    }

    private void newGame() {
        restartButton.setVisibility(View.GONE);
        boardView.invalidate();
        for (View v: boardPieces) rootLayout.removeView(v);
        model.reset();
    }

    @Override
    public void update(Observable observable, Object object) {

        Piece piece = model.getLastPlacedPiece();
        drawPieceOnBoard(piece, drawables[piece.getId()]);

        // check if game is over and show the winning pattern
        if (model.isGameOver()){
            drawWinningPattern(model.getWinningPattern());
            showWinOrLoseMessage(model.getWinningPattern()[0].getId());
            restartButton.setVisibility(View.VISIBLE);
        }
    }

    private void drawPieceOnBoard(Piece piece, Drawable drawable){
        ImageView imageView = new ImageView(this);
        imageView.setImageDrawable(drawable);

        int sizeId = piece.getSize();
        float offset = (widthOfSquare - pieceSizes[sizeId]) / 2;
        imageView.setX(widthOfSquare * piece.getRowId() + offset);
        imageView.setY(widthOfSquare * piece.getColId() + offset);

        int size = pieceSizes[sizeId];
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(size, size);
        imageView.setLayoutParams(layoutParams);
        rootLayout.addView(imageView);

        boardPieces.add(imageView);
    }

    private void drawWinningPattern(Piece[] pieces){
        // use a different color to emphasize the winning pattern
        Drawable drawable = getResources().getDrawable(R.drawable.square_red);
        for (int i = 0; i < pieces.length; ++i){
            drawPieceOnBoard(pieces[i], drawable);
        }
    }

    public void showWinOrLoseMessage(Integer winnerId){
        String msg = (winnerId.equals(model.getMyPlayerId())) ? "You win. " : "You lose. ";
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}