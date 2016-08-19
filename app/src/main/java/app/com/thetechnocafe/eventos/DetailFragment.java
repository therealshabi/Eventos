package app.com.thetechnocafe.eventos;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {

    private LinearLayout mRecentComments;

    public static DetailFragment getInstance() {
        return new DetailFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_detail, container, false);

        mRecentComments = (LinearLayout) view.findViewById(R.id.fragment_detail_comment_container);

        //Add comments
        addRecentComments();

        //Set up the toolbar
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);

        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
            activity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_back);
        }

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                getActivity().supportFinishAfterTransition();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void addRecentComments() {
        for (int i = 0; i < 3; i++) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.comment_recent_item, null);
            mRecentComments.addView(view);
        }
    }
}
