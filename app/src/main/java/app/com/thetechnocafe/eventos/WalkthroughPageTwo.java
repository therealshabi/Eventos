package app.com.thetechnocafe.eventos;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by shahbaz on 25/8/16.
 */
public class WalkthroughPageTwo extends Fragment {

    public WalkthroughPageTwo() {
        // Required empty public constructor
    }

    public static WalkthroughPageTwo getInstance() {
        return new WalkthroughPageTwo();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_walkthrough_page_two, container, false);
        TextView skip;
        skip = (TextView) view.findViewById(R.id.skip2);
        skip.setOnClickListener(new View.OnClickListener() {

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
                startActivity(intent);
            }
        });

        return view;
    }

}
