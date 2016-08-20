package app.com.thetechnocafe.eventos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class WalkthroughActivity extends AppCompatActivity {

    ViewPager mViewPager;
    ViewPagerAdapter mViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walkthrough);

        mViewPager=(ViewPager)findViewById(R.id.viewPager);
        mViewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());
        mViewPagerAdapter.addFragment(new WalkthroughPageOne());
        mViewPagerAdapter.addFragment(new WalkthroughPageTwo());
        mViewPagerAdapter.addFragment(new WalkthroughPageThree());
        mViewPager.setAdapter(mViewPagerAdapter);
     }

    public class WalkthroughPageOne extends Fragment
    {

        public WalkthroughPageOne() {
            // Required empty public constructor
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            View view=  inflater.inflate(R.layout.fragment_walkthrough_page_one, container, false);

            return view;
        }
    }

    public class WalkthroughPageTwo extends Fragment
    {
        public WalkthroughPageTwo() {
            // Required empty public constructor
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            View view=  inflater.inflate(R.layout.fragment_walkthrough_page_two, container, false);

            return view;
        }
    }

    public class WalkthroughPageThree extends Fragment
    {

        public WalkthroughPageThree() {
            // Required empty public constructor
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            View view=  inflater.inflate(R.layout.fragment_walkthrough_page_three, container, false);

            return view;
        }

    }
}
