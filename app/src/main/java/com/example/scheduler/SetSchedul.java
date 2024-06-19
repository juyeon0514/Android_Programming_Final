package com.example.scheduler;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;

public class SetSchedul extends AppCompatActivity {

    private EditText startTimeEditText, endTimeEditText, emojiEditText, titleEditText, descriptionEditText;
    private Switch alaramButton;
    private Button saveButton, cancelButton;
    private FloatingActionButton fabTag;
    private LinearLayout tagContainer;
    private androidx.appcompat.widget.Toolbar toolbar;
    private String[] tags = {"운동", "공부", "약속", "이벤트", "데이트", "여행", "기타"};
    private String selectedDate;
    private Switch alaram;
    private boolean alarmCheck;

    private boolean alarmSet = false;
    private int alarmRequestCode = 100;
    private SchedulerItem currentItem;
    private int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_schedul);

        emojiEditText = findViewById(R.id.emojiEditText);
        titleEditText = findViewById(R.id.titleEditText);
        startTimeEditText = findViewById(R.id.startTimeEditText);
        endTimeEditText = findViewById(R.id.endTimeEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        alaramButton = findViewById(R.id.alaramButton);
        saveButton = findViewById(R.id.saveButton);
        cancelButton = findViewById(R.id.cancelButton);
        tagContainer = findViewById(R.id.tagContainer);
        fabTag = findViewById(R.id.fabTag);
        toolbar = findViewById(R.id.toolbar1);
        alaram = findViewById(R.id.alaramButton);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_arrow_back_50);

        selectedDate = getIntent().getStringExtra("selectedDate");

        currentItem = (SchedulerItem) getIntent().getSerializableExtra("item");
        position = getIntent().getIntExtra("position", -1);

        if (currentItem != null) {
            emojiEditText.setText(currentItem.getEmoji());
            titleEditText.setText(currentItem.getTitle());
            descriptionEditText.setText(currentItem.getDescription());
            String[] times = currentItem.getTime().split(" ~ ");
            if (times.length == 2) {
                startTimeEditText.setText(times[0]);
                endTimeEditText.setText(times[1]);
            }
            addTag(currentItem.getTag());
            alaramButton.setChecked(currentItem.isAlarmOn());
        }

        startTimeEditText.setOnClickListener(v -> showTimePickerDialog(startTimeEditText));
        endTimeEditText.setOnClickListener(v -> showTimePickerDialog(endTimeEditText));

        fabTag.setOnClickListener(v -> showTagDialog());
        saveButton.setOnClickListener(v -> saveData());
        cancelButton.setOnClickListener(v -> deleteData());

        alaramButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Calendar calendar = Calendar.getInstance();
                String[] startTimeParts = startTimeEditText.getText().toString().split(":");
                if (startTimeParts.length == 2) {
                    int hour = Integer.parseInt(startTimeParts[0]);
                    int minute = Integer.parseInt(startTimeParts[1]);
                    calendar.set(Calendar.HOUR_OF_DAY, hour);
                    calendar.set(Calendar.MINUTE, minute);
                    setAlarm(titleEditText.getText().toString(), calendar);
                    alarmCheck = true;
                }
            } else {
                cancelAlarm();
                alarmCheck = false;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id)
        {
            case android.R.id.home: {
                finish();
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void showTimePickerDialog(EditText timeEditText) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minuteOfHour) -> {
                    String time = String.format("%02d:%02d", hourOfDay, minuteOfHour);
                    timeEditText.setText(time);
                }, hour, minute, true);
        timePickerDialog.show();
    }

    private void showTagDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Tag");

        builder.setItems(tags, (dialog, which) -> {
            String selectedTag = tags[which];
            addTag(selectedTag);
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void addTag(String tag) {
        TextView tagView = new TextView(this);
        tagView.setText(tag);
        tagView.setPadding(16, 8, 16, 8);
        tagView.setTextColor(getResources().getColor(android.R.color.black));
        tagView.setTextSize(16);
        tagView.setGravity(Gravity.CENTER);
        tagView.setOnClickListener(v -> tagContainer.removeView(tagView));

        tagContainer.removeAllViews();
        tagContainer.addView(tagView);
    }

    private void saveData() {
        String emoji = emojiEditText.getText().toString();
        String title = titleEditText.getText().toString();
        String startTime = startTimeEditText.getText().toString();
        String endTime = endTimeEditText.getText().toString();
        String description = descriptionEditText.getText().toString();
        boolean isAlarmOn = alaramButton.isChecked();

        StringBuilder tags = new StringBuilder();
        for (int i = 0; i < tagContainer.getChildCount(); i++) {
            TextView tagView = (TextView) tagContainer.getChildAt(i);
            tags.append(tagView.getText().toString());
            if (i < tagContainer.getChildCount() - 1) {
                tags.append(", ");
            }
        }

        SchedulerItem newItem = new SchedulerItem(emoji, title, startTime + " ~ " + endTime, tags.toString(), description,false, isAlarmOn);

        Intent resultIntent = new Intent();
        resultIntent.putExtra("newItem", newItem);
        resultIntent.putExtra("position", position);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void deleteData() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("deleteItem", true);
        resultIntent.putExtra("position", position);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void setAlarm(String title, Calendar calendar) {
        Intent intent = new Intent(this, AlarmReciver.class);
        intent.putExtra("title", title);
        intent.putExtra("message", "일정 시작 20분 전입니다.");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() - 20 * 60 * 1000, pendingIntent);
        }
    }

    private void cancelAlarm() {
        Intent intent = new Intent(this, AlarmReciver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }
}
