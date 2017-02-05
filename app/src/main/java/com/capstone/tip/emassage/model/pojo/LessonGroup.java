package com.capstone.tip.emassage.model.pojo;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

/**
 * Created by Cholo Mia on 2/5/2017.
 */

public class LessonGroup extends ExpandableGroup<LessonParcelable> {

    public LessonGroup(String title, List<LessonParcelable> items) {
        super(title, items);
    }

}
