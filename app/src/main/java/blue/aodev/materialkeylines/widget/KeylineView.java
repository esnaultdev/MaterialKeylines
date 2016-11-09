package blue.aodev.materialkeylines.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import blue.aodev.materialkeylines.R;
import blue.aodev.materialkeylines.utils.ColorUtil;

public class KeylineView extends View {

    public static final int DIRECTION_VERTICAL = 0;
    public static final int DIRECTION_HORIZONTAL = 1;
    public static final int DIRECTION_BOTH = 2;

    protected Paint paint;

    private int color;
    private float opacity;
    private int direction;

    public KeylineView(Context context) {
        super(context);

        setupDefaultValues();
        initPaint();
    }

    public KeylineView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setupDefaultValues();
        readAttributes(attrs, 0, 0);
        initPaint();
    }

    public KeylineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setupDefaultValues();
        readAttributes(attrs, defStyleAttr, 0);
        initPaint();
    }

    public KeylineView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        setupDefaultValues();
        readAttributes(attrs, defStyleAttr, defStyleRes);
        initPaint();
    }

    protected void setupDefaultValues() {
        Resources r = getResources();
        color = ColorUtil.getColor(r, getContext().getTheme(), R.color.material_color_red_500);
        opacity = 0.5f;
        direction = DIRECTION_VERTICAL;
    }

    protected void readAttributes(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs, R.styleable.KeylineView, defStyleAttr, defStyleRes);

        try {
            color = a.getColor(R.styleable.KeylineView_color, color);
            opacity = a.getFloat(R.styleable.KeylineView_opacity, opacity);
            direction = a.getInt(R.styleable.KeylineView_direction, direction);
        } finally {
            a.recycle();
        }
    }

    protected void initPaint() {
        paint = new Paint();
        paint.setColor(color);
        paint.setAlpha((int)(opacity*255));
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
        paint.setColor(color);
        paint.setAlpha((int)(opacity*255));

        invalidate();
    }

    public float getOpacity() {
        return opacity;
    }

    public void setOpacity(float opacity) {
        this.opacity = opacity;
        paint.setAlpha((int)(opacity*255));

        invalidate();
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }
}
