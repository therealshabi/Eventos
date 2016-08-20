package app.com.thetechnocafe.eventos;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by gurleensethi on 20/08/16.
 */
public class CommentsFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private CommentAdapter mCommentAdapter;

    public static CommentsFragment getInstance() {
        return new CommentsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comments, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_comments_recycler_view);

        //Set up the recycler view
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mCommentAdapter = new CommentAdapter();
        mRecyclerView.setAdapter(mCommentAdapter);

        //Set up the toolbar
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.fragment_comments_toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            activity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_back);
            activity.getSupportActionBar().setTitle(getString(R.string.comments));
        }

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                getActivity().finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    //ViewHolder for recycler view
    private class CommentViewHolder extends RecyclerView.ViewHolder {
        private TextView mIdText;
        private TextView mDateText;
        private TextView mCommentText;

        CommentViewHolder(View view) {
            super(view);

            mIdText = (TextView) view.findViewById(R.id.comment_recent_item_id);
            mDateText = (TextView) view.findViewById(R.id.comment_recent_item_date);
            mCommentText = (TextView) view.findViewById(R.id.comment_recent_item_comment);
        }

        public void bindData() {

        }
    }

    //Adapter class for recycler view
    class CommentAdapter extends RecyclerView.Adapter<CommentViewHolder> {

        @Override
        public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.comment_recent_item, parent, false);
            return new CommentViewHolder(view);
        }

        @Override
        public void onBindViewHolder(CommentViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 10;
        }
    }
}
