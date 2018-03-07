package cs446.cs.uw.tictacwoah.models;

import java.io.Serializable;

import cs446.cs.uw.tictacwoah.activities.SettingActivity;
import cs446.cs.uw.tictacwoah.views.PieceView;

/**
 * Created by ASUS on 2018/3/6.
 */

// This class stores the game settings
public class Setting implements Serializable {

    public static final String SETTING_KEY = "setting_key";

    private PieceView.SHAPE shape;
    private int timeLimit;
    private AI.LEVEL level;
    private int numAIs;

    // default settings
    public Setting(){
        shape = PieceView.SHAPE.CIRCLE;
        timeLimit = 15;
        level = AI.LEVEL.EASY;
        numAIs = 1;
    }

    public PieceView.SHAPE getShape() {
        return shape;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public AI.LEVEL getLevel() {
        return level;
    }

    public int getNumAIs() {
        return numAIs;
    }

    public void setShape(SettingActivity.KeyToChangeSetting key, PieceView.SHAPE shape) {
        if (key != null)    this.shape = shape;
    }

    public void setTimeLimit(SettingActivity.KeyToChangeSetting key, int timeLimit) {
        if (key != null)    this.timeLimit = timeLimit;
    }

    public void setLevel(SettingActivity.KeyToChangeSetting key, AI.LEVEL level) {
        if (key != null)    this.level = level;
    }

    public void setNumAIs(SettingActivity.KeyToChangeSetting key, int numAIs) {
        if (key != null)    this.numAIs = numAIs;
    }
}
