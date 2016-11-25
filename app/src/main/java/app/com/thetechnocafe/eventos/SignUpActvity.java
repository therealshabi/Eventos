package app.com.thetechnocafe.eventos;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import app.com.thetechnocafe.eventos.DataSync.RequestUtils;
import app.com.thetechnocafe.eventos.DataSync.StringUtils;
import app.com.thetechnocafe.eventos.Utils.EncryptionUtils;

public class SignUpActvity extends AppCompatActivity {

    private EditText mEmailEditText;
    private EditText mPasswordExitText;
    private EditText mPasswordConfirmEditText;
    private EditText mPhoneEditText;
    private Button mSignUpButton;
    private EditText mFullNameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mEmailEditText = (EditText) findViewById(R.id.fragment_signup_email_edit_text);
        mPasswordExitText = (EditText) findViewById(R.id.fragment_signup_password_edit_text);
        mPasswordConfirmEditText = (EditText) findViewById(R.id.fragment_signup_password_confirm_edit_text);
        mPhoneEditText = (EditText) findViewById(R.id.fragment_signup_phone_edit_text);
        mSignUpButton = (Button) findViewById(R.id.fragment_signup_button);
        mFullNameEditText = (EditText) findViewById(R.id.fragment_signup_full_name_edit_text);

        setUpOnClickListeners();
    }

    private void setUpOnClickListeners() {
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check for validation and send sign up request
                if (validateForm()) {
                    new RequestUtils() {
                        @Override
                        public void isRequestSuccessful(boolean isSuccessful, String message) {
                            Toast.makeText(SignUpActvity.this, message, Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }.signUp(
                            SignUpActvity.this,
                            getInJSONFormat()
                    );
                }

            }
        });
    }

    //Validate form
    private boolean validateForm() {
        if (mFullNameEditText.getText().toString().equals("")) {
            mFullNameEditText.requestFocus();
            return false;
        }
        if (mEmailEditText.getText().toString().equals("")) {
            mEmailEditText.requestFocus();
            return false;
        }
        if (mPasswordExitText.getText().toString().equals("")) {
            mEmailEditText.requestFocus();
            return false;
        }
        if (mPasswordConfirmEditText.getText().toString().equals("")) {
            mEmailEditText.requestFocus();
            return false;
        }
        if (mPasswordExitText.getText().toString().equals("")) {
            mEmailEditText.requestFocus();
            return false;
        }
        if (!mPasswordConfirmEditText.getText().toString().equals(mPasswordExitText.getText().toString())) {
            mPasswordConfirmEditText.requestFocus();
            Snackbar.make(mPasswordConfirmEditText, getString(R.string.password_match_error), Snackbar.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    //Convert to JSON Object
    private JSONObject getInJSONFormat() {
        JSONObject object = new JSONObject();
        try {
            object.put(StringUtils.JSON_EMAIL, mEmailEditText.getText().toString());
            object.put(StringUtils.JSON_PASSWORD, EncryptionUtils.encryptPassword(mPasswordExitText.getText().toString()));
            object.put(StringUtils.JSON_PHONE, mPhoneEditText.getText().toString());
            object.put(StringUtils.JSON_FULL_NAME, mFullNameEditText.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object;
    }
}
