package com.example.michal.rentmate.ui.apartment.myApartment;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import com.example.michal.rentmate.model.pojo.Apartment;
import com.example.michal.rentmate.model.repositories.ApartmentRepository;
import com.example.michal.rentmate.util.Constants;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MyApptMapDetailFragment extends SupportMapFragment {

  private GoogleMap map;
  private Apartment apartment;
  private LatLng position;

  public static MyApptMapDetailFragment newInstance(String apartmentID) {
    Bundle arg = new Bundle();
    arg.putSerializable(Constants.ARG_APT_ID, apartmentID);
    MyApptMapDetailFragment detailMapFragment = new MyApptMapDetailFragment();
    detailMapFragment.setArguments(arg);
    return detailMapFragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    String apartmentID = (String) getArguments().getSerializable(Constants.ARG_APT_ID);
    apartment = ApartmentRepository.getInstance().getApartment(apartmentID);

    getMapAsync(new OnMapReadyCallback() {
      @Override
      public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        initMap();
      }
    });
  }

  private Address getAddress(Apartment apartment) {
    Geocoder geocoder = new Geocoder(getActivity());
    Address address = null;

    String longAddress = getLongAddress(apartment);
    List<Address> addressList = null;
    try {
      addressList = geocoder.getFromLocationName(longAddress, 1);
    } catch (IOException e) {
      e.printStackTrace();
    }
    if (addressList != null) {
      address = addressList.get(0);
      position = new LatLng(address.getLatitude(), address.getLongitude());
      MarkerOptions marker = new MarkerOptions().position(position);
      map.addMarker(marker);
    }
    return address;
  }

  private LatLng getlatLng(Address address) {
    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
    return latLng;
  }

  private void drawMark() {
    MarkerOptions mo = new MarkerOptions();
    mo.position(getlatLng(getAddress(apartment)));
    map.addMarker(mo);
  }

  public void initMap() {
    drawMark();
    CameraPosition cameraPosition = new CameraPosition.Builder()
        .target(getlatLng(getAddress(apartment)))
        .zoom(10)
        .build();
    map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    map.getUiSettings().setRotateGesturesEnabled(false);
    map.getUiSettings().setZoomControlsEnabled(true);
    map.getUiSettings().isMyLocationButtonEnabled();
  }

  public String getLongAddress(Apartment apartment) {
    String longAddress = apartment.getStreet() + ", " +
        apartment.getZip() + ", " +
        apartment.getCity() + ", " +
        apartment.getCountry();
    return longAddress;
  }
}
