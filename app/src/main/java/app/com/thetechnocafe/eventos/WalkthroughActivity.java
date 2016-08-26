package app.com.thetechnocafe.eventos;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.viewpagerindicator.CirclePageIndicator;

public class WalkthroughActivity extends AppCompatActivity {

    ViewPager mViewPager;
    ViewPagerAdapter mViewPagerAdapter;
    boolean previouslyStarted;
    TextView skip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walkthrough);

        Intent intent = new Intent(WalkthroughActivity.this, SigninActivity.class);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        previouslyStarted = prefs.getBoolean(getString(R.string.prefs_previously_started), false);
        if (previouslyStarted) {
            startActivity(intent);
        }

        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPagerAdapter.addFragment(WalkthroughPageOne.getInstance());
        mViewPagerAdapter.addFragment(WalkthroughPageTwo.getInstance());
        mViewPagerAdapter.addFragment(WalkthroughPageThree.getInstance());
        mViewPagerAdapter.addFragment(WalkthroughPageFour.getInstance());
        mViewPagerAdapter.addFragment(WalkthroughPageFive.getInstance());
        mViewPager.setAdapter(mViewPagerAdapter);
        CirclePageIndicator viewIndicator = (CirclePageIndicator) findViewById(R.id.titles);
        viewIndicator.setViewPager(mViewPager);

    }
}

