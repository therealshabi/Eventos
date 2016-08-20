package app.com.thetechnocafe.eventos;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class CommentsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_comments_container);

        if (fragment == null) {
            fragment = CommentsFragment.getInstance();
            fragmentManager.beginTransaction().add(R.id.fragment_comments_container, fragment).commit();
        }
    }
}
