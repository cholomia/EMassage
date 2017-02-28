package com.capstone.tip.emassage.ui.courses;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.capstone.tip.emassage.R;
import com.capstone.tip.emassage.databinding.ItemCourseBinding;
import com.capstone.tip.emassage.model.data.Course;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cholo Mia on 12/4/2016.
 */

public class CoursesListAdapter extends RecyclerView.Adapter<CoursesListAdapter.ViewHolder> {

    private List<Course> courses;
    private CoursesView coursesView;

    public CoursesListAdapter(CoursesView coursesView) {
        this.coursesView = coursesView;
        courses = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemCourseBinding itemCourseBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.item_course, parent, false);
        return new ViewHolder(itemCourseBinding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemCourseBinding.setCourse(courses.get(position));
        holder.itemCourseBinding.setView(coursesView);
        /*Glide.with(holder.itemView.getContext())
                .load(Endpoints.BASE_URL + courses.get(position).getCoverImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(holder.itemCourseBinding.imageCourse);*/
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    public void setCourses(List<Course> courses) {
        this.courses.clear();
        this.courses.addAll(courses);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemCourseBinding itemCourseBinding;

        public ViewHolder(ItemCourseBinding itemCourseBinding) {
            super(itemCourseBinding.getRoot());
            this.itemCourseBinding = itemCourseBinding;
        }
    }
}
