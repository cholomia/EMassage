package com.capstone.tip.emassage.model.pojo;

import java.text.DecimalFormat;

/**
 * Created by Cholo Mia on 2/5/2017.
 */

public class FakeGrade {

    private String title;
    private double prelim;
    private double midterm;
    private double finals;

    public FakeGrade(String title, double prelim, double midterm, double finals) {
        this.title = title;
        this.prelim = prelim;
        this.midterm = midterm;
        this.finals = finals;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrelim() {
        return prelim;
    }

    public void setPrelim(double prelim) {
        this.prelim = prelim;
    }

    public double getMidterm() {
        return midterm;
    }

    public void setMidterm(double midterm) {
        this.midterm = midterm;
    }

    public double getFinals() {
        return finals;
    }

    public void setFinals(double finals) {
        this.finals = finals;
    }

    public String getDisplayGrade(double grade) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        return decimalFormat.format(grade) + "%";
    }
}
