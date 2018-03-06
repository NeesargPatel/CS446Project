package cs446.cs.uw.tictacwoah.views;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import cs446.cs.uw.tictacwoah.R;
import cs446.cs.uw.tictacwoah.models.Board;

public class BoardView extends View {

    private final int STROKE_WIDTH = 5;
    public final int MARGIN_TOP;

    private float cellWidth;
    private Paint gridPaint;

    public BoardView(Context context, int marginTop, float cellWidth) {
        super(context);
        MARGIN_TOP = marginTop;
        this.cellWidth = cellWidth;
        gridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        gridPaint.setStrokeWidth(STROKE_WIDTH);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        cellWidth = width / Board.boardSize;
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float totalWidth = cellWidth * Board.boardSize;
        for (int i = 1; i < Board.boardSize; i++) {
            // vertical lines
            float startX = cellWidth * i;
            float startY = MARGIN_TOP;
            float stopX = startX;
            float stopY = MARGIN_TOP + totalWidth;
            canvas.drawLine(startX, startY, stopX, stopY, gridPaint);

            // horizontal lines
            startX = 0;
            startY = MARGIN_TOP + cellWidth * i;
            stopX = totalWidth;
            stopY = startY;
            canvas.drawLine(startX, startY, stopX, stopY, gridPaint);
        }
    }

    // x is the center of the PieceView
    public Integer getRowId(float x){
        for (int i = 1; i <= Board.boardSize; ++i){
            if (x < cellWidth * i) return i - 1;
        }
        return  null;
    }

    public Integer getColId(float y){
        if (y < MARGIN_TOP) return null;
        for (int i = 1; i <= Board.boardSize; ++i){
            if (y < cellWidth * i + MARGIN_TOP) return i - 1;
        }
        return  null;
    }

    public float getCellWidth(){
        return cellWidth;
    }
}