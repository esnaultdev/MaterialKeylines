package blue.aodev.materialgrids;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.View;
import android.widget.Switch;
import android.widget.Toolbar;

import blue.aodev.materialgrids.utils.ColorUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends Activity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    private Switch appBarSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setupAppBar();
        updateSwitch(OverlayService.isStarted());
        appBarSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (OverlayService.isStarted()) {
                    stopOverlayService();
                } else {
                    startOverlayService();
                }
            }
        });
    }

    private void setupAppBar() {
        Resources r = getResources();

        toolbar.setTitle(R.string.app_name);
        int color = ColorUtil.getColor(r, getTheme(), material.values.R.color.material_color_white);
        toolbar.setTitleTextColor(color);

        toolbar.inflateMenu(R.menu.app_bar_menu);
        appBarSwitch = (Switch) toolbar.findViewById(R.id.action_switch);
    }

    private void startOverlayService() {
        startService(new Intent(MainActivity.this, OverlayService.class));
        updateSwitch(true);
    }

    private void stopOverlayService() {
        stopService(new Intent(MainActivity.this, OverlayService.class));
        updateSwitch(false);
    }

    private void updateSwitch(boolean isStarted) {
        appBarSwitch.setChecked(isStarted);
    }

    public static class SettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            
            addPreferencesFromResource(R.xml.preferences);
        }
    }
}
