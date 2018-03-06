package cs446.cs.uw.tictacwoah.views;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

import cs446.cs.uw.tictacwoah.models.Piece;

/**
 * Created by ASUS on 2018/3/4.
 */

public class PieceView extends View{
    private static final int WHITE = 0;  // for animation
    private static final int ANIMATION_DURATION = 800;  // in ms
    protected static final int STROKE_WIDTH = 10;  // derived classes need to use it to calculate size of shape to be drawn
    public static final int[] SIZES = new int[] { 90, 160, 230 };
    public static final int[] COLORS = new int[] {
        0xFF85C1E9,  // blue
        0xFF82E0AA,  // green
        0xFFE59866,  // orange
        0xFFEC7063  // red
    };
    public static final int RECTANGLE = 0;
    public static final int CIRCLE = 1;
    public static final int TRIANGLE = 2;

    public static PieceView getPieceView(Context context, int x, int y, int size, int color, int shape){
        switch (shape){
            case RECTANGLE:
                return new  RectanglePieceView(context, x, y, size, color);
            case CIRCLE:
                return new CirclePieceView(context, x, y, size, color);
            case TRIANGLE:
                return new TrianglePieceView(context, x, y, size, color);
            default:
                return null;
        }
    }

    private int size;
    private ObjectAnimator animator;
    protected Paint paint;  // set it protected because derived classes need to use it to draw

    public PieceView(Context context, int x, int y, int size, int color) {
        super(context);
        init(size, color);
        setupLayout(x, y, size);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int desiredWidth = size;
        int desiredHeight = size;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            //Must be this size
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            width = Math.min(desiredWidth, widthSize);
        } else {
            //Be whatever you want
            width = desiredWidth;
        }

        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            //Must be this size
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            height = Math.min(desiredHeight, heightSize);
        } else {
            //Be whatever you want
            height = desiredHeight;
        }

        setMeasuredDimension(width, height);
    }

    // Because we implement onClickListener in the Activity,
    // so I override this method to avoid warning message
    @Override
    public boolean performClick(){
        return super.performClick();
    }

    public int getColor(){
        return paint.getColor();
    }

    public void setColor(int color){
        paint.setColor(color);
        invalidate();
    }

    public void startAnimation(){
        animator.setDuration(ANIMATION_DURATION);
        animator.start();
    }

    public void stopAnimation(){
        animator.setDuration(0);
        animator.reverse();
        // If I put animator.cancel() here, the animation won't stop
    }

    private void init(int size, int color){
        this.size = size;

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setStrokeWidth(STROKE_WIDTH);
        paint.setStyle(Paint.Style.STROKE);

        animator = ObjectAnimator.ofArgb(this, "color", WHITE);
        animator.setDuration(ANIMATION_DURATION);
        animator.setRepeatCount(Animation.INFINITE);
        animator.setRepeatMode(ValueAnimator.REVERSE);
    }

    private void setupLayout(int x, int y, int size){
        setX(x);
        setY(y);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(size, size);
        setLayoutParams(layoutParams);
    }
}
