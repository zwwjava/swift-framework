package org.swift.chapter.model;

/**
 * @Description -
 * @Author zww
 * @Date 2019/7/29 16:13
 */
public class Weather {

    private int wId;
    private String w_date;
    private String w_detail;
    private String w_temperature_low;
    private String w_temperature_high;

    public int getwId() {
        return wId;
    }

    public void setwId(int wId) {
        this.wId = wId;
    }

    public String getW_date() {
        return w_date;
    }

    public void setW_date(String w_date) {
        this.w_date = w_date;
    }

    public String getW_detail() {
        return w_detail;
    }

    public void setW_detail(String w_detail) {
        this.w_detail = w_detail;
    }

    public String getW_temperature_low() {
        return w_temperature_low;
    }

    public void setW_temperature_low(String w_temperature_low) {
        this.w_temperature_low = w_temperature_low;
    }

    public String getW_temperature_high() {
        return w_temperature_high;
    }

    public void setW_temperature_high(String w_temperature_high) {
        this.w_temperature_high = w_temperature_high;
    }
}
