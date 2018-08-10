package com.allen.androidcustomview.wave;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import java.util.Random;

/**
 * Created by chenxin on 2018/8/6.
 */

public class WaveView extends View {

    private float[] waveAmplitude = {1.0f, 0.7f, 0.4f, 0.1f, -1.0f};
    public WaveView(Context context) {
        super(context);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float period = 2.0f;// 区域内，正弦波的周期

        // 将绘图原点移动到区域中心
        int width = getWidth();
        int height = getHeight();
        float midWidth = width / 2.0f;
        float midHeight = height / 4.0f;
        canvas.translate(midWidth, midHeight);

        // 初始化画笔
        Paint paint = new Paint();
        paint.setStrokeWidth(1);// 画线宽度
        //paint.setStyle(Paint.Style.STROKE);//空心效果
        paint.setAntiAlias(true);//抗锯齿
        //paint.setColor(Color.BLACK);

        // 初始化线条
        Path sinPath = new Path();
        sinPath.moveTo(-midWidth, 0);

        int[] wavePhase = {0, 6, -9, 15, -21};

        // 计算线条
        for (float x = -midWidth; x < midWidth; x++) {

            double scaling = 1 - Math.pow(x / midWidth, 2);

            double sine = Math.sin(2 * Math.PI * period * (x / width));//计算该点上的正弦值
            //float y = (float) (midHeight * sine);// 将正弦值限定到绘图区的高度上
            float y = (float) (midHeight * sine * scaling);
            sinPath.lineTo(x, y);
        }

      //  canvas.drawPath(sinPath, paint);//绘制线条


        Path parabola1 = new Path();
        parabola1.moveTo(-midWidth, 0);
        Path parabola2 = new Path();
        parabola2.moveTo(-midWidth, 0);

// 计算线条
        for (float x = -midWidth; x < midWidth; x++) {
            double scaling = 1 - Math.pow(x / midWidth, 2);
            double sine = Math.sin(2 * Math.PI * period * (x / width));//计算该点上的正弦值
            float y = (float) (midHeight * sine * scaling);// 将正弦值限定到绘图区的高度上
            sinPath.lineTo(x, y);
            parabola1.lineTo(x, (float) scaling * midHeight);
            parabola2.lineTo(x, -(float) scaling * midHeight);
        }

        //canvas.drawPath(sinPath, paint);//绘制正弦线
      //   canvas.drawPath(parabola1, paint);//绘制抛物线1
      //  canvas.drawPath(parabola2, paint);//绘制抛物线2


        float[] waveWidth = {3, 2, 2, 1, 1};
        DisplayMetrics metric = getResources().getDisplayMetrics();
        float density = metric.density; // 屏幕密度
        for (int i = 0; i < waveWidth.length; i++) {
            waveWidth[i] = waveWidth[i] * density / 2;
        }

        //float[] waveAlpha = {1.0f, 0.9f, 0.7f, 0.4f, 0.2f};
        float[] waveAlpha = {1.0f, 0.9f, 0.7f, 0.4f, 1.0f};
        //float[] waveAmplitude = {1.0f, 0.7f, 0.4f, 0.1f, -0.2f};
        //float[] waveAmplitude = {1.0f, 0.7f, 0.4f, 0.1f, -0.5f};
        for (int i = 0; i < waveWidth.length; i++) {
            paint.setStrokeWidth(waveWidth[i]);//画笔宽度
            paint.setAlpha((int) (waveAlpha[i] * 255));//画笔透明度
            //paint.setColor(Color.parseColor(strings[i]));
            paint.setColor(Color.parseColor(getRandomColor()));


            int colorStart = Color.parseColor(getRandomColor());
            int color1 = Color.parseColor(getRandomColor());
            int colorEnd = Color.parseColor(getRandomColor());
            LinearGradient backGradient = new LinearGradient(0, 0, width, height, new int[]{colorStart, color1 ,colorEnd}, null, Shader.TileMode.CLAMP);
//        LinearGradient backGradient = new LinearGradient(0, 0, 0, height, new int[]{colorStart, color1 ,colorEnd}, null, Shader.TileMode.CLAMP);
            paint.setShader(backGradient);

            sinPath.reset();//重置线条
            sinPath.moveTo(-midWidth, 0);

            // 计算线条
            for (float x = -midWidth; x < midWidth; x++) {
                double scaling = 1 - Math.pow(1 / midWidth * x, 2);
                double sine = Math.sin(2 * Math.PI * period * ((x  + phase+ wavePhase[i]) / width));//计算该点上的正弦值
                float y = (float) (midHeight// 将正弦值限定到绘图区的高度上
                        * sine   // 正弦值
                        * scaling// 振幅修正 - 距离中心越远，振幅越小
                        * getFloatWate()//waveAmplitude[i]// 副波纹振幅修正
                );
                sinPath.lineTo(x, y);
            }

            canvas.drawPath(sinPath, paint);//绘制线条
        }


        canvas.save();
        canvas.restore();
    }

    private String[] strings = new String[]{"#179F76", "#2A74FF", "#9B2E2E","#9B2E2E","#0099CC"};



    private float phase;
    private Random ra;
    public void nextPhase(float n) {
        phase -= n;
        invalidate();
    }



    public float getFloatWate() {
        Random random = new Random();
        int i = random.nextInt(5);
        return waveAmplitude[i];
    }

    public void start(){
        ra =new Random();
        handler.sendEmptyMessageDelayed(1, getRandomTime());
    }


    public String getRandomColor(){
        Random random = new Random();
        int i = random.nextInt(5);
       // int arr[] = {10,30,20,50};
        return strings[i];
    }
    public int getRandomTime() {
        Random random = new Random();
        int i = random.nextInt(4);
        int arr[] = {10,30,20,50};
        return arr[i];
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    nextPhase(ra.nextInt(50)+100);
                   // handler.sendEmptyMessageDelayed(1, getRandomTime());
                    handler.sendEmptyMessageDelayed(1, 100);
                    break;
            }
        }
    };
}


