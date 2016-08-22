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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by gurleensethi on 20/08/16.
 */
public class AddEventFragment extends Fragment {
    private static final String TAG = "AddEventFragment";
    private TextView mInfoText;
    private ImageButton mAddContactImageButton;
    private LinearLayout mContactsContainer;
    private ImageButton mAddForumLinkImageButton;
    private LinearLayout mLinkContainer;
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
        mAddContactImageButton = (ImageButton) view.findViewById(R.id.fragment_add_event_add_contact);
        mContactsContainer = (LinearLayout) view.findViewById(R.id.fragment_add_event_contacts_container);
        mAddForumLinkImageButton = (ImageButton) view.findViewById(R.id.fragment_add_event_add_forum_link);
        mLinkContainer = (LinearLayout) view.findViewById(R.id.fragment_add_event_forum_link_container);

        //Set up the toolbar
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.fragment_add_event_toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

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

        //Set up on click for add contact image button (add new view to linear layout contacts container)
        mAddContactImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addContactToContainer();
            }
        });

        //Set up on click for add forum link image button (add new view to linear layout link container)
        mAddForumLinkImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addForumLinkToContainer();
            }
        });

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

    private void addContactToContainer() {
        final View view = LayoutInflater.from(getContext()).inflate(R.layout.contact_email_phone, null);
        ImageButton imageButton = (ImageButton) view.findViewById(R.id.contact_email_phone_remove);
        imageButton.setVisibility(View.VISIBLE);
        //Set up remove function for remove view
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContactsContainer.removeView(view);
            }
        });
        mContactsContainer.addView(view);
    }

    private void addForumLinkToContainer() {
        final View view = LayoutInflater.from(getContext()).inflate(R.layout.submission_forum, null);
        ImageButton imageButton = (ImageButton) view.findViewById(R.id.submission_forum_remove_button);
        imageButton.setVisibility(View.VISIBLE);
        //Set up remove function to remove view
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLinkContainer.removeView(view);
            }
        });
        mLinkContainer.addView(view);
    }
}
