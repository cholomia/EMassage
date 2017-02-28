package com.capstone.tip.emassage.ui.lessons.detail;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.capstone.tip.emassage.R;
import com.capstone.tip.emassage.databinding.FragmentLessonSummaryBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LessonSummaryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LessonSummaryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String objective;
    private String summary;

    private FragmentLessonSummaryBinding binding;
    private LessonDetailView lessonDetailView;

    public LessonSummaryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param objective Parameter 1.
     * @param summary   Parameter 2.
     * @return A new instance of fragment LessonSummaryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LessonSummaryFragment newInstance(String objective, String summary) {
        LessonSummaryFragment fragment = new LessonSummaryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, objective);
        args.putString(ARG_PARAM2, summary);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            objective = getArguments().getString(ARG_PARAM1);
            summary = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_lesson_summary, container, false);
        binding.setObjective(objective);
        binding.setSummary(summary);
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
