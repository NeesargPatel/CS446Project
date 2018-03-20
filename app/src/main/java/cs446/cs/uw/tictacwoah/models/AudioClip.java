package cs446.cs.uw.tictacwoah.models;

/**
 * Created by Neesarg on 3/18/2018.
 */

public class AudioClip {

    /*
    Each Piece gets an id which can be easily differentiated for each player...
        range from 1 to n (number of players)
     */
    private final Integer playerId;
    private final byte[] audioClip;

    // constructor
    public AudioClip(Integer playerId, byte[] audioClip) {
        this.playerId = playerId;
        this.audioClip = audioClip;
    }

    public Integer getId() {
        return playerId;
    }
    public byte[] getAudioClip() { return audioClip; }
}
