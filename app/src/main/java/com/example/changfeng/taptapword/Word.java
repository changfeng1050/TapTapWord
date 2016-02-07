package com.example.changfeng.taptapword;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

/**
 * Created by changfeng on 2015/4/19.
 */
public class Word {
    private UUID mUuid;
    private long mId;
    private String mLanguage;
    private String mName;
    private String mEnPhone;
    private String mAmPhone;
    private String mMeans;
    private boolean mArchived = false;

    private int mYear;
    private int mMonth;
    private int mDate;
    private int mHour;
    private int mMinute;
    private int mSecond;

    private static final String JSON_UUID = "uuid";
    private static final String JSONG_LANGUAGE = "language";
    private static final String JSON_NAME = "name";
    private static final String JSON_EN_PHONE = "en_phone";
    private static final String JSON_AM_PHONE = "am_phone";
    private static final String JSON_MEANS = "means";
    private static final String JSON_ARCHIVED = "archived";
    private static final String JSON_YEAR = "year";
    private static final String JSON_MONTH = "month";
    private static final String JSON_DATE = "date";
    private static final String JSON_HOUR = "hour";
    private static final String JSON_MINUTE = "minute";
    private static final String JSON_SECOND = "second";

    public Word(JSONObject jsonObject) throws JSONException {
        mUuid = UUID.fromString(jsonObject.getString(JSON_UUID));
        mName = jsonObject.getString(JSON_NAME);
        if (jsonObject.has(JSON_EN_PHONE)) {
            mEnPhone = jsonObject.getString(JSON_EN_PHONE);
        }
        if (jsonObject.has(JSON_AM_PHONE)) {
            mAmPhone = jsonObject.getString(JSON_AM_PHONE);
        }

        if (jsonObject.has(JSON_MEANS)) {
            mMeans = jsonObject.getString(JSON_MEANS);
        }

        if (jsonObject.has(JSON_ARCHIVED)) {
            mArchived = jsonObject.getBoolean(JSON_ARCHIVED);
        }

    }

    public JSONObject toJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(JSON_UUID, mUuid.toString());
        jsonObject.put(JSON_NAME, mName);
        jsonObject.put(JSON_EN_PHONE, mEnPhone);
        jsonObject.put(JSON_AM_PHONE, mAmPhone);
        jsonObject.put(JSON_MEANS, mMeans);
        jsonObject.put(JSON_ARCHIVED, mArchived);
        return jsonObject;
    }

    public UUID getUUID() {
        return mUuid;
    }

    public void setId(long id) {
        mId = id;
    }

    public long getId() {
        return mId;
    }


    public void setLanguage(String language) {
        mLanguage = language;
    }

    public String getLanguage() {
        return mLanguage;
    }

    /**
     * Getter for property 'mName'.
     *
     * @return Value for property 'mName'.
     */
    public String getName() {
        return mName;
    }

    /**
     * Setter for property 'mName'.
     *
     * @param name Value to set for property 'mName'.
     */
    public void setName(String name) {
        this.mName = name;
    }

    /**
     * Getter for property 'mEnPhone'.
     *
     * @return Value for property 'mEnPhone'.
     */
    public String getEnPhone() {
        return mEnPhone;
    }

    /**
     * Setter for property 'mEnPhone'.
     *
     * @param enPhone Value to set for property 'mEnPhone'.
     */
    public void setEnPhone(String enPhone) {
        this.mEnPhone = enPhone;
    }

    /**
     * Getter for property 'mAmPhone'.
     *
     * @return Value for property 'mAmPhone'.
     */
    public String getAmPhone() {
        return mAmPhone;
    }

    /**
     * Setter for property 'mAmPhone'.
     *
     * @param amPhone Value to set for property 'mAmPhone'.
     */
    public void setAmPhone(String amPhone) {
        this.mAmPhone = amPhone;
    }

    public String getFormatPhones() {

        return "美:[" + getEnPhone() + "] " + "英:[" + getEnPhone() + "]";
    }

    public String getFormatEnPhone() {
        return "英:[" + getEnPhone() + "] ";
    }

    public String getFormatAmPhone() {
        return "美:[" + getEnPhone() + "] ";
    }

    public void setMeans(String means) {
        this.mMeans = means;
    }

    public String getMeans() {
        return mMeans;
    }

    public void setArchived(boolean isArchived) {
        this.mArchived = isArchived;
    }

    public boolean isArchived() {
        return mArchived;
    }

    public void setYear(int year) {
        mYear = year;
    }

    public int getYear() {
        return mYear;
    }

    public void setMonth(int month) {
        mMonth = month;
    }

    public int getMonth() {
        return mMonth;
    }

    public void setDate(int date) {
        mDate = date;
    }

    public int getDate() {
        return mDate;
    }

    public void setHour(int hour) {
        mHour = hour;
    }

    public int getHour() {
        return mHour;
    }

    public void setMinute(int minute) {
        mMinute = minute;
    }

    public int getMinute() {
        return mMinute;
    }

    public void setSecond(int second) {
        mSecond =second;
    }

    public int getSecond() {
        return mSecond;
    }

    public Word() {
        mUuid = UUID.randomUUID();
    }

    public boolean hasEnPhone() {
       return mEnPhone != null;
    }

    public boolean hasAmPhone() {
        return mAmPhone != null;
    }

    public boolean hasMeans() {
        return mMeans != null;
    }

}
