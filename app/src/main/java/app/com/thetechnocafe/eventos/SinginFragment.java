package app.com.thetechnocafe.eventos;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.scottyab.aescrypt.AESCrypt;

import org.json.JSONObject;

import java.security.GeneralSecurityException;

import app.com.thetechnocafe.eventos.DataSync.RequestUtils;
import app.com.thetechnocafe.eventos.DataSync.StringUtils;
import app.com.thetechnocafe.eventos.Utils.SharedPreferencesUtils;

/**
 * Created by gurleensethi on 12/08/16.
 */
public class SinginFragment extends Fragment {
    private TextView mProblemTextView;
    private Button mSignInButton;
    private EditText mUsernameEditText;
    private EditText mPasswordEditText;
    private TextInputLayout mUsernameTextLayout;
    private TextInputLayout mPasswordTextLayout;
    private TextView mSignUpTextView;

    public static SinginFragment getInstance() {
        return new SinginFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signin, container, false);

        mProblemTextView = (TextView) view.findViewById(R.id.fragment_signin_problem_text);
        mSignInButton = (Button) view.findViewById(R.id.fragment_signin_singin_button);
        mUsernameEditText = (EditText) view.findViewById(R.id.fragment_signin_username_edit_text);
        mPasswordEditText = (EditText) view.findViewById(R.id.fragment_signin_password_edit_text);
        mUsernameTextLayout = (TextInputLayout) view.findViewById(R.id.fragment_signin_username_text_layout);
        mPasswordTextLayout = (TextInputLayout) view.findViewById(R.id.fragment_signin_password_text_layout);
        mSignUpTextView = (TextView) view.findViewById(R.id.fragment_signin_signup_text_view);

        setUpOnClickListeners();

        return view;
    }

    private void setUpOnClickListeners() {
        //Problem Dialog Box
        mProblemTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.problem_dialog, null);
                builder.setView(dialogView);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        //TODO:Implement proper authentication from server here
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //For testing purposes
                if (mUsernameEditText.getText().toString().equals("test")) {
                    Intent intent = new Intent(getContext(), HomeStreamActivity.class);
                    startActivity(intent);
                    return;
                }


                if (validateInputs()) {
                    new RequestUtils() {
                        @Override
                        public void isRequestSuccessful(boolean isSuccessful, String message) {
                            if (isSuccessful) {
                                //Save username and password in shared preferences (save encrypted password), save login state
                                try {
                                    SharedPreferencesUtils.setUsername(getContext(), mUsernameEditText.getText().toString());
                                    SharedPreferencesUtils.setPassword(getContext(), AESCrypt.encrypt(StringUtils.ENCRYPTION_KEY, mPasswordEditText.getText().toString()));
                                    SharedPreferencesUtils.setLoginState(getContext(), true);
                                } catch (GeneralSecurityException e) {
                                    e.printStackTrace();
                                }

                                //Go to Home fragment
                                Intent intent = new Intent(getContext(), HomeStreamActivity.class);
                                startActivity(intent);

                                //Finish the activity
                                getActivity().finish();
                            }
                            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                        }
                    }.signIn(getContext(), getSignInJSONObject());
                }
            }
        });

        mSignUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SignUpActvity.class);
                startActivity(intent);
            }
        });
    }

    private boolean validateInputs() {
        boolean isValid = true;

        if (mUsernameEditText.getText().toString().equals("")) {
            mUsernameTextLayout.setError(getString(R.string.username_error));
            isValid = false;
        }

        if (mPasswordEditText.getText().toString().equals("")) {
            mPasswordTextLayout.setError(getString(R.string.password_error));
            isValid = false;
        }

        return isValid;
    }

    private JSONObject getSignInJSONObject() {
        JSONObject signInJSONObject = new JSONObject();
        try {
            signInJSONObject.put(StringUtils.JSON_EMAIL, mUsernameEditText.getText().toString());
            signInJSONObject.put(StringUtils.JSON_PASSWORD, AESCrypt.encrypt(StringUtils.ENCRYPTION_KEY, mPasswordEditText.getText().toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return signInJSONObject;
    }
}
