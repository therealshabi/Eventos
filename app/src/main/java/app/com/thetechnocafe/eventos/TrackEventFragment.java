package app.com.thetechnocafe.eventos;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

/**
 * Created by gurleensethi on 25/08/16.
 */
public class TrackEventFragment extends Fragment {

    private PieChart mAttendingPieChart;
    private HorizontalBarChart mRatingBarChart;

    public static TrackEventFragment getInstance() {
        return new TrackEventFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_track_event, container, false);

        mAttendingPieChart = (PieChart) view.findViewById(R.id.fragment_track_event_attending_pie_chart);
        mRatingBarChart = (HorizontalBarChart) view.findViewById(R.id.fragment_track_event_rating_bar_chart);

        //Set up toolbar
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.fragment_track_event_toolbar);
        activity.setSupportActionBar(toolbar);
        if(activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayShowTitleEnabled(true);
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        setUpAttendingPieChart();
        setUpRatingBarChart();

        return view;
    }

    private void setUpAttendingPieChart() {
        //Set up entries
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(11f,0));
        entries.add(new Entry(5f,1));
        entries.add(new Entry(11f,2));

        //Set up labels
        ArrayList<String> labels = new ArrayList<>();
        labels.add("14's");
        labels.add("15's");
        labels.add("16's");

        //Create data set
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
        dataSet.setValueTextSize(12f);

        //Create pie data
        PieData data = new PieData(labels, dataSet);

        mAttendingPieChart.setData(data);
        mAttendingPieChart.setDrawHoleEnabled(false);
        mAttendingPieChart.setDescription("");
        mAttendingPieChart.animateY(1500);

    }

    private void setUpRatingBarChart() {
        //Set up the data
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(5,0));
        entries.add(new BarEntry(2,1));
        entries.add(new BarEntry(7,2));
        entries.add(new BarEntry(1,3));
        entries.add(new BarEntry(4,4));

        //Set up labels
        ArrayList<String> labels = new ArrayList<>();
        labels.add("1");
        labels.add("2");
        labels.add("3");
        labels.add("4");
        labels.add("5");

        //Create data set
        BarDataSet dataSet = new BarDataSet(entries,"");
        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(Color.WHITE);

        dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);

        //Create bar data
        BarData data = new BarData(labels, dataSet);

        mRatingBarChart.setData(data);
        mRatingBarChart.setDescription("");
        mRatingBarChart.animateY(1500);
        //Hide the axis lines
        mRatingBarChart.getAxisLeft().setEnabled(false);
        mRatingBarChart.getAxisRight().setEnabled(false );
        mRatingBarChart.getAxisLeft().setDrawGridLines(false);
        mRatingBarChart.getAxisLeft().setDrawAxisLine(false);
        mRatingBarChart.getAxisRight().setDrawGridLines(false);
        mRatingBarChart.getAxisRight().setDrawAxisLine(false);
        mRatingBarChart.getXAxis().setDrawGridLines(false);
        mRatingBarChart.getXAxis().setDrawAxisLine(false);
        mRatingBarChart.getXAxis().setTextColor(Color.WHITE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
