package blue.aodev.materialgrids;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import blue.aodev.materialgrids.utils.ColorUtil;

public class GridView extends View {

    private Paint gridPaint;

    private int color;
    private float spacing;
    private float opacity;
    private float strokeWidth;

    public GridView(Context context) {
        super(context);

        setupDefaultValues();
        init();
    }

    public GridView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setupDefaultValues();
        readAttributes(attrs, 0, 0);
        init();
    }

    public GridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setupDefaultValues();
        readAttributes(attrs, defStyleAttr, 0);
        init();
    }

    public GridView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        setupDefaultValues();
        readAttributes(attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void setupDefaultValues() {
        Resources r = getResources();
        color = ColorUtil.getColor(r, getContext().getTheme(), R.color.material_color_red_500);
        spacing = r.getDimension(material.values.R.dimen.material_baseline_grid_1x);
        opacity = 0.5f;
        strokeWidth = 1;
    }

    private void readAttributes(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs, R.styleable.GridView, defStyleAttr, defStyleRes);

        try {
            color = a.getColor(R.styleable.GridView_color, color);
            spacing = a.getDimension(R.styleable.GridView_spacing, spacing);
            opacity = a.getFloat(R.styleable.GridView_opacity, opacity);
            strokeWidth = a.getFloat(R.styleable.GridView_strokeWidth, strokeWidth);
        } finally {
            a.recycle();
        }
    }

    private void init() {
        gridPaint = new Paint();
        gridPaint.setColor(color);
        gridPaint.setAlpha((int)(opacity*255));
        gridPaint.setStrokeWidth(strokeWidth);
        gridPaint.setStyle(Paint.Style.STROKE);
    }

    public float getSpacing() {
        return spacing;
    }

    public void setSpacing(float spacing) {
        this.spacing = spacing;

        invalidate();
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
        gridPaint.setColor(color);
        gridPaint.setAlpha((int)(opacity*255));

        invalidate();
    }

    public float getOpacity() {
        return opacity;
    }

    public void setOpacity(float opacity) {
        this.opacity = opacity;
        gridPaint.setAlpha((int)(opacity*255));

        invalidate();
    }

    public float getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
        gridPaint.setStrokeWidth(strokeWidth);

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int i = 0; i*spacing < getWidth(); i++) {
            canvas.drawLine(i*spacing, 0, i*spacing, getHeight(), gridPaint);
        }

        for (int i = 0; i*spacing <= getHeight(); i++) {
            canvas.drawLine(0, i*spacing, getWidth(), i*spacing, gridPaint);
        }
    }
}
