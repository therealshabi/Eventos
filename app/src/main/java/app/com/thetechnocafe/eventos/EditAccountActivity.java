package app.com.thetechnocafe.eventos;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.scottyab.aescrypt.AESCrypt;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.GeneralSecurityException;

import app.com.thetechnocafe.eventos.DataSync.RequestUtils;
import app.com.thetechnocafe.eventos.DataSync.StringUtils;
import app.com.thetechnocafe.eventos.Dialogs.LoadingDialog;
import app.com.thetechnocafe.eventos.Utils.SharedPreferencesUtils;

public class EditAccountActivity extends AppCompatActivity {

    private TextView mFullNameTextView;
    private TextView mEmailTextView;
    private TextView mPhoneTextView;
    private EditText mOldPasswordEditText;
    private EditText mNewPasswordEditText;
    private EditText mNewPasswordConfirmEditText;
    private Button mUpdateButton;
    private LoadingDialog mLoadingDialog;
    private static final String LOADIN_DIALOG_TAG = "loading_dialog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);

        mFullNameTextView = (TextView) findViewById(R.id.full_name_text_view);
        mEmailTextView = (TextView) findViewById(R.id.email_text_view);
        mPhoneTextView = (TextView) findViewById(R.id.phone_number_text_view);
        mOldPasswordEditText = (EditText) findViewById(R.id.old_password_edit_text);
        mNewPasswordEditText = (EditText) findViewById(R.id.new_password_edit_text);
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
        mUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Show loading dialog
                mLoadingDialog = LoadingDialog.getInstance(getString(R.string.updating_account_details));
                mLoadingDialog.show(getSupportFragmentManager(), LOADIN_DIALOG_TAG);

                if (arePasswordFieldsValid()) {
                    new RequestUtils() {
                        @Override
                        public void isRequestSuccessful(boolean isSuccessful, String message) {
                            if (isSuccessful) {
                                //Stop dialog
                                mLoadingDialog.dismiss();

                                Toast.makeText(getApplicationContext(), R.string.account_update_success, Toast.LENGTH_SHORT).show();

                                //Update password in shared preferences
                                try {
                                    SharedPreferencesUtils.setPassword(getApplicationContext(), AESCrypt.encrypt(StringUtils.ENCRYPTION_KEY, mNewPasswordEditText.getText().toString()));
                                } catch (GeneralSecurityException e) {
                                    e.printStackTrace();
                                }

                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }.updateAccountDetails(getApplicationContext(), getJSONObject());
                }
            }
        });
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

    private boolean arePasswordFieldsValid() {
        try {
            if (!AESCrypt.encrypt(StringUtils.ENCRYPTION_KEY, mOldPasswordEditText.getText().toString()).equals(SharedPreferencesUtils.getPassword(this))) {
                mOldPasswordEditText.requestFocus();
                mOldPasswordEditText.setError(getString(R.string.old_password_match));
                return false;
            }
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }

        if (mNewPasswordEditText.getText().toString().equals("")) {
            mNewPasswordEditText.requestFocus();
            mNewPasswordEditText.setError(getString(R.string.password_empty));
        }

        if (!mNewPasswordEditText.getText().toString().equals(mNewPasswordConfirmEditText.getText().toString())) {
            mNewPasswordEditText.requestFocus();
            mNewPasswordEditText.setError(getString(R.string.password_match_error));
        }

        return true;
    }

    //Returns the JSON Data of new Account
    private JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        try {
            object.put(StringUtils.JSON_EMAIL, SharedPreferencesUtils.getUsername(this));
            object.put(StringUtils.JSON_PASSWORD, AESCrypt.encrypt(StringUtils.ENCRYPTION_KEY, mNewPasswordEditText.getText().toString()));
            object.put(StringUtils.JSON_PHONE, SharedPreferencesUtils.getPhoneNumber(this));
            object.put(StringUtils.JSON_FULL_NAME, SharedPreferencesUtils.getFullName(this));
            object.put(StringUtils.JSON_OLD_PASSWORD, AESCrypt.encrypt(StringUtils.ENCRYPTION_KEY, mOldPasswordEditText.getText().toString()));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }

        return object;
    }
}
