package com.example.demo.model;

/**
 * TODO
 *
 * @author zhou.xy
 * @since 1.0.0
 */
public class MobileDO {
    private int mts;
    private String province;
    private String catName;
    private String telString;
    private int areaVid;
    private int ispVid;
    private String carrier;

    public int getMts() {
        return mts;
    }

    public void setMts(int mts) {
        this.mts = mts;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getTelString() {
        return telString;
    }

    public void setTelString(String telString) {
        this.telString = telString;
    }

    public int getAreaVid() {
        return areaVid;
    }

    public void setAreaVid(int areaVid) {
        this.areaVid = areaVid;
    }

    public int getIspVid() {
        return ispVid;
    }

    public void setIspVid(int ispVid) {
        this.ispVid = ispVid;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }
}
