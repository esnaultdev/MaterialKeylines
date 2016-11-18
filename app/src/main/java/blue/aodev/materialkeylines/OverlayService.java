package blue.aodev.materialkeylines;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import blue.aodev.materialkeylines.widget.IrregularLineView;
import blue.aodev.materialkeylines.widget.RegularLineView;

public class OverlayService extends Service {
    private NotificationManager notificationManager;
    private WindowManager windowManager;

    private static final String UPDATE_EXTRA = "update";

    private static boolean running;

    private View view;
    private RegularLineView baselineGridView;
    private RegularLineView incrementGridView;
    private RegularLineView typographyLinesView;
    private IrregularLineView contentKeylinesView;

    private static final int NOTIFICATION_ID = 1;

    @NonNull
    public static Intent getUpdateIntent(@NonNull Context context) {
        Intent intent = new Intent(context, OverlayService.class);
        intent.putExtra(UPDATE_EXTRA, true);

        return intent;
    }

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
        running = false;

        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!running) {
            showNotification();
            showOverlay();

            running = true;
        } else if (intent.hasExtra(UPDATE_EXTRA)) {
            readPreferences();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static boolean isRunning() {
        return running;
    }

    private void showOverlay() {
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
        typographyLinesView = (RegularLineView) view.findViewById(R.id.typography_lines);
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
        OverlayPreference overlayPref = new OverlayPreference(this);
        overlayPref.updateBaselineGridView(baselineGridView);
        overlayPref.updateIncrementGridView(incrementGridView);
        overlayPref.updateTypographyLinesView(typographyLinesView);
        overlayPref.updateContentKeylinesView(contentKeylinesView);
    }

    private void readIncrementPreferences() {
        OverlayPreference overlayPref = new OverlayPreference(this);
        overlayPref.updateIncrementGridView(incrementGridView);
    }

    /**
     * Show a notification while this service is running.
     */
    private void showNotification() {
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);

        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_material_keylines_face_on_24dp)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(getText(R.string.active_overlay))
                .setContentText(getText(R.string.overlay_notification_text))
                .setContentIntent(contentIntent)
                .setPriority(Notification.PRIORITY_LOW)
                .setOngoing(true)
                .build();

        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    private void removeNotification() {
        notificationManager.cancel(NOTIFICATION_ID);
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
        readIncrementPreferences();
    }

    public class OverlayBinder extends Binder {
        public OverlayService getService() {
            return OverlayService.this;
        }
    }
}
