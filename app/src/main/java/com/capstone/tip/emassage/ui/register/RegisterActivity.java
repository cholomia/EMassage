package com.capstone.tip.emassage.ui.register;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.Toast;

import com.capstone.tip.emassage.R;
import com.capstone.tip.emassage.databinding.ActivityRegisterBinding;
import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateActivity;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;

public class RegisterActivity extends MvpViewStateActivity<RegisterView, RegisterPresenter>
        implements RegisterView, TextWatcher {

    private ActivityRegisterBinding binding;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        binding.setView(getMvpView());
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.etUsername.addTextChangedListener(this);
        binding.etEmail.addTextChangedListener(this);
        binding.etFirstName.addTextChangedListener(this);
        binding.etLastName.addTextChangedListener(this);
        binding.etPassword.addTextChangedListener(this);
        binding.etRepeatPassword.addTextChangedListener(this);
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

    /***
     * Start of MvpViewStateActivity
     ***/

    @NonNull
    @Override
    public RegisterPresenter createPresenter() {
        return new RegisterPresenter();
    }

    @NonNull
    @Override
    public ViewState<RegisterView> createViewState() {
        setRetainInstance(true);
        return new RegisterViewState();
    }

    @Override
    public void onNewViewStateInstance() {
        initializeViewStateValues();

    }


    /***
     * End of MvpViewStateActivity
     ***/

    /***
     * Start of RegisterView
     ***/

    @Override
    public void onRegisterButtonClicked() {
        presenter.register(
                binding.etUsername.getText().toString(),
                binding.etEmail.getText().toString(),
                binding.etFirstName.getText().toString(),
                binding.etLastName.getText().toString(),
                binding.etPassword.getText().toString(),
                binding.etRepeatPassword.getText().toString()
        );
    }

    @Override
    public void setEditTextValue(String username, String firstName, String lastName, String password, String repeatPassword) {
        binding.etUsername.setText(username);
        binding.etFirstName.setText(firstName);
        binding.etLastName.setText(lastName);
        binding.etPassword.setText(password);
        binding.etRepeatPassword.setText(repeatPassword);
    }

    @Override
    public void showAlert(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void startLoading() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(RegisterActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Signing up...");
        }
        progressDialog.show();
    }

    @Override
    public void stopLoading() {
        if (progressDialog != null) progressDialog.dismiss();
    }

    @Override
    public void onRegisterSuccessful() {
        new AlertDialog.Builder(this)
                .setTitle("Register Successful")
                .setMessage("Link for account validation hasve been send to your email address")
                .setCancelable(false)
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        RegisterActivity.this.finish();
                    }
                })
                .show();
    }

    /***
     * End of RegisterView
     ***/

    /***
     * Start of TextWatcher
     ***/

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        initializeViewStateValues();
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    /***
     * End of TextWatcher
     ***/

    private void initializeViewStateValues() {
        RegisterViewState registerViewState = (RegisterViewState) getViewState();
        registerViewState.setUsername(binding.etUsername.getText().toString());
        registerViewState.setFirstName(binding.etFirstName.getText().toString());
        registerViewState.setLastName(binding.etLastName.getText().toString());
        registerViewState.setPassword(binding.etPassword.getText().toString());
        registerViewState.setRepeatPassword(binding.etRepeatPassword.getText().toString());
    }

}
