package app.com.thetechnocafe.eventos;


import android.content.Intent;
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
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {

    private LinearLayout mRecentComments;
    private TextView mShowMoreCommentsText;
    private LinearLayout mLinkContainer;

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
        mShowMoreCommentsText = (TextView) view.findViewById(R.id.fragment_detail_show_more_comments);
        mLinkContainer = (LinearLayout) view.findViewById(R.id.fragment_detail_link_container);

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

        //Set up show more comments
        mShowMoreCommentsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CommentsActivity.class);
                startActivity(intent);
            }
        });

        addLink();

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
            TextView textView = (TextView) view.findViewById(R.id.comment_recent_item_comment);
            if (i == 0) {
                textView.setText("Nice event, maybe next time do your research and not waste time on just installing softwares");
            } else if (i == 1) {
                textView.setText("Very poor event");
            }
            mRecentComments.addView(view);
        }
    }

    private void addLink() {
        for (int i = 0; i < 1; i++) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.link_item, null);
            TextView textView = (TextView) view.findViewById(R.id.link_item_text);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), WebViewActivity.class);
                    intent.putExtra(WebViewActivity.EXTRA_MESSAGE, "http://www.google.com");
                    startActivity(intent);
                }
            });
            mLinkContainer.addView(view);
        }
    }
}
