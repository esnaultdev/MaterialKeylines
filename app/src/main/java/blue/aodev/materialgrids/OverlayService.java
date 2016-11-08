package blue.aodev.materialgrids;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import blue.aodev.materialgrids.widget.IrregularLineView;
import blue.aodev.materialgrids.widget.KeylineView;
import blue.aodev.materialgrids.widget.RegularLineView;

public class OverlayService extends Service {
    private NotificationManager notificationManager;
    private WindowManager windowManager;

    private static boolean alreadyStarted;

    private View view;
    private RegularLineView baselineGridView;
    private RegularLineView incrementGridView;
    private RegularLineView typographyKeylinesView;
    private IrregularLineView contentKeylinesView;

    private static final int NOTIFICATION_ID = 1;

    @Override
    public void onCreate() {
        super.onCreate();

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
    }

    @Override
    public void onDestroy() {
        removeNotification();
        removeGrid();
        alreadyStarted = false;

        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!alreadyStarted) {
            showNotification();
            showOverlay();

            alreadyStarted = true;
        }

        return super.onStartCommand(intent, flags, startId);
    }

    public void showOverlay() {
        view = LayoutInflater.from(this).inflate(R.layout.overlay, null);
        bindViews();
        setupContentKeylines();
        readPreferences();

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                PixelFormat.TRANSLUCENT);

        windowManager.addView(view, params);
    }

    private void bindViews() {
        baselineGridView = (RegularLineView) view.findViewById(R.id.baseline_grid);
        incrementGridView = (RegularLineView) view.findViewById(R.id.increment_grid);
        typographyKeylinesView = (RegularLineView) view.findViewById(R.id.typography_lines);
        contentKeylinesView = (IrregularLineView) view.findViewById(R.id.content_keylines);
    }

    private void setupContentKeylines() {
        float contentEdgeMargin = getResources().getDimension(
                material.values.R.dimen.material_content_edge_margin_horizontal);
        float secondaryContentEdgeMargin = getResources().getDimension(
                material.values.R.dimen.material_content_secondary_edge_margin_start);
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        float screenWidth = metrics.widthPixels;

        float[] coordinates = new float[] {
                contentEdgeMargin,
                secondaryContentEdgeMargin,
                screenWidth - contentEdgeMargin
        };
        contentKeylinesView.setCoordinates(coordinates);
    }

    private void readPreferences() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        readBaselineGridPrefs(preferences);
        readIncrementGridPrefs(preferences);
        readTypographyLinesPrefs(preferences);
        readContentKeylinesPrefs(preferences);
    }

    private void readBaselineGridPrefs(@NonNull SharedPreferences preferences) {
        boolean enable = readEnable(preferences, R.string.pref_key_baseline_grid_enabled);
        setVisible(baselineGridView, enable);
        if (!enable) {
            readOpacity(preferences, R.string.pref_key_baseline_grid_transparency,
                    baselineGridView);
        }
    }

    private void readIncrementGridPrefs(@NonNull SharedPreferences preferences) {
        boolean enable = readEnable(preferences, R.string.pref_key_increment_grid_enabled);
        setVisible(incrementGridView, enable);
        if (!enable) {
            readOpacity(preferences, R.string.pref_key_baseline_grid_transparency,
                    baselineGridView);
        }
    }

    private void readTypographyLinesPrefs(@NonNull SharedPreferences preferences) {
        boolean enable = readEnable(preferences, R.string.pref_key_typography_lines_enabled);
        setVisible(typographyKeylinesView, enable);
        if (!enable) {
            readOpacity(preferences, R.string.pref_key_baseline_grid_transparency,
                    baselineGridView);
        }
    }

    private void readContentKeylinesPrefs(@NonNull SharedPreferences preferences) {
        boolean enable = readEnable(preferences, R.string.pref_key_content_keylines_enabled);
        setVisible(contentKeylinesView, enable);
        if (!enable) {
            readOpacity(preferences, R.string.pref_key_baseline_grid_transparency,
                    baselineGridView);
        }
    }

    private boolean readEnable(@NonNull SharedPreferences preferences, @StringRes int keyStringId) {
        return preferences.getBoolean(getString(keyStringId), false);
    }

    private static void setVisible(@NonNull View view, boolean visible) {
        int visibility = visible ? View.VISIBLE : View.INVISIBLE;
        view.setVisibility(visibility);
    }

    private void readOpacity(@NonNull SharedPreferences preferences, @StringRes int keyStringId,
                             @NonNull KeylineView view) {
        String defaultOpacityString = getString(R.string.pref_values_transparency_default);
        String opacityString = preferences.getString(getString(keyStringId), defaultOpacityString);
        int opacity = Integer.parseInt(opacityString);
        view.setOpacity(opacity / 100f);
    }

    /**
     * Show a notification while this service is running.
     */
    private void showNotification() {
        PendingIntent deleteIntent = PendingIntent.getBroadcast(this, 0 ,
                ToggleReceiver.getIntent(false), 0);

        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_material_grids_face_on_24dp)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(getText(R.string.app_name))
                .setContentText(getText(R.string.overlay_notification_text))
                .setContentIntent(deleteIntent)
                .setPriority(Notification.PRIORITY_LOW)
                .setOngoing(true)
                .build();

        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    private void removeGrid() {
        if (view != null) {
            windowManager.removeView(view);
            view = null;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setupContentKeylines();
    }

    private void removeNotification() {
        notificationManager.cancel(NOTIFICATION_ID);
    }

    public static boolean isStarted() {
        return alreadyStarted;
    }
}
