package blue.aodev.materialkeylines.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.util.AttributeSet;

import blue.aodev.materialkeylines.R;

public abstract class LineView extends KeylineView {

    private float strokeWidth;

    public LineView(Context context) {
        super(context);
    }

    public LineView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public LineView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void setupDefaultValues() {
        super.setupDefaultValues();
        strokeWidth = 1;
    }

    @Override
    protected void readAttributes(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super.readAttributes(attrs, defStyleAttr, defStyleRes);

        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs, R.styleable.LineView, defStyleAttr, defStyleRes);

        try {
            strokeWidth = a.getFloat(R.styleable.LineView_strokeWidth, strokeWidth);
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void initPaint() {
        super.initPaint();
        paint.setStrokeWidth(strokeWidth);
        paint.setStyle(Paint.Style.STROKE);
    }

    public float getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
        paint.setStrokeWidth(strokeWidth);

        invalidate();
    }
}
