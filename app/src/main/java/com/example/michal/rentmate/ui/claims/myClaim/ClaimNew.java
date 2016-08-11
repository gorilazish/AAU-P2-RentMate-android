package com.example.michal.rentmate.ui.claims.myClaim;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.michal.rentmate.R;
import com.example.michal.rentmate.model.pojo.Apartment;
import com.example.michal.rentmate.model.pojo.Claim;
import com.example.michal.rentmate.model.pojo.User;
import com.example.michal.rentmate.model.repositories.ApartmentRepository;
import com.example.michal.rentmate.model.repositories.UserRepository;
import com.example.michal.rentmate.networking.RentMateApi;
import com.example.michal.rentmate.networking.RestService;
import com.example.michal.rentmate.util.Constants;
import com.example.michal.rentmate.util.Helper;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClaimNew extends Fragment {

  @Bind(R.id.apartments_spinner) Spinner aptSpinner;
  @Bind(R.id.claim_title_edit_text) EditText titleEditText;
  @Bind(R.id.claim_detail_description) EditText descriptionEditText;
  @Bind(R.id.create_claim_button) Button saveButton;

  private RentMateApi service;
  private User user;
  private ApartmentRepository aptRepo;

  public static ClaimNew newInstance() {
    return new ClaimNew();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    user = UserRepository.getInstance().getUser();
    aptRepo = ApartmentRepository.getInstance();
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.frag_new_claim, container, false);
    ButterKnife.bind(this, view);
    setAptSpinner();
    return view;
  }

  //  Listeners
  @OnClick(R.id.create_claim_button)
  public void onClaimSaved() {
    Claim claim = setClaimProp();
    saveClaim(Helper.getHeader(user), claim);
  }

  private void saveClaim(String header, Claim claim) {

    service = RestService.getInstance();
    Call<Claim> call = service.createClaim(header, claim);
    call.enqueue(new Callback<Claim>() {
      @Override
      public void onResponse(Call<Claim> call, Response<Claim> response) {
        if (response.isSuccessful()) {
          Log.e(Constants.TAG_ON_CREATED, String.valueOf(response));
          sendResult(Activity.RESULT_OK, true);
        } else {
          Log.e(Constants.TAG_ON_CREATED, "CLAIM IS NOT CREATED");
        }
      }

      @Override
      public void onFailure(Call<Claim> call, Throwable t) {

      }
    });
  }

  private Claim setClaimProp() {
    final Claim claim = new Claim();
    String title = String.valueOf(titleEditText.getText());
    String description = String.valueOf(descriptionEditText.getText());
    Apartment apt = (Apartment) aptSpinner.getSelectedItem();
    claim.setRelatedApt(apt.getApartmentId());
    claim.setTitle(title);
    claim.setDescription(description);
    return claim;
  }

  private void sendResult(int resultCode, Boolean isClaimAdded) {
    if (getTargetFragment() == null) {
      return;
    }
    Intent intent = new Intent();
    intent.putExtra(Constants.EXTRA_NEW_CLAIM, isClaimAdded);
    getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
  }

  private void setAptSpinner() {
    List<Apartment> aptList = aptRepo.getApartmentList();
    ArrayAdapter<Apartment> dataAdapter = new ArrayAdapter<Apartment>(getContext(), android.R.layout.simple_spinner_item, aptList);
    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    aptSpinner.setAdapter(dataAdapter);
  }
}
