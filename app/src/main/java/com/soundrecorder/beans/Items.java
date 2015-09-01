package com.soundrecorder.beans;

/**
 * Created by BLUEHORSE 123 on 8/28/2015.
 */
public class Items {
    private String record_name,duration;
    private int size;
    private int index;

    public Items()
    {

    }
    public Items(String record_name, String duration, int size) {
        this.record_name = record_name;
        this.duration = duration;
        this.size = size;
    }

    public Items(String record_name, String duration, int size, int index) {
        this.record_name = record_name;
        this.duration = duration;
        this.size = size;
        this.index = index;
    }


    public String getRecord_name() {
        return record_name;
    }

    public void setRecord_name(String record_name) {
        this.record_name = record_name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
