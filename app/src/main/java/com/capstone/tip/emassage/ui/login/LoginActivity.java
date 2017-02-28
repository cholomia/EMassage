package com.capstone.tip.emassage.ui.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import com.capstone.tip.emassage.R;
import com.capstone.tip.emassage.databinding.ActivityLoginBinding;
import com.capstone.tip.emassage.model.data.User;
import com.capstone.tip.emassage.ui.main.MenuActivity;
import com.capstone.tip.emassage.ui.register.RegisterActivity;
import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateActivity;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;

import io.realm.Realm;

public class LoginActivity extends MvpViewStateActivity<LoginView, LoginPresenter>
        implements LoginView, TextWatcher {

    private ActivityLoginBinding binding;
    private ProgressDialog progressDialog;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        realm = Realm.getDefaultInstance();
        User user = realm.where(User.class).findFirst();
        if (user != null) onLoginSuccess();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.setView(getMvpView());
        binding.etUsername.addTextChangedListener(this);
        binding.etPassword.addTextChangedListener(this);
    }

    @Override
    protected void onDestroy() {
        realm.close();
        super.onDestroy();
    }

    /***
     * Start of MvpViewStateActivity
     ***/

    @NonNull
    @Override
    public LoginPresenter createPresenter() {
        return new LoginPresenter();
    }

    @NonNull
    @Override
    public ViewState<LoginView> createViewState() {
        setRetainInstance(true);
        return new LoginViewState();
    }

    @Override
    public void onNewViewStateInstance() {
        saveValues();
    }

    /***
     * End of MvpViewStateActivity
     ***/

    /***
     * Start of LoginView
     ***/

    @Override
    public void onLoginButtonClicked() {
        presenter.login(
                binding.etUsername.getText().toString(),
                binding.etPassword.getText().toString()
        );
    }

    @Override
    public void onRegisterButtonClicked() {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    @Override
    public void showAlert(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setEditTextValue(String username, String password) {
        binding.etUsername.setText(username);
        binding.etPassword.setText(password);
    }

    @Override
    public void startLoading() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Logging in...");
        }
        progressDialog.show();
    }

    @Override
    public void stopLoading() {
        if (progressDialog != null) progressDialog.dismiss();
    }

    @Override
    public void onLoginSuccess() {
        startActivity(new Intent(this, MenuActivity.class));
        finish();
    }

    /***
     * End of LoginView
     ***/

    /***
     * Start of TextWatcher
     ***/

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        saveValues();
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    /***
     * End of TextWatcher
     ***/

    private void saveValues() {
        LoginViewState loginViewState = (LoginViewState) getViewState();
        loginViewState.setUsername(binding.etUsername.getText().toString());
        loginViewState.setPassword(binding.etPassword.getText().toString());
    }

}
