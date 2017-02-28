package com.capstone.tip.emassage.ui.lessons.detail;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.capstone.tip.emassage.R;
import com.capstone.tip.emassage.databinding.FragmentLessonBodyBinding;


public class LessonBodyFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String title;
    private String body;
    private LessonDetailView lessonDetailView;

    public LessonBodyFragment() {
        // Required empty public constructor
    }

    public static LessonBodyFragment newInstance(String title, String body) {
        LessonBodyFragment fragment = new LessonBodyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, title);
        args.putString(ARG_PARAM2, body);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(ARG_PARAM1);
            body = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentLessonBodyBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_lesson_body, container, false);
        binding.setTitle(title);
        binding.setBody(body);
        binding.setView(lessonDetailView);
        return binding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            lessonDetailView = (LessonDetailView) context;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDetach() {
        lessonDetailView = null;
        super.onDetach();
    }

}
