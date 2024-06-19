package com.example.scheduler;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

public class EventItem {
    private String title;
    private String time;
    private String tagColor;

    public EventItem(String title, String time, String tag) {
        this.title = title;
        this.time = time;
        this.tagColor = tag;
    }

    public String getTitle() {
        return title;
    }

    public String getTime() {
        return time;
    }

    public String getTag() {
        return tagColor;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", title);
        jsonObject.put("time", time);
        jsonObject.put("tag", tagColor);
        return jsonObject;
    }

    public static EventItem fromJson(JSONObject jsonObject) throws JSONException {
        return new EventItem(
                jsonObject.getString("title"),
                jsonObject.getString("time"),
                jsonObject.getString("tag")
        );
    }


    public int getTagColor(Context context) {
        switch (tagColor.toLowerCase()) {
            case "운동":
                return context.getResources().getColor(R.color.red);
            case "공부":
                return context.getResources().getColor(R.color.blue);
            case "약속":
                return context.getResources().getColor(R.color.green);
            case "이벤트":
                return context.getResources().getColor(R.color.yellow);
            case "데이트":
                return context.getResources().getColor(R.color.pink);
            case "여행":
                return context.getResources().getColor(R.color.skyblue);
            default:
                return context.getResources().getColor(R.color.gray);
        }
    }
}