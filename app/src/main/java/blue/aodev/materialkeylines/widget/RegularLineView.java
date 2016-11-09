package blue.aodev.materialkeylines.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;

import blue.aodev.materialkeylines.R;

public class RegularLineView extends LineView {

    private float spacing;

    public RegularLineView(Context context) {
        super(context);
    }

    public RegularLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RegularLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public RegularLineView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void setupDefaultValues() {
        super.setupDefaultValues();
        Resources r = getResources();
        spacing = r.getDimension(material.values.R.dimen.material_baseline_grid_1x);
    }

    @Override
    protected void readAttributes(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super.readAttributes(attrs, defStyleAttr, defStyleRes);

        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs, R.styleable.RegularLineView, defStyleAttr, defStyleRes);

        try {
            spacing = a.getDimension(R.styleable.RegularLineView_spacing, spacing);
        } finally {
            a.recycle();
        }
    }

    public float getSpacing() {
        return spacing;
    }

    public void setSpacing(float spacing) {
        this.spacing = spacing;

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float spacing = getSpacing();
        int direction = getDirection();
        boolean drawVertical = direction == DIRECTION_VERTICAL || direction == DIRECTION_BOTH;
        boolean drawHorizontal = direction == DIRECTION_HORIZONTAL || direction == DIRECTION_BOTH;


        if (drawVertical) {
            for (int i = 0; i * spacing < getWidth(); i++) {
                canvas.drawLine(i * spacing, 0, i * spacing, getHeight(), paint);
            }
        }

        if (drawHorizontal) {
            for (int i = 0; i * spacing <= getHeight(); i++) {
                canvas.drawLine(0, i * spacing, getWidth(), i * spacing, paint);
            }
        }
    }
}
