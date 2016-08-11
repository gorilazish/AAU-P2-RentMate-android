package com.example.michal.rentmate.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.michal.rentmate.R;
import com.example.michal.rentmate.model.pojo.TokenResponce;
import com.example.michal.rentmate.model.pojo.User;
import com.example.michal.rentmate.model.repositories.UserRepository;
import com.example.michal.rentmate.networking.RentMateApi;
import com.example.michal.rentmate.networking.RestService;
import com.example.michal.rentmate.ui.activity.RentMateActivity;
import com.example.michal.rentmate.util.Constants;
import com.example.michal.rentmate.util.ValidUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpFragment extends Fragment {

  @Bind(R.id.new_user_email_edit_text) EditText emailEditText;
  @Bind(R.id.new_user_password_edit_text) EditText passwordEditText;
  @Bind(R.id.new_user_switch) Switch userSwitch;
  @Bind(R.id.sign_up_button) Button signUpButton;
  @Bind(R.id.email_text_input_layout) TextInputLayout emailInputLayout;
  @Bind(R.id.pass_text_input_layout) TextInputLayout passInputLayout;

  private String token;
  private UserRepository userRepo;
  private RentMateApi service;

  public static SignUpFragment newInstance() {
    return new SignUpFragment();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    userRepo = UserRepository.getInstance();
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.frag_sign_up, container, false);
    ButterKnife.bind(this, view);
    return view;
  }

  //  Listener
  @OnClick(R.id.sign_up_button)
  public void onSignUpressed() {
    String email = emailEditText.getText().toString();
    String pass = passwordEditText.getText().toString();

    if (isInputValid()) {
      signUp(email, pass);
    } else {
      Toast.makeText(getContext(), "Insert email and pass", Toast.LENGTH_SHORT).show();
    }
  }

  private void signUp(String email, String pass) {
    User user = setUserProperties(email, pass);
    service = RestService.getInstance();
    Call<TokenResponce> call = service.createUser(user);
    call.enqueue(new Callback<TokenResponce>() {
      @Override
      public void onResponse(Call<TokenResponce> call, Response<TokenResponce> response) {
        if (response.isSuccessful()) {
          token = response.body().getToken();
          userRepo.getUser().setToken(token);

          Intent intent = RentMateActivity.newIntent(getActivity());
          startActivity(intent);
          getActivity().finish();
        }
      }

      @Override
      public void onFailure(Call<TokenResponce> call, Throwable t) {
        Log.e(Constants.TAG_ON_FAILURE, "NEW USER IS NOT CREATED");
      }
    });
  }

  private boolean isInputValid() {
    boolean isValidated = true;

    String email = emailEditText.getText().toString();
    if (!ValidUtil.isValidEmail(email)) {
      emailInputLayout.setError(getString(R.string.validation_wrong_email));
      isValidated = false;
    } else {
      emailInputLayout.setErrorEnabled(false);
      isValidated = true;
    }
    String pass = passwordEditText.getText().toString();
    if (!ValidUtil.isValidPassword(pass)) {
      passInputLayout.setError(getString(R.string.validation_wrong_pass));
      isValidated = false;
    } else {
      passInputLayout.setErrorEnabled(false);
      isValidated = true;
    }
    return isValidated;
  }

  private User setUserProperties(String email, String pass) {
    User user = new User();
    if (userSwitch.isActivated()) {
      user.setGroupId(Constants.GROUP_ID_LANDLORD);
    } else {
      user.setGroupId(Constants.GROUP_ID_TENANT);
    }
    user.setEmail(email);
    user.setPassword(pass);
    return user;
  }
}
