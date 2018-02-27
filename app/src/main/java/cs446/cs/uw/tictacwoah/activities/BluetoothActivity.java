package cs446.cs.uw.tictacwoah.activities;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import cs446.cs.uw.tictacwoah.R;
import cs446.cs.uw.tictacwoah.activityModels.GamePlayModel;

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
                intent = new Intent(this, LobbyActivity.class);
            }
            else intent = new Intent(this, GamePlayActivity.class);

            intent.putExtra(GamePlayModel.HOST_KEY, isHost);  // creator of room is host
            startActivity(intent);
        }
        else {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }
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
