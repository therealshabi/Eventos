package app.com.thetechnocafe.eventos;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.viewpagerindicator.CirclePageIndicator;

import app.com.thetechnocafe.eventos.Utils.SharedPreferencesUtils;

public class WalkthroughActivity extends AppCompatActivity {

    ViewPager mViewPager;
    ViewPagerAdapter mViewPagerAdapter;
    boolean previouslyStarted;
    TextView skip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walkthrough);

        boolean checkState;
        checkState = SharedPreferencesUtils.getSharedPreferencsWalkthroughState(getBaseContext());
        if(checkState){
            //If is logged in, send to home screen
            if (SharedPreferencesUtils.getLoginState(getApplicationContext())) {
                //Send to home screen
                Log.d("WALK", "Sending to home");
                SharedPreferencesUtils.setSharedPreferencsWalkthroughState(getBaseContext());
                Intent homeIntent = new Intent(WalkthroughActivity.this, HomeStreamActivity.class);
                startActivity(homeIntent);
                finish();
            } else {
                Log.d("WALK", "Sending to login" + SharedPreferencesUtils.getLoginState(getApplicationContext()));
                SharedPreferencesUtils.setSharedPreferencsWalkthroughState(getBaseContext());
                Intent intent = new Intent(WalkthroughActivity.this, SigninActivity.class);
                startActivity(intent);
                finish();
            }
        }

       /* SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        previouslyStarted = prefs.getBoolean(getString(R.string.prefs_previously_started), false);
        if (previouslyStarted) {
            //If is logged in, send to home screen
            if (SharedPreferencesUtils.getLoginState(getApplicationContext())) {
                //Send to home screen
                Log.d("WALK", "Sending to home");
                Intent homeIntent = new Intent(WalkthroughActivity.this, HomeStreamActivity.class);
                startActivity(homeIntent);
            } else {
                Log.d("WALK", "Sending to login" + SharedPreferencesUtils.getLoginState(getApplicationContext()));
                Intent intent = new Intent(WalkthroughActivity.this, SigninActivity.class);
                startActivity(intent);
            }
        }*/

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

    @Override
    protected void onRestart() {
        super.onRestart();
        if(SharedPreferencesUtils.getSharedPreferencsWalkthroughState(getBaseContext())){
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(SharedPreferencesUtils.getSharedPreferencsWalkthroughState(getBaseContext())){
            finish();
        }
    }
}

