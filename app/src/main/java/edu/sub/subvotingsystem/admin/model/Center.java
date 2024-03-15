package edu.sub.subvotingsystem.admin.model;

public class Center {
    private String center_id;
    private String center_name;

    public Center(String center_id, String center_name) {
        this.center_id = center_id;
        this.center_name = center_name;
    }

    public String getCenter_id() {
        return center_id;
    }

    public void setCenter_id(String center_id) {
        this.center_id = center_id;
    }

    public String getCenter_name() {
        return center_name;
    }

    public void setCenter_name(String center_name) {
        this.center_name = center_name;
    }
}
