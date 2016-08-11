package com.example.michal.rentmate.ui.apartment.myApartment;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import com.example.michal.rentmate.model.pojo.Apartment;
import com.example.michal.rentmate.model.repositories.ApartmentRepository;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyApptMapFragment extends SupportMapFragment {

  private GoogleMap map;
  private Geocoder geocoder;
  private ApartmentRepository repoAprt;
  private List<Apartment> apartmentList;
  private List<List<Address>> ListOfAddressList;

  public static MyApptMapFragment newInstance() {
    return new MyApptMapFragment();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    geocoder = new Geocoder(getActivity());
    repoAprt = ApartmentRepository.getInstance();
    apartmentList = repoAprt.getApartmentList();

    getMapAsync(new OnMapReadyCallback() {
      @Override
      public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        drawApartments(apartmentList, geocoder);
        initMap();
      }
    });
  }

  public void initMap() {
    if (ListOfAddressList.size() == 0) {
      return;
    }
    CameraPosition cameraPosition = new CameraPosition.Builder()
        .target(((getPositions(ListOfAddressList).get(0))))
        .zoom(10)
        .build();
    map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    map.getUiSettings().setRotateGesturesEnabled(false);
    map.getUiSettings().setZoomControlsEnabled(true);
    map.getUiSettings().isMyLocationButtonEnabled();
  }

  public void drawApartments(List<Apartment> apartmentList, Geocoder geocoder) {
    ListOfAddressList = new ArrayList<List<Address>>();
    for (int i = 0; i < apartmentList.size(); i++) {
      String address = getLongAddress(apartmentList, i);
      List<Address> addresses = null;
      try {
        addresses = geocoder.getFromLocationName(address, 1);
      } catch (IOException e) {
        e.printStackTrace();
      }
      ListOfAddressList.add(addresses);
    }
    List<LatLng> positions = getPositions(ListOfAddressList);
    for (int i = 0; i < positions.size(); i++)
      drawMarker(positions.get(i));
  }

  public List<LatLng> getPositions(List<List<Address>> addressList) {
    List<LatLng> positions = new ArrayList<>();
    for (int i = 0; i < addressList.size(); i++) {
      Address address = addressList.get(i).get(0);
      LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
      positions.add(latLng);
    }
    return positions;
  }

  public void drawMarker(LatLng position) {
    MarkerOptions mo = new MarkerOptions();
    mo.position(position);
    map.addMarker(mo);
  }

  public String getLongAddress(List<Apartment> apartmentList, int i) {
    String longAddress = apartmentList.get(i).getStreet() + ", " +
        apartmentList.get(i).getZip() + ", " +
        apartmentList.get(i).getCity() + ", " +
        apartmentList.get(i).getCountry();
    return longAddress;
  }
}
