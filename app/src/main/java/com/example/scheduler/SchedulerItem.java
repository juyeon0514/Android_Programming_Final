package com.example.scheduler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class SchedulerItem implements Serializable {
    private String emoji;
    private String title;
    private String time;
    private String tag;
    private String description;
    private boolean isChecked;
    private boolean isAlarmOn;

    public SchedulerItem(String emoji, String title, String time, String tag, String description, boolean isChecked, boolean isAlarmOn) {
        this.emoji = emoji;
        this.title = title;
        this.time = time;
        this.tag = tag;
        this.description = description;
        this.isChecked = false;
        this.isAlarmOn = isAlarmOn;
    }

    public boolean isAlarmOn() {
        return isAlarmOn;
    }

    public void setAlarmOn(boolean alarmOn) {
        isAlarmOn = alarmOn;
    }

    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("emoji", emoji);
        jsonObject.put("title", title);
        jsonObject.put("time", time);
        jsonObject.put("tag", tag);
        jsonObject.put("description", description);
        jsonObject.put("isChecked", isChecked);
        jsonObject.put("isAlarmOn", isAlarmOn);
        return jsonObject;
    }

    public static SchedulerItem fromJson(JSONObject jsonObject) throws JSONException {
        String emoji = jsonObject.getString("emoji");
        String title = jsonObject.getString("title");
        String time = jsonObject.getString("time");
        String tag = jsonObject.getString("tag");
        String description = jsonObject.getString("description");
        boolean isChecked = jsonObject.getBoolean("isChecked");
        boolean isAlarmOn = jsonObject.getBoolean("isAlarmOn");
        return new SchedulerItem(emoji, title, time, tag, description, isChecked, isAlarmOn);
    }
}