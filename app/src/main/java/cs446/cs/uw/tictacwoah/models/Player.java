package cs446.cs.uw.tictacwoah.models;


import android.media.JetPlayer;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private Integer id;
    private Integer colour;
    private List<Object> objects;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getColour() {
        return colour;
    }

    public void setColour(Integer colour) {
        this.colour = colour;
    }

    public List<Object> getObjects() {
        return objects;
    }


    // constructor
    public Player(Integer id, Integer colour, Integer shape) {
        this.id = id;
        this.colour = colour;
        List<Object> objects = new ArrayList<>();
        for (int i=0;i<3;i++)
            objects.add(new Object(id,1,colour,shape));
        for (int i=0;i<3;i++)
            objects.add(new Object(id,2,colour,shape));
        for (int i=0;i<3;i++)
            objects.add(new Object(id,3,colour,shape));
        this.objects = objects;
    }
}
