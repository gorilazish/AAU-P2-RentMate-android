package com.example.michal.rentmate.ui.apartment.myApartment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.michal.rentmate.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyApptJoin extends Fragment {

  public static MyApptJoin newInstance() {
    return new MyApptJoin();
  }

  @Bind(R.id.join_apartment_edit_text)EditText joinEditText;
  @Bind(R.id.join_apartment_button)Button joinButton;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.frag_my_appt_join, container, false);
    ButterKnife.bind(this,view);
    return view;
  }
//  Listeners
  @OnClick(R.id.join_apartment_button)
  public void onAptJoined(){
  }
}
