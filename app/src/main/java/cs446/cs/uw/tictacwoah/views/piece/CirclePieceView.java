package cs446.cs.uw.tictacwoah.views.piece;

import android.content.Context;
import android.graphics.Canvas;

/**
 * Created by ASUS on 2018/3/5.
 */

public class CirclePieceView extends PieceView {
    private final float centerX, centerY, radius;

    public CirclePieceView(Context context, int x, int y, int size, int color) {
        super(context, x, y, size, color);
        radius = (size - PieceView.STROKE_WIDTH) / 2;
        centerX = radius + PieceView.STROKE_WIDTH / 2;
        centerY = radius + PieceView.STROKE_WIDTH / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(centerX, centerY, radius, paint);
    }
}
