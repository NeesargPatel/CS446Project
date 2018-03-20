package cs446.cs.uw.tictacwoah.activities;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import cs446.cs.uw.tictacwoah.R;
import cs446.cs.uw.tictacwoah.activityModels.GameModel;

public class BluetoothActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 3;

    private BluetoothAdapter bluetoothAdapter;
    private Button createButton, joinButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        createButton = findViewById(R.id.create_button);
        createButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onClickBtn(v, true);  // host is the one that creates room
            }
        });
        joinButton = findViewById(R.id.join_button);
        joinButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onClickBtn(v, false);
            }
        });
    }

    public void onClickBtn(View view, Boolean isHost) {
        // assume that this device has blue tooth functionality,
        // so bluetoothAdapter won't be null
        if (bluetoothAdapter.isEnabled()){
            Intent intent;
            if (view.getId() == R.id.join_button){
                // go to lobby to find a room to join
                intent = new Intent(this, LobbyActivity.class);
            }
            // go to adjust the settings for the game the user is going to host
            else intent = new Intent(this, SettingActivity.class);

            intent.putExtra(GameModel.GAME_MODE_KEY, GameModel.GameMode.MULTI_PLAYER);
            intent.putExtra(GameModel.HOST_KEY, isHost);
            startActivity(intent);
        }
        else {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }

        // Set result and finish this Activity
        setResult(Activity.RESULT_OK);
        finish();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ENABLE_BT: {
                // When the request to enable Bluetooth returns
                if (resultCode != Activity.RESULT_OK) {
                    Toast.makeText(getApplicationContext(), "you have to enable bluetooth",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
