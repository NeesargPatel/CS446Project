package cs446.cs.uw.tictacwoah.models;

import java.io.Serializable;

import cs446.cs.uw.tictacwoah.activities.SettingActivity;
import cs446.cs.uw.tictacwoah.views.piece.PieceView;

/**
 * Created by ASUS on 2018/3/6.
 */

// This class stores the game settings
public class Setting implements Serializable {

    public static final String SETTING_KEY = "setting_key";

    public static final PieceView.SHAPE defulatShape = PieceView.SHAPE.CIRCLE;
    public static final int defaultTimeLimit = 15;
    public static final AI.LEVEL defaultAILevel = AI.LEVEL.EASY;
    public static final int defaultNumAIs = 1;
    public static final int defaultNumHsPlayers = 1;        // default # of hotseat players

    private PieceView.SHAPE shape;
    private int timeLimit;
    private AI.LEVEL level;
    private int numAIs;
    private int numHsPlayers;     // number of players in hotseat mode

    // default settings
    public Setting(){
        shape = defulatShape;
        timeLimit = defaultTimeLimit;
        level = defaultAILevel;
        numAIs = defaultNumAIs;
        numHsPlayers = defaultNumHsPlayers;
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

    public int getNumHsPlayers() {
        return numHsPlayers;
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

    public void setNumHsPlayers(SettingActivity.KeyToChangeSetting key, int numHsPlayers) {
        if (key != null)    this.numHsPlayers = numHsPlayers;
    }
}
