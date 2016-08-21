package app.com.thetechnocafe.eventos;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by gurleensethi on 20/08/16.
 */
public class AddEventFragment extends Fragment {
    private static final String TAG = "AddEventFragment";
    private TextView mInfoText;
    public static AddEventFragment getInstance() {
        return new AddEventFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_event, container, false);

        mInfoText = (TextView) view.findViewById(R.id.fragment_add_event_info);

        //Set up info text with image view in it
        SpannableStringBuilder builder = new SpannableStringBuilder();
        Drawable textImage = getResources().getDrawable(R.drawable.ic_show_info);
        if (textImage != null) {
            textImage.setBounds(0, 0, mInfoText.getLineHeight(), mInfoText.getLineHeight());
        }
        builder.append(getString(R.string.new_event_info))
                .append(" ")
                .setSpan(new ImageSpan(textImage), builder.length() - 1, builder.length(), 0);
        mInfoText.setText(builder);

        //Set up the toolbar
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.fragment_add_event_toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //activity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_back);
            activity.getSupportActionBar().setDisplayShowTitleEnabled(true);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.add_new_event_menu, menu);
    }
}