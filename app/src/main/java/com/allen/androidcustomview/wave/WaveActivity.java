package com.allen.androidcustomview.wave;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.allen.androidcustomview.R;

import java.util.Random;

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

        MyCustomView dul = findViewById(R.id.key_dul);
        dul.startWave();

        /*WaveView waveView = findViewById(R.id.key_wave);
        waveView.start();*/


        /*SiriView siriView = (SiriView) findViewById(R.id.siriView);
// 停止波浪曲线
        siriView.stop();
// 设置曲线高度，height的取值是0f~1f
        siriView.setWaveHeight(0.5f);
// 设置曲线的粗细，width的取值大于0f
        siriView.setWaveWidth(10f);
// 设置曲线颜色
        siriView.setWaveColor(Color.rgb(39, 188, 136));
// 设置曲线在X轴上的偏移量，默认值为0f
        siriView.setWaveOffsetX(0f);
// 设置曲线的数量，默认是4
        siriView.setWaveAmount(4);
// 设置曲线的速度，默认是0.1f
        siriView.setWaveSpeed(0.1f);*/
    }

    private float[] speedArr = {0.1f,0.2f,0.3f,0.4f,0.5f,0.6f,0.7f,0.8f,0.9f,1.0f};
    public float getFloatWate() {
        Random random = new Random();
        int i = random.nextInt(10);
        return speedArr[i];
    }

}
