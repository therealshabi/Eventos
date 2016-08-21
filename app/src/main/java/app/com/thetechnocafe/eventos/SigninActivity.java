package app.com.thetechnocafe.eventos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public class SigninActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.singin_fragment_container);

        if(fragment == null) {
            fragment = SinginFragment.getInstance();
            fragmentManager.beginTransaction().add(R.id.singin_fragment_container, fragment).commit();
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
