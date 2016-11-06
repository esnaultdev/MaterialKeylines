package blue.aodev.materialgrids;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends Activity {

    @BindView(R.id.start_stop_button) Button startStopButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        updateStartStopButton(OverlayService.isStarted());

        startStopButton.setOnClickListener(new View.OnClickListener() {
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

    private void startOverlayService() {
        startService(new Intent(MainActivity.this, OverlayService.class));
        updateStartStopButton(true);
    }

    private void stopOverlayService() {
        stopService(new Intent(MainActivity.this, OverlayService.class));
        updateStartStopButton(false);
    }

    private void updateStartStopButton(boolean isStarted) {
        int resId;
        if (isStarted) {
            resId = R.string.stop;
        } else {
            resId = R.string.start;
        }
        startStopButton.setText(resId);
    }
}
