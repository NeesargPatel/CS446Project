package cs446.cs.uw.tictacwoah.views.piece;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;

/**
 * Created by ASUS on 2018/3/5.
 */

public class TrianglePieceView extends PieceView {
    private Path path;

    public TrianglePieceView(Context context, int x, int y, int size, int color) {
        super(context, x, y + 1600 / size, size, color);  // 1600 / size is used to tweak UI
        float offset = PieceView.STROKE_WIDTH / 2;
        float edgeLength = size - PieceView.STROKE_WIDTH;
        float height = (float)(edgeLength * (Math.sqrt(3) / 2));
        float bottomY = (size - height) / 2 + height;

        path = new Path();
        path.moveTo(size / 2, bottomY - height);  // the top point
        path.lineTo(offset, bottomY);  // the bottom-left point
        path.lineTo(offset + edgeLength, bottomY);  // the bottom-right point
        path.close();  // a line segment back to the top point is automatically added
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(path, paint);
    }
}
