package com.example.scheduler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class EventItemAdapter extends BaseAdapter {

    private Context context;
    private List<EventItem> eventItems;

    public EventItemAdapter(Context context, List<EventItem> eventItems) {
        this.context = context;
        this.eventItems = eventItems;
    }

    @Override
    public int getCount() {
        return Math.min(eventItems.size(), 3);
    }

    @Override
    public Object getItem(int position) {
        return eventItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_event_item, parent, false);
        }

        TextView tagColor = convertView.findViewById(R.id.tagColor);
        TextView eventTitle = convertView.findViewById(R.id.eventTitle);
        TextView eventTime = convertView.findViewById(R.id.eventTime);

        EventItem eventItem = eventItems.get(position);

        tagColor.setBackgroundColor(eventItem.getTagColor(context));
        eventTitle.setText(eventItem.getTitle());
        eventTime.setText(eventItem.getTime());

        return convertView;
    }

}
