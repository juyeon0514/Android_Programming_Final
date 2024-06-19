package com.example.scheduler;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SchedulerItemAdapter extends RecyclerView.Adapter<SchedulerItemAdapter.SchedulerItemViewHolder> {
    private List<SchedulerItem> items;
    private Context context;
    private GestureDetector gestureDetector;


    public SchedulerItemAdapter(Context context, List<SchedulerItem> items) {
        this.context = context;
        this.items = items;
        gestureDetector = new GestureDetector(context, new GestureListener());
    }

    @NonNull
    @Override
    public SchedulerItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_scheduler_item, parent, false);
        return new SchedulerItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SchedulerItemViewHolder holder, int position) {
        SchedulerItem item = items.get(position);
        holder.emojiTitleText.setText(item.getEmoji());
        holder.titleTextView.setText(item.getTitle());
        holder.timeTextView.setText(item.getTime());
        holder.tagTextItem.setText(item.getTag());
        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(item.isChecked());


        if (item.isChecked()) {
            setTextStyle(holder, Color.GRAY, Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            setTextStyle(holder, Color.BLACK, 0);
        }

        if (item.isAlarmOn()) {
            holder.alarmImageView.setImageResource(R.drawable.baseline_notifications_24);
        } else {
            holder.alarmImageView.setImageResource(R.drawable.baseline_notifications_off_24);
        }

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            item.setChecked(isChecked);
            if (isChecked) {
                setTextStyle(holder, Color.GRAY, Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                setTextStyle(holder, Color.BLACK, 0);
            }
        });

        holder.itemView.setOnClickListener(v -> holder.checkBox.setChecked(!holder.checkBox.isChecked()));

        holder.itemView.setOnLongClickListener(v -> {
            Intent intent = new Intent(context, SetSchedul.class);
            intent.putExtra("item", item);
            intent.putExtra("position", position);
            ((SchedulList) context).startActivityForResult(intent, SchedulList.REQUEST_CODE_EDIT_ITEM);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private void setTextStyle(SchedulerItemViewHolder holder, int color, int flags) {
        holder.titleTextView.setTextColor(color);
        holder.titleTextView.setPaintFlags(flags);
        holder.timeTextView.setTextColor(color);
        holder.timeTextView.setPaintFlags(flags);
        holder.tagTextItem.setTextColor(color);
        holder.tagTextItem.setPaintFlags(flags);
    }

    public static class SchedulerItemViewHolder extends RecyclerView.ViewHolder {
        TextView emojiTitleText, titleTextView, timeTextView, tagTextItem;
        CheckBox checkBox;
        ImageView alarmImageView;

        public SchedulerItemViewHolder(@NonNull View itemView) {
            super(itemView);
            emojiTitleText = itemView.findViewById(R.id.emojiTitleText);
            titleTextView = itemView.findViewById(R.id.title);
            timeTextView = itemView.findViewById(R.id.time);
            tagTextItem = itemView.findViewById(R.id.tagTextItem);
            checkBox = itemView.findViewById(R.id.checkBox);
            alarmImageView = itemView.findViewById(R.id.alaram_img);
        }
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return true;
        }
    }
}