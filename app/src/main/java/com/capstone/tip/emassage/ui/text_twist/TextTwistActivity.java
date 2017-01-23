package com.capstone.tip.emassage.ui.text_twist;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.capstone.tip.emassage.R;
import com.capstone.tip.emassage.databinding.ActivityTextTwistBinding;
import com.capstone.tip.emassage.model.data.Twist;
import com.capstone.tip.emassage.model.pojo.Letter;
import com.hannesdorfmann.mosby.mvp.MvpActivity;

import java.util.List;

public class TextTwistActivity extends MvpActivity<TextTwistView, TextTwistPresenter> implements TextTwistView {

    private ActivityTextTwistBinding binding;
    private ProgressDialog progressDialog;
    private Twist twist;
    private LetterListAdapter answerAdapter;
    private LetterListAdapter choiceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_text_twist);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.setView(getMvpView());

        answerAdapter = new LetterListAdapter(getMvpView(), false);
        choiceAdapter = new LetterListAdapter(getMvpView(), true);

        binding.recyclerViewAnswer.setAdapter(answerAdapter);
        binding.recyclerViewAnswer.setLayoutManager(new GridLayoutManager(this, 5));
        binding.recyclerViewAnswer.setItemAnimator(new DefaultItemAnimator());

        binding.recyclerViewChoices.setAdapter(choiceAdapter);
        binding.recyclerViewChoices.setLayoutManager(new GridLayoutManager(this, 5));
        binding.recyclerViewChoices.setItemAnimator(new DefaultItemAnimator());

        presenter.onStart();
    }

    @Override
    protected void onDestroy() {
        presenter.onStop();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_grades, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_refresh:
                presenter.refresh();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @NonNull
    @Override
    public TextTwistPresenter createPresenter() {
        return new TextTwistPresenter();
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

    @Override
    public void initData(int size) {
        binding.setEmpty(size <= 0);
    }

    @Override
    public void onRefresh() {
        presenter.refresh();
    }

    @Override
    public void onFinish() {
        new AlertDialog.Builder(this)
                .setTitle("Congratulations!")
                .setMessage("You have finish the mini game!!!")
                .setCancelable(false)
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        TextTwistActivity.this.finish();
                    }
                })
                .setNegativeButton("Reload", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        presenter.refresh();
                    }
                })
                .show();
    }

    @Override
    public void setTwist(Twist twist) {
        this.twist = twist;
        answerAdapter.clear();
        binding.txtClue.setText(twist.getClue());
        answerAdapter.setLetters(presenter.getLetterList(twist.getWord(), false));
        choiceAdapter.setLetters(presenter.getLetterList(twist.getWord(), true));
    }

    @Override
    public void onTwist() {
        choiceAdapter.shuffle();
    }

    @Override
    public void onEnter() {
        if (answerAdapter.getAnswer().equalsIgnoreCase(twist.getWord())) {
            presenter.twistDone(twist);
        } else {
            Toast.makeText(this, "Incorrect Answer!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLetterClicked(Letter letter, int position, boolean choice) {
        String strLetter = letter.getLetter();
        if (choice) {
            int emptyIndex = answerAdapter.getEmptyIndex();
            if (emptyIndex != -1) {
                choiceAdapter.removeLetter(position);
                answerAdapter.addLetter(strLetter, emptyIndex);
            }
        } else {
            int emptyIndex = choiceAdapter.getEmptyIndex();
            if (emptyIndex != -1) {
                answerAdapter.removeLetter(position);
                choiceAdapter.addLetter(strLetter, emptyIndex);
            }
        }
    }
}
