package com.capstone.tip.emassage.ui.quiz;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;
import android.widget.Toast;

import com.capstone.tip.emassage.R;
import com.capstone.tip.emassage.app.Constants;
import com.capstone.tip.emassage.databinding.ActivityQuizBinding;
import com.capstone.tip.emassage.databinding.DialogQuizSummaryBinding;
import com.capstone.tip.emassage.model.data.Choice;
import com.capstone.tip.emassage.model.data.Grade;
import com.capstone.tip.emassage.model.data.Lesson;
import com.capstone.tip.emassage.model.data.Question;
import com.capstone.tip.emassage.model.data.User;
import com.capstone.tip.emassage.model.pojo.UserAnswer;
import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateActivity;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import okhttp3.Credentials;

public class QuizActivity extends MvpViewStateActivity<QuizView, QuizPresenter>
        implements QuizView {

    private ActivityQuizBinding binding;
    private ChoiceListAdapter adapter;

    private Realm realm;
    private Lesson lesson;
    private User user;

    private List<Question> questionList;
    private List<UserAnswer> userAnswerList;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        realm = Realm.getDefaultInstance();
        user = realm.where(User.class).findFirst();
        int id = getIntent().getIntExtra(Constants.ID, -1);
        if (id == -1) {
            Toast.makeText(getApplicationContext(), "No Intent Extra Found!", Toast.LENGTH_SHORT)
                    .show();
            finish();
        }

        lesson = realm.where(Lesson.class).equalTo(Constants.ID, id).findFirst();

        binding = DataBindingUtil.setContentView(this, R.layout.activity_quiz);
        binding.setView(getMvpView());

        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adapter = new ChoiceListAdapter();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
        binding.recyclerView.setAdapter(adapter);

        if (getSupportActionBar() != null) {
            String title = "Quiz: " + lesson.getTitle();
            getSupportActionBar().setTitle(title);
            String strNumItems = "Number of Items: " + lesson.getQuestions().size();
            getSupportActionBar().setSubtitle(strNumItems);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Exit?")
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("EXIT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        QuizActivity.this.finish();
                    }
                })
                .setNegativeButton("CANCEL", null)
                .show();
    }

    @Override
    protected void onDestroy() {
        lesson.removeChangeListeners();
        realm.close();
        super.onDestroy();
    }

    /***
     * Start of MvpViewStateActivity
     ***/

    @NonNull
    @Override
    public QuizPresenter createPresenter() {
        return new QuizPresenter();
    }

    @NonNull
    @Override
    public ViewState<QuizView> createViewState() {
        setRetainInstance(true);
        return new QuizViewState();
    }

    @Override
    public void onNewViewStateInstance() {
        // alert user that quiz already taken
        if (lesson.getQuestions().size() <= 0) {
            new AlertDialog.Builder(this)
                    .setTitle("No Questions")
                    .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            QuizActivity.this.finish();
                        }
                    })
                    .setCancelable(false)
                    .show();
        }

        Grade grade = realm.where(Grade.class).equalTo("lesson", lesson.getId()).findFirst();
        if (grade != null) {
            if (grade.getTryCount() >= 3) {
                // quiz can be taken 3 times maximum
                new AlertDialog.Builder(this)
                        .setTitle("Unable to take Quiz")
                        .setMessage("Quiz can only be taken 3 times!")
                        .setCancelable(false)
                        .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                QuizActivity.this.finish();
                            }
                        })
                        .show();
            } else {
                // already taken the quiz
                new AlertDialog.Builder(this)
                        .setTitle("Retake Quiz?")
                        .setMessage("If Submitted, it will overwrite previous grade!")
                        .setCancelable(false)
                        .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                QuizActivity.this.finish();
                            }
                        })
                        .show();
            }
        }


        ((QuizViewState) getViewState()).setCounter(0);

        userAnswerList = new ArrayList<>();
        questionList = presenter.getShuffledQuestionList(realm.copyFromRealm(lesson.getQuestions()));

        ((QuizViewState) getViewState()).setUserAnswerList(userAnswerList);
        ((QuizViewState) getViewState()).setQuestionList(questionList);

        if (questionList.size() > 0)
            onSetQuestion(questionList.get(((QuizViewState) getViewState()).getCounter()));
    }

    /***
     * End of MvpViewStateActivity
     ***/

    /***
     * Start of QuizView
     ***/

    @Override
    public void onPrevious() {
        if (((QuizViewState) getViewState()).getCounter() <= 0) {
            // TODO: 24/11/2016 disable previous button instead of alert here if counter is 0
            new AlertDialog.Builder(this)
                    .setTitle("Exit?")
                    .setMessage("Are you sure you want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("EXIT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            QuizActivity.this.finish();
                        }
                    })
                    .setNegativeButton("CANCEL", null)
                    .show();
        } else {
            setUserAnswer(false);

            ((QuizViewState) getViewState()).decrementCounter();
            onSetQuestion(questionList.get(((QuizViewState) getViewState()).getCounter()));
        }
    }

    @Override
    public void onNext() {
        String message = setUserAnswer(true);
        if (message != null) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            return;
        }

        if (((QuizViewState) getViewState()).getCounter() < questionList.size() - 1) {
            ((QuizViewState) getViewState()).incrementCounter();
            onSetQuestion(questionList.get(((QuizViewState) getViewState()).getCounter()));
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Submit?")
                    .setCancelable(false)
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            showSummary();
                            dialogInterface.dismiss();
                        }
                    })
                    .setNegativeButton("BACK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            userAnswerList.remove(((QuizViewState) getViewState()).getCounter());
                            ((QuizViewState) getViewState()).setUserAnswerList(userAnswerList);
                            QuizActivity.this.onSetQuestion(questionList.get(((QuizViewState) getViewState()).getCounter()));
                        }
                    })
                    .show();
        }
    }

    @Override
    public void restoreData(int counter, List<Question> questionList, List<UserAnswer> userAnswerList) {
        this.questionList = questionList;
        this.userAnswerList = userAnswerList;
        if (questionList.size() > 0)
            onSetQuestion(questionList.get(counter));
    }

    /***
     * End of QuizView
     ***/

    /**
     * @param question question to display
     */
    private void onSetQuestion(Question question) {
        int counter = ((QuizViewState) getViewState()).getCounter();
        binding.txtQuestion.setText((counter + 1) + ".) " + question.getBody());

        UserAnswer userAnswer = null;
        if (userAnswerList.size() > counter)
            userAnswer = userAnswerList.get(counter);


        adapter.setChoiceList(question.getChoices());
        if (userAnswer != null) adapter.setAnswer(userAnswer.getUserAnswer());
    }

    private String setUserAnswer(boolean hasReturn) {
        Question question = questionList.get(((QuizViewState) getViewState()).getCounter());
        UserAnswer userAnswer = new UserAnswer();
        userAnswer.setQuestionId(question.getId());
        userAnswer.setCorrectAnswer(question.getAnswer());

        Choice choice = adapter.getSelectedChoice();
        if (choice == null && hasReturn) {
            return "Select Answer";
        }
        userAnswer.setUserAnswer(choice != null ? choice.getBody() : "");
        userAnswer.setChoiceType(1);

        if (userAnswerList.size() > ((QuizViewState) getViewState()).getCounter())
            userAnswerList.set(((QuizViewState) getViewState()).getCounter(), userAnswer);
        else
            userAnswerList.add(userAnswer);
        ((QuizViewState) getViewState()).setUserAnswerList(userAnswerList);
        return null;
    }

    /**
     * Show Dialog Summary
     */
    private void showSummary() {

        DialogQuizSummaryBinding dialogBinding = DataBindingUtil.inflate(
                getLayoutInflater(),
                R.layout.dialog_quiz_summary,
                null,
                false);
        int score = 0;
        final int items = userAnswerList.size();
        for (UserAnswer userAnswer : userAnswerList) {
            if (userAnswer.isCorrect()) score++;
        }
        dialogBinding.txtRawScore.setText(score + "/" + items);
        String ave = presenter.getAverage(score, items) + "%";
        dialogBinding.txtAverage.setText(ave);

        dialogBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dialogBinding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        dialogBinding.recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        SummaryListAdapter summaryListAdapter = new SummaryListAdapter();
        summaryListAdapter.setUserAnswerList(userAnswerList);
        dialogBinding.recyclerView.setAdapter(summaryListAdapter);

        final int finalScore = score;
        realm.beginTransaction();
        Grade grade = realm.where(Grade.class).equalTo("lesson", lesson.getId()).findFirst();
        if (grade == null) {
            grade = new Grade();
            grade.setId("l-" + lesson.getId() + "-" + user.getUsername());
            grade.setLesson(lesson.getId());
            grade.setTryCount(0);
        }
        grade.setRawScore(finalScore);
        grade.setItemCount(items);
        grade.setSaved(false);
        int tryCount = grade.getTryCount();
        grade.setTryCount(tryCount + 1);
        grade = realm.copyToRealmOrUpdate(grade);
        realm.commitTransaction();

        presenter.saveGrade(realm.copyFromRealm(grade), Credentials.basic(user.getUsername(), user.getPassword()));

        new AlertDialog.Builder(this)
                .setTitle("Summary")
                .setView(dialogBinding.getRoot())
                .setCancelable(false)
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        QuizActivity.this.finish();
                    }
                })
                .show();
    }

    @Override
    public void startLoading() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
        }
        progressDialog.show();
    }

    @Override
    public void stopLoading() {
        if (progressDialog != null) progressDialog.dismiss();
    }

    @Override
    public void showAlert(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
