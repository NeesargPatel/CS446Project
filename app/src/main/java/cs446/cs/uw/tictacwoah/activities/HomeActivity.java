package cs446.cs.uw.tictacwoah.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import cs446.cs.uw.tictacwoah.R;
import cs446.cs.uw.tictacwoah.activityModels.GamePlayModel;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        final Button singlePlayerButton = findViewById(R.id.single_player_button);
        singlePlayerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onSingleBtn(v);
            }
        });

        final Button multiPlayerButton = findViewById(R.id.multi_player_button);
        multiPlayerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onBlueBtn(v);
            }
        });
    }

    @Override
    protected  void onStart() {
        super.onStart();
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
    }

    public void onSingleBtn(View view) {
        Intent singlePlayer = new Intent(this, GamePlayActivity.class);
        singlePlayer.putExtra(GamePlayModel.GAME_MODE_KEY, GamePlayModel.SINGLE_MODE);
        startActivity(singlePlayer);
    }

    public void onBlueBtn(View view) {
        Intent bluetooth = new Intent(this, BluetoothActivity.class);
        startActivity(bluetooth);
    }
}
