package blue.aodev.materialkeylines;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.util.TypedValue;
import android.view.View;

import blue.aodev.materialkeylines.utils.ColorUtil;
import blue.aodev.materialkeylines.widget.IrregularLineView;
import blue.aodev.materialkeylines.widget.KeylineView;
import blue.aodev.materialkeylines.widget.LineView;
import blue.aodev.materialkeylines.widget.RegularLineView;

public class OverlayPreference {

    private Context context;
    private SharedPreferences preferences;

    public OverlayPreference(@NonNull Context context) {
        this.context = context;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void updateBaselineGridView(@NonNull RegularLineView view) {
        boolean enable = readEnable(preferences, R.string.pref_key_baseline_grid_enabled);
        setVisible(view, enable);
        if (enable) {
            updateOpacity(preferences, R.string.pref_key_baseline_grid_opacity, view);
            updateColor(preferences, R.string.pref_key_baseline_grid_color, view,
                    R.color.pref_baseline_grid_default_color);
            updateSize(preferences, R.string.pref_key_baseline_grid_size, view,
                    R.string.pref_value_size_small);
        }
    }

    public void updateIncrementGridView(@NonNull RegularLineView view) {
        boolean enable = readEnable(preferences, R.string.pref_key_increment_grid_enabled);
        setVisible(view, enable);
        if (enable) {
            updateOpacity(preferences, R.string.pref_key_increment_grid_opacity, view);
            updateColor(preferences, R.string.pref_key_increment_grid_color, view,
                    R.color.pref_increment_grid_default_color);
            updateSize(preferences, R.string.pref_key_increment_grid_size, view,
                    R.string.pref_value_size_medium);
            updateIncrementSize(preferences, view);
        }
    }

    public void updateTypographyLinesView(@NonNull RegularLineView view) {
        boolean enable = readEnable(preferences, R.string.pref_key_typography_lines_enabled);
        setVisible(view, enable);
        if (enable) {
            updateOpacity(preferences, R.string.pref_key_typography_lines_opacity, view);
            updateColor(preferences, R.string.pref_key_typography_lines_color, view,
                    R.color.pref_typography_lines_default_color);
            updateSize(preferences, R.string.pref_key_typography_lines_size, view,
                    R.string.pref_value_size_small);
        }
    }

    public void updateContentKeylinesView(@NonNull IrregularLineView view) {
        boolean enable = readEnable(preferences, R.string.pref_key_content_keylines_enabled);
        setVisible(view, enable);
        if (enable) {
            updateOpacity(preferences, R.string.pref_key_content_keylines_opacity, view);
            updateColor(preferences, R.string.pref_key_content_keylines_color, view,
                    R.color.pref_content_keylines_default_color);
            updateSize(preferences, R.string.pref_key_content_keylines_size, view,
                    R.string.pref_value_size_large);
        }
    }

    private boolean readEnable(@NonNull SharedPreferences preferences, @StringRes int keyStringId) {
        return preferences.getBoolean(context.getString(keyStringId), false);
    }

    private static void setVisible(@NonNull View view, boolean visible) {
        int visibility = visible ? View.VISIBLE : View.INVISIBLE;
        view.setVisibility(visibility);
    }

    private void updateOpacity(@NonNull SharedPreferences preferences, @StringRes int keyStringId,
                               @NonNull KeylineView view) {
        Resources r = context.getResources();

        String defaultString = r.getString(R.string.pref_value_opacity_default);
        String opacityString = preferences.getString(r.getString(keyStringId), defaultString);
        int opacity = Integer.parseInt(opacityString);
        view.setOpacity(opacity / 100f);
    }

    private void updateColor(@NonNull SharedPreferences preferences, @StringRes int keyStringId,
                             @NonNull KeylineView view, @ColorRes int defaultColorId) {
        Resources r = context.getResources();

        int defaultColor = ColorUtil.getColor(r, context.getTheme(), defaultColorId);
        int color = preferences.getInt(r.getString(keyStringId), defaultColor);
        view.setColor(color);
    }

    private void updateSize(@NonNull SharedPreferences preferences, @StringRes int keyStringId,
                            @NonNull LineView view, @StringRes int defaultSizeId) {
        Resources r = context.getResources();

        String defaultSizeString = r.getString(defaultSizeId);
        String sizeString = preferences.getString(r.getString(keyStringId), defaultSizeString);
        int size = Integer.parseInt(sizeString);
        view.setStrokeWidth(size);
    }

    private void updateIncrementSize(@NonNull SharedPreferences preferences,
                                     @NonNull RegularLineView view) {
        Resources r = context.getResources();

        String defaultSizeString = r.getString(R.string.pref_value_increment_size_auto);
        String key = r.getString(R.string.pref_key_increment_grid_increment_size);
        String sizeString = preferences.getString(key, defaultSizeString);
        int sizeDp = Integer.parseInt(sizeString);

        float size;
        if (sizeDp != 0) {
            size = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, sizeDp, r.getDisplayMetrics());
        } else {
            // Automatic
            size = r.getDimension(material.values.R.dimen.material_increment_1x);
        }
        view.setSpacing(size);
    }
}
