package blue.aodev.materialgrids;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import blue.aodev.materialgrids.utils.ColorUtil;

public class GridView extends View {

    private Paint gridPaint;
    private float gridSpacing;

    public GridView(Context context) {
        super(context);

        init();
    }

    public GridView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public GridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    public GridView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init();
    }

    private void init() {
        Resources r = getResources();

        float strokeWidth = 1;
        gridSpacing = r.getDimension(material.values.R.dimen.material_baseline_grid_1x);

        int colorRes = material.values.R.color.material_color_red_primary;
        int color = ColorUtil.getColor(r, getContext().getTheme(), colorRes);

        gridPaint = new Paint();
        gridPaint.setColor(color);
        gridPaint.setAlpha(64);
        gridPaint.setStrokeWidth(strokeWidth);
        gridPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int saveCount = canvas.save();

        for (int i = 0; (i - 1)*gridSpacing < getWidth(); i++) {
            canvas.drawLine(i*gridSpacing, 0, i*gridSpacing, getHeight(), gridPaint);
        }

        for (int i = 0; (i - 1)*gridSpacing <= getHeight(); i++) {
            canvas.drawLine(0, i*gridSpacing, getWidth(), i*gridSpacing, gridPaint);
        }

        canvas.restoreToCount(saveCount);
    }
}
