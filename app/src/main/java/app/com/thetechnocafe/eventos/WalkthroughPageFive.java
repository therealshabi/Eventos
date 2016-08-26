package app.com.thetechnocafe.eventos;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by shahbaz on 26/8/16.
 */
public class WalkthroughPageFive extends Fragment {

    public WalkthroughPageFive() {
        // Required empty public constructor
    }

    public static WalkthroughPageFive getInstance() {
        return new WalkthroughPageFive();
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
                boolean previouslyStarted;
                Intent intent = new Intent(getActivity(), SigninActivity.class);
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
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
