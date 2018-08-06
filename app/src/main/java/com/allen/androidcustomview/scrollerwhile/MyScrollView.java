package com.allen.androidcustomview.scrollerwhile;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import com.allen.androidcustomview.R;

/**
 * Created by liuml on 2016/6/11.
 */

public class MyScrollView extends View {
	// 其资源引用的是同一张图片
	private Bitmap bmpBackGround1;
	private Bitmap bmpBackGround2;
	int speed;
	int pointX;
	int tempy1;
	private Context mcontext;

	private int isfirst = 1;
	// 按钮的坐标
	private int btnX, btnY;
	// 按钮是否按下标识位
	private Boolean isPress;
	// 界面背景坐标
	private int bg1x, bg1y, bg2x, bg2y;
	private int cloudx, cloudy;

	List<Map<String, Object>> mapList;

	// 星星坐标
	private int stars0X, stars0Y, stars1X, stars1Y, stars2X, stars2Y, stars3X,
			stars3Y, stars4X, stars4Y, stars5X, stars5Y, stars6X, stars6Y,
			stars7X, stars7Y, stars8X, stars8Y, stars9X, stars9Y, stars10X,
			stars10Y;
	// //定义第二页的星星坐标
	private int MaxStars = 80;// 最大的星星数量 80个
	private int textContnt[] = new int[MaxStars];// 数字

	private int pointsY[];
	private int pointsX[];

	private int pointY, moveY = 0;

	private int activityNumber = 2;
	private Bitmap treeBackGround;// 界面背景
	private Bitmap cloud_bg;// 界面背景
	private Bitmap starsBg;// 星星
	private Bitmap starsBg_n;// 星星未做的
	// 声明一个Resources实例便于加载图片
	private Resources res = this.getResources();
	// 声明一个画笔
	private Paint paint;
	private DisplayMetrics dm; // 获取屏幕分辨率的类
	// private Scroller mScroller;
	private boolean isFirst = true;
	// 点击事件星星相关
	int mDownX = 0;
	int mDownY = 0;
	int mTempX = 0;
	int mTempY = 0;
	private static final int MAX_DISTANCE_FOR_CLICK = 100;

	private ScrollViewListener listener;

	private int screenHeight;
	private int screenWidth;

