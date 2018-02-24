package cs446.cs.uw.tictacwoah.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import cs446.cs.uw.tictacwoah.R;
import cs446.cs.uw.tictacwoah.models.Board;
import cs446.cs.uw.tictacwoah.models.Player;

public class GameplayActivity extends AppCompatActivity {

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
    }

    @Override
    protected void onStart() {
        super.onStart();
        Integer gameMode = getIntent().getIntExtra("gameMode", 0);

        // get number of players here from Home?
        Integer numPlayers;
        List<Player> players = new ArrayList<>();
        for (int i=0; i<numPlayers; i++)
            players.add(new Player(i));
        Board board = new Board(numPlayers);
        board.setCurrPlayer(0);
    }
}
