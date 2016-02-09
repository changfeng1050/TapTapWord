package com.example.changfeng.taptapword;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

/**
 * Created by changfeng on 2015/4/19.
 */
public class Word {
    private UUID uuid;
    private long id;
    private String language;
    private String name;
    private String enPhone;
    private String amPhone;
    private String means;
    private boolean archived = false;
    private String note;
    private String webExplains;

    private int year;
    private int month;
    private int date;
    private int hour;
    private int minute;
    private int second;


    public UUID getUUID() {
        return uuid;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }


    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLanguage() {
        return language;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnPhone() {
        return enPhone;
    }

    public void setEnPhone(String enPhone) {
        this.enPhone = enPhone;
    }

    public String getAmPhone() {
        return amPhone;
    }

    public void setAmPhone(String amPhone) {
        this.amPhone = amPhone;
    }

    public String getFormatPhones() {
        StringBuilder phone = new StringBuilder();
        if (amPhone != null && !amPhone.isEmpty()) {
            phone.append("美:[" + amPhone + "] ");
        }
        if (enPhone != null && !enPhone.isEmpty()) {
            phone.append("英:[" + enPhone + "]");
        }
        return phone.toString();
    }

    public String getFormatEnPhone() {
        if (enPhone != null && !enPhone.isEmpty()) {
            return "英:[" + enPhone + "] ";
        }
        return "";
    }

    public String getFormatAmPhone() {
        if (amPhone != null && !amPhone.isEmpty()) {
            return "美:[" + getEnPhone() + "] ";
        }
        return "";
    }

    public void setMeans(String means) {
        this.means = means;
    }

    public String getMeans() {
        return means;
    }

    public void setArchived(boolean isArchived) {
        this.archived = isArchived;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getNote() {
        return note;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getYear() {
        return year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getMonth() {
        return month;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getDate() {
        return date;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getHour() {
        return hour;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getMinute() {
        return minute;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public int getSecond() {
        return second;
    }

    public void setWebExplains(String webExplains) {
        this.webExplains = webExplains;
    }

    public String getWebExplains() {
        return webExplains;
    }

    public Word() {
        uuid = UUID.randomUUID();
    }
}
