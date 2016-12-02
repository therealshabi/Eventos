package app.com.thetechnocafe.eventos.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import app.com.thetechnocafe.eventos.AddEventFragment;
import app.com.thetechnocafe.eventos.R;

/**
 * Created by gurleensethi on 02/12/16.
 */

public class CategorySelectDialog extends android.support.v4.app.DialogFragment {
    private List<String> mResources;
    private RecyclerView mRecyclerView;

    public static CategorySelectDialog getInstance() {
        return new CategorySelectDialog();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.category_dialog, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        mResources = new ArrayList<>();
        populateList();

        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity().getApplicationContext(), 3));
        mRecyclerView.setAdapter(new CustomRecyclerAdapter());

        return view;
    }

    private void populateList() {
        for (int count = 0; count < 22; count++) {
            mResources.add("avatar_" + (count + 1));
        }
    }

    class CustomRecyclerAdapter extends RecyclerView.Adapter<CustomRecyclerAdapter.CustomViewHolder> {

        @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_image_view, parent, false);
            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(CustomViewHolder holder, int position) {
            holder.bindData(position);
        }

        @Override
        public int getItemCount() {
            return mResources.size();
        }

        //Custom View Holder
        class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private ImageView mImageView;
            private String mImageString;

            public CustomViewHolder(View view) {
                super(view);
                mImageView = (ImageView) view.findViewById(R.id.image_view);
                mImageView.setOnClickListener(this);
            }

            public void bindData(int position) {
                String uri = mResources.get(position);
                mImageString = uri;
                Log.d("Categroy", uri);
                int imageResource = getResources().getIdentifier(uri, "drawable", mImageView.getContext().getPackageName());
                mImageView.setImageResource(imageResource);
            }

            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra(AddEventFragment.DIALOG_IMAGE_URL, mImageString);
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                dismiss();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }
}
