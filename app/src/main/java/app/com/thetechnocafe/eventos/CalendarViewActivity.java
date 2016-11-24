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

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import app.com.thetechnocafe.eventos.Database.EventsDatabaseHelper;
import app.com.thetechnocafe.eventos.Models.EventsModel;
import app.com.thetechnocafe.eventos.Utils.DateUtils;

public class CalendarViewActivity extends AppCompatActivity {

    private CalendarView mCalendarView;
    private RecyclerView mCalendarRecyclerView;
    private CalendarRecyclerAdapter mCalendarRecyclerAdapter;
    private EventsDatabaseHelper mDatabaseHelper;
    private List<EventsModel> mEventsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_view);

        mCalendarView = (CalendarView) findViewById(R.id.calendar_view_calendar);
        mCalendarRecyclerView = (RecyclerView) findViewById(R.id.calendar_view_recycler_view);

        mDatabaseHelper = new EventsDatabaseHelper(getApplicationContext());

        //Set up toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.fragment_add_track_event_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_back);
        }

        setUpOnClickListeners();
        setUpInitialList();
    }

    private void setUpOnClickListeners() {
        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                Calendar calendar = GregorianCalendar.getInstance();

                //Set start dat time
                calendar.set(year, month, dayOfMonth, 0, 0);
                long startDayTime = calendar.getTimeInMillis();

                //Set end day time
                calendar.set(year, month, dayOfMonth + 1, 0, 0);
                long endDayTime = calendar.getTimeInMillis();

                //Get list from database
                mEventsList = mDatabaseHelper.getEventsOnADay(startDayTime, endDayTime);

                setUpOrRefreshRecyclerView();
            }
        });
    }

    private void setUpInitialList() {
        Calendar calendar = GregorianCalendar.getInstance();

        //Set start dat time
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long startDayTime = calendar.getTimeInMillis();

        //Set end day time
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        long endDayTime = calendar.getTimeInMillis();

        //Get list from database
        mEventsList = mDatabaseHelper.getEventsOnADay(startDayTime, endDayTime);

        setUpOrRefreshRecyclerView();
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

            CalendarViewHolder(View view) {
                super(view);

                mTimeTextView = (TextView) view.findViewById(R.id.item_calendar_view_recycler_time_text_view);
                mTitleTextView = (TextView) view.findViewById(R.id.item_calendar_view_recycler_title_text_view);
            }

            void bindData(int position) {
                mTitleTextView.setText(mEventsList.get(position).getTitle());
                mTimeTextView.setText(DateUtils.convertToTime(mEventsList.get(position).getDate()));
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
            return mEventsList.size();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseHelper.close();
    }
}
