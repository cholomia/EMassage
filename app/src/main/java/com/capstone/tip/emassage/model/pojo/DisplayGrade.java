package com.capstone.tip.emassage.model.pojo;

import com.capstone.tip.emassage.model.data.Grade;

import java.text.DecimalFormat;
import java.util.List;

/**
 * @author pocholomia
 * @since 10/01/2017
 */

public class DisplayGrade {

    public static final int VIEW_COURSE = 1;
    public static final int VIEW_CATEGORY = 2;
    public static final int VIEW_LESSON = 3;

    private int sequence;
    private int view;
    private String title;
    private List<Grade> grades;

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public int getView() {
        return view;
    }

    public void setView(int view) {
        this.view = view;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Grade> getGrades() {
        return grades;
    }

    public void setGrades(List<Grade> grades) {
        this.grades = grades;
    }

    public String formattedAverage() {
        if (grades.size() > 0) {
            double ave = grades.get(grades.size() - 1).average();
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            return decimalFormat.format(ave) + "%";
        }
        return "N/A";
    }
}
