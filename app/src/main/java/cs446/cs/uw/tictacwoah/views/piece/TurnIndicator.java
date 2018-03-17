package cs446.cs.uw.tictacwoah.views.piece;

import android.content.Context;
import android.graphics.Paint;

/**
 * Created by ASUS on 2018/3/6.
 */

public class TurnIndicator extends CirclePieceView {

    public static final int WIDTH = PieceView.SIZES[0];

    public TurnIndicator(Context context, int x, int y, int color){
        super(context, x, y, WIDTH, color);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
    }
}
