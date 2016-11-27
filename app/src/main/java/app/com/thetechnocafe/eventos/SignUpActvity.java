package app.com.thetechnocafe.eventos;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.scottyab.aescrypt.AESCrypt;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.GeneralSecurityException;

import app.com.thetechnocafe.eventos.DataSync.RequestUtils;
import app.com.thetechnocafe.eventos.DataSync.StringUtils;
import app.com.thetechnocafe.eventos.Utils.EncryptionUtils;
import app.com.thetechnocafe.eventos.Utils.SharedPreferencesUtils;

public class SignUpActvity extends AppCompatActivity {

    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private EditText mPasswordConfirmEditText;
    private EditText mPhoneEditText;
    private Button mSignUpButton;
    private EditText mFullNameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mEmailEditText = (EditText) findViewById(R.id.fragment_signup_email_edit_text);
        mPasswordEditText = (EditText) findViewById(R.id.fragment_signup_password_edit_text);
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
                            if (isSuccessful) {
                                //Save username and password in shared preferences (save encrypted password), save login state
                                try {
                                    SharedPreferencesUtils.setUsername(getBaseContext(), mEmailEditText.getText().toString());
                                    SharedPreferencesUtils.setPassword(getBaseContext(), AESCrypt.encrypt(StringUtils.ENCRYPTION_KEY, mPasswordEditText.getText().toString()));
                                    SharedPreferencesUtils.setLoginState(getBaseContext(), true);
                                } catch (GeneralSecurityException e) {
                                    e.printStackTrace();
                                }

                                //Go to Home fragment
                                Intent intent = new Intent(SignUpActvity.this, HomeStreamActivity.class);
                                startActivity(intent);

                                Toast.makeText(SignUpActvity.this, "You've Signed up Successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else{
                                mFullNameEditText.requestFocus();
                                Toast.makeText(getBaseContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
                            }


                        }

                    }.signUp(SignUpActvity.this, getInJSONFormat()
                    );
                }

            }
        });
    }

    //Validate form
    private boolean validateForm() {
        if (mFullNameEditText.getText().toString().equals("")) {
            mFullNameEditText.requestFocus();
            Snackbar.make(mFullNameEditText, getString(R.string.full_name_empty), Snackbar.LENGTH_SHORT).show();
            return false;
        }
        if (mEmailEditText.getText().toString().equals("")) {
            mEmailEditText.requestFocus();
            Snackbar.make(mEmailEditText, getString(R.string.email_empty_error), Snackbar.LENGTH_SHORT).show();
            return false;
        }
        if (mPasswordEditText.getText().toString().equals("")) {
            mPasswordEditText.requestFocus();
            Snackbar.make(mPasswordConfirmEditText, getString(R.string.signup_password_empty), Snackbar.LENGTH_SHORT).show();
            return false;
        }
        if (mPasswordConfirmEditText.getText().toString().equals("")) {
            mPasswordConfirmEditText.requestFocus();
            Snackbar.make(mPasswordConfirmEditText, getString(R.string.password_match_error), Snackbar.LENGTH_SHORT).show();
            return false;
        }
        if (mPhoneEditText.getText().toString().equals("")) {
            Snackbar.make(mPhoneEditText, getString(R.string.phone_number_empty_error), Snackbar.LENGTH_SHORT).show();
            return false;
        }

        if(!isAlpha(mFullNameEditText.getText().toString())){
            mFullNameEditText.requestFocus();
            Snackbar.make(mFullNameEditText,getString(R.string.full_name_error),Snackbar.LENGTH_SHORT).show();
            return false;
        }

        if (!mPasswordConfirmEditText.getText().toString().equals(mPasswordEditText.getText().toString())) {
            mPasswordConfirmEditText.requestFocus();
            Snackbar.make(mPasswordConfirmEditText, getString(R.string.password_match_error), Snackbar.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    //Function to check if a string only contains alphabet
    public boolean isAlpha(String name) {
        return name.matches("[a-zA-Z]+");
    }

    //Convert to JSON Object
    private JSONObject getInJSONFormat() {
        JSONObject object = new JSONObject();
        try {
            object.put(StringUtils.JSON_EMAIL, mEmailEditText.getText().toString());
            object.put(StringUtils.JSON_PASSWORD, EncryptionUtils.encryptPassword(mPasswordEditText.getText().toString()));
            object.put(StringUtils.JSON_PHONE, mPhoneEditText.getText().toString());
            object.put(StringUtils.JSON_FULL_NAME, mFullNameEditText.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
