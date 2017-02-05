package com.capstone.tip.emassage.model.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Cholo Mia on 2/5/2017.
 */

public class LessonParcelable implements Parcelable {

    private int id;
    private String title;

    public LessonParcelable(int id, String title) {
        this.id = id;
        this.title = title;
    }

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


    public LessonParcelable(Parcel in) {
        id = in.readInt();
        title = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<LessonParcelable> CREATOR = new Parcelable.Creator<LessonParcelable>() {
        @Override
        public LessonParcelable createFromParcel(Parcel in) {
            return new LessonParcelable(in);
        }

        @Override
        public LessonParcelable[] newArray(int size) {
            return new LessonParcelable[size];
        }
    };
}
