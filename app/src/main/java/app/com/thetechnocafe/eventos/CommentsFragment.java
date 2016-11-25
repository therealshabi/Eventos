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

import java.util.Date;
import java.util.List;

import app.com.thetechnocafe.eventos.Database.EventsDatabaseHelper;
import app.com.thetechnocafe.eventos.Models.CommentsModel;
import app.com.thetechnocafe.eventos.Utils.DateUtils;

/**
 * Created by gurleensethi on 20/08/16.
 */
public class CommentsFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private CommentAdapter mCommentAdapter;
    private List<CommentsModel> mCommentsModelList;
    private EventsDatabaseHelper mDatabaseHelper;
    private static final String EVENT_ID_TAG = "event_tag";
    private static final String EVENT_ID = "event_id";
    private TextView mNoCommentsTextView;

    public static CommentsFragment getInstance(String eventID) {
        CommentsFragment fragment = new CommentsFragment();
        Bundle args = new Bundle();
        args.putString(EVENT_ID_TAG, eventID);
        fragment.setArguments(args);
        return fragment;
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
        mNoCommentsTextView = (TextView) view.findViewById(R.id.fragment_comments_no_comments_text_view);

        //Set up the toolbar
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.fragment_comments_toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            activity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_back);
            activity.getSupportActionBar().setTitle(getString(R.string.comments));
        }

        mDatabaseHelper = new EventsDatabaseHelper(getContext());
        mCommentsModelList = mDatabaseHelper.getCommentsList(getArguments().getString(EVENT_ID_TAG));

        setUpOrRefershRecyclerView();

        return view;
    }

    private void setUpOrRefershRecyclerView() {
        if(mCommentAdapter == null) {
            //Set up the recycler view
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            mCommentAdapter = new CommentAdapter();
            mRecyclerView.setAdapter(mCommentAdapter);
        } else {
            mCommentAdapter.notifyDataSetChanged();
        }

        if(mCommentsModelList.size() == 0) {
            mNoCommentsTextView.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }
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

        public void bindData(int position) {
            mIdText.setText(mCommentsModelList.get(position).getFrom());
            mDateText.setText(DateUtils.getFormattedDate(new Date(mCommentsModelList.get(position).getTime())));
            mCommentText.setText(mCommentsModelList.get(position).getComment());
        }
    }

    //Adapter class for recycler view
    class CommentAdapter extends RecyclerView.Adapter<CommentViewHolder> {

        @Override
        public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.comment_item, parent, false);
            return new CommentViewHolder(view);
        }

        @Override
        public void onBindViewHolder(CommentViewHolder holder, int position) {
            holder.bindData(position);
        }

        @Override
        public int getItemCount() {
            return mCommentsModelList.size();
        }
    }
}