	public MyScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mcontext = context;
		// 背景加载资源
		treeBackGround = BitmapFactory.decodeResource(res, R.drawable.tree);
		cloud_bg = BitmapFactory.decodeResource(res, R.drawable.cloud_bg);
		starsBg = BitmapFactory.decodeResource(res, R.drawable.stars_big);
		starsBg_n = BitmapFactory.decodeResource(res, R.drawable.startbg_n);
		this.bmpBackGround1 = treeBackGround;
		this.bmpBackGround2 = treeBackGround;
		bg1x = 0;
		bg2x = 0;
		cloudx = 0;
		pointsY = new int[22];// Y轴
		pointsX = new int[22];// X轴
		screenHeight = DisplayUtils.getScreenHeight(context);
		screenWidth = DisplayUtils.getScreenWidth(context);
		initStartsXY();
		initTextContent();
		// mScroller = new Scroller(context);
		// LogUtils.d("屏幕 分别率 高 = " + DisplayUtils.getScreenHeight(context) +
		// " 屏幕 分别率 宽度 = " + DisplayUtils.getScreenWidth(context));
		// LogUtils.d("屏幕 分别率 getDisplayDensity = " +
		// DisplayUtils.getDisplayDensity(context));

	}

	public void setMapList(List list) {
		mapList = list;
	}

	// //调用此方法滚动到目标位置
	// public void smoothScrollTo(int fx, int fy) {
	// int dx = fx - mScroller.getFinalX();
	// int dy = fy - mScroller.getFinalY();
	// smoothScrollBy(dx, dy);
	// }
	//
	// //调用此方法设置滚动的相对偏移
	// public void smoothScrollBy(int dx, int dy) {
	//
	// //设置mScroller的滚动偏移量
	// mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), dx,
	// dy);
	// invalidate();//这里必须调用invalidate()才能保证computeScroll()会被调用，否则不一定会刷新界面，看不到滚动效果
	// }
	//
	// @Override
	// public void computeScroll() {
	// //先判断mScroller滚动是否完成
	// if (mScroller.computeScrollOffset()) {
	//
	// //这里调用View的scrollTo()完成实际的滚动
	// scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
	//
	// //必须调用该方法，否则不一定能看到滚动效果
	// postInvalidate();
	// }
	// super.computeScroll();
	// }

	private void initTextContent() {
		for (int i = 0; i < MaxStars; i++) {
			textContnt[i] = i + 1;
		}
	}

	public void setOnclick(ScrollViewListener listener) {
		this.listener = listener;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		// 首先让第一张背景底部正好填满整个屏幕
		int treeHeight = bmpBackGround1.getHeight() - getHeight();
		// LogUtils.d("bg1y = " + bg1y + " bmpBackGround1.getHeight()  = " +
		// bmpBackGround1.getHeight()
		// + " getHeight() = " + getHeight());
		if (treeHeight > 0) {// 图高大于屏幕高度

			// 第二张背景图紧接在第一张背景的上方
			// 虽然两张背景图无缝隙连接但是因为图片资源头尾
			// 直接连接不和谐，为了让视觉看不出是两张图连接而修正的位置
			bg1y = -(bmpBackGround1.getHeight() - getHeight());
			bg2y = bg1y - bmpBackGround1.getHeight();// 这里可以+100
														// 可以控制两张图之间的距离修正位置

		} else {
			bg1y = Math.abs(bmpBackGround1.getHeight() - getHeight());
			bg2y = bg1y - bmpBackGround1.getHeight();// 这里可以+100
														// 可以控制两张图之间的距离修正位置
		}

		// LogUtils.d("cloudy = " + cloudy + " bg1y  = " + bg1y + "  bg2y = " +
		// bg2y + " screenH  = " + getHeight());

		// 实例一个画笔
		paint = new Paint();
		paint.setAntiAlias(true);// 设置没有锯齿
		// 设置画笔颜色为白色
		paint.setColor(Color.WHITE);
	}

	@Override
	protected void onDraw(Canvas canvas) {

		// LogUtils.d("屏幕 分别率 h = " + getHeight());
		// LogUtils.d("屏幕 分别率 w = " + getWidth());
		// 画云背景
		canvas.drawBitmap(resizeBitmap(cloud_bg, getWidth(), getHeight()),
				cloudx, cloudy, paint);
		// drawImage(canvas, cloud_bg, cloudx, cloudy, cloud_bg.getWidth(),
		// cloud_bg.getHeight(), cloudx, cloudy, paint);
		// 绘制两张背景
		if (isFirst) {
			bmpBackGround1 = resizeBitmap(bmpBackGround1, getWidth(),
					getHeight());
			isFirst = false;
			initBgView();

		}
		canvas.drawBitmap(bmpBackGround1, bg1x, bg1y, paint);
		canvas.drawBitmap(bmpBackGround1, bg2x, bg2y, paint);
		// LogUtils.d("bmpBackGround1.getHeight() = " +
		// bmpBackGround1.getHeight() + " screenH  = " + getHeight() +
		// "  bg1y = " + bg1y);
		// LogUtils.d("bg1x = " + bg1x + " bg1y  = " + bg1y + "  bg2x = " + bg2x
		// + "  bg2y = " + bg2y + " screenH  = " + getHeight());

		starsDraw(canvas);// 绘制星星
		TextViewDraw(canvas);// 绘制文本
		super.onDraw(canvas);

	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {

		boolean isDown;
		// speed = t - oldt;
		//
		// //判断向上移动还是向下移动
		// if (speed > 0) {
		// isDown = true;
		// } else {
		// isDown = false;
		// }
		// logic(isDown);//计算点的距离

		super.onScrollChanged(l, t, oldl, oldt);
	}

	public Bitmap resizeBitmap(Bitmap bitmap, int w, int h) {
		if (bitmap != null) {
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			int newWidth = w;
			int newHeight = h;
			float scaleWight = ((float) newWidth) / width;
			float scaleHeight = ((float) newHeight) / height;
			Matrix matrix = new Matrix();
			matrix.postScale(scaleWight, scaleHeight);

			Bitmap res = Bitmap.createBitmap(bitmap, 0, 0, width, height,
					matrix, true);// oom问题 待解决
			return res;
		} else {
			return null;
		}
	}

	// 画文字
	private void TextViewDraw(Canvas canvas) {
		// 判断当前画的是第几个
		int c = activityNumber * 11;
		int k = 0;
		if (activityNumber > 2) {

			k = c - ((activityNumber - 1) * 10);
		}

		// LogUtils.d("比例 x 28 = " + (double) 28 / screenWidth);
		// LogUtils.d("比例 y 65 = " + (double) 65 / screenHeight);
		// LogUtils.d("比例 x 40 = " + (double) 40 / screenWidth);
		for (int i = 0; i < 22; i++) {

			paint.setTextSize((int) (0.037037037037037035 * screenWidth));// 设置字体大小
			// paint.setTypeface(typeface);//设置字体类型
			// Typeface.DEFAULT：默认字体。
			// Typeface.DEFAULT_BOLD：加粗字体。
			// Typeface.MONOSPACE：monospace字体。
			// Typeface.SANS_SERIF：sans字体。
			// Typeface.SERIF：serif字体。
			paint.setColor(Color.BLUE);
			// 每页第几个 我推算出来的公式 (x-1)*11+i 这是普通的推算 具体问题 需要具体改变
			// setTextColorUseReflection(Color.BLUE);
			paint.setStrokeWidth(2); // 描边宽度
			paint.setFakeBoldText(true); // 外层text采用粗体
			if (activityNumber <= 2) {
				if (textContnt[i] > 9) {

					paint.setColor(res.getColor(R.color.text_color));
					canvas.drawText(
							String.valueOf(textContnt[i]),
							pointsX[i]
									+ (int) (0.025925925925925925 * screenWidth),
							pointsY[i]
									+ (int) (0.033854166666666664 * screenHeight),
							paint);
				} else {

					paint.setColor(res.getColor(R.color.text_color));
					canvas.drawText(
							String.valueOf(textContnt[i]),
							pointsX[i]
									+ (int) (0.037037037037037035 * screenWidth),
							pointsY[i]
									+ (int) (0.033854166666666664 * screenHeight),
							paint);
				}

			} else {
				// 下面的都是从在这里做操作对text 数值
				// int u = (activityNumber - 2) * 11 + i-11;
				// 必须分前后 前面的 是一队 后面的是一队
				if (activityNumber % 2 != 0) {// 第三个开始 奇数 是第二个页面在下面 第一个页面在上
												// 判断上下的 作用
					int befo;// (n-1)*11+i
					int after;
					if (i < 11) {// 数组前面的
						befo = (activityNumber - 1) * 11 + i;
						if (befo < MaxStars) {
							if (textContnt[befo] > 9) {
								paint.setColor(res.getColor(R.color.text_color));
								canvas.drawText(
										String.valueOf(textContnt[befo]),
										pointsX[i]
												+ (int) (0.025925925925925925 * screenWidth),
										pointsY[i]
												+ (int) (0.033854166666666664 * screenHeight),
										paint);
							} else {
								paint.setColor(res.getColor(R.color.text_color));
								canvas.drawText(
										String.valueOf(textContnt[befo]),
										pointsX[i]
												+ (int) (0.037037037037037035 * screenWidth),
										pointsY[i]
												+ (int) (0.033854166666666664 * screenHeight),
										paint);
							}
						}
					} else {// 数组后面的
						after = (activityNumber - 3) * 11 + i;
						if (after < MaxStars) {
							if (textContnt[after] > 9) {
								paint.setColor(res.getColor(R.color.text_color));
								canvas.drawText(
										String.valueOf(textContnt[after]),
										pointsX[i]
												+ (int) (0.025925925925925925 * screenWidth),
										pointsY[i]
												+ (int) (0.033854166666666664 * screenHeight),
										paint);
							} else {

								paint.setColor(res.getColor(R.color.text_color));
								canvas.drawText(
										String.valueOf(textContnt[after]),
										pointsX[i]
												+ (int) (0.037037037037037035 * screenWidth),
										pointsY[i]
												+ (int) (0.033854166666666664 * screenHeight),
										paint);
							}
						}
					}
				} else {
					int befo;// (n-1)*11+i
					int after;
					if (i < 11) {// 数组前面的
						befo = (activityNumber - 2) * 11 + i;
						if (befo < MaxStars) {
							if (textContnt[befo] > 9) {
								paint.setColor(res.getColor(R.color.text_color));
								canvas.drawText(
										String.valueOf(textContnt[befo]),
										pointsX[i]
												+ (int) (0.025925925925925925 * screenWidth),
										pointsY[i]
												+ (int) (0.033854166666666664 * screenHeight),
										paint);
							} else {

								paint.setColor(res.getColor(R.color.text_color));
								canvas.drawText(
										String.valueOf(textContnt[befo]),
										pointsX[i]
												+ (int) (0.037037037037037035 * screenWidth),
										pointsY[i]
												+ (int) (0.033854166666666664 * screenHeight),
										paint);
							}
						}

					} else {// 数组后面的
						after = (activityNumber - 2) * 11 + i;
						if (after < MaxStars) {
							if (textContnt[after] > 9) {
								paint.setColor(res.getColor(R.color.text_color));
								canvas.drawText(
										String.valueOf(textContnt[after]),
										pointsX[i]
												+ (int) (0.025925925925925925 * screenWidth),
										pointsY[i]
												+ (int) (0.033854166666666664 * screenHeight),
										paint);
							} else {
								paint.setColor(res.getColor(R.color.text_color));
								canvas.drawText(
										String.valueOf(textContnt[after]),
										pointsX[i]
												+ (int) (0.037037037037037035 * screenWidth),
										pointsY[i]
												+ (int) (0.033854166666666664 * screenHeight),
										paint);
							}
						}
					}
				}

			}

			// LogUtils.d("pointsY  [" + i + "]=   " + pointsY[i]);
		}
		paint.setColor(Color.WHITE);
	}

	// 获取当前页数的开始数量
	private int getCurrent(int i) {
		int befo;
		// 判断上下
		if (activityNumber % 2 != 0) {// 奇数 第一页在上 第二页在下
			befo = (activityNumber - 1) * 11 + i;
			// LogUtils.d("befo =  " + befo + "   activityNumber = " +
			// activityNumber + " i = " + i);
		} else {
			befo = (activityNumber - 2) * 11 + i;
		}

		return befo;
	}

	// 获取点击的位置的数字
	private int getOnclickNo(int i) {
		int befo;// (n-1)*11+i
		int after;
		// 判断上下
		if (activityNumber % 2 != 0) {// 奇数 第一页在上 第二页在下
			if (i < 11) {
				befo = (activityNumber - 1) * 11 + i;
				// LogUtils.d("i 小于11  befo =  " + befo + "   activityNumber = "
				// + activityNumber + " i = " + i);
			} else {
				befo = (activityNumber - 3) * 11 + i;
				// LogUtils.d("i 大于11  befo =  " + befo + "   activityNumber = "
				// + activityNumber + " i = " + i);
			}
		} else {
			if (i < 11) {
				befo = (activityNumber - 2) * 11 + i;
				// LogUtils.d(" 偶数的时候 befo =  " + befo + "   activityNumber = "
				// + activityNumber + " i = " + i);
			} else {
				befo = (activityNumber - 2) * 11 + i;

			}
		}

		return befo;
	}

	private void starsDraw(Canvas canvas) {

		// 判断画一页当中 最多的星星 是多少
		int starsMax = (activityNumber) * 11;
		int page;

		if (starsMax > MaxStars) {// 判断当前的最多的星星是否大于定好的星星数量
			if (mapList == null) {
				for (int i = 0; i < 22; i++) {
					// 判断当前画的是第几个
					page = getCurrent(i);
					// LogUtils.d("当前画的是第几个 = " + page);
					canvas.drawBitmap(starsBg_n, pointsX[i], pointsY[i], paint);
				}
			} else {
				for (int i = 0; i < 22; i++) {
					// 判断当前画的是第几个
					page = getCurrent(i);
					// LogUtils.d("当前的 最前面的是多少 current =  " + page);

					if (page < MaxStars) {
						if (page < i) {
							Map<String, Object> stringObjectMap = mapList
									.get(page);
							String type = (String) stringObjectMap.get("type");
							if (type.equals("1")) {
								canvas.drawBitmap(starsBg, pointsX[i],
										pointsY[i], paint);
							} else {
								canvas.drawBitmap(starsBg_n, pointsX[i],
										pointsY[i], paint);
							}
						} else {
							canvas.drawBitmap(starsBg_n, pointsX[i],
									pointsY[i], paint);
						}
					}
					// LogUtils.d("pointsY  [" + i + "]=    " + pointsY[i]);
				}
			}

		} else {
			if (mapList == null) {
				for (int i = 0; i < 22; i++) {
					// 判断当前画的是第几个
					page = getCurrent(i);
					// LogUtils.d("当前画的是第几个 = " + page);
					canvas.drawBitmap(starsBg_n, pointsX[i], pointsY[i], paint);
				}
			} else {
				int size = mapList.size();
				for (int i = 0; i < 22; i++) {
					// 判断当前画的是第几个
					page = getCurrent(i);
					// LogUtils.d("当前画的是第几个 = " + page);
					if (page < size) {
						Map<String, Object> stringObjectMap = mapList.get(page);
						String type = (String) stringObjectMap.get("type");
						if (type.equals("1")) {
							canvas.drawBitmap(starsBg, pointsX[i], pointsY[i],
									paint);
						} else {
							canvas.drawBitmap(starsBg_n, pointsX[i],
									pointsY[i], paint);
						}
					} else {
						canvas.drawBitmap(starsBg_n, pointsX[i], pointsY[i],
								paint);
					}
				}
			}

		}

	}

	private void canvasStats() {

	}

	private void initStartsXY() {

		stars0X = 340;//
		stars0Y = 1180;//
		stars1X = 280;
		stars1Y = 1065;
		stars2X = 500;
		stars2Y = 967;
		stars3X = 240;
		stars3Y = 842;
		stars4X = 400;
		stars4Y = 761;
		stars5X = 540;
		stars5Y = 685;
		stars6X = 330;
		stars6Y = 526;
		stars7X = 540;
		stars7Y = 431;
		stars8X = 375;
		stars8Y = 245;
		stars9X = 550;
		stars9Y = 113;
		stars10X = 310;
		stars10Y = 57;
		speed = 0;

		pointsX[0] = (int) (screenWidth * 0.39351851851851855);
		pointsX[1] = (int) (screenWidth * 0.25925925925925924);
		pointsX[2] = (int) (screenWidth * 0.46296296296296297);
		pointsX[3] = (int) (screenWidth * 0.2222222222222222);
		pointsX[4] = (int) (screenWidth * 0.37037037037037035);
		pointsX[5] = (int) (screenWidth * 0.5);
		pointsX[6] = (int) (screenWidth * 0.3055555555555556);
		pointsX[7] = (int) (screenWidth * 0.5);
		pointsX[8] = (int) (screenWidth * 0.3472222222222222);
		pointsX[9] = (int) (screenWidth * 0.5092592592592593);
		pointsX[10] = (int) (screenWidth * 0.28703703703703703);

		// pointsX[0] = stars0X;
		// pointsX[1] = stars1X;
		// pointsX[2] = stars2X;
		// pointsX[3] = stars3X;
		// pointsX[4] = stars4X;
		// pointsX[5] = stars5X;
		// pointsX[6] = stars6X;
		// pointsX[7] = stars7X;
		// pointsX[8] = stars8X;
		// pointsX[9] = stars9X;
		// pointsX[10] = stars10X;

		pointsX[11] = pointsX[0];
		pointsX[12] = pointsX[1];
		pointsX[13] = pointsX[2];
		pointsX[14] = pointsX[3];
		pointsX[15] = pointsX[4];
		pointsX[16] = pointsX[5];
		pointsX[17] = pointsX[6];
		pointsX[18] = pointsX[7];
		pointsX[19] = pointsX[8];
		pointsX[20] = pointsX[9];
		pointsX[21] = pointsX[10];

		pointsY[0] = stars0Y;
		pointsY[1] = stars1Y;
		pointsY[2] = stars2Y;
		pointsY[3] = stars3Y;
		pointsY[4] = stars4Y;
		pointsY[5] = stars5Y;
		pointsY[6] = stars6Y;
		pointsY[7] = stars7Y;
		pointsY[8] = stars8Y;
		pointsY[9] = stars9Y;
		pointsY[10] = stars10Y;

		// pointsY[0] = (int) (screenHeight * 0.6145833333333334);
		// pointsY[1] = (int) (screenHeight * 0.5546875);
		// pointsY[2] = (int) (screenHeight * 0.5036458333333333);
		// pointsY[3] = (int) (screenHeight * 0.43854166666666666);
		// pointsY[4] = (int) (screenHeight * 0.3963541666666667);
		// pointsY[5] = (int) (screenHeight * 0.3411458333333333);
		// pointsY[6] = (int) (screenHeight * 0.27395833333333336);
		// pointsY[7] = (int) (screenHeight * 0.22447916666666667);
		// pointsY[8] = (int) (screenHeight * 0.12760416666666666);
		// pointsY[9] = (int) (screenHeight * 0.058854166666666666);
		// pointsY[10] = (int) (screenHeight * 0.0296875);

		pointsY[11] = pointsY[0] - (bmpBackGround1.getHeight());
		pointsY[12] = pointsY[1] - (bmpBackGround1.getHeight());
		pointsY[13] = pointsY[2] - (bmpBackGround1.getHeight());
		pointsY[14] = pointsY[3] - (bmpBackGround1.getHeight());
		pointsY[15] = pointsY[4] - (bmpBackGround1.getHeight());
		pointsY[16] = pointsY[5] - (bmpBackGround1.getHeight());
		pointsY[17] = pointsY[6] - (bmpBackGround1.getHeight());
		pointsY[18] = pointsY[7] - (bmpBackGround1.getHeight());
		pointsY[19] = pointsY[8] - (bmpBackGround1.getHeight());
		pointsY[20] = pointsY[9] - (bmpBackGround1.getHeight());
		pointsY[21] = pointsY[10] - (bmpBackGround1.getHeight());

		for (int i = 0; i < 11; i++) {
			double x = (double) pointsX[i] / screenWidth;
			DecimalFormat df = new DecimalFormat("0.00");// 格式化小数，.后跟几个零代表几位小数
			LogUtils.d("比例 i " + i + " x  =        " + x);
		}
		for (int i = 0; i < 11; i++) {
			double y = (double) pointsY[i] / screenHeight;
			DecimalFormat df = new DecimalFormat("0.00");// 格式化小数，.后跟几个零代表几位小数
			LogUtils.d("比例 i " + i + " y  =       " + y);
		}
		// float y = (float) 10 / screenHeight;
		// float x = (float) 10 / screenWidth;

		// LogUtils.d("比例 i 10  x  =        " + x + "比例 i 10  y  =        " +
		// y);
	}

	private void speedStarsXY() {
		for (int i = 0; i < 22; i++) {
			pointsY[i] += speed;
		}

	}

	private void setStartsXYDown(int type) {
		// LogUtils.d("pointy 0 之前= " + pointsY[0]);
		// LogUtils.d("pointy 11之前 = " + pointsY[11]);
		if (type == 0) {
			for (int i = 0; i < 11; i++) {

				int p = pointsY[i + 11];
				pointsY[i] = p - (bmpBackGround1.getHeight());
				// LogUtils.d("改变 后的 pointsY[" + i + "] = " + pointsY[i]);
			}

		} else {
			for (int i = 0; i < 11; i++) {
				// LogUtils.d("向下的第二种  pointsY[i+ 11] " + (pointsY[i] -
				// (bmpBackGround1.getHeight())));
				int p = pointsY[i];
				pointsY[i + 11] = p - (bmpBackGround1.getHeight());
				// LogUtils.d("改变 后的 向下的第二种 pointsY[" + i + "] = " +
				// pointsY[i]);
			}
		}
	}

	private void setStartsXYUp(int type) {
		if (type == 0) {
			for (int i = 0; i < 11; i++) {
				pointsY[i] = pointsY[i + 11] + (bmpBackGround1.getHeight());
			}
		} else {
			for (int i = 0; i < 11; i++) {
				pointsY[i + 11] = pointsY[i] + (bmpBackGround1.getHeight());
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// 获取当前触控位置
		// LogUtils.d("触摸事件 event.getAction() = " + event.getAction());
		int x = (int) event.getX();
		int y = (int) event.getY();

		boolean isDown;
		switch (event.getAction()) {

		case MotionEvent.ACTION_DOWN:// 当用户是按下
			pointX = x;
			pointY = y;
			mDownX = (int) event.getX();
			mDownY = (int) event.getY();
			LogUtils.d(" 按下的 点 x = " + pointX + "  y = " + pointY);
			// LogUtils.d(" 按下的 点的比例 x = " + (double) pointX / screenWidth +
			// "  y = " + (double) pointY / screenHeight);
			break;
		case MotionEvent.ACTION_MOVE:// 移动
			// LogUtils.d("init moveY " + moveY);
			tempy1 = moveY;

			moveY = (int) event.getY();

			speed = moveY - tempy1;

			// 判断向上移动还是向下移动
			if (speed > 0) {
				isDown = true;
			} else {
				isDown = false;
			}
			logic(isDown);// 计算点的距离
			// LogUtils.d("pointY = " + pointY + " moveY = " + moveY + " speed "
			// + (speed));
			break;
		case MotionEvent.ACTION_UP:
			moveY = 0;
			mTempX = (int) event.getX();
			mTempY = (int) event.getY();
			if (Math.abs(mTempX - mDownX) > MAX_DISTANCE_FOR_CLICK
					|| Math.abs(mTempY - mDownY) > MAX_DISTANCE_FOR_CLICK) {
				// 抬起的距离 和按下的距离太远 不形成点击事件
				// LogUtils.d("起的距离 和按下的距离太远 不形成点击事件");
			} else {
				isOnclick(mTempX, mTempY);

			}

			break;
		}

		invalidate();// 刷新界面
		// 使系统响应事件，返回true
		return true;

	}

	// 添加自定义点击事件
	private boolean isOnclick(int x, int y) {
		LogUtils.d("点击了");
		if (listener != null) {
			// 判断是否点击到星星上面
			return isStarsOnclick(x, y, listener);
		} else {
			return false;
		}
	}

	private boolean isStarsOnclick(int x, int y, ScrollViewListener listener) {

		int x1;
		int y1;
		int x2;
		int y2;
		// LogUtils.d("点击 X = " + x + "  点击 Y = " + y + "  星星 X = " + pointsX[0]
		// + "  星星 y = " + pointsY[0]);
		for (int i = 0; i < 22; i++) {
			x1 = pointsX[i];
			y1 = pointsY[i];
			// LogUtils.d("比例 y 100 = " + (double) 100 / screenHeight);
			// LogUtils.d("比例 x 100 = " + (double) 100 / screenWidth);
			x2 = pointsX[i] + (int) (screenWidth * 0.09259259259259259);
			y2 = pointsY[i] + (int) (screenHeight * 0.052083333333333336);
			if (x > x1 && x < x2 && y > y1 && y < y2) {
				// LogUtils.d("点击到了");
				int current = getOnclickNo(i) + 1;
				listener.myOnclick(current);
				// LogUtils.d("点击的第几位 : " + current);
				return true;

			}
		}
		// LogUtils.d("没有点击到");

		return false;

	}

	// 判断是否到顶部了
	private void isTop() {

		if (activityNumber >= 8 && bg1y > getHeight()) {
			// LogUtils.d("当第1张图片的Y坐标超出屏幕， 图片向上的情况  activityNumber 加上的  =======到顶部= bg1y"
			// + activityNumber);
			bg1y = getHeight();
			bg2y = bg1y - bmpBackGround1.getHeight();
			speed = 0;
			speedStarsXY();
			return;
		} else {
			speedStarsXY();
		}
	}

	// 背景滚动的逻辑函数
	public void logic(boolean isDown) {

		if (isDown) {// 手指向下

			if (tempy1 != 0) {
				bg1y += speed;
				bg2y += speed;
				// LogUtils.d("isTop = " + isTop());
				isTop();
			}
			// //判断是否到顶部了
			// if (activityNumber >= 8 && bg1y > getHeight()) {
			// //LogUtils.d("当第1张图片的Y坐标超出屏幕， 图片向上的情况  activityNumber 加上的  =======到顶部= bg1y"
			// + activityNumber);
			// bg1y = getHeight();
			// bg2y = bg1y - bmpBackGround1.getHeight();
			// return;
			// }

			// LogUtils.d("bg1y  = " + bg1y + "   bg2y  = " + bg2y +
			// "  bg1y - bmpBackGround1.getHeight() = " + (bg1y -
			// bmpBackGround1.getHeight()) +
			// "  -Math.abs(bmpBackGround1.getHeight() - getHeight()) = " +
			// (-Math.abs(bmpBackGround1.getHeight() - getHeight())));
			// 当第一张图片的Y坐标超出屏幕， 手指向下的情况
			// 立即将其坐标设置到第二张图的上方
			if (bg1y > getHeight()) {
				bg1y = bg2y - bmpBackGround1.getHeight();
				activityNumber += 1;
				// LogUtils.d("bg1y = " + bg1y + " getHeight = " + getHeight() +
				// "  bmpBackGround1.getHeight() = " +
				// bmpBackGround1.getHeight() + " activityNumber 加上的 =  " +
				// activityNumber);
				setStartsXYDown(0);
				// LogUtils.d("当第1张图片的Y坐标超出屏幕， 图片向上的情况  activityNumber 加上的  = "
				// + activityNumber);
			}
			// 当第二张图片的Y坐标超出屏幕，向下的情况
			// 立即将其坐标设置到第一张图的上方
			if (bg2y > getHeight()) {
				bg2y = bg1y - bmpBackGround1.getHeight();
				activityNumber += 1;
				// LogUtils.d("当第1张图片的Y坐标超出屏幕， 图片向上的情况  activityNumber 加上的  = "
				// + activityNumber);
				// LogUtils.d("bg1y = " + bg1y + " getHeight = " + getHeight() +
				// "  bmpBackGround1.getHeight() = " +
				// bmpBackGround1.getHeight() + " activityNumber 加上的 =  " +
				// activityNumber);
				setStartsXYDown(1);
			}

		} else {

			// 当第一张图片的Y坐标超出屏幕， 手指向上的情况
			// 立即将其坐标设置到第二张图的下方
			if (tempy1 != 0) {
				bg1y += speed;
				bg2y += speed;
				// //判断是否到底部
				isBottom();
			}
			// LogUtils.d("bg1y  = " + bg2y + "   bg2y  = " + bg2y +
			// "  bg1y - bmpBackGround1.getHeight() = " + (bg1y -
			// bmpBackGround1.getHeight()) +
			// "  -Math.abs(bmpBackGround1.getHeight() - getHeight()) = " +
			// (-Math.abs(bmpBackGround1.getHeight() - getHeight())));
			if (bg1y < -Math.abs(bmpBackGround1.getHeight() - getHeight())) {
				if (bg2y < bg1y) {
					activityNumber -= 1;
					setStartsXYUp(1);
					// LogUtils.d("当第1张图片的Y坐标超出屏幕， 图片向下的情况  activityNumber 减去的  = "
					// + activityNumber);
				}
				bg2y = bg1y + bmpBackGround1.getHeight();// 换屏了 首尾相接
			}
			if (bg2y < -Math.abs(bmpBackGround1.getHeight() - getHeight())) {
				if (bg1y < bg2y) {// 当换图的时候 给减一
					activityNumber -= 1;
					setStartsXYUp(0);
					// LogUtils.d("当第二张图片的Y坐标超出屏幕， 图片向下的情况  activityNumber 减去的  = "
					// + activityNumber);
				}
				bg1y = bg2y + bmpBackGround1.getHeight();
			}

		}
	}

	private void isBottom() {
		int treeHeight = bmpBackGround1.getHeight() - getHeight();

		if (treeHeight < 0) {
			if (activityNumber <= 2
					&& bg1y < Math
							.abs(bmpBackGround1.getHeight() - getHeight())) {// 如果是一开始的
																				// 到底部时
																				// 不能滑动
																				// 并且重置
																				// 坐标点
				activityNumber = 2;
				bg1y = Math.abs(bmpBackGround1.getHeight() - getHeight());
				// 第二张背景图紧接在第一张背景的上方
				// 直接连接不和谐，为了让视觉看不出是两张图连接而修正的位置
				bg2y = bg1y - bmpBackGround1.getHeight();
				initStartsXY();
				return;
			} else {
				speedStarsXY();
				// LogUtils.d("pointy speedStarsXY  11之后 = " + pointsY[11]);
			}
		} else {
			if (activityNumber <= 2
					&& bg1y < -Math.abs(bmpBackGround1.getHeight()
							- getHeight())) {// 如果是一开始的 到底部时 不能滑动 并且重置 坐标点
				activityNumber = 2;
				bg1y = -Math.abs(bmpBackGround1.getHeight() - getHeight());

				// 第二张背景图紧接在第一张背景的上方
				// +101的原因：虽然两张背景图无缝隙连接但是因为图片资源头尾
				// 直接连接不和谐，为了让视觉看不出是两张图连接而修正的位置
				bg2y = bg1y - bmpBackGround1.getHeight();
				initStartsXY();
				return;
			} else {
				speedStarsXY();
				// LogUtils.d("pointy speedStarsXY  11之后 = " + pointsY[11]);
			}
		}
	}

	private void initBgView() {
		int treeHeight = bmpBackGround1.getHeight() - getHeight();

		if (treeHeight < 0) {
			activityNumber = 2;
			bg1y = Math.abs(bmpBackGround1.getHeight() - getHeight());
			// 第二张背景图紧接在第一张背景的上方
			// 直接连接不和谐，为了让视觉看不出是两张图连接而修正的位置
			bg2y = bg1y - bmpBackGround1.getHeight();
			initStartsXY();
			return;
		} else {
			activityNumber = 2;
			bg1y = -Math.abs(bmpBackGround1.getHeight() - getHeight());
			// 第二张背景图紧接在第一张背景的上方
			// +101的原因：虽然两张背景图无缝隙连接但是因为图片资源头尾
			// 直接连接不和谐，为了让视觉看不出是两张图连接而修正的位置
			bg2y = bg1y - bmpBackGround1.getHeight();
			initStartsXY();
			return;
		}
	}

}
