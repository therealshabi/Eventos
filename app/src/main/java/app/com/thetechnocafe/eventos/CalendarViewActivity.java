package app.com.thetechnocafe.eventos;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

public class CalendarViewActivity extends AppCompatActivity {

    private CalendarView mCalendarView;
    private RecyclerView mCalendarRecyclerView;
    private CalendarRecyclerAdapter mCalendarRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_view);

        mCalendarView = (CalendarView) findViewById(R.id.calendar_view_calendar);
        mCalendarRecyclerView = (RecyclerView) findViewById(R.id.calendar_view_recycler_view);

        //Set up toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.fragment_add_track_event_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_back);
        }

        setUpOnClickListeners();
        setUpOrRefreshRecyclerView();
    }

    private void setUpOnClickListeners() {
        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                Toast.makeText(getApplicationContext(), "Date changed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Set up or refresh recycler view
    private void setUpOrRefreshRecyclerView() {
        if (mCalendarRecyclerAdapter == null) {
            mCalendarRecyclerAdapter = new CalendarRecyclerAdapter();
            mCalendarRecyclerView.setAdapter(mCalendarRecyclerAdapter);
            mCalendarRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        } else {
            mCalendarRecyclerAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Adapter and View Holder for calendar recycler view
     */
    class CalendarRecyclerAdapter extends RecyclerView.Adapter<CalendarRecyclerAdapter.CalendarViewHolder> {

        //View Holder class
        class CalendarViewHolder extends RecyclerView.ViewHolder {
            private TextView mTitleTextView;
            private TextView mTimeTextView;

            public CalendarViewHolder(View view) {
                super(view);

                mTimeTextView = (TextView) findViewById(R.id.item_calendar_view_recycler_time_text_view);
                mTitleTextView = (TextView) findViewById(R.id.item_calendar_view_recycler_title_text_view);
            }

            public void bindData(int position) {

            }
        }

        @Override
        public CalendarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_calendar_view_recycler, parent, false);
            return new CalendarViewHolder(view);
        }

        @Override
        public void onBindViewHolder(CalendarViewHolder holder, int position) {
            holder.bindData(position);
        }

        @Override
        public int getItemCount() {
            return 5;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
