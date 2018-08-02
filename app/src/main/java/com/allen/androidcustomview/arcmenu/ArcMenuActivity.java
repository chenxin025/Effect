package com.allen.androidcustomview.arcmenu;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.allen.androidcustomview.R;


/**
 * @author：BoBoMEe Created at 2016/1/4.
 */
public class ArcMenuActivity extends Activity {

    private LineMenu mArcMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.arc_menu_layout2);

        initViews();
    }

    private void initViews() {
        mArcMenu =  findViewById(R.id.id_arcmenu);

        initArcMenu();
    }

    private void initArcMenu() {

        ImageView people = new ImageView(this);
        people.setImageResource(R.drawable.composer_sleep);
        people.setTag("Sleep");
        mArcMenu.addView(people);

      /*  mArcMenu.setOnMenuItemClickListener(new ArcMenu.OnMenuItemClickListener() {
            @Override
            public void onClick(View view, int pos) {
                Toast.makeText(ArcMenuActivity.this, view.getTag() + "; position :" + pos, Toast.LENGTH_LONG).show();
            }
        });*/

        /*mArcMenu.setOnMenuItemClickListener((view, pos) -> {
            Toast.makeText(this, view.getTag() + "; position :" + pos, Toast.LENGTH_LONG).show();
        });

        mArcMenu.setStatusChange(mStatus -> mArcMenu.setBackgroundColor(
            mStatus == ArcMenu.Status.OPEN ? Color.LTGRAY : Color.TRANSPARENT));*/
    }
}
