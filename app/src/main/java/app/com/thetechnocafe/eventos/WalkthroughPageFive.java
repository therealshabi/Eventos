package app.com.thetechnocafe.eventos;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import app.com.thetechnocafe.eventos.Utils.SharedPreferencesUtils;

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
                SharedPreferencesUtils.setSharedPreferencsWalkthroughState(getContext());
                    if(SharedPreferencesUtils.getLoginState(getContext())){
                        Intent intent = new Intent(getActivity(),HomeStreamActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                    else{
                        Intent intent = new Intent(getActivity(),SigninActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }

            }
        });

        return view;
    }
}
