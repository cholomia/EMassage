package com.capstone.tip.emassage.model.data;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Cholo Mia on 12/4/2016.
 */

public class Course extends RealmObject {

    @PrimaryKey
    private int id;
    private String title;
    private String description;
    private String objective;
    private int sequence;
    private String coverImage;
    private RealmList<Category> categories;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getObjective() {
        return objective;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public RealmList<Category> getCategories() {
        return categories;
    }

    public void setCategories(RealmList<Category> categories) {
        this.categories = categories;
    }
}
