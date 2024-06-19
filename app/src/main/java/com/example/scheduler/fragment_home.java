package com.example.scheduler;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class fragment_home extends Fragment {

    private static final String PREFS_NAME = "schedule_prefs";
    private static final String KEY_ITEMS = "items";

    private CalendarView calendarView;
    private TextView todayEvent;
    private ListView eventListView;
    private EventItemAdapter eventItemAdapter;
    private List<EventItem> eventItems;

    private Handler handler = new Handler();
    private int clickCount = 0;
    private long startTime;
    private static final int DOUBLE_CLICK_TIME_DELTA = 300;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        calendarView = view.findViewById(R.id.calendarView);
        todayEvent = view.findViewById(R.id.todayEvent);
        eventListView = view.findViewById(R.id.eventListView);

        eventItems = new ArrayList<>();
        eventItemAdapter = new EventItemAdapter(getContext(), eventItems);
        eventListView.setAdapter(eventItemAdapter);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;

                clickCount++;
                handler.postDelayed(() -> {
                    if (clickCount == 1) {
                        loadEventsForDate(selectedDate);
                    } else if (clickCount == 2) {
                        Intent intent = new Intent(getActivity(), SchedulList.class);
                        intent.putExtra("selectedDate", selectedDate);
                        startActivity(intent);
                    }
                    clickCount = 0;
                }, DOUBLE_CLICK_TIME_DELTA);
            }
        });

        loadEventsForDate(getCurrentDate());

        return view;
    }

    private String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // January is 0
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return year + "-" + month + "-" + day;
    }

    private void loadEventsForDate(String date) {
        eventItems.clear();
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String jsonString = sharedPreferences.getString(KEY_ITEMS + date, null);
        if (jsonString != null) {
            try {
                JSONArray jsonArray = new JSONArray(jsonString);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    EventItem eventItem = EventItem.fromJson(jsonObject);
                    eventItems.add(eventItem);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (eventItems.isEmpty()) {
            todayEvent.setText("No events");
        } else {
            todayEvent.setText("Today Event");
        }
        eventItemAdapter.notifyDataSetChanged();
    }
}