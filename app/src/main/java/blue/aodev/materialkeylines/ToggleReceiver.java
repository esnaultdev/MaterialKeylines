package blue.aodev.materialkeylines;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ToggleReceiver extends BroadcastReceiver {

    private static final String EXTRA_ENABLED = "enabled";

    public static Intent getIntent(boolean enabled) {
        Intent intent = new Intent("blue.aodev.materialgrids.TOGGLE");
        intent.putExtra(EXTRA_ENABLED, enabled);
        return intent;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context, OverlayService.class);
        if (intent.getBooleanExtra(EXTRA_ENABLED, true)) {
            context.startService(service);
        } else {
            context.stopService(service);
        }
    }
}
