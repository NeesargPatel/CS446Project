package cs446.cs.uw.tictacwoah.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import cs446.cs.uw.tictacwoah.R;
import cs446.cs.uw.tictacwoah.activityModels.GameModel;
import cs446.cs.uw.tictacwoah.models.AI;
import cs446.cs.uw.tictacwoah.models.Setting;
import cs446.cs.uw.tictacwoah.views.piece.PieceView;

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

    private Spinner shapeSpinner, timeLimitSpinner, AILevelSpinner, numberOfAISpinner, numberOfHsPlayersSpinner;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
        setContentView(R.layout.activity_setting);

        setting = new Setting();

        TextView AILevelTextView = findViewById(R.id.text_view_AI_level);
        TextView numberOfAITextView = findViewById(R.id.text_view_number_of_AI);
        TextView numberOfHsPlayersView = findViewById(R.id.text_view_number_of_players);

        shapeSpinner = findViewById(R.id.spinner_shape);
        timeLimitSpinner = findViewById(R.id.spinner_time_limit);
        AILevelSpinner = findViewById(R.id.spinner_AI_level);
        numberOfAISpinner = findViewById(R.id.spinner_number_of_AI);
        numberOfHsPlayersSpinner = findViewById(R.id.spinner_number_of_players);
        shapeSpinner.setOnItemSelectedListener(this);
        timeLimitSpinner.setOnItemSelectedListener(this);
        AILevelSpinner.setOnItemSelectedListener(this);
        numberOfAISpinner.setOnItemSelectedListener(this);
        numberOfHsPlayersSpinner.setOnItemSelectedListener(this);
        inflateSpinners();

        ImageView playButton = findViewById(R.id.play_button);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickPlayButton();
            }
        });

        GameModel.GameMode gameMode = (GameModel.GameMode)
                getIntent().getExtras().getSerializable(GameModel.GAME_MODE_KEY);
        if (gameMode.equals(GameModel.GameMode.SINGLE)) {
            numberOfHsPlayersView.setVisibility(View.GONE);
            numberOfHsPlayersSpinner.setVisibility(View.GONE);
        }
        else if (gameMode.equals(GameModel.GameMode.MULTI_PLAYER)){
            AILevelTextView.setVisibility(View.GONE);
            numberOfAITextView.setVisibility(View.GONE);
            AILevelSpinner.setVisibility(View.GONE);
            numberOfAISpinner.setVisibility(View.GONE);
            numberOfHsPlayersView.setVisibility(View.GONE);
            numberOfHsPlayersSpinner.setVisibility(View.GONE);
        }
        else if(gameMode.equals(GameModel.GameMode.HOTSEAT)) {
            AILevelTextView.setVisibility(View.GONE);
            numberOfAITextView.setVisibility(View.GONE);
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
            case R.id.spinner_number_of_players:
                setting.setNumHsPlayers(key, Integer.parseInt(selectedString));
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
        setSpinnerArrayAndSelectDefaultOption(shapeSpinner, spinnerArray, Setting.defulatShape.toString());

        spinnerArray = new ArrayList<>();
        int interval = GameModel.TIME_LIMIT_INTERVAL;
        int max = GameModel.MAX_TIME_LIMIT;
        for (int i = interval; i <= max; i += interval){
            spinnerArray.add(Integer.toString(i));
        }
        setSpinnerArrayAndSelectDefaultOption(timeLimitSpinner, spinnerArray, Integer.toString(Setting.defaultTimeLimit));

        spinnerArray = new ArrayList<>();
        for (AI.LEVEL level : AI.LEVEL.values()){
            spinnerArray.add(level.toString());
        }
        setSpinnerArrayAndSelectDefaultOption(AILevelSpinner, spinnerArray, Setting.defaultAILevel.toString());

        spinnerArray = new ArrayList<>();
        for (int i = 1; i <= GameModel.MAX_AI_PLAYERS; ++i){
            spinnerArray.add(Integer.toString(i));
        }
        setSpinnerArrayAndSelectDefaultOption(numberOfAISpinner, spinnerArray, Integer.toString(Setting.defaultNumAIs));

        spinnerArray = new ArrayList<>();
        for (int i = 1; i <= GameModel.MAX_HS_PLAYERS; ++i){
            spinnerArray.add(Integer.toString(i));
        }
        setSpinnerArrayAndSelectDefaultOption(numberOfHsPlayersSpinner, spinnerArray, Integer.toString(Setting.defaultNumHsPlayers));
    }

    private void setSpinnerArrayAndSelectDefaultOption(Spinner spinner, List<String> items, String option){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(adapter.getPosition(option));
    }

    private void onClickPlayButton(){
        Intent intent = new Intent(this, GamePlayActivity.class);

        GameModel.GameMode gameMode = (GameModel.GameMode)
                getIntent().getExtras().getSerializable(GameModel.GAME_MODE_KEY);
        intent.putExtra(GameModel.GAME_MODE_KEY, gameMode);

        GameModel model;  // The model we're going to use in this game
        if (gameMode.equals(GameModel.GameMode.MULTI_PLAYER)){
            intent.putExtra(GameModel.HOST_KEY, true);  // Only the host will be in this Activity
            model = GameModel.getInstance(gameMode, true);
        } else {  // single mode
            model = GameModel.getInstance(gameMode, null);
        }
        model.init(setting);

        startActivity(intent);
        // Set result and finish this Activity
        setResult(Activity.RESULT_OK);
        finish();
    }
}
