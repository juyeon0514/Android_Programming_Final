package com.example.scheduler;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SchedulList extends AppCompatActivity {

    private static final int REQUEST_CODE_ADD_ITEM = 1;
    public static final int REQUEST_CODE_EDIT_ITEM = 2;
    private static final String PREFS_NAME = "schedule_prefs";
    private static final String KEY_ITEMS = "items";

    private TextView titleSchedulList;
    private FloatingActionButton fab;

    private List<SchedulerItem> items;
    private SchedulerItemAdapter adapter;
    private RecyclerView recyclerView;
    private String selectedDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedul_list);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        selectedDate = getIntent().getStringExtra("selectedDate");

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_arrow_back_50);

        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        items = new ArrayList<>();
        loadItems(selectedDate);
        adapter = new SchedulerItemAdapter(this, items);
        recyclerView.setAdapter(adapter);

        titleSchedulList = findViewById(R.id.titleSchedulList);
        fab = findViewById(R.id.fab);

        String selectedDate = getIntent().getStringExtra("selectedDate");
        if (selectedDate != null) {
            titleSchedulList.setText(selectedDate);
        }

        fab.setOnClickListener(v -> {
            Intent intent = new Intent(SchedulList.this, SetSchedul.class);
            intent.putExtra("selectedDate", selectedDate);
            startActivityForResult(intent, REQUEST_CODE_ADD_ITEM);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            SchedulerItem item = (SchedulerItem) data.getSerializableExtra("newItem");
            if (requestCode == REQUEST_CODE_ADD_ITEM && item != null) {
                items.add(item);
                adapter.notifyItemInserted(items.size() - 1);
                saveItems(selectedDate);
            } else if (requestCode == REQUEST_CODE_EDIT_ITEM && item != null) {
                int position = data.getIntExtra("position", -1);
                if (position != -1) {
                    items.set(position, item);
                    adapter.notifyItemChanged(position);
                    saveItems(selectedDate);
                }
            } else if (requestCode == REQUEST_CODE_EDIT_ITEM) {
                int position = data.getIntExtra("position", -1);
                if (position != -1) {
                    items.remove(position);
                    adapter.notifyItemRemoved(position);
                    saveItems(selectedDate);
                }
            }
        }
    }

    private void saveItems(String date) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        JSONArray jsonArray = new JSONArray();
        for (SchedulerItem item : items) {
            try {
                jsonArray.put(item.toJson());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        editor.putString(KEY_ITEMS + date, jsonArray.toString());
        editor.apply();
    }

    private void loadItems(String date) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String jsonString = sharedPreferences.getString(KEY_ITEMS + date, null);
        if (jsonString != null) {
            try {
                JSONArray jsonArray = new JSONArray(jsonString);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    items.add(SchedulerItem.fromJson(jsonObject));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}