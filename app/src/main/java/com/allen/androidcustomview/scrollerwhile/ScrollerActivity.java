package com.allen.androidcustomview.scrollerwhile;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.allen.androidcustomview.R;

public class ScrollerActivity extends Activity implements ScrollViewListener {
	MyScrollView myscrollview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scroller_while);
		
		myscrollview = (MyScrollView) findViewById(R.id.myscrollview);
		myscrollview.setOnclick(this);
	}

	@Override
	public boolean myOnclick(int select) {
		Toast.makeText(this, "点击了 第几个 = "+select, Toast.LENGTH_LONG).show();
		return false;
	}

}
