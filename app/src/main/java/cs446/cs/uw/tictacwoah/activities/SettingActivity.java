package cs446.cs.uw.tictacwoah.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cs446.cs.uw.tictacwoah.R;
import cs446.cs.uw.tictacwoah.activityModels.GamePlayModel;
import cs446.cs.uw.tictacwoah.models.AI;
import cs446.cs.uw.tictacwoah.models.Setting;
import cs446.cs.uw.tictacwoah.views.PieceView;

/**
 * Created by ASUS on 2018/3/6.
 */

public class SettingActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    // to simulate friend keyword in C++
    public static final class KeyToChangeSetting{
        private KeyToChangeSetting(){}
    }
    private static final KeyToChangeSetting key = new KeyToChangeSetting();

    private Setting setting;

    private Spinner shapeSpinner, timeLimitSpinner, AILevelSpinner, numberOfAISpinner;
    private Button playButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        setting = new Setting();

        shapeSpinner = findViewById(R.id.spinner_shape);
        timeLimitSpinner = findViewById(R.id.spinner_time_limit);
        AILevelSpinner = findViewById(R.id.spinner_AI_level);
        numberOfAISpinner = findViewById(R.id.spinner_number_of_AI);
        shapeSpinner.setOnItemSelectedListener(this);
        timeLimitSpinner.setOnItemSelectedListener(this);
        AILevelSpinner.setOnItemSelectedListener(this);
        numberOfAISpinner.setOnItemSelectedListener(this);
        inflateSpinners();

        playButton = findViewById(R.id.play_button);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickPlayButton();
            }
        });

        String gameMode = getIntent().getExtras().getString(GamePlayModel.GAME_MODE_KEY);
        if (gameMode.equals(GamePlayModel.MULTIPLAYER_MODE)){
            AILevelSpinner.setVisibility(View.GONE);
            numberOfAISpinner.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedString = parent.getItemAtPosition(position).toString();
        switch (parent.getId()){
            case R.id.spinner_shape:
                setting.setShape(key, PieceView.SHAPE.valueOf(selectedString));
                break;
            case R.id.spinner_time_limit:
                setting.setTimeLimit(key, Integer.parseInt(selectedString));
                break;
            case R.id.spinner_AI_level:
                setting.setLevel(key, AI.LEVEL.valueOf(selectedString));
                break;
            case R.id.spinner_number_of_AI:
                setting.setNumAIs(key, Integer.parseInt(selectedString));
                break;
            default:
                // should not in here
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void inflateSpinners(){
        List<String> spinnerArray =  new ArrayList<>();
        for (PieceView.SHAPE shape : PieceView.SHAPE.values()){
            spinnerArray.add(shape.toString());
        }
        setSpinnerArray(shapeSpinner, spinnerArray);

        spinnerArray = new ArrayList<>();
        int interval = GamePlayModel.TIME_LIMIT_INTERVAL;
        int max = GamePlayModel.MAX_TIME_LIMIT;
        for (int i = interval; i <= max; i += interval){
            spinnerArray.add(Integer.toString(i));
        }
        setSpinnerArray(timeLimitSpinner, spinnerArray);

        spinnerArray = new ArrayList<>();
        for (AI.LEVEL level : AI.LEVEL.values()){
            spinnerArray.add(level.toString());
        }
        setSpinnerArray(AILevelSpinner, spinnerArray);

        spinnerArray = new ArrayList<>();
        for (int i = 1; i <= GamePlayModel.MAX_AI_PLAYERS; ++i){
            spinnerArray.add(Integer.toString(i));
        }
        setSpinnerArray(numberOfAISpinner, spinnerArray);
    }

    private void setSpinnerArray(Spinner spinner, List<String> items){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void onClickPlayButton(){
        Intent intent = new Intent(this, GamePlayActivity.class);
        intent.putExtra(Setting.SETTING_KEY, setting);
        String gameMode = getIntent().getExtras().getString(GamePlayModel.GAME_MODE_KEY);
        intent.putExtra(GamePlayModel.GAME_MODE_KEY, gameMode);
        startActivity(intent);
        // Set result and finish this Activity
        setResult(Activity.RESULT_OK);
        finish();
    }
}
