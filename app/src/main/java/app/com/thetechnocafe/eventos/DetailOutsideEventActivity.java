package app.com.thetechnocafe.eventos;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DetailOutsideEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_outside_event);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.main_outside_activity);

        if (fragment == null) {
            fragment = DetailOutsideFragment.getInstance(getIntent().getStringExtra(OutsideEventFragment.INTENT_EXTRA_EVENT_ID));
            fragmentManager.beginTransaction().add(R.id.main_outside_activity, fragment).commit();
        }
    }
}
