package com.example.michal.rentmate.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.michal.rentmate.R;
import com.example.michal.rentmate.model.pojo.Apartment;
import com.example.michal.rentmate.model.pojo.Claim;
import com.example.michal.rentmate.model.pojo.TokenRequest;
import com.example.michal.rentmate.model.pojo.TokenResponce;
import com.example.michal.rentmate.model.pojo.User;
import com.example.michal.rentmate.model.repositories.ApartmentRepository;
import com.example.michal.rentmate.model.repositories.ClaimRepository;
import com.example.michal.rentmate.model.repositories.UserRepository;
import com.example.michal.rentmate.networking.RentMateApi;
import com.example.michal.rentmate.networking.RestService;
import com.example.michal.rentmate.ui.activity.LogInActivity;
import com.example.michal.rentmate.ui.activity.RentMateActivity;
import com.example.michal.rentmate.util.Constants;
import com.example.michal.rentmate.util.Helper;
import com.example.michal.rentmate.util.ValidUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogInFragment extends Fragment {

  @Bind(R.id.log_in_button) Button logInButton;
  @Bind(R.id.user_email_edit_text) EditText emailEditText;
  @Bind(R.id.password_edit_text) EditText passwordEditText;
  @Bind(R.id.email_text_input_layout) TextInputLayout emailInputLayout;
  @Bind(R.id.pass_text_input_layout) TextInputLayout passInputLayout;

  private String token;
  private UserRepository userRepo;
  private ClaimRepository claimRepo;
  private RentMateApi service;
  private boolean isSuccess;

  public static LogInFragment newInstance() {
    return new LogInFragment();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    userRepo = UserRepository.getInstance();
    claimRepo = ClaimRepository.getInstance();
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.frag_login, container, false);
    ButterKnife.bind(this, view);
    return view;
  }

  //  Listener
  @OnClick(R.id.log_in_button)
  public void onLogInPressed() {
    if (isInputValid()) {
      TokenRequest request = createTokenRequest();
      getToken(request);
    } else {
      Toast.makeText(getContext(), "Insert email and pass", Toast.LENGTH_SHORT).show();
    }
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

  public TokenRequest createTokenRequest() {
    TokenRequest request = new TokenRequest();
    request.setEmail(String.valueOf(emailEditText.getText()));
    request.setPassword(String.valueOf(passwordEditText.getText()));
    return request;
  }

  public void getToken(TokenRequest request) {
    service = RestService.getInstance();
    Call<TokenResponce> callToken = service.getToken(request);
    callToken.enqueue(new Callback<TokenResponce>() {
      @Override
      public void onResponse(Call<TokenResponce> call, Response<TokenResponce> response) {
        if (response.isSuccessful()) {
          isSuccess = response.isSuccessful();
          Log.e(Constants.TAG_TOKEN, response.body().getToken());
          token = response.body().getToken();
          userRepo.getUser().setToken(token);
        } else {
          Toast.makeText(getContext(), "Wrong password or email", Toast.LENGTH_SHORT).show();
        }
        if (isSuccess) {
          getUser();
        } else {
          Log.e(Constants.TAG_TOKEN, "WRONG PASSWORD");
        }
      }

      @Override
      public void onFailure(Call<TokenResponce> call, Throwable t) {
        Log.e(Constants.TAG_ON_FAILURE, "TOKEN IS NOT RECEIVED");
      }
    });
  }

  public void getUser() {
    Call<User> callUser = service.getUser(Helper.getHeader(userRepo.getUser()));
    callUser.enqueue(new Callback<User>() {
      @Override
      public void onResponse(Call<User> call, Response<User> response) {
        if (response.isSuccessful()) {
          Log.e(Constants.TAG_USER, response.body().getEmail());
          userRepo.setUser(response.body());
          userRepo.getUser().setToken(token);
          setUserClaims(userRepo.getUser());
          setUserApt(response.body().getApartments());
          setUserClaims(response.body().getUserClaims());
          Intent intent = RentMateActivity.newIntent(getActivity());
          startActivity(intent);
          getActivity().finish();
        }
      }

      @Override
      public void onFailure(Call<User> call, Throwable t) {
        Log.e(Constants.TAG_ON_FAILURE, t.getMessage());
        Log.e(Constants.TAG_ON_FAILURE, "USER IS NOT RECEIVED");
      }
    });
  }

  public void setUserClaims(List<Claim> claimList) {
    ClaimRepository claimRepo = ClaimRepository.getInstance();
    claimRepo.setClaimList(claimList);
  }

  public void setUserApt(List<Apartment> aptList) {
    ApartmentRepository aptRepo = ApartmentRepository.getInstance();
    aptRepo.setApartmentList(aptList);
  }

  public void setUserClaims(User user) {
    List<Claim> userClaims = user.getUserClaims();
    for (int i = 0; i < user.getApartments().size(); i++) {
      for (int j = 0; j < user.getApartments().get(i).getClaims().size(); j++) {
        userClaims.add(user.getApartments().get(i).getClaims().get(j));
      }
    }
    claimRepo.setClaimList(userClaims);
  }
}
