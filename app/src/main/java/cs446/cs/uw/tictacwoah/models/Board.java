package cs446.cs.uw.tictacwoah.models;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private Integer numPlayers;
    private List<Object> objectPos;
    private boolean gameover;
    private Integer winPlayer;


    public Integer getNumPlayers() {
        return numPlayers;
    }

    public void setNumPlayers(Integer numPlayers) {
        this.numPlayers = numPlayers;
    }

    public List<Object> getObjectPos() {
        return objectPos;
    }

    public void setObjectPos(Object objectPos) {
        this.objectPos.add(objectPos);
    }

    public boolean isGameover() {
        return gameover;
    }

    public void setGameover(boolean gameover) {
        this.gameover = gameover;
    }

    public Integer getWinPlayer() {
        return winPlayer;
    }

    public void setWinPlayer(Integer winPlayer) {
        this.winPlayer = winPlayer;
    }

    // constructor
    public Board(Integer num) {
        this.numPlayers = num;
        this.gameover = false;
        this.winPlayer = 0;
        this.objectPos = new ArrayList<>();
    }
}
