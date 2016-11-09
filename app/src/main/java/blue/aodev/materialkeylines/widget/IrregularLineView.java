package blue.aodev.materialkeylines.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

public class IrregularLineView extends LineView {

    private float[] coordinates;

    public IrregularLineView(Context context) {
        super(context);
    }

    public IrregularLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IrregularLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public IrregularLineView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public float[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(float[] coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (coordinates == null) {
            return;
        }

        int direction = getDirection();
        boolean drawVertical = direction == DIRECTION_VERTICAL || direction == DIRECTION_BOTH;
        boolean drawHorizontal = direction == DIRECTION_HORIZONTAL || direction == DIRECTION_BOTH;


        for (float coordinate : coordinates) {
            if (drawVertical) {
                canvas.drawLine(coordinate, 0, coordinate, getHeight(), paint);
            }
            if (drawHorizontal) {
                canvas.drawLine(0, coordinate, getWidth(), coordinate, paint);
            }
        }
    }
}
