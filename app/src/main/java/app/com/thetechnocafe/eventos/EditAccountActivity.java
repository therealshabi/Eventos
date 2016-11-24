package app.com.thetechnocafe.eventos;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import app.com.thetechnocafe.eventos.Utils.SharedPreferencesUtils;

public class EditAccountActivity extends AppCompatActivity {

    private TextView mFullNameTextView;
    private TextView mEmailTextView;
    private TextView mPhoneTextView;
    private EditText mOldPasswordEditText;
    private EditText mNewaPasswordEditText;
    private EditText mNewPasswordConfirmEditText;
    private Button mUpdateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);

        mFullNameTextView = (TextView) findViewById(R.id.full_name_text_view);
        mEmailTextView = (TextView) findViewById(R.id.email_text_view);
        mPhoneTextView = (TextView) findViewById(R.id.phone_number_text_view);
        mOldPasswordEditText = (EditText) findViewById(R.id.old_password_edit_text);
        mNewaPasswordEditText = (EditText) findViewById(R.id.new_password_edit_text);
        mNewPasswordConfirmEditText = (EditText) findViewById(R.id.new_password_confirm_edit_text);
        mUpdateButton = (Button) findViewById(R.id.update_button);

        //Set up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_back);

        setUpData();
        setUpOnClickListeners();
    }

    private void setUpData() {
        mFullNameTextView.setText(SharedPreferencesUtils.getFullName(this));
        mEmailTextView.setText(SharedPreferencesUtils.getUsername(this));
        mPhoneTextView.setText(SharedPreferencesUtils.getPhoneNumber(this));
    }

    private void setUpOnClickListeners() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
