package com.askia.coremodel.datamodel.http.parm;

public class NearStoreParm {

    /**
     * 1、可传分页参数；
     * 2、可按关键字（字段:keyStr）搜索；
     * 3、必传当前位置经度(longitude)，纬度(latitude)，
     * 地区信息(location);
     * 4、传id查询单个店面详情;5、非必传搜索范围(radius:单位米)
     */
    private int size = 10;
    private String longitude;
    private String latitude;
    private String location;
    private String current = "1";
    private String radius = "10000";

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public String getRadius() {
        return radius;
    }

    public void setRadius(String radius) {
        this.radius = radius;
    }
}
