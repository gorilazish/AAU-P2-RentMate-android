package com.example.michal.rentmate.ui.apartment.newApartment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.michal.rentmate.R;
import com.example.michal.rentmate.util.Constants;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MyApptNewDialog extends DialogFragment {

  @Bind(R.id.wrong_address_text_view) TextView wrongAddress;
  @Bind(R.id.correct_address_button) Button correctButton;
  @Bind(R.id.wrong_address_button) Button wrongButton;

  private GoogleMap map;
  private LatLng position;
  private SupportMapFragment fragment;
  private boolean isAddressCorrect = true;

  public static MyApptNewDialog newInstance(LatLng position) {
    Bundle arg = new Bundle();
    arg.putParcelable(Constants.ARG_ADDRESS, position);

    MyApptNewDialog dialog = new MyApptNewDialog();
    dialog.setArguments(arg);
    return dialog;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    position = getArguments().getParcelable(Constants.ARG_ADDRESS);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
      savedInstanceState) {
    View view = inflater.inflate(R.layout.frag_map_dialog, container, false);
    ButterKnife.bind(this, view);
    initMap();

    return view;
  }

  //  Listeners
  @OnClick(R.id.correct_address_button)
  public void onCorrectBtnClicked() {
    sendResult(Activity.RESULT_OK, isAddressCorrect);
    getDialog().cancel();
  }

  @OnClick(R.id.wrong_address_button)
  public void onWrongBtnclicked() {
    sendResult(Activity.RESULT_OK, !isAddressCorrect);
    getDialog().cancel();
  }

  private void initMap() {
    fragment = new SupportMapFragment();
    fragment.getMapAsync(new OnMapReadyCallback() {
      @Override
      public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.addMarker(new MarkerOptions().position(position));
        CameraPosition cameraPosition = new CameraPosition.Builder()
            .target(position)
            .zoom(13)
            .build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.getUiSettings().setRotateGesturesEnabled(false);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().isMyLocationButtonEnabled();
      }
    });
    FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
    transaction.add(R.id.map_fragment, fragment).commit();
  }

  private void sendResult(int resultCode, Boolean isAddressCorrect) {
    if (getTargetFragment() == null) {
      return;
    }
    Intent intent = new Intent();
    intent.putExtra(Constants.EXTRA_ADDRESS, isAddressCorrect);
    getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
  }
}
