package app.com.thetechnocafe.eventos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public class OutsideEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outside_event);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.outside_event_container);

        if (fragment == null) {
            fragment = OutsideEventFragment.getInstance();
            fragmentManager.beginTransaction().add(R.id.outside_event_container, fragment).commit();
        }
    }
}
