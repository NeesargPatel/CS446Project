package cs446.cs.uw.tictacwoah.models;


public class AI extends Player {

    // 0 - random
    private Integer level;

    // constructor
    public AI(Integer id) {
        super(id);
        this.level = 0;
    }

    public AI(Integer id, Integer level) {
        super(id);
        this.level = level;
    }

    // randomly select a piece of certain size to move to a location in board and return Integer[size, row, column]
    public Integer[] setPiece() {

    }
}
