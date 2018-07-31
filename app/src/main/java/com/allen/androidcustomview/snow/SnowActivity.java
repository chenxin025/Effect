package com.allen.androidcustomview.snow;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.allen.androidcustomview.R;

/**
 *
 */
public class SnowActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.snow_ui);
        final EditText etDuration = (EditText) findViewById(R.id.edit_duration);
        final SnowFlyView snowFlyView = (SnowFlyView) findViewById(R.id.snow_fly);
        final Button btnAnimation = (Button) findViewById(R.id.btn_animation);
        btnAnimation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (snowFlyView.isRunning()) {
                    btnAnimation.setText(SnowActivity.this.getResources().getString(R.string.start_animation));
                    snowFlyView.stopAnimationNow();
                } else {
                    String str = etDuration.getText().toString();
                    if (!TextUtils.isEmpty(str))
                        snowFlyView.setSnowDuration(Long.valueOf(str));
                    btnAnimation.setText(SnowActivity.this.getResources().getString(R.string.stop_animation));
                    snowFlyView.startAnimation();
                }
            }
        });
    }
}
