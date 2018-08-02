package com.allen.androidcustomview.arcmenu;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;

import com.allen.androidcustomview.R;
import com.allen.androidcustomview.bottompop.BottomDialog;

/**
 * Created by chenxin on 2018/8/2.
 */

public class AnimatorActivity extends AppCompatActivity {
    private ImageView mImageView1;
    private ImageView mImageView2;
    private ImageView mImageView3;
    private ImageView mImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_animator);
        mImageView1 = findViewById(R.id.anim1);
        mImageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animRunBack(mImageView1,mImageView2,mImageView3);
                BottomDialog.showDialog(AnimatorActivity.this);

            }
        });
        mImageView2 = findViewById(R.id.anim2);
        mImageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animRunBack(mImageView1,mImageView2,mImageView3);
            }
        });
        mImageView3 = findViewById(R.id.anim3);
        mImageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animRunBack(mImageView1,mImageView2,mImageView3);
            }
        });
        mImageView = findViewById(R.id.boss);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animRun(mImageView1,mImageView2,mImageView3);
            }
        });


    }

    //菜单展开的动画
    public void animRun(final View view1, final View view2, final View view3) {
        //获取屏幕宽度
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        final int screenWidth = dm.widthPixels;

        //左边控件的动画
        //1 以动画开始的时候当前控件为原点(只有一个)
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(view1, "translationY",
                0.0F, -screenWidth / 3f).setDuration(300);//Y方向移动距离
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(view1, "translationX",
                0.0F, -screenWidth / 4f).setDuration(300);//X方向移动距离
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(view1, "scaleX", 1.0f, 1.5f).setDuration(300);//X方向放大
        ObjectAnimator animator4 = ObjectAnimator.ofFloat(view1, "scaleY", 1.0f, 1.5f).setDuration(300);//Y方向放大
        AnimatorSet animSet1 = new AnimatorSet();
        animSet1.setInterpolator(new OvershootInterpolator());//到达指定位置后继续向前移动一定的距离然后弹回指定位置,达到颤动的特效
        animSet1.playTogether(animator1, animator2, animator3, animator4);//四个动画同时执行

        //中间控件的动画,因需要设置监听所以与
        final ObjectAnimator animator5 = ObjectAnimator.ofFloat(view2, "translationY",
                0.0F, -screenWidth / 2f).setDuration(300);
        ObjectAnimator animator6 = ObjectAnimator.ofFloat(view2, "scaleX", 1.0f, 1.5f).setDuration(300);
        ObjectAnimator animator7 = ObjectAnimator.ofFloat(view2, "scaleY", 1.0f, 1.5f).setDuration(300);
        final AnimatorSet animatorSet2 = new AnimatorSet();
        animatorSet2.setInterpolator(new OvershootInterpolator());
        animatorSet2.playTogether(animator5, animator6, animator7);
        animatorSet2.setStartDelay(50);//监听第一个动画开始之后50ms开启第二个动画,达到相继弹出的效果

        //右侧控件的动画
        ObjectAnimator animator8 = ObjectAnimator.ofFloat(view3, "translationY",
                0.0F, -screenWidth / 3).setDuration(300);
        ObjectAnimator animator9 = ObjectAnimator.ofFloat(view3, "translationX",
                0.0F, screenWidth / 4f).setDuration(300);
        ObjectAnimator animator10 = ObjectAnimator.ofFloat(view3, "scaleX", 1.0f, 1.5f).setDuration(300);
        ObjectAnimator animator11 = ObjectAnimator.ofFloat(view3, "scaleY", 1.0f, 1.5f).setDuration(300);
        final AnimatorSet animatorSet3 = new AnimatorSet();
        animatorSet3.setInterpolator(new OvershootInterpolator());
        animatorSet3.playTogether(animator8, animator9, animator10, animator11);
        animatorSet3.setStartDelay(50);


        //三个动画结束之后设置boss按键可点击,点击即收回动画
        animatorSet3.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mImageView.setClickable(true);
            }
        });

        //第二个开始之后再开启第三个
        animator3.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                animatorSet3.start();
            }

        });

        //第一个动画开始之后再开启第二个
        animSet1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                animatorSet2.start();
            }
        });

        animSet1.start();//放在最后是为了初始化完毕所有的动画之后才触发第一个控件的动画
    }


    //收回动画,相当于反向执行展开动画,此处不做更详细的注释
    public void animRunBack(final View view1, final View view2, final View view3) {
        //获取屏幕宽度
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        final int screenWidth = dm.widthPixels;

        //第一个收回动画
        //setTranslationX(float translationX) :表示在X轴上的平移距离,以当前控件为原点，向右为正方向，参数translationX表示移动的距离。
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(view1, "translationY",
                -screenWidth / 3f, 0.0F).setDuration(300);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(view1, "translationX",
                -screenWidth / 4f, 0.0F).setDuration(300);

        ObjectAnimator animator3 = ObjectAnimator.ofFloat(view1, "scaleX", 1.5f, 1.0f).setDuration(300);
        ObjectAnimator animator4 = ObjectAnimator.ofFloat(view1, "scaleY", 1.5f, 1.0f).setDuration(300);
        AnimatorSet animSet = new AnimatorSet();
        animSet.setInterpolator(new DecelerateInterpolator());
        animSet.playTogether(animator1, animator2, animator3, animator4);

        //第二个收回动画
        final ObjectAnimator animator5 = ObjectAnimator.ofFloat(view2, "translationY",
                -screenWidth / 2f, 0.0F).setDuration(300);
        ObjectAnimator animator6 = ObjectAnimator.ofFloat(view2, "scaleX", 1.5f, 1.0f).setDuration(300);
        ObjectAnimator animator7 = ObjectAnimator.ofFloat(view2, "scaleY", 1.5f, 1.0f).setDuration(300);
        //                animator3.setInterpolator(new OvershootInterpolator());
        final AnimatorSet animatorSet2 = new AnimatorSet();
        animatorSet2.setInterpolator(new DecelerateInterpolator());
        animatorSet2.playTogether(animator5, animator6, animator7);
        animatorSet2.setStartDelay(50);

        //第三个收回动画
        ObjectAnimator animator8 = ObjectAnimator.ofFloat(view3, "translationY",
                -screenWidth / 3, 0.0F).setDuration(300);
        ObjectAnimator animator9 = ObjectAnimator.ofFloat(view3, "translationX",
                screenWidth / 4f, 0.0F).setDuration(300);
        ObjectAnimator animator10 = ObjectAnimator.ofFloat(view3, "scaleX", 1.5f, 1.0f).setDuration(300);
        ObjectAnimator animator11 = ObjectAnimator.ofFloat(view3, "scaleY", 1.5f, 1.0f).setDuration(300);
        final AnimatorSet animatorSet3 = new AnimatorSet();
        animatorSet3.setInterpolator(new DecelerateInterpolator());
        //四个动画同时执行
        animatorSet3.playTogether(animator8, animator9, animator10, animator11);
        animatorSet3.setStartDelay(50);

        animator3.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {

                animatorSet3.start();
                animatorSet3.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                      //  finish();//收回动画结束后finish此页面
                    }
                });
            }

        });

        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                animatorSet2.start();
            }
        });

        animSet.start();
    }
}
