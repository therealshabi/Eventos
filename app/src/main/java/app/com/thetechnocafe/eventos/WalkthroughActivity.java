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
        mViewPagerAdapter.addFragment(new WalkthroughPageOne());
        mViewPagerAdapter.addFragment(new WalkthroughPageTwo());
        mViewPagerAdapter.addFragment(new WalkthroughPageThree());
        mViewPagerAdapter.addFragment(new WalkthroughPageFour());
        mViewPagerAdapter.addFragment(new WalkthroughPageFive());
        mViewPager.setAdapter(mViewPagerAdapter);
        CirclePageIndicator viewIndicator = (CirclePageIndicator) findViewById(R.id.titles);
        viewIndicator.setViewPager(mViewPager);
    }

    public class WalkthroughPageOne extends Fragment {

        public WalkthroughPageOne() {
            // Required empty public constructor
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            View view = inflater.inflate(R.layout.fragment_walkthrough_page_one, container, false);
            TextView skip;
            skip = (TextView) view.findViewById(R.id.skip1);
            skip.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(WalkthroughActivity.this, SigninActivity.class);
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    previouslyStarted = prefs.getBoolean(getString(R.string.prefs_previously_started), false);
                    if (!previouslyStarted) {
                        SharedPreferences.Editor edit = prefs.edit();
                        edit.putBoolean(getString(R.string.prefs_previously_started), Boolean.TRUE);
                        edit.commit();
                    }
                    startActivity(intent);
                }
            });

            return view;
        }

    }

    public class WalkthroughPageTwo extends Fragment {
        public WalkthroughPageTwo() {
            // Required empty public constructor
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            View view = inflater.inflate(R.layout.fragment_walkthrough_page_two, container, false);

            skip = (TextView) view.findViewById(R.id.skip2);
            skip.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(WalkthroughActivity.this, SigninActivity.class);
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    previouslyStarted = prefs.getBoolean(getString(R.string.prefs_previously_started), false);
                    if (!previouslyStarted) {
                        SharedPreferences.Editor edit = prefs.edit();
                        edit.putBoolean(getString(R.string.prefs_previously_started), Boolean.TRUE);
                        edit.commit();
                    }
                    startActivity(intent);
                }
            });

            return view;
        }
    }

    public class WalkthroughPageThree extends Fragment {

        public WalkthroughPageThree() {
            // Required empty public constructor
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            View view = inflater.inflate(R.layout.fragment_walkthrough_page_three, container, false);
            ImageView mImageViewFilling = (ImageView) view.findViewById(R.id.heart);
            ((AnimationDrawable) mImageViewFilling.getBackground()).start();

            skip = (TextView) view.findViewById(R.id.skip3);
            skip.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(WalkthroughActivity.this, SigninActivity.class);
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    previouslyStarted = prefs.getBoolean(getString(R.string.prefs_previously_started), false);
                    if (!previouslyStarted) {
                        SharedPreferences.Editor edit = prefs.edit();
                        edit.putBoolean(getString(R.string.prefs_previously_started), Boolean.TRUE);
                        edit.commit();
                    }
                    startActivity(intent);
                }
            });

            return view;
        }

    }

    public class WalkthroughPageFour extends Fragment {

        public WalkthroughPageFour() {
            // Required empty public constructor
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            View view = inflater.inflate(R.layout.fragment_walkthrough_page_four, container, false);

            skip = (TextView) view.findViewById(R.id.skip4);
            skip.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(WalkthroughActivity.this, SigninActivity.class);
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    previouslyStarted = prefs.getBoolean(getString(R.string.prefs_previously_started), false);
                    if (!previouslyStarted) {
                        SharedPreferences.Editor edit = prefs.edit();
                        edit.putBoolean(getString(R.string.prefs_previously_started), Boolean.TRUE);
                        edit.commit();
                    }
                    startActivity(intent);
                }
            });

            return view;
        }

    }

    public class WalkthroughPageFive extends Fragment {

        public WalkthroughPageFive() {
            // Required empty public constructor
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            View view = inflater.inflate(R.layout.fragment_walkthrough_page_five, container, false);
            Button btn = (Button) view.findViewById(R.id.get_started_btn);
            final TextView mTitleText = (TextView) view.findViewById(R.id.app_name);
            final ImageView mImageLogo = (ImageView) view.findViewById(R.id.logo);


            btn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(WalkthroughActivity.this, SigninActivity.class);
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    previouslyStarted = prefs.getBoolean(getString(R.string.prefs_previously_started), false);
                    if (!previouslyStarted) {
                        SharedPreferences.Editor edit = prefs.edit();
                        edit.putBoolean(getString(R.string.prefs_previously_started), Boolean.TRUE);
                        edit.commit();
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Pair<View, String> p1 = Pair.create((View) mTitleText, getString(R.string.textTrans));
                        Pair<View, String> p2 = Pair.create((View) mImageLogo, getString(R.string.imgTrans));
                        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), p1, p2);
                        startActivity(intent, optionsCompat.toBundle());
                    } else {
                        startActivity(intent);
                    }
                }
            });
            return view;
        }

    }
}
