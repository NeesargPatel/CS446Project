package cs446.cs.uw.tictacwoah.activities;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;

import cs446.cs.uw.tictacwoah.R;
import me.relex.circleindicator.CircleIndicator;

/**
 * Created by vicdragon on 2018-03-17.
 */

public class HelpActivity extends AppCompatActivity {
    
    private static final int PAGES = 4;

    private ViewPager pager;
    private PagerAdapter pageAdapter;

    private int displayWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_pager);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );

        pager = findViewById(R.id.help_pager);
        pageAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), this);
        pager.setAdapter(pageAdapter);
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.help_indicator);
        indicator.setViewPager(pager);
        pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                invalidateOptionsMenu();
            }
        });
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        displayWidth = displayMetrics.widthPixels;
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        private Context context;
        ScreenSlidePagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public Fragment getItem(int position) {
            return HelpFragment.create(position, displayWidth, context);
        }

        @Override
        public int getCount() {
            return PAGES;
        }
    }
}
