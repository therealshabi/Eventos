package app.com.thetechnocafe.eventos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public class HomeStreamActivity extends AppCompatActivity {

    boolean LikeBtnStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_stream);

        // getIntent().getBooleanExtra("likeBtnStatus", LikeBtnStatus);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.home_stream_fragment_container);

        if (fragment == null) {
            fragment = HomeStreamFragment.getInstance();
            fragmentManager.beginTransaction().add(R.id.home_stream_fragment_container, fragment).commit();
        }

    }

}
