package edu.sub.subvotingsystem;

public class ReadWriteUserDetails {
    public String doB, gender, mobile;

    //Empty Constructor
    public ReadWriteUserDetails(){};

    public ReadWriteUserDetails(String textDoB, String textGender, String textMobile) {
        this.doB = textDoB;
        this.gender = textGender;
        this.mobile = textMobile;
    }
}
