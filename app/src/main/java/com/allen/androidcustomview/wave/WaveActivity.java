package com.allen.androidcustomview.wave;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.allen.androidcustomview.R;

/**
 * Created by chenxin on 2018/8/6.
 */

public class WaveActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wave_ui);
       /* final MyCustomView myCustomView = findViewById(R.id.key_wave);

        Button btn = findViewById(R.id.key_start);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myCustomView.startWave();
            }
        });*/

        WaveView waveView = findViewById(R.id.key_wave);
        waveView.start();
    }
}
