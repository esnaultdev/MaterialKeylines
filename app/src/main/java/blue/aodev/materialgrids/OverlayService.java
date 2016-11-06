package blue.aodev.materialgrids;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

public class OverlayService extends Service {
    private NotificationManager notificationManager;
    private WindowManager windowManager;

    private static boolean alreadyStarted;
    private View view;

    private int NOTIFICATION_ID = 1;

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
            showGrid();

            alreadyStarted = true;
        }

        return super.onStartCommand(intent, flags, startId);
    }

    public void showGrid() {
        view = LayoutInflater.from(this).inflate(R.layout.grid, null);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                PixelFormat.TRANSLUCENT);

        windowManager.addView(view, params);
    }

    /**
     * Show a notification while this service is running.
     */
    private void showNotification() {
        CharSequence text = getText(R.string.stop);

        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);

        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker(text)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(getText(R.string.app_name))
                .setContentText(text)
                .setContentIntent(contentIntent)
                .build();

        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    private void removeGrid() {
        if (view != null) {
            windowManager.removeView(view);
            view = null;
        }
    }

    private void removeNotification() {
        notificationManager.cancel(NOTIFICATION_ID);
    }

    public static boolean isStarted() {
        return alreadyStarted;
    }
}
