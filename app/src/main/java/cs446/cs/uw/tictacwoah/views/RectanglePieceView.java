package cs446.cs.uw.tictacwoah.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Created by ASUS on 2018/3/5.
 */

public class RectanglePieceView extends PieceView {
    private Rect rect;

    public RectanglePieceView(Context context, int x, int y, int size, int color) {
        super(context, x, y, size, color);
        int offset = PieceView.STROKE_WIDTH / 2;
        rect = new Rect(offset, offset, size - offset, size - offset);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(rect, paint);
    }
}
