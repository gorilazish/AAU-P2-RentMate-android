package com.example.michal.rentmate.ui.apartment.newApartment;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.michal.rentmate.R;
import com.example.michal.rentmate.model.pojo.Apartment;
import com.example.michal.rentmate.model.repositories.UserRepository;
import com.example.michal.rentmate.networking.RentMateApi;
import com.example.michal.rentmate.networking.RestService;
import com.example.michal.rentmate.util.Constants;
import com.example.michal.rentmate.util.Helper;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyApptNew extends Fragment {

  @Bind(R.id.new_apt_check_location_button) Button checkLocationButton;
  @Bind(R.id.new_apt_save_button) Button saveButton;

  @Bind(R.id.new_apt_country_edit_text) EditText countryEditText;
  @Bind(R.id.new_apt_city_edit_text) EditText cityEditText;
  @Bind(R.id.new_apt_street_edit_text) EditText streetEditText;
  @Bind(R.id.new_apt_zip_edit_text) EditText zipEditText;

  private LatLng position;
  private RentMateApi service;
  private UserRepository userRepo;
  private boolean isApartmentAdded = true;

  public static MyApptNew newInstance() {
    return new MyApptNew();
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.frag_new_apartment, container, false);
    ButterKnife.bind(this, view);
    return view;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    userRepo = UserRepository.getInstance();
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (resultCode == Activity.RESULT_OK) {
      switch (requestCode) {
        case Constants.REQUEST_ADDRESS:
          boolean isAddressCorrect = (boolean) data.getSerializableExtra(Constants.EXTRA_ADDRESS);
          saveButton.setEnabled(isAddressCorrect);
      }
    }
  }

  //  Listeners
  @OnClick(R.id.new_apt_check_location_button)
  public void onCheckAptLocation() {
    if (getFullAddress().toCharArray().length > 10 && getAddress(getFullAddress()) != null) {
      getPosition(getAddress(getFullAddress()));
      openMapDialog();
    } else {
      Toast.makeText(getActivity(), "Fill correct address", Toast.LENGTH_LONG).show();
    }
  }

  @OnClick(R.id.new_apt_save_button)
  public void onApartmentSaved() {
    String header = Helper.getHeader(userRepo.getUser());
    Apartment apartment = setApartmantProp();
    saveApartment(header, apartment);
  }

  private void saveApartment(String header, Apartment apartment) {
    service = RestService.getInstance();
    Call<Apartment> call = service.createApartment(header, apartment);
    call.enqueue(new Callback<Apartment>() {
      @Override
      public void onResponse(Call<Apartment> call, Response<Apartment> response) {

        if (response.isSuccessful()) {
          userRepo.getUser().getApartments().add(response.body());
          Log.e(Constants.TAG_ON_CREATED, String.valueOf(response.isSuccessful()));
          sendResult(Activity.RESULT_OK, isApartmentAdded);
        } else {
          sendResult(Activity.RESULT_OK, !isApartmentAdded);
          Log.e(Constants.TAG_ON_CREATED, "APARTMENT IS NOT CREATED");
        }
      }

      @Override
      public void onFailure(Call<Apartment> call, Throwable t) {

      }
    });
    Toast.makeText(getActivity(), "HA you just saved apt", Toast.LENGTH_LONG).show();
  }

  private Apartment setApartmantProp() {
    Apartment apartment = new Apartment();
    String country = String.valueOf(countryEditText.getText());
    String street = String.valueOf(streetEditText.getText());
    String zip = String.valueOf(zipEditText.getText());
    String city = String.valueOf(cityEditText.getText());
//    TODO set the name of apartment
    apartment.setName("STATIC_NAME");
    apartment.setStreet(street);
    apartment.setZip(zip);
    apartment.setCity(city);
    apartment.setCountry(country);

    return apartment;
  }

  private Address getAddress(String longAddress) {
    Geocoder geocoder = new Geocoder(getActivity());
    Address address = null;

    String adrs = String.valueOf(longAddress);
    List<Address> addressList = null;
    try {
      addressList = geocoder.getFromLocationName(adrs, 1);
    } catch (IOException e) {
      e.printStackTrace();
      Toast.makeText(getActivity(), "Fill correct address", Toast.LENGTH_SHORT).show();
    }
    if (addressList != null && addressList.size() != 0) {
      address = addressList.get(0);
    } else {
      Toast.makeText(getActivity(), "We can not find your location", Toast.LENGTH_SHORT).show();
    }
    return address;
  }

  private String getFullAddress() {
    String longAddress;
    String country = String.valueOf(countryEditText.getText());
    if (TextUtils.isEmpty(country)) {
      countryEditText.setError("Country must be specified");
    }
    String city = String.valueOf(cityEditText.getText());
    if (TextUtils.isEmpty(city)) {
      cityEditText.setError("City must be specified");
    }
    String street = String.valueOf(streetEditText.getText());
    if (TextUtils.isEmpty(street)) {
      streetEditText.setError("Street must be specified");
    }
    String zipCode = String.valueOf(zipEditText.getText());
    if (TextUtils.isEmpty(zipCode)) {
      zipEditText.setError("ZIP code must be specified");
    }
    longAddress = street + ", " + zipCode + " " + city + ", " + country;
    return longAddress;
  }

  private LatLng getPosition(Address address) {
    return position = new LatLng(address.getLatitude(), address.getLongitude());
  }

  private LatLng getlatLng(Address address) {
    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
    return latLng;
  }

  private void openMapDialog() {
    FragmentManager manager = getFragmentManager();
    MyApptNewDialog dialog = MyApptNewDialog.newInstance(position);
    dialog.setTargetFragment(MyApptNew.this, Constants.REQUEST_ADDRESS);
    dialog.show(manager, Constants.DIALOG_ADDRESS);
  }

  private void sendResult(int resultCode, Boolean isApartmentAdded) {
    if (getTargetFragment() == null) {
      return;
    }
    Intent intent = new Intent();
    intent.putExtra(Constants.EXTRA_NEW_APARTMENT, isApartmentAdded);
    getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
  }
}
