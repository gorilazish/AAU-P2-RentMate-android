package com.example.michal.rentmate.ui.apartment.myApartment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ShareCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.michal.rentmate.R;
import com.example.michal.rentmate.model.pojo.Apartment;
import com.example.michal.rentmate.model.repositories.ApartmentRepository;
import com.example.michal.rentmate.util.Constants;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyApptDetailFragment extends Fragment {

  @Bind(R.id.apartment_detail_address_textview) TextView detailAddress;
  @Bind(R.id.apartment_detail_postalcode_textview) TextView detailPostalCode;
  @Bind(R.id.apartment_detail_access_Key_textview) TextView detailAccesKey;
  @Bind(R.id.apartment_detail_occupied_textview) TextView detailOccupied;
  @Bind(R.id.apartment_ID_layout) LinearLayout idLayout;

  private Apartment apartment;

  public static MyApptDetailFragment newInstance(String apartmentID) {
    Bundle arg = new Bundle();
    arg.putSerializable(Constants.ARG_APT, apartmentID);
    MyApptDetailFragment detailFragment = new MyApptDetailFragment();
    detailFragment.setArguments(arg);
    return detailFragment;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    String apartmentId = (String) getArguments().getSerializable(Constants.ARG_APT);
    apartment = ApartmentRepository.getInstance().getApartment(apartmentId);
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.frag_my_appt_detail, container, false);
    ButterKnife.bind(this, view);
    populateUI(apartment);
    setMapFragment(apartment);
    return view;
  }
//  Listeners
  @OnClick(R.id.apartment_ID_layout)
  public void sendApartmetID() {
    ShareCompat.IntentBuilder.from(getActivity())
        .setType("text/plain")
        .setText(setEmailText())
        .setSubject("New Apartment ID")
        .setChooserTitle("Send ID via: ")
        .startChooser();
  }

  private String setEmailText() {
    String email = "";
    email = getString(R.string.welcome_new_tenant) + "\n" +
        getString(R.string.use_the_ID) + "\n" +
        getString(R.string.new_ID) +
        apartment.getApartmentId();
    return email;
  }

  private void setMapFragment(Apartment apartment) {
    FragmentManager manager = getChildFragmentManager();
    Fragment fragment = manager.findFragmentByTag(Constants.MAP_DETAIL_FRAGMENT);
    if (fragment == null) {
      fragment = MyApptMapDetailFragment.newInstance(apartment.getApartmentId());
    }
    manager.beginTransaction()
        .add(R.id.frag_detail_map_container, fragment, Constants.MAP_DETAIL_FRAGMENT)
        .addToBackStack(Constants.MAP_DETAIL_FRAGMENT)
        .commit();
  }

  private void populateUI(Apartment apartment) {
    detailAddress.setText(apartment.getName());
//    detailPostalCode.setText(apartment.getPostalCode);
    detailAccesKey.setText(apartment.getApartmentId());
    if (apartment.isOccupied()) {
      detailOccupied.setText(getString(R.string.apartment_occupied));
    } else {
      detailOccupied.setText(getString(R.string.apartment_vacant));
    }
  }
}
